package main;

import java.util.Random;

/**
 * Created by peppe on 12/12/2015.
 */
public class Car extends Thread {
    //Global variables
    private Bridge bridge;
    private int direction;

    // Constructor
    public Car(String name, Bridge bridge, int direction){
        super(name);
        this.bridge = bridge;
        this.direction = direction;
    }

    // Run class, start Thread
    public void run(){
        try{
            bridge.enterBridge(this);
            Thread.sleep(500);
            bridge.exitBridge(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Print

    public void println(String msg){
        System.out.println(this.getName() + ": " + msg+" with direction: "+getDirection());
    }

    // Getters and setters
    public int getDirection(){
        return this.direction;
    }
}
