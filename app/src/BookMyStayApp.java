import java.util.*;

// Custom Exception for invalid bookings
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation class
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
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

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType;
    }
}

// Inventory class to manage room availability
class RoomInventory {

    private Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("Single", 2);
        rooms.put("Double", 2);
        rooms.put("Suite", 1);
    }

    // Validate and allocate room
    public void allocateRoom(String roomType) throws InvalidBookingException {

        if (!rooms.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid Room Type: " + roomType);
        }

        int available = rooms.get(roomType);

        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }

        rooms.put(roomType, available - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Room Inventory:");
        for (String type : rooms.keySet()) {
            System.out.println(type + " Rooms Available: " + rooms.get(type));
        }
    }
}

// Validator class
class InvalidBookingValidator {

    public static void validateInput(String reservationId, String guestName, String roomType)
            throws InvalidBookingException {

        if (reservationId == null || reservationId.isEmpty()) {
            throw new InvalidBookingException("Reservation ID cannot be empty.");
        }

        if (guestName == null || guestName.isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        if (roomType == null || roomType.isEmpty()) {
            throw new InvalidBookingException("Room type must be provided.");
        }
    }
}

// Main Class
public class BookMyStayApp{

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        RoomInventory inventory = new RoomInventory();
        List<Reservation> reservations = new ArrayList<>();

        boolean running = true;

        while (running) {

            System.out.println("\n=== Booking System with Validation ===");
            System.out.println("1. Create Booking");
            System.out.println("2. View Reservations");
            System.out.println("3. View Inventory");
            System.out.println("4. Exit");

            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    try {

                        System.out.print("Enter Reservation ID: ");
                        String id = scanner.nextLine();

                        System.out.print("Enter Guest Name: ");
                        String guest = scanner.nextLine();

                        System.out.print("Enter Room Type (Single/Double/Suite): ");
                        String room = scanner.nextLine();

                        // Validate input
                        InvalidBookingValidator.validateInput(id, guest, room);

                        // Validate inventory and allocate
                        inventory.allocateRoom(room);

                        Reservation reservation = new Reservation(id, guest, room);
                        reservations.add(reservation);

                        System.out.println("Booking successfully confirmed.");

                    } catch (InvalidBookingException e) {
                        System.out.println("Booking Failed: " + e.getMessage());
                    }

                    break;

                case 2:

                    if (reservations.isEmpty()) {
                        System.out.println("No reservations found.");
                    } else {
                        System.out.println("\nConfirmed Reservations:");
                        for (Reservation r : reservations) {
                            System.out.println(r);
                        }
                    }

                    break;

                case 3:

                    inventory.displayInventory();
                    break;

                case 4:

                    running = false;
                    System.out.println("Exiting System.");
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }

        scanner.close();
    }
}