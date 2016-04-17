import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by peppe on 15/12/2015.
 */
public class Dock {
    /////////////////////////////////////////////////////////////////////////////////
    // variables
    /////////////////////////////////////////////////////////////////////////////////
    private String name;                // DOCK NAME
    private int TTL;                    // Time to leave for the ferry
    private ArrayList<Car> queue;       // Cars queue
    private Ferry boardingFerry;        // Actual ferry boarding on dock

    /////////////////////////////////////////////////////////////////////////////////
    // constructor
    /////////////////////////////////////////////////////////////////////////////////
    public Dock(String name, int TTL){
        this.name = name;
        this.TTL = TTL;
        queue = new ArrayList<>();
        boardingFerry = null;
    }


    /////////////////////////////////////////////////////////////////////////////////
    // Custom methods
    /////////////////////////////////////////////////////////////////////////////////
    public String getName(){
        return this.name;
    }


    /////////////////////////////////////////////////////////////////////////////////
    // synchronized
    /////////////////////////////////////////////////////////////////////////////////

    // Load waiting passengers in the dock after arrival
    public synchronized void board(Ferry f) throws InterruptedException {
        f.log("arrived to the dock in " + f.getActualDock().getName());
        // Now it is time to unboard passengers, wait before boarding passengers that the ferry is empty
        f.log("unboard passengers partiti da " + f.getNextDock().getName() + " per " + f.getActualDock().getName());
        // Notify cars that was on the ferry to unboard, will be awaken also waiting cars to board but
        // bording ferry is null and they will sleep once more
        while(!f.isEmpty()){
            notifyAll();
            wait();
            f.log("Someone notified me, i was unboarding.");
        }
        f.printStatus();
        f.log("ferry is empty, board passengers.");
        // Notify that the ferry as arrived so waiting cars can go onboard
        boardingFerry = f;
        notifyAll();
        // Remember arrival time
        long arrivalTime = System.currentTimeMillis();
        while((System.currentTimeMillis() - arrivalTime < TTL) && ! f.isFull()){
            f.log("can load more cars");
            wait(200);
        }
        if(System.currentTimeMillis() - arrivalTime >= TTL){
            f.log("max waiting time expired.");
        }
        if(f.isFull()){
            f.log("is full.");
        }
        f.log("leave now, bye.");
        boardingFerry = null;
    }

    // Car get on ferry
    public synchronized void getOn(Car c) throws InterruptedException {
        c.log("bought ticket, wait to leave from " + c.getActualDock().getName());
        queue.add(c);
        // Car needs to wait both
        // 1 - If there is no ferry in the dock
        // 2 - Or if it can't be boarded in the actual ferry
        // 3 - It is not their turn so if they aren't the first in the queue
        while(boardingFerry == null || !boardingFerry.canBoard(c.getWeigth()) || queue.indexOf(c) != 0){
            if(boardingFerry == null){
                c.log("there is no ferry, wait");
            }
            else if(queue.indexOf(c) != 0){
                c.log("it is not my turn");
            }
            else {
                c.log("the actual ferry is full.");
                c.log("can board is " + boardingFerry.canBoard(c.getWeigth()));
            }
            wait();
            c.log("Someone notified me.");
        }
        // car has been boarded remove from the queue and notify other waiting car that it may be their turn
        queue.remove(c);
        c.setFerry(boardingFerry);
        notifyAll();
        boardingFerry.boardCar(c.getWeigth());
        c.log("boarded in the " + boardingFerry.getName());
    }

    // when ferry will arrive in the arrival dock unboard cars
    public synchronized void getOff(Car c) throws InterruptedException {
        while(c.getActualDock().getName().equals(c.getFerry().getActualDock().getName())){
            c.log("Is travelling by now");
            wait();
            c.log("notified");
        }
        // Now car can unboard
        c.log("unboard from " + c.getFerry().getName());
        c.getFerry().unboard(c.getWeigth());
        c.setFerry(null);
        // Notify ferry that car unboarded, maybe it was the last one so ferry can go boarding new cars
        notifyAll();
        c.log("bye");
    }

    /////////////////////////////////////////////////////////////////////////////////
    // main
    /////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) throws InterruptedException {
        Dock A = new Dock("Livorno", 2000);
        Dock B = new Dock("Olbia", 2000);

        Ferry ferry = new Ferry("Time Bandit", A, B, 200, 5);
        ferry.start();
        ArrayList<Car> cars = new ArrayList<>();

        for(int i=0; i<20; i++){
            if(Math.random()>=0.5)
                cars.add(new Car("Car#"+i, A, (int)(Math.random()*50)+10));
            else
                cars.add(new Car("Car#"+i, B, (int)(Math.random()*50)+10));
        }
        Collections.shuffle(cars);

        for(Car c: cars){
            c.start();
            Thread.sleep((int)(Math.random()*50));
        }

        for(Car c: cars)
            c.join();
    }
}
