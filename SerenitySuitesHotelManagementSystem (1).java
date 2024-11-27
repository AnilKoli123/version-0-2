import java.util.Scanner;
import java.util.Vector;

public class SerenitySuitesHotelManagementSystem {
    private Vector<Room> rooms = new Vector<>();
    private Vector<Booking> bookings = new Vector<>();
    private Scanner scanner = new Scanner(System.in);
    private boolean isFirstRun = true;

    public SerenitySuitesHotelManagementSystem() {
        for (int i = 101; i <= 115; i++) {
            rooms.add(new Room(i, (i % 3 == 0) ? "Suite" : (i % 2 == 0) ? "Double" : "Single"));
        }
    }

    public void bookRoom() {
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();
        System.out.print("Enter contact: ");
        String contact = scanner.nextLine();
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.println("Available rooms:");
        rooms.stream().filter(Room::isAvailable).forEach(System.out::println);

        Vector<Room> bookedRooms = new Vector<>();
        double totalAmount = 0;

        while (true) {
            System.out.print("Enter room number to book (or 0 to finish): ");
            int roomNumber = scanner.nextInt();
            if (roomNumber == 0) break;

            Room room = rooms.stream().filter(r -> r.getRoomNumber() == roomNumber && r.isAvailable()).findFirst().orElse(null);
            if (room != null) {
                bookedRooms.add(room);
                room.setAvailable(false);
                totalAmount += room.getPrice();
                System.out.println("Room " + roomNumber + " booked successfully.");
            } else {
                System.out.println("Room not available or invalid room number.");
            }
        }

        if (!bookedRooms.isEmpty()) {
            bookings.add(new Booking(bookedRooms, new Customer(name, contact, address, email)));
            System.out.println("\nBooking Complete!");
            System.out.println("Customer Details: " + name + ", Contact: " + contact + ", Address: " + address + ", Email: " + email);
            System.out.println("Booked Rooms: ");
            bookedRooms.forEach(room -> System.out.println("Room " + room.getRoomNumber() + " (" + room.getType() + ") - Price: " + room.getPrice()));
            System.out.println("Total MRP: " + totalAmount);
        } else {
            System.out.println("No rooms booked. Please book at least one room.");
        }
        scanner.nextLine();
    }

    public void viewAvailableRooms() {
        System.out.println("Available rooms:");
        rooms.stream().filter(Room::isAvailable).forEach(System.out::println);
    }

    public void cancelBooking() {
        System.out.print("Enter customer name to cancel booking: ");
        String name = scanner.nextLine();
        bookings.removeIf(booking -> {
            if (booking.getCustomer().getName().equalsIgnoreCase(name)) {
                booking.getRooms().forEach(room -> room.setAvailable(true));
                System.out.println("Booking canceled.");
                return true;
            }
            return false;
        });
    }

    public void searchBookingByCustomerName() {
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();
        boolean bookingFound = false;

        for (Booking booking : bookings) {
            if (booking.getCustomer().getName().equalsIgnoreCase(name)) {
                System.out.println(booking);
                bookingFound = true;
            }
        }

        if (!bookingFound) {
            System.out.println("No booking found for customer: " + name);
        }
    }

    public void updateCustomerDetails() {
        System.out.print("Enter customer name to update details: ");
        String name = scanner.nextLine();
        bookings.stream()
                .filter(booking -> booking.getCustomer().getName().equalsIgnoreCase(name))
                .findFirst()
                .ifPresentOrElse(booking -> {
                    Customer customer = booking.getCustomer();
                    System.out.print("New contact: ");
                    customer.setContact(scanner.nextLine());
                    System.out.print("New address: ");
                    customer.setAddress(scanner.nextLine());
                    System.out.print("New email: ");
                    customer.setEmail(scanner.nextLine());
                    System.out.println("Details updated.");
                }, () -> System.out.println("Booking not found."));
    }

    public void viewRoomDetails() {
        System.out.print("Enter room number: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine();
        rooms.stream().filter(room -> room.getRoomNumber() == roomNumber)
                .findFirst()
                .ifPresentOrElse(System.out::println, () -> System.out.println("Room not found."));
    }

    public void updateRoomType() {
        System.out.print("Enter room number: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine();
        rooms.stream().filter(room -> room.getRoomNumber() == roomNumber)
                .findFirst()
                .ifPresentOrElse(room -> {
                    System.out.print("New room type: ");
                    room.setType(scanner.nextLine());
                    System.out.println("Room type updated.");
                }, () -> System.out.println("Room not found."));
    }

    public void generateInvoice() {
        System.out.print("Enter customer name for invoice: ");
        String name = scanner.nextLine();
        bookings.stream()
                .filter(booking -> booking.getCustomer().getName().equalsIgnoreCase(name))
                .forEach(booking -> {
                    System.out.println("\nINVOICE for Serenity Suites Hotel\n" + booking);
                });
    }

    public void listAllRooms() {
        rooms.forEach(System.out::println);
    }

    public static void main(String[] args) {
        SerenitySuitesHotelManagementSystem hms = new SerenitySuitesHotelManagementSystem();
        Scanner scanner = new Scanner(System.in);
        int choice;

        if (hms.isFirstRun) {
            System.out.println("Welcome to Serenity Suites Hotel");
            hms.isFirstRun = false;
        }

        do {
            System.out.println("\n1. Book Room\n2. View Available Rooms\n3. Cancel Booking\n4. Search Booking by Customer\n5. Update Customer Details\n6. View Room Details\n7. Update Room Type\n8. Generate Invoice\n9. List All Rooms\n10. Exit");
            System.out.print("Choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: hms.bookRoom(); break;
                case 2: hms.viewAvailableRooms(); break;
                case 3: hms.cancelBooking(); break;
                case 4: hms.searchBookingByCustomerName(); break;
                case 5: hms.updateCustomerDetails(); break;
                case 6: hms.viewRoomDetails(); break;
                case 7: hms.updateRoomType(); break;
                case 8: hms.generateInvoice(); break;
                case 9: hms.listAllRooms(); break;
                case 10: 
                    System.out.println("Thank you for visiting Serenity Suites Hotel!"); 
                    break;
                default: System.out.println("Invalid choice.");
            }
        } while (choice != 10);
        scanner.close();
    }
}

class Room {
    private int roomNumber;
    private String type;
    private boolean available;
    private double price;

    public Room(int roomNumber, String type) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.available = true;
        switch (type) {
            case "Single":
                this.price = 1000;
                break;
            case "Double":
                this.price = 1700;
                break;
            case "Suite":
                this.price = 3000;
                break;
            default:
                this.price = 0;
        }
    }

    public int getRoomNumber() { return roomNumber; }
    public String getType() { return type; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public void setType(String type) { this.type = type; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return "Room " + roomNumber + " (" + type + ") - Price: " + price + " Available: " + (available ? "Yes" : "No");
    }
}

class Customer {
    private String name;
    private String contact;
    private String address;
    private String email;

    public Customer(String name, String contact, String address, String email) {
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.email = email;
    }

    public String getName() { return name; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

class Booking {
    private Vector<Room> rooms;
    private Customer customer;

    public Booking(Vector<Room> rooms, Customer customer) {
        this.rooms = rooms;
        this.customer = customer;
    }

    public Customer getCustomer() { return customer; }
    public Vector<Room> getRooms() { return rooms; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Customer: ").append(customer.getName()).append("\nRooms: ");
        rooms.forEach(room -> sb.append(room.getRoomNumber()).append(" "));
        return sb.toString();
    }
}
