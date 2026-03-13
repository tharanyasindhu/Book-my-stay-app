import java.util.*;

// Booking Request class
class BookingRequest {

    private String guestName;
    private String roomType;

    public BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Shared Room Inventory
class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    // Critical section (thread-safe)
    public synchronized boolean allocateRoom(String roomType) {

        if (!inventory.containsKey(roomType)) {
            return false;
        }

        int available = inventory.get(roomType);

        if (available <= 0) {
            return false;
        }

        inventory.put(roomType, available - 1);

        return true;
    }

    public synchronized void displayInventory() {
        System.out.println("\nFinal Inventory Status:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " Rooms Available: " + inventory.get(type));
        }
    }
}

// Shared Booking Queue
class BookingQueue {

    private Queue<BookingRequest> queue = new LinkedList<>();

    public synchronized void addRequest(BookingRequest request) {
        queue.add(request);
    }

    public synchronized BookingRequest getRequest() {
        return queue.poll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Concurrent Booking Processor
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(BookingQueue queue, RoomInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {

        while (true) {

            BookingRequest request;

            synchronized (queue) {
                if (queue.isEmpty()) {
                    break;
                }
                request = queue.getRequest();
            }

            if (request != null) {

                boolean success = inventory.allocateRoom(request.getRoomType());

                if (success) {
                    System.out.println(
                            Thread.currentThread().getName() +
                                    " allocated " + request.getRoomType() +
                                    " room to " + request.getGuestName());
                } else {
                    System.out.println(
                            Thread.currentThread().getName() +
                                    " failed booking for " + request.getGuestName() +
                                    " (No " + request.getRoomType() + " rooms left)");
                }
            }
        }
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();

        // Simulated guest booking requests
        queue.addRequest(new BookingRequest("Alice", "Single"));
        queue.addRequest(new BookingRequest("Bob", "Single"));
        queue.addRequest(new BookingRequest("Charlie", "Single"));
        queue.addRequest(new BookingRequest("David", "Double"));
        queue.addRequest(new BookingRequest("Eva", "Double"));
        queue.addRequest(new BookingRequest("Frank", "Suite"));
        queue.addRequest(new BookingRequest("Grace", "Suite"));

        // Multiple threads processing bookings
        BookingProcessor t1 = new BookingProcessor(queue, inventory);
        BookingProcessor t2 = new BookingProcessor(queue, inventory);
        BookingProcessor t3 = new BookingProcessor(queue, inventory);

        t1.setName("Processor-1");
        t2.setName("Processor-2");
        t3.setName("Processor-3");

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        inventory.displayInventory();
    }
}