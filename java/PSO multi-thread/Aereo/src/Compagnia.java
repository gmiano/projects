import java.util.ArrayList;
import java.util.Random;

/**
 * Created by peppe on 16/12/2015.
 */
public class Compagnia {
    private ArrayList<Passeggero> queue;                // ArrayList contenente la coda dei passeggeri (Con il riferimento ai thread)
    private Elicottero e;                               // Riferimento all'oggetto elicottero attualmente in aeroport
    private int X;                                      // Tempo massimo di attesa prima che l'aereo decolli se c'è almeno un passeggero
    private static int N;                               // Numero max di passeggeri

    public Compagnia(int X){
        this.queue = new ArrayList<>();
        this.e = null;
        this.X = X;
        this.N = 20;
    }
    /////////////////////////////////////////////////
    // Synchronized methods
    /////////////////////////////////////////////////

    /**
     * Classe invocata quando un nuovo passeggero arriva all'aeroporto e vuole prendere l'elicottero per fare il giro turistico
     * @param p oggetto passeggero
     */
    public synchronized void getOn(Passeggero p) throws InterruptedException {
        // Quando arriva un passeggero se non c'è l'elicottero deve aspettare, oppure deve aspettare se c'è l'elicottero ma sta caricando l'altra tipologia di passeggeri (singolo o gruppo)
        queue.add(p);
        while(e == null || !e.canBoard(p) || !priority(p)){
            if(e == null){
                p.log("non posso salire perchè non è presente nessun aereo");
            }
            else
                p.log("non posso salire perchè non sta caricando passeggeri " +  p.type());
            wait();
            p.log("Qualcuno mi ha risvegliato mentre stavo aspettando di salire a bordo di un aereo");
        }
        // Giunto qui il passeggero/ gruppo può salire
        queue.remove(p);
        e.board(p);
        p.log("Sono salito a bordo dell'elicottero");
        // Risveglio l'elicottero
        notifyAll();
    }

    /**
     * Classe invocata quando ad un passeggero viene notificato che l'aereo ha fatto il giro turistico è rientrato e adesso può scendere
     * @param p oggetto passeggero
     */
    public synchronized void getOff(Passeggero p) throws InterruptedException {
        while(p.getE() != null){
            p.log("I'm travelling");
            wait();
        }
        p.log("Bye, wonderful trip.");
    }

    /**
     * Classe invocata quando l'elicottero atterra all'aeroporto ed è pronto per caricare nuovi passeggeri
     * @param e oggetto elicottero
     */
    public synchronized void board(Elicottero e) throws InterruptedException {
        e.log("pronto a caricare nuovi passeggeri");
        // L'aereo partirà quando 1 - Sarà scaduto il tempo e ci sarà almeno un passeggero a bordo 2 - sarà pieno e ci sono altre possibili persone da caricare
        // Salvo in una variabile il momento dell'atterraggio per poter calcolare il tempo trascorso da allora
        long arrival = System.currentTimeMillis();
        // Notifico ai passeggeri in attesa che sono arrivato
        this.e = e;
        notifyAll();
        while(((!e.isFull() && (System.currentTimeMillis() - arrival < X)) || (e.isEmpty())) ){
            e.log("attendo altri passeggeri");
            e.printStatus();
            if(queue.size() == 0 && e.isEmpty()){
                e.log("La coda è vuota, mi fermo finchè un passeggero non mi risveglia");
                wait();
            }
            else{
                wait(200);
            }
            e.log("Qualcuno mi ha risvegliato mentre attendevo nuovi passeggeri.");
        }
        e.log("leaving now.");
        this.e = null;
    }

    /**
     * Classe invocata quando l'aereo ha terminato il giro e deve scaricare i passeggeri.
     * @param e oggetto elicottero
     */
    public synchronized void unboard(Elicottero e){
        e.log("Rientrato dal tour! Scarico i passeggeri a bordo");
        e.printStatus();
        e.unboard();
        // Risveglia i passeggeri a bordo
        notifyAll();
    }

    public boolean priority(Passeggero p){
        // Scandisco tutti gli elementi della coda, se ho almeno un elemento di tipo gruppo e l'elemento controllato non è un gruppo non può salire perchè i gruppi hanno la priorità
        for(Passeggero p1 : queue){
            if(p1.type().equals("gruppo") && p.type().equals("singolo")) return false;
        }
        return true;
    }

    /**
     * Main class
     * @param args
     */
    public static void main(String[] args){
        // Inizializzo un istanza dell'oggetto compagnia, rappresenta l'oggetto su cui viene effettuata la sincronizzazione
        Compagnia c = new Compagnia(500);
        new Elicottero("Elicopter#1", c, N).start();
        for(int i = 0 ; i <= 20 ; i++){
            if(Math.random()>=0.2){
                new Passeggero("Passeggero#" + i, c).start();
            }
            else {
                new Passeggero("Passeggero#" + i, c, new Random().nextInt(N - 1) + 2).start();
            }
        }
    }
}
