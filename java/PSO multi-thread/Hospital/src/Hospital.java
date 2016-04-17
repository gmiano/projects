/**
 * Created by peppe on 17/12/2015.
 */
public interface Hospital {
    /////////////////////////////////////////////////////////////////
    // Synchronized methods
    /////////////////////////////////////////////////////////////////
    void cura(Doctor d);
    void rilascia(Doctor d);
    void ricovero(Patient p);
    void uscita(Patient p);
}
