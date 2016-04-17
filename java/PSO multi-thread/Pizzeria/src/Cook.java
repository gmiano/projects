import java.util.Random;

/**
 * Created by peppe on 13/12/2015.
 */
public class Cook extends Thread {
    private Restaurant r;

    public Cook(String name, Restaurant r){
        super(name);
        this.r = r;
    }

    // Thread run class
    public void run(){
        while(true){
            try {
                r.prepareFood(this);
                Thread.sleep(new Random().nextInt(100));
                r.dealFood(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Logger
    public void toString(String msg){
        System.out.println(this.getName()+": "+msg);
    }
}
