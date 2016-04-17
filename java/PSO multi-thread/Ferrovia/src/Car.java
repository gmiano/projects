/**
 * Created by peppe on 15/12/2015.
 */
public class Car extends Thread{
    /////////////////////////////////////////////////////////////////////////////////
    // variables
    /////////////////////////////////////////////////////////////////////////////////
    private Dock actualDock;
    private int weigth;
    private Ferry f;
    /////////////////////////////////////////////////////////////////////////////////
    // constructor
    /////////////////////////////////////////////////////////////////////////////////
    public Car(String name, Dock actualDock, int weigth){
        super(name);
        this.actualDock = actualDock;
        this.weigth = weigth;
    }

    /////////////////////////////////////////////////////////////////////////////////
    // Custom methods
    /////////////////////////////////////////////////////////////////////////////////
    public void log(String msg){
        System.out.println(this.getName()+"["+this.getWeigth()+"]: "+msg);
    }

    public Dock getActualDock(){
        return this.actualDock;
    }

    public int getWeigth(){
        return this.weigth;
    }

    public void setFerry(Ferry f){
        this.f = f;
    }

    public Ferry getFerry(){
        return this.f;
    }
    /////////////////////////////////////////////////////////////////////////////////
    // run class
    /////////////////////////////////////////////////////////////////////////////////
    public void run(){
        try {
            actualDock.getOn(this);
            actualDock.getOff(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
