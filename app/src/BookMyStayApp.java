import java.util.LinkedList;
import java.util.Queue;

// Actor: Reservation (Guest booking intent)
class Reservation {
    private String guestName;
    private String roomType;
    private int nights;

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

    @Override
    public String toString() {
        return "Guest: " + guestName + ", Room Type: " + roomType + ", Nights: " + nights;
    }
}

// Booking Request Queue (Manages incoming booking requests)
class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Add booking request
    public void addBookingRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Booking request added: " + reservation.getGuestName());
    }

    // View all queued requests
    public void displayQueue() {
        if (requestQueue.isEmpty()) {
            System.out.println("No booking requests in queue.");
            return;
        }

        System.out.println("\nBooking Requests in Arrival Order:");
        for (Reservation r : requestQueue) {
            System.out.println(r);
        }
    }

    // Peek next request (for future allocation)
    public Reservation peekNextRequest() {
        return requestQueue.peek();
    }

    // Remove request after processing (not used here but ready for next use case)
    public Reservation processNextRequest() {
        return requestQueue.poll();
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        BookingRequestQueue queue = new BookingRequestQueue();

        // Guest booking requests arriving
        Reservation r1 = new Reservation("Amit", "Deluxe", 2);
        Reservation r2 = new Reservation("Priya", "Suite", 3);
        Reservation r3 = new Reservation("Rahul", "Standard", 1);
        Reservation r4 = new Reservation("Sneha", "Deluxe", 2);

        // Step 1: Guests submit booking requests
        queue.addBookingRequest(r1);
        queue.addBookingRequest(r2);
        queue.addBookingRequest(r3);
        queue.addBookingRequest(r4);

        // Step 2: Requests stored in FIFO order
        queue.displayQueue();

        // Step 3: Show next request to be processed (without allocation)
        Reservation next = queue.peekNextRequest();
        if (next != null) {
            System.out.println("\nNext request to be processed (FIFO): " + next);
        }

        System.out.println("\nNote: No room allocation performed at this stage.");
    }
}