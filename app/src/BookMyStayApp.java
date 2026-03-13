import java.util.*;

// Represents an Add-On Service
class AddOnService {
    private String serviceName;
    private double price;

    public AddOnService(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return serviceName + " (₹" + price + ")";
    }
}

// Manages Add-On Services linked with reservations
class AddOnServiceManager {

    // ReservationID -> List of Services
    private Map<String, List<AddOnService>> reservationServices;

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    // Add service to reservation
    public void addService(String reservationId, AddOnService service) {

        reservationServices
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println(service.getServiceName() +
                " added to reservation " + reservationId);
    }

    // Display services for reservation
    public void displayServices(String reservationId) {

        List<AddOnService> services = reservationServices.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        System.out.println("\nServices for Reservation: " + reservationId);

        for (AddOnService service : services) {
            System.out.println("- " + service);
        }
    }

    // Calculate total additional cost
    public double calculateTotalCost(String reservationId) {

        List<AddOnService> services = reservationServices.get(reservationId);

        if (services == null)
            return 0;

        double total = 0;

        for (AddOnService service : services) {
            total += service.getPrice();
        }

        return total;
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        AddOnServiceManager manager = new AddOnServiceManager();

        System.out.println("==== Add-On Service Selection ====");

        System.out.print("Enter Reservation ID: ");
        String reservationId = scanner.nextLine();

        // Sample services
        AddOnService breakfast = new AddOnService("Breakfast", 500);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 1200);
        AddOnService spa = new AddOnService("Spa Access", 1500);
        AddOnService extraBed = new AddOnService("Extra Bed", 800);

        boolean running = true;

        while (running) {

            System.out.println("\nSelect Add-On Service");
            System.out.println("1. Breakfast");
            System.out.println("2. Airport Pickup");
            System.out.println("3. Spa Access");
            System.out.println("4. Extra Bed");
            System.out.println("5. View Selected Services");
            System.out.println("6. Calculate Total Add-On Cost");
            System.out.println("7. Exit");

            System.out.print("Choose option: ");
            int choice = scanner.nextInt();

            switch (choice) {

                case 1:
                    manager.addService(reservationId, breakfast);
                    break;

                case 2:
                    manager.addService(reservationId, airportPickup);
                    break;

                case 3:
                    manager.addService(reservationId, spa);
                    break;

                case 4:
                    manager.addService(reservationId, extraBed);
                    break;

                case 5:
                    manager.displayServices(reservationId);
                    break;

                case 6:
                    double total = manager.calculateTotalCost(reservationId);
                    System.out.println("Total Add-On Cost: ₹" + total);
                    break;

                case 7:
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice");
            }
        }

        scanner.close();
        System.out.println("Program Ended");
    }
}
