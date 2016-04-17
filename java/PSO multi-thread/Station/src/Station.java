import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by peppe on 16/12/2015.
 */
public class Station {

    ////////////////////////////////////////////
    // variables
    ////////////////////////////////////////////
    private boolean board;            // Banchina di carico
    private boolean[] unboard;            // Insieme di banchine di scarico
    private Train t;
    private String name;

    /**
     *
     * @param name of the class
     * @param num_banchine di scarico, ce n'è una di carico ma n di scarico
     */
    public Station(String name, int num_banchine){
        this.name = name;
        // Tutte le banchine sono inizialmente libere
        board = true;
        unboard = new boolean[num_banchine];
        for(int i = 0 ; i < num_banchine ; i++){
            unboard[i] = true;
        }
        t = null;
    }



    ////////////////////////////////////////////
    // synchronized methods
    ////////////////////////////////////////////
    public synchronized void getOn(Passenger p) throws InterruptedException {
        p.log(" arrivato alla banchina di carico.");
        while(t == null || t.isFull()){
            if(t == null){
                p.log("devo aspettare, non c'è nessun treno.");
            }
            else
                p.log("devo aspettare, il treno è pieno.");
            wait();
            p.log("qualcuno mi ha notificato di svegliarmi, stavo aspettando di poter salire su un treno.");
        }
        p.log("salgo a bordo.");
        p.setTrain(t);
        t.board(p);
        t.printStatus();
    }

    public synchronized void board(Train t) throws InterruptedException {
        t.log("arrivato alla stazione." + t.getStartStation().getName());
        int i = -1;
        // scarica i passeggeri che sono sul treno
        if(!t.isEmpty()){
            t.log("scarica passeggeri");
            while((i = emptyDock()) == -1){
                // Non c'è nessuna banchina libera per scaricare i passeggeri, aspetta
                t.log("non c'è nessuna banchina libera per scaricare, aspetta.");
                wait();
                t.log("qualcuno mi ha risvegliato, stavo aspettando che si liberasse una banchina per scaricare i passeggeri.");
            }
            // occupa la banchina
            unboard[i] = false;
            // setta il treno di tutti i passeggeri come null e notificagli di svegliarsi così possono scendere
            t.unboard();
            notifyAll();
        }
        // Aspetta che il treno sia vuoto e che la banchina di carico sia libera per poterti spostare
        while(!t.isEmpty() && board == false){
            if(!t.isEmpty()){
                t.log("Sto finendo di scaricare i passeggeri.");
            }
            else{
                t.log("La banchina di carico è occupata.");
            }
            wait();
            t.log("qualcuno mi ha risvegliato mentre aspettavo di poter andare alla banchina di carico.");
        }
        // libera la banchina di scarico se era stata occupata
        if(i >= 0){
            unboard[i] = true;
        }
        // occupa la banchina di carico
        while(board == false){
            wait();
        }
        t.log("sono vuoto, adesso aspetto di caricare passeggeri.");
        this.t = t;
        // risveglia i passeggeri che stavano aspettando di salire
        notifyAll();
        // Rimango sulla banchina finchè non ho raggiunto il minimo
        while(!t.minimum()) wait();
        board = true;
        // risveglio i treni in attesa di caricare
        notifyAll();
        t.log("Partito da "+t.getStartStation().getName());
    }

    public synchronized void getOff(Passenger p) throws InterruptedException {
        //Fintantochè il valore dell'oggetto t non è null significa che sono sul treno e devo attendere
        while(p.getTrain() != null){
            p.log("sono in viaggio");
            wait();
            p.log("qualcuno mi ha risvegliato mentre ero in viaggio, attendendo di giungere a destinazione.");
        }
        p.log("Sono arrivato, bye.");
    }

    /**
     *
     * @return class name
     */
    public String getName(){
        return this.name;
    }

    /**
     * Questa funzione restituisce una stazione random tra quelle all'interno dell'arraylist
     * @param stations
     * @return
     */
    public static Station randomStation(ArrayList<Station> stations){
        return stations.get(new Random().nextInt(stations.size()));
    }

    /**
     * Controlla se c'è una banchina per scaricare libera e restituiscine l'indice dell'array, se non c'è una banchina libera ritorna -1
     */
    public int emptyDock(){
        for(int i = 0; i < unboard.length; i++){
            if(unboard[i] == true) return i;
        }
        return -1;
    }

    /**
     * Main class
     *
     * @param args
     */
    public static void main(String[] args){
        // Creo un array affinchè se varia il numero di stazioni non devo modificare il codice, così si presuppone però che un passeggero
        ArrayList<Station> stations = new ArrayList<>();
        int num_stations = 2;
        for(int i = 0; i < num_stations; i++){
            Station t = new Station((char) (i+65)+"", new Random().nextInt(5)+1);
            stations.add(t);
        }
        // Inizializzo i due treni assegnandogli una stazione random
        new Train("1#", 5, 1, randomStation(stations), randomStation(stations)).start();
        new Train("2#", 5, 1, randomStation(stations), randomStation(stations)).start();
        // Inizializzo N=20 passeggeri
        for(int i = 0; i < 20; i++){
            // Ogni passeggero parte da una stazione random tra le due disponibili
            new Passenger(i+"#", randomStation(stations)).start();
        }

    }

}
