package CabManagement;

import java.util.*;

class User {
    String name;
    public User(String name) { this.name = name; }
}

class Driver {
    String name;
    boolean available = true;
    public Driver(String name) { this.name = name; }
}

class Ride {
    String user;
    String driver;
    String pickup;
    String destination;
    double distance;
    double fare;
    String status;
    Date date;

    public Ride(String user, String driver, String pickup, String destination, double distance) {
        this.user = user;
        this.driver = driver;
        this.pickup = pickup;
        this.destination = destination;
        this.distance = distance;
        this.fare = distance * 15;
        this.status = "Ongoing";
        this.date = new Date();
    }
}

public class Main {
    static Scanner sc = new Scanner(System.in);
    static List<User> users = new ArrayList<>();
    static List<Driver> drivers = new ArrayList<>();
    static List<Ride> rides = new ArrayList<>();

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n=== CAB MANAGEMENT SYSTEM ===");
            System.out.println("1. Add User");
            System.out.println("2. Add Driver");
            System.out.println("3. View Available Drivers");
            System.out.println("4. Book Ride");
            System.out.println("5. Complete Ride");
            System.out.println("6. Cancel Ride");
            System.out.println("7. Search Ride by User");
            System.out.println("8. View All Rides");
            System.out.println("9. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt(); sc.nextLine();

            switch (choice) {
                case 1 -> addUser();
                case 2 -> addDriver();
                case 3 -> showAvailableDrivers();
                case 4 -> bookRide();
                case 5 -> completeRide();
                case 6 -> cancelRide();
                case 7 -> searchRideByUser();
                case 8 -> showAllRides();
                case 9 -> System.out.println("Thank you for using the system!");
                default -> System.out.println("Invalid choice!");
            }
        } while (choice != 9);
    }

    static void addUser() {
        System.out.print("Enter user name: ");
        users.add(new User(sc.nextLine()));
        System.out.println("User added!");
    }

    static void addDriver() {
        System.out.print("Enter driver name: ");
        drivers.add(new Driver(sc.nextLine()));
        System.out.println("Driver added!");
    }

    static void showAvailableDrivers() {
        System.out.println("\nAvailable Drivers:");
        boolean found = false;
        for (Driver d : drivers) {
            if (d.available) {
                System.out.println("- " + d.name);
                found = true;
            }
        }
        if (!found) System.out.println("No drivers available currently.");
    }

    static void bookRide() {
        System.out.print("Enter username: ");
        String uname = sc.nextLine();
        System.out.print("Enter pickup: ");
        String pickup = sc.nextLine();
        System.out.print("Enter destination: ");
        String dest = sc.nextLine();
        System.out.print("Enter distance (km): ");
        double distance = sc.nextDouble();
        sc.nextLine();

        Driver availableDriver = drivers.stream().filter(d -> d.available).findFirst().orElse(null);
        if (availableDriver == null) {
            System.out.println("No available drivers at the moment!");
            return;
        }

        availableDriver.available = false;
        rides.add(new Ride(uname, availableDriver.name, pickup, dest, distance));
        System.out.println("Ride booked successfully with driver: " + availableDriver.name);
    }

    static void completeRide() {
        System.out.print("Enter driver name to complete ride: ");
        String dname = sc.nextLine();
        for (Ride r : rides) {
            if (r.driver.equals(dname) && r.status.equals("Ongoing")) {
                r.status = "Completed";
                drivers.stream().filter(d -> d.name.equals(dname)).forEach(d -> d.available = true);
                System.out.println("Ride completed successfully! Fare: ₹" + r.fare);
                return;
            }
        }
        System.out.println("No ongoing ride for this driver.");
    }

    static void cancelRide() {
        System.out.print("Enter username to cancel ride: ");
        String uname = sc.nextLine();
        for (Ride r : rides) {
            if (r.user.equals(uname) && r.status.equals("Ongoing")) {
                r.status = "Cancelled";
                drivers.stream().filter(d -> d.name.equals(r.driver)).forEach(d -> d.available = true);
                System.out.println("Ride cancelled!");
                return;
            }
        }
        System.out.println("No ongoing ride for this user.");
    }

    static void searchRideByUser() {
        System.out.print("Enter username: ");
        String uname = sc.nextLine();
        rides.stream().filter(r -> r.user.equals(uname))
                .forEach(r -> System.out.println(r.date + " | " + r.user + " | " + r.pickup + " -> " + r.destination + " | " + r.status + " | ₹" + r.fare));
    }

    static void showAllRides() {
        if (rides.isEmpty()) System.out.println("No rides found!");
        else rides.forEach(r ->
                System.out.println(r.date + " | " + r.user + " | " + r.driver + " | " + r.pickup + " -> " + r.destination + " | " + r.status + " | ₹" + r.fare));
    }
}
