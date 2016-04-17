/**
 * Created by peppe on 16/12/2015.
 */
public class Passeggero extends Thread {
    private int M;
    private Elicottero e;             // Variabile che contiene il riferimento all'elicottero su cui il passeggero è salito
    private Compagnia c;                    // Riferimento all'oggetto su cui viene eseguita la sincronizzazione

    //Costruttore, sono due diversi nel caso si tratti di una persona singola o
    //di un gruppo, per una persona singola non viene specificato M che viene automaticamente
    //settato ad 1. Verrà inoltre settata una variabile booleana gruppo che avrà valore true se il
    //thread rappresenta un gruppo e valore false se il thread rappresenta un singolo passeggero
    public Passeggero(String name, Compagnia c){
        this(name, c, 1);
    }

    public Passeggero(String name, Compagnia c, int M){
        super(name);
        this.M = M;
        this.c = c;
        this.e = null;
    }

    public void log(String msg){
        System.out.println(this.getName() + "[" + type() + "]" + ": " + msg);
    }

    public int getNumP(){
        return this.M;
    }
    public String type(){
        if(this.M >= 2) return "gruppo";
        return "singolo";
    }

    public Elicottero getE(){
        return this.e;
    }

    public void setE(Elicottero e){
        this.e = e;
    }

    public void run(){
        try {
            c.getOn(this);
            c.getOff(this);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }
}









