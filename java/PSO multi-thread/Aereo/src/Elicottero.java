import java.util.ArrayList;
import java.util.Random;

/**
 * Created by peppe on 16/12/2015.
 */
public class Elicottero extends Thread {
    private int N;
    private ArrayList<Passeggero> boarded;          // Riferimento ai passeggeri caricati
    private Compagnia c;                            // Riferimento all'oggetto su cui viene eseguita la sincronizzazione
    private int numP;                               // Numero di passeggeri effettivamente caricati



    // Costruttore
    public Elicottero(String name, Compagnia c, int N){
        super(name);
        this.N = N;
        this.numP = 0;
        this.c = c;
        this.boarded = new ArrayList<>();
    }

    public void log(String msg){
        System.out.println(this.getName() + ": " + msg);
    }

    /**
     * Controllo se l'elicottero è pieno e non ci stanno più passeggeri
     * @return
     */
    public boolean isFull(){
        return this.numP == N ? true : false;
    }

    public boolean isEmpty(){
        return this.numP == 0 ? true : false;
    }

    public void unboard(){
        // Setta l'elicottero su cui sono tutti i passeggeri a null così gli viene notificato di scendere
        for(Passeggero p: boarded){
            p.setE(null);
            this.numP -= p.getNumP();
        }
        // Svuota l'array
        boarded.clear();
    }

    public void board(Passeggero p){
        // Aggiungo il numero di persone del gruppo che va da 1 a N e setto l'elicottero del gruppo (Per gruppo intendo sia gruppo che passeggero singolo
        this.numP += p.getNumP();
        p.setE(this);
        boarded.add(p);
    }

    // Se non è ancora salito nessun passeggero può salire
    // oppure può salire se il primo passeggero che è salito è della sua stessa tipologia
    public boolean canBoard(Passeggero p){
        if(boarded.size() == 0 || (p.type().equals(boarded.get(0).type()) && numP + p.getNumP() <= N)){
            return true;
        }
        else return false;
    }

    public void printStatus(){
        synchronized (System.out){
            System.out.println("---------------" + this.getName() + "---------------");
            System.out.println("Passeggeri attualmente caricati: " + numP + (boarded.size() > 0 ? ". Del tipo: " + boarded.get(0).type() : ""));
            System.out.print(numP > 0 ? "I passeggeri sono: " : "");
            for(Passeggero p: boarded){
                System.out.println("| " + p.getName() + ", numP: " + p.getNumP() + ", tipo: " + p.type() + " | ");
            }
        }
    }

    public void run(){
        while(true){
            try {
                c.board(this);
                Thread.sleep(new Random().nextInt(500));
                c.unboard(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
