// Version 3.1
// Use Case 3: Centralized Room Inventory Management

import java.util.HashMap;
import java.util.Map;

// Version 2.1 (Refactored)
abstract class Room {

    private int numberOfBeds;
    private int roomSize;
    private double pricePerNight;

    public Room(int numberOfBeds, int roomSize, double pricePerNight) {
        this.numberOfBeds = numberOfBeds;
        this.roomSize = roomSize;
        this.pricePerNight = pricePerNight;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public int getRoomSize() {
        return roomSize;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public abstract String getRoomType();

    public void displayRoomDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Room Size: " + roomSize + " sq.ft");
        System.out.println("Price Per Night: $" + pricePerNight);
    }
}


// Version 2.0
class SingleRoom extends Room {

    public SingleRoom() {
        super(1, 200, 100);
    }

    public String getRoomType() {
        return "Single Room";
    }
}


// Version 2.0
class DoubleRoom extends Room {

    public DoubleRoom() {
        super(2, 350, 180);
    }

    public String getRoomType() {
        return "Double Room";
    }
}


// Version 2.0
class SuiteRoom extends Room {

    public SuiteRoom() {
        super(3, 600, 350);
    }

    public String getRoomType() {
        return "Suite Room";
    }
}


// Version 3.0 (New Class)
class RoomInventory {

    private HashMap<String, Integer> inventory;

    // Constructor initializes inventory
    public RoomInventory() {

        inventory = new HashMap<>();

        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Retrieve availability
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability
    public void updateAvailability(String roomType, int count) {
        inventory.put(roomType, count);
    }

    // Display inventory
    public void displayInventory() {

        System.out.println("\n===== Current Room Inventory =====");

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue() + " rooms available");
        }
    }
}


// Version 3.1 (Main Class)
public class BookMyStayApp{

    public static void main(String[] args) {

        // Initialize centralized inventory
        RoomInventory inventory = new RoomInventory();

        // Create room objects
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        System.out.println("===== Room Details & Availability =====\n");

        singleRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + inventory.getAvailability(singleRoom.getRoomType()));
        System.out.println();

        doubleRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + inventory.getAvailability(doubleRoom.getRoomType()));
        System.out.println();

        suiteRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + inventory.getAvailability(suiteRoom.getRoomType()));

        // Show centralized inventory
        inventory.displayInventory();
    }
}