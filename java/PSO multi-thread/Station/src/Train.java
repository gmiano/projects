import sun.nio.ch.sctp.MessageInfoImpl;

import java.util.ArrayList;

/**
 * Created by peppe on 16/12/2015.
 */
public class Train extends Thread {

    ////////////////////////////////////////////
    // variable
    ////////////////////////////////////////////
    private int MAX;
    private int MIN;
    private Station s, a;                       // s is the start station and arrive is the arrival station
    private ArrayList<Passenger> onBoard;       // Creo un arrayList che conterrà un riferimento a tutti i passeggeri a bordo
    /**
     * Constructor thread
     */

    public Train(String name, int max, int min, Station s, Station a){
        super(name);
        this.MAX = max;
        this.MIN = min;
        this.a = a;
        this.s = s;
        this.onBoard = new ArrayList<>();
    }

    /**
     * controlla se il treno è pieno, ovvero se il numero di passeggeri eguaglia la capacità massima
     * @return
     */
    public boolean isFull(){
        if(onBoard.size() == MAX){
            return true;
        }
        return false;
    }

    /**
     * Controlla se il treno è vuoto
     */
    public boolean isEmpty(){
        return (onBoard.size() == 0 ? true : false);
    }
    /**
     * faccio salire il passeggero a bordo
     * @param p
     */
    public void board(Passenger p){
        onBoard.add(p);
    }

    /**
     * faccio scendere tutti i passeggeri dal treno, setto il valore per il treno su cui sono saliti a null
     */
    public void unboard(){
        for(Passenger p: onBoard){
            p.setTrain(null);
        }
        onBoard.clear();
    }

    public Station getStartStation(){
        return this.s;
    }
    /**
     * Stampa messaggio per conto del treno
     * @param msg
     */
    public void log(String msg){
        System.out.println(this.getName()+": "+msg);
    }

    public boolean minimum(){
        return onBoard.size() >= MIN ? true : false;
    }


    public void printStatus(){
        synchronized (System.out){
            System.out.println("");
            System.out.println("------------------TRAIN "+getName()+"--------------------");
            System.out.print("Sono a bordo: |");
            for(Passenger p: onBoard){
                System.out.print(" "+ p.getName() + " |");
            }
            System.out.println("");
            System.out.println("Per un totale di "+onBoard.size());
            System.out.println("Su un massimo di "+MAX);
            System.out.println("-------------------------------------------");
            System.out.println("");
        }
    }

    ////////////////////////////////////////////
    // run class
    ////////////////////////////////////////////
    public void run(){
        Station temp = s;
        while (true){
            try {
                s.board(this);
                // Aspetta
                Thread.sleep(200);
                s = a;
                a = temp;
                temp = s;
                // Scambia stazione di partenza e stazione di arrivo
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
