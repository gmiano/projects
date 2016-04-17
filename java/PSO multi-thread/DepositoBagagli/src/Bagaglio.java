/**
 * Created by peppe on 12/12/2015.
 */
public class Bagaglio extends Thread {
    private DepositoBagagli deposito;
    private int vanoAssegnato;
    private int numBagagli;

    public Bagaglio(String name, DepositoBagagli deposito, int numBagagli){
        super(name);
        this.deposito = deposito;
        this.numBagagli = numBagagli;
    }


    public void setVanoAssegnato(int vanoAssegnato){
        this.vanoAssegnato = vanoAssegnato;
    }

    public int getVanoAssegnato(){
        return this.vanoAssegnato;
    }

    public int getNumBagagli(){
        return this.numBagagli;
    }

    public void println(String msg){
        System.out.println(this.getName()+": "+msg);
    }

    public void run(){
        try {
            deposito.setBagagli(this);
            Thread.sleep(500);
            deposito.getBagagli(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
