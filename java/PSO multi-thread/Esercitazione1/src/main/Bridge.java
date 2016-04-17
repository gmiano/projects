package main;

/**
 * Created by peppe on 12/12/2015.
 */
public interface Bridge {
    public void enterBridge(Car car) throws InterruptedException;
    public void exitBridge(Car car);
}
