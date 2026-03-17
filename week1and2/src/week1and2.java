import java.util.*;

public class week1and2 {

    class Vehicle {
        String licensePlate;
        long entryTime;

        Vehicle(String licensePlate, long entryTime) {
            this.licensePlate = licensePlate;
            this.entryTime = entryTime;
        }
    }

    private int capacity;
    private Vehicle[] spots;

    public week1and2(int capacity) {
        this.capacity = capacity;
        spots = new Vehicle[capacity];
    }

    // Simple hash function based on license plate
    private int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % capacity;
    }

    // Park vehicle (linear probing)
    public boolean park(String licensePlate) {
        int index = hash(licensePlate);

        for (int i = 0; i < capacity; i++) {
            int probeIndex = (index + i) % capacity;
            if (spots[probeIndex] == null) {
                spots[probeIndex] = new Vehicle(licensePlate, System.currentTimeMillis());
                System.out.println(licensePlate + " parked at spot " + probeIndex);
                return true;
            }
        }

        System.out.println("Parking full! " + licensePlate + " cannot be parked.");
        return false;
    }

    // Exit vehicle and calculate billing
    public void exit(String licensePlate) {
        int index = hash(licensePlate);

        for (int i = 0; i < capacity; i++) {
            int probeIndex = (index + i) % capacity;
            Vehicle v = spots[probeIndex];
            if (v != null && v.licensePlate.equals(licensePlate)) {
                long duration = (System.currentTimeMillis() - v.entryTime) / 1000; // seconds
                spots[probeIndex] = null;
                System.out.println(licensePlate + " exited from spot " + probeIndex
                        + ". Duration: " + duration + " sec.");
                return;
            }
        }

        System.out.println(licensePlate + " not found in parking lot.");
    }

    // Find nearest available spot to entrance
    public int nearestAvailable() {
        for (int i = 0; i < capacity; i++) {
            if (spots[i] == null) return i;
        }
        return -1;
    }

    // Display parking statistics
    public void statistics() {
        int occupied = 0;
        for (Vehicle v : spots) if (v != null) occupied++;
        double occupancyRate = (occupied * 100.0) / capacity;
        System.out.println("Occupied spots: " + occupied + "/" + capacity);
        System.out.println("Average occupancy: " + occupancyRate + "%");
    }

    public static void main(String[] args) throws InterruptedException {
        week1and2 lot = new week1and2(10);

        lot.park("KA01AB1234");
        lot.park("KA01AB5678");
        lot.park("KA02XY9999");

        System.out.println("Nearest available spot: " + lot.nearestAvailable());

        Thread.sleep(2000); // simulate parking duration

        lot.exit("KA01AB1234");
        lot.exit("KA02XY9999");

        lot.statistics();
    }
}