import java.io.*;
import java.util.*;

// Reservation class
class Reservation implements Serializable {

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

    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType;
    }
}

// Inventory class
class RoomInventory implements Serializable {

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
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

    public void displayInventory() {

        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " Rooms Available: " + inventory.get(type));
        }
    }
}

// System State class
class SystemState implements Serializable {

    List<Reservation> reservations;
    RoomInventory inventory;

    public SystemState(List<Reservation> reservations, RoomInventory inventory) {
        this.reservations = reservations;
        this.inventory = inventory;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "hotel_state.dat";

    public static void saveState(SystemState state) {

        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            out.writeObject(state);
            System.out.println("System state saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving system state.");
        }
    }

    public static SystemState loadState() {

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            SystemState state = (SystemState) in.readObject();
            System.out.println("System state restored successfully.");
            return state;

        } catch (Exception e) {

            System.out.println("No saved state found. Starting fresh system.");

            return new SystemState(
                    new ArrayList<>(),
                    new RoomInventory()
            );
        }
    }
}

// Main Class (Renamed)
public class BookMyStayApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        SystemState state = PersistenceService.loadState();

        List<Reservation> reservations = state.reservations;
        RoomInventory inventory = state.inventory;

        boolean running = true;

        while (running) {

            System.out.println("\n=== Hotel Booking System ===");
            System.out.println("1. Create Booking");
            System.out.println("2. View Reservations");
            System.out.println("3. View Inventory");
            System.out.println("4. Save & Exit");

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

                    if (!inventory.allocateRoom(room)) {
                        System.out.println("Booking failed. Room not available.");
                        break;
                    }

                    Reservation reservation =
                            new Reservation(id, guest, room);

                    reservations.add(reservation);

                    System.out.println("Booking confirmed.");
                    break;

                case 2:

                    if (reservations.isEmpty()) {
                        System.out.println("No reservations found.");
                    } else {
                        for (Reservation r : reservations) {
                            System.out.println(r);
                        }
                    }

                    break;

                case 3:
                    inventory.displayInventory();
                    break;

                case 4:

                    PersistenceService.saveState(
                            new SystemState(reservations, inventory)
                    );

                    running = false;
                    System.out.println("System shutting down.");
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }

        scanner.close();
    }
}