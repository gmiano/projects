/**
 * Created by peppe on 13/12/2015.
 */
public class Restaurant {

    private Customer serving;
    private int nextToServe;
    private final static int C = 20;

    // Constructor
    public Restaurant(){
        this.serving = null;
        nextToServe = 0;
    }
    //Synchronized methods
    public synchronized void orderFood(Customer c) throws InterruptedException {
        while(serving != null || nextToServe != c.getTicket()){
            if(serving != null){
                c.toString("Please wait, cooker is occupied");
            }
            else if(nextToServe != c.getTicket()){
                c.toString("It is not my turn.");
            }
            wait();
        }
        nextToServe++;
        c.toString("it is my turn, I can be served.");
        serving = c;
        notifyAll();
   }

    public synchronized void prepareFood(Cook c) throws InterruptedException {
        if(serving == null){
            c.toString("There is no customer to serve.");
            wait();
        }
        c.toString("Is serving "+serving.getName());
    }

    public synchronized void dealFood(Cook c){
        c.toString("Preparation completed. Served "+serving.getName());
        notifyAll();
        serving.toString("Served");
        serving = null;
    }

    // Main class
    public static void main(String[] args){
        Restaurant r = new Restaurant();
        new Cook("Cook#1", r).start();
        for(int i = 0; i<C; i++){
            new Customer("Customer#"+i, r, i).start();
        }
    }
}
