// Version 4.1
// Use Case 4: Room Search & Availability Check

import java.util.*;

// Version 2.1 (Refactored Room Domain Model)
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


// Version 3.1 (Centralized Inventory)
class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 0); // Example: unavailable
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // No modification methods used in search (read-only access)
}


// Version 4.0 (New Class)
class SearchService {

    public void searchAvailableRooms(RoomInventory inventory, List<Room> rooms) {

        System.out.println("===== Available Rooms =====\n");

        for (Room room : rooms) {

            int available = inventory.getAvailability(room.getRoomType());

            // Defensive check: only show rooms with availability
            if (available > 0) {
                room.displayRoomDetails();
                System.out.println("Available Rooms: " + available);
                System.out.println();
            }
        }
    }
}


// Version 4.1 (Main Application)
public class BookMyStayApp {

    public static void main(String[] args) {

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Create room domain objects
        List<Room> rooms = new ArrayList<>();
        rooms.add(new SingleRoom());
        rooms.add(new DoubleRoom());
        rooms.add(new SuiteRoom());

        // Initialize search service
        SearchService searchService = new SearchService();

        // Guest performs search
        searchService.searchAvailableRooms(inventory, rooms);

        // Inventory remains unchanged (read-only operation)
    }
}