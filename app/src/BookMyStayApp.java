import java.util.*;

// Actor: Reservation (Guest booking intent)
class Reservation {
    private String guestName;
    private String roomType;
    private int nights;
    private String roomId; // assigned during allocation

    public Reservation(String guestName, String roomType, int nights) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNights() {
        return nights;
    }

    public String getRoomId() {
        return roomId;
    }

    public void assignRoomId(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "Guest: " + guestName +
                ", Room Type: " + roomType +
                ", Nights: " + nights +
                (roomId != null ? ", Assigned Room ID: " + roomId : "");
    }
}

// Booking Request Queue (Use Case 5)
class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    public void addBookingRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Booking request added: " + reservation.getGuestName());
    }

    public Reservation getNextRequest() {
        return requestQueue.poll();
    }

    public boolean hasRequests() {
        return !requestQueue.isEmpty();
    }

    public void displayQueue() {
        if (requestQueue.isEmpty()) {
            System.out.println("No booking requests in queue.");
            return;
        }
        System.out.println("\nCurrent Booking Queue:");
        for (Reservation r : requestQueue) {
            System.out.println(r);
        }
    }
}

// Inventory and Allocation Service (Use Case 6)
class RoomInventoryService {
    private Map<String, Integer> roomInventory; // Room type -> available count
    private Map<String, Set<String>> allocatedRooms; // Room type -> assigned room IDs
    private Random random;

    public RoomInventoryService() {
        roomInventory = new HashMap<>();
        allocatedRooms = new HashMap<>();
        random = new Random();

        // Initialize inventory
        roomInventory.put("Standard", 5);
        roomInventory.put("Deluxe", 3);
        roomInventory.put("Suite", 2);

        // Initialize allocated rooms map
        for (String type : roomInventory.keySet()) {
            allocatedRooms.put(type, new HashSet<>());
        }
    }

    // Check availability
    public boolean isAvailable(String roomType) {
        return roomInventory.getOrDefault(roomType, 0) > 0;
    }

    // Allocate a room safely
    public String allocateRoom(String roomType) {
        if (!isAvailable(roomType)) {
            return null;
        }

        String roomId;
        Set<String> allocated = allocatedRooms.get(roomType);
        do {
            roomId = roomType.substring(0, 1).toUpperCase() + (random.nextInt(100) + 1);
        } while (allocated.contains(roomId));

        allocated.add(roomId);
        roomInventory.put(roomType, roomInventory.get(roomType) - 1); // decrement inventory
        return roomId;
    }

    // Display inventory state
    public void displayInventory() {
        System.out.println("\nCurrent Inventory State:");
        for (Map.Entry<String, Integer> entry : roomInventory.entrySet()) {
            System.out.println(entry.getKey() + " -> Available: " + entry.getValue() +
                    ", Allocated Rooms: " + allocatedRooms.get(entry.getKey()));
        }
    }
}

// Main class
public class BookMyStayApp {
    public static void main(String[] args) {
        BookingRequestQueue queue = new BookingRequestQueue();
        RoomInventoryService inventoryService = new RoomInventoryService();

        // Step 1: Guests submit booking requests
        queue.addBookingRequest(new Reservation("Amit", "Deluxe", 2));
        queue.addBookingRequest(new Reservation("Priya", "Suite", 3));
        queue.addBookingRequest(new Reservation("Rahul", "Standard", 1));
        queue.addBookingRequest(new Reservation("Sneha", "Deluxe", 2));
        queue.addBookingRequest(new Reservation("Anjali", "Suite", 1)); // test limited inventory

        // Display current queue
        queue.displayQueue();

        // Step 2: Process queue and allocate rooms
        System.out.println("\nProcessing booking requests...");
        while (queue.hasRequests()) {
            Reservation reservation = queue.getNextRequest();
            String roomType = reservation.getRoomType();

            if (inventoryService.isAvailable(roomType)) {
                String roomId = inventoryService.allocateRoom(roomType);
                reservation.assignRoomId(roomId);
                System.out.println("Reservation confirmed: " + reservation);
            } else {
                System.out.println("No available rooms for: " + reservation.getGuestName() + " (" + roomType + ")");
            }
        }

        // Display final inventory state
        inventoryService.displayInventory();
    }
}