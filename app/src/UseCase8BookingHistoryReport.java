import java.util.*;

// Represents a Reservation
class Reservation {

    private String reservationId;
    private String guestName;
    private String roomType;
    private int nights;

    public Reservation(String reservationId, String guestName, String roomType, int nights) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
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

    public int getNights() {
        return nights;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room: " + roomType +
                ", Nights: " + nights;
    }
}

// Stores confirmed booking history
class BookingHistory {

    private List<Reservation> reservations;

    public BookingHistory() {
        reservations = new ArrayList<>();
    }

    // Add confirmed reservation
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    // Retrieve all reservations
    public List<Reservation> getReservations() {
        return reservations;
    }
}

// Generates reports from booking history
class BookingReportService {

    public void generateReport(List<Reservation> reservations) {

        if (reservations.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        System.out.println("\n=== Booking History Report ===");

        for (Reservation r : reservations) {
            System.out.println(r);
        }

        System.out.println("\nTotal Bookings: " + reservations.size());

        // Count room types
        Map<String, Integer> roomCount = new HashMap<>();

        for (Reservation r : reservations) {
            roomCount.put(r.getRoomType(),
                    roomCount.getOrDefault(r.getRoomType(), 0) + 1);
        }

        System.out.println("\nRoom Type Summary:");
        for (String room : roomCount.keySet()) {
            System.out.println(room + ": " + roomCount.get(room));
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        boolean running = true;

        while (running) {

            System.out.println("\n=== Booking History System ===");
            System.out.println("1. Confirm Booking");
            System.out.println("2. View Booking History");
            System.out.println("3. Generate Booking Report");
            System.out.println("4. Exit");

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
                    String room = scanner.nextLine();

                    System.out.print("Enter Nights: ");
                    int nights = scanner.nextInt();
                    scanner.nextLine();

                    Reservation reservation = new Reservation(id, guest, room, nights);

                    history.addReservation(reservation);

                    System.out.println("Booking Confirmed and Added to History.");

                    break;

                case 2:

                    System.out.println("\n=== Booking History ===");

                    List<Reservation> reservations = history.getReservations();

                    if (reservations.isEmpty()) {
                        System.out.println("No bookings available.");
                    } else {
                        for (Reservation r : reservations) {
                            System.out.println(r);
                        }
                    }

                    break;

                case 3:

                    reportService.generateReport(history.getReservations());

                    break;

                case 4:

                    running = false;
                    System.out.println("Exiting Program.");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }

        scanner.close();
    }
}