/**
 * Created by peppe on 16/12/2015.
 */
public class Passenger extends Thread {

    ////////////////////////////////////////////
    // variables
    ////////////////////////////////////////////
    private Station s;                  // Riferimento alla stazione in cui si attende
    private Train t;                   // Riferimento al treno su cui il passeggero è salito


    /**
     * Constructor thread
     */
    public Passenger(String name, Station s){
        super(name);
        this.s = s;
        t = null;
    }

    /**
     * stampa un messaggio da parte di questo passeggero, partito dalla stazione s.getName()
     * @param msg
     */
    public void log(String msg){
        System.out.println(this.getName()+"["+this.s.getName()+"]: "+msg);
    }

    /**
     * Quando il passeggero sale su treno tieni traccia di ciò
     */
    public void setTrain(Train t){
        this.t = t;
    }

    /**
     * Ottieni informazioni (restituisci) il trego su cui è salito il passeggero, se non è ancora salito su un treno il valore sarà null
     * @return
     */
    public Train getTrain(){
        return this.t;
    }

    ////////////////////////////////////////////
    //  run class
    ////////////////////////////////////////////
    public void run(){
        try {
            s.getOn(this);
            s.getOff(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
