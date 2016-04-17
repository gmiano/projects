/**
 * Created by peppe on 13/12/2015.
 */
public class Customer extends Thread {
    private Restaurant r;
    private int ticket;

    public Customer(String name, Restaurant r, int ticket){
        super(name);
        this.r = r;
        this.ticket = ticket;
    }

    // Thread run class
    public void run(){
        try {
            r.orderFood(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Logger
    public void toString(String msg){
        System.out.println(this.getName()+": "+msg);
    }

    public int getTicket(){
        return this.ticket;
    }
}
