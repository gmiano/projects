package development;

import main.Bridge;
import main.Car;

import java.util.Random;

/**
 * Created by peppe on 12/12/2015.
 */
public class BridgeOneWayCapacity implements Bridge {
    // Variable containing actual direction (it can be 0 or 1 so if it is -1 no car is on the bridge)
    private int direction;
    private final static int NUMBER_CARS = 20;
    private int numberCarsInTheBridge;
    private int MAX_CAPAC = 3;

    // Constructor to initialize variables
    public BridgeOneWayCapacity(){
        this.direction = new Random().nextInt(2);
        this.numberCarsInTheBridge = 0;
    }

    // Main class
    public static void main(String[] args){
        BridgeOneWay bridge = new BridgeOneWay();
        Random random = new Random();
        // Create cars threads
        for(int i = 0; i < NUMBER_CARS; i++){
            new Car("Car"+i, bridge, random.nextInt(2)).start();
        }
    }

    @Override
    public synchronized void enterBridge(Car car) throws InterruptedException {
        // If there are cars in the other direction
        while((direction != car.getDirection() && numberCarsInTheBridge != 0) || numberCarsInTheBridge == MAX_CAPAC){
            car.println("Not the same direction as cars on the bridge, suspend and wait. Or Capacity max filled.");
            wait();
        }
        numberCarsInTheBridge++;
        direction = car.getDirection();
        car.println("Go over the bridge.");
        printBridgeStatus();
    }

    @Override
    public synchronized void exitBridge(Car car) {
        numberCarsInTheBridge--;
        notifyAll();
        car.println("Exit from the bridge.");
        printBridgeStatus();
    }

    private void printBridgeStatus(){
        System.out.println("-----------");
        System.out.println("#Cars:\t"+numberCarsInTheBridge);
        System.out.println(" Dir:\t"+direction);
        System.out.println("-----------");
    }


}
