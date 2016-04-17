/**
 * Created by peppe on 15/12/2015.
 */
public class Ferry extends Thread{
    /////////////////////////////////////////////////////////////////////////////////
    // Variables
    /////////////////////////////////////////////////////////////////////////////////
    private int MAX_CARS;
    private int MAX_WEIGTH;
    // Remember how many cars are in and their weigth
    private int actualCars, actualWeigth;
    private Dock actualDock, nextDock;

    // Constructor, variables initialization
    public Ferry(String name, Dock actualDock, Dock nextDock, int MAX_WEIGTH, int MAX_CARS){
        super(name);
        this.MAX_CARS = MAX_CARS;
        this.MAX_WEIGTH = MAX_WEIGTH;
        this.actualDock = actualDock;
        this.nextDock = nextDock;
        this.actualWeigth = 0;
        this.actualCars = 0;
    }

    /////////////////////////////////////////////////////////////////////////////////
    // custom methods
    /////////////////////////////////////////////////////////////////////////////////
    public boolean isEmpty(){
        if(actualCars == 0){
            return true;
        }
        else return false;
    }

    // By starting from zero the ferry is full when the number of cars + 1 is equal to the car number limit
    public boolean isFull(){
        if(actualCars == MAX_CARS || actualWeigth == MAX_WEIGTH){
            return  true;
        }
        else return false;
    }

    public Dock getActualDock(){
        return this.actualDock;
    }

    public Dock getNextDock(){
        return this.nextDock;
    }

    public void log(String msg){
        System.out.println(this.getName()+": "+msg);
    }

    // Check if a car can board
    public boolean canBoard(int peso){
        if(actualCars + 1 <= MAX_CARS && actualWeigth + peso <= MAX_WEIGTH){
            return true;
        }
        else return false;
    }

    // Board the new car adding its weight and increasing cars number
    public void boardCar(int peso){
        this.actualWeigth += peso;
        this.actualCars ++;
        printStatus();
    }

    public void unboard(int peso){
        this.actualWeigth -= peso;
        this.actualCars --;
        printStatus();
    }

    // Print ferry status
    public void printStatus(){
        System.out.println("------------------------------- " + this.getName() + " status -----------------------------------");
        System.out.println("-- Loaded cars: " + actualCars + " --");
        System.out.println("-- Actual weigth: " + actualWeigth + " --");
        System.out.println("-- Max cars: " + MAX_CARS + " --");
        System.out.println("-- Max weigth: " + MAX_WEIGTH + " --");
        System.out.println("-- This dock: " + actualDock + " --");
        System.out.println("-- Next dock: " + nextDock + " --");
        System.out.println("--------------------------------------------------------------------");

    }

    /////////////////////////////////////////////////////////////////////////////////
    // run class
    /////////////////////////////////////////////////////////////////////////////////
    public void run(){
        Dock temp = actualDock;
        while(true){
            try {
                actualDock.board(this);
                Thread.sleep(100);
                actualDock = nextDock;
                nextDock = temp;
                temp = actualDock;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
