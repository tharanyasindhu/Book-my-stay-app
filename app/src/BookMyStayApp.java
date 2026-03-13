import java.util.*;

// Reservation class
class Reservation {

    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private boolean active;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.active = true;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public boolean isActive() {
        return active;
    }

    public void cancel() {
        active = false;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType +
                ", Room ID: " + roomId +
                ", Status: " + (active ? "CONFIRMED" : "CANCELLED");
    }
}

// Room inventory management
class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public boolean allocateRoom(String roomType) {

        if (!inventory.containsKey(roomType))
            return false;

        int count = inventory.get(roomType);

        if (count <= 0)
            return false;

        inventory.put(roomType, count - 1);
        return true;
    }

    public void restoreRoom(String roomType) {
        inventory.put(roomType, inventory.get(roomType) + 1);
    }

    public void displayInventory() {

        System.out.println("\nCurrent Inventory:");

        for (String type : inventory.keySet()) {
            System.out.println(type + " Rooms Available: " + inventory.get(type));
        }
    }
}

// Cancellation service with rollback
class CancellationService {

    private Stack<String> rollbackStack = new Stack<>();

    public void cancelReservation(String reservationId,
                                  Map<String, Reservation> reservations,
                                  RoomInventory inventory) {

        if (!reservations.containsKey(reservationId)) {
            System.out.println("Cancellation Failed: Reservation does not exist.");
            return;
        }

        Reservation reservation = reservations.get(reservationId);

        if (!reservation.isActive()) {
            System.out.println("Cancellation Failed: Reservation already cancelled.");
            return;
        }

        // Record room ID for rollback
        rollbackStack.push(reservation.getRoomId());

        // Restore inventory
        inventory.restoreRoom(reservation.getRoomType());

        // Update reservation status
        reservation.cancel();

        System.out.println("Reservation cancelled successfully.");
        System.out.println("Rollback recorded for Room ID: " + rollbackStack.peek());
    }
}

// Main class
public class BookMyStayApp{

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        RoomInventory inventory = new RoomInventory();
        CancellationService cancellationService = new CancellationService();

        Map<String, Reservation> reservations = new HashMap<>();

        int roomCounter = 1;

        boolean running = true;

        while (running) {

            System.out.println("\n=== Booking System ===");
            System.out.println("1. Create Booking");
            System.out.println("2. Cancel Booking");
            System.out.println("3. View Reservations");
            System.out.println("4. View Inventory");
            System.out.println("5. Exit");

            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:

                    System.out.print("Enter Reservation ID: ");
                    String id = scanner.nextLine();

                    System.out.print("Enter Guest Name: ");
                    String guest = scanner.nextLine();

                    System.out.print("Enter Room Type (Single/Double/Suite): ");
                    String roomType = scanner.nextLine();

                    if (!inventory.allocateRoom(roomType)) {
                        System.out.println("Booking failed. Room not available.");
                        break;
                    }

                    String roomId = "RM" + roomCounter++;

                    Reservation reservation =
                            new Reservation(id, guest, roomType, roomId);

                    reservations.put(id, reservation);

                    System.out.println("Booking confirmed. Room ID: " + roomId);

                    break;

                case 2:

                    System.out.print("Enter Reservation ID to cancel: ");
                    String cancelId = scanner.nextLine();

                    cancellationService.cancelReservation(
                            cancelId,
                            reservations,
                            inventory
                    );

                    break;

                case 3:

                    if (reservations.isEmpty()) {
                        System.out.println("No reservations found.");
                    } else {
                        System.out.println("\nReservation List:");
                        for (Reservation r : reservations.values()) {
                            System.out.println(r);
                        }
                    }

                    break;

                case 4:

                    inventory.displayInventory();
                    break;

                case 5:

                    running = false;
                    System.out.println("System exited.");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }

        scanner.close();
    }
}