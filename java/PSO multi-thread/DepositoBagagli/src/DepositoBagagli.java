import java.util.Random;

/**
 * Created by peppe on 12/12/2015.
 */
public class DepositoBagagli {
    private int V = 3;
    private Vano[] posti;
    private final static int CLIENTI = 20;
    private final static int N = 5;


    public DepositoBagagli(){
        posti = new Vano[V];
        for(int i = 0 ; i<V ; i++){
            posti[i] = new Vano(N);
        }
    }


    public synchronized void setBagagli(Bagaglio b) throws InterruptedException {
        int numBagagli = b.getNumBagagli();
        boolean space = false;
        int i = 0;
        while(!space){
            for(i = 0;i < V; i++){
                if(posti[i].getPostiLiberi()>=numBagagli){
                    space = true;
                    break;
                }
            }
            if(!space){
                b.println("Attendi, non è disponibile nessun vano con "+numBagagli+" necessario");
                wait();
            }
        }

        // I indica il posto scelto
        posti[i].aggiungiBagagli(numBagagli);
        b.setVanoAssegnato(i);
        b.println("Ha depositato "+numBagagli+" bagagli in posizione "+i);
    }

    public synchronized void getBagagli(Bagaglio b){
        b.println("Ritiro di "+b.getNumBagagli()+" bagagli da posizione "+b.getVanoAssegnato());
        posti[b.getVanoAssegnato()].rimuoviBagagli(b.getNumBagagli());
        notifyAll();
    }


    public static void main(String[] args){
        DepositoBagagli deposito = new DepositoBagagli();
        for(int i = 0 ; i < CLIENTI ; i++){
            new Bagaglio("Cliente"+i, deposito, new Random().nextInt(N)+1).start();
        }

    }
}

class Vano{
    private int N;

    public Vano(int postiLiberi){
        this.N = postiLiberi;
    }

    public void aggiungiBagagli(int numBagagli){
        this.N-=numBagagli;
    }

    public void rimuoviBagagli(int numBagagli){
        this.N+=numBagagli;
    }

    public int getPostiLiberi(){
        return this.N;
    }
}
