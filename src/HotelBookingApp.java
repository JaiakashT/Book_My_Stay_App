import java.util.*;

/**
 * Hotel Booking Management System
 * Book My Stay App
 *
 * Demonstrates use cases UC1–UC6 using Core Java and Data Structures.
 *
 * @author Jai Akash
 * @version 6.0
 */

/* =========================
   UC2: Room Domain Model
   ========================= */

abstract class Room {

    protected String type;
    protected int beds;
    protected double price;

    public Room(String type, int beds, double price) {
        this.type = type;
        this.beds = beds;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public void displayDetails() {
        System.out.println(type + " | Beds: " + beds + " | Price: $" + price);
    }
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single", 1, 100);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double", 2, 180);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite", 3, 300);
    }
}


/* =========================
   UC3: Inventory Management
   ========================= */

class RoomInventory {

    private HashMap<String, Integer> availability = new HashMap<>();

    public RoomInventory() {
        availability.put("Single", 3);
        availability.put("Double", 2);
        availability.put("Suite", 1);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }

    public void decrementRoom(String type) {
        availability.put(type, availability.get(type) - 1);
    }

    public void displayInventory() {

        System.out.println("\nCurrent Room Inventory:");

        for (String key : availability.keySet()) {
            System.out.println(key + " rooms available: " + availability.get(key));
        }
    }
}


/* =========================
   UC5: Reservation Request
   ========================= */

class Reservation {

    String guestName;
    String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}


/* =========================
   UC6: Booking Service
   ========================= */

class BookingService {

    private Queue<Reservation> requestQueue = new LinkedList<>();
    private Set<String> allocatedRoomIds = new HashSet<>();
    private HashMap<String, Set<String>> roomAllocations = new HashMap<>();

    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void addRequest(Reservation reservation) {

        requestQueue.add(reservation);
        System.out.println("Booking request received from " + reservation.guestName +
                " for " + reservation.roomType + " room.");
    }

    public void processRequests() {

        while (!requestQueue.isEmpty()) {

            Reservation req = requestQueue.poll();

            if (inventory.getAvailability(req.roomType) > 0) {

                String roomId = req.roomType + "-" + UUID.randomUUID().toString().substring(0, 5);

                allocatedRoomIds.add(roomId);

                roomAllocations
                        .computeIfAbsent(req.roomType, k -> new HashSet<>())
                        .add(roomId);

                inventory.decrementRoom(req.roomType);

                System.out.println("Reservation confirmed for "
                        + req.guestName + " | Room ID: " + roomId);

            } else {
                System.out.println("No rooms available for "
                        + req.roomType + ". Request rejected.");
            }
        }
    }

    public void displayAllocations() {

        System.out.println("\nAllocated Rooms:");

        for (String type : roomAllocations.keySet()) {
            System.out.println(type + " -> " + roomAllocations.get(type));
        }
    }
}


/* =========================
   MAIN APPLICATION
   ========================= */

public class HotelBookingApp {

    public static void main(String[] args) {

        /* =========================
           UC1: Application Start
           ========================= */

        System.out.println("=====================================");
        System.out.println("      BOOK MY STAY HOTEL SYSTEM");
        System.out.println("           Version 6.0");
        System.out.println("=====================================");

        /* =========================
           UC2: Room Initialization
           ========================= */

        Room single = new SingleRoom();
        Room dbl = new DoubleRoom();
        Room suite = new SuiteRoom();

        System.out.println("\nRoom Types:");
        single.displayDetails();
        dbl.displayDetails();
        suite.displayDetails();

        /* =========================
           UC3: Inventory Setup
           ========================= */

        RoomInventory inventory = new RoomInventory();
        inventory.displayInventory();

        /* =========================
           UC4: Room Search
           ========================= */

        System.out.println("\nAvailable Rooms:");

        if (inventory.getAvailability("Single") > 0)
            single.displayDetails();

        if (inventory.getAvailability("Double") > 0)
            dbl.displayDetails();

        if (inventory.getAvailability("Suite") > 0)
            suite.displayDetails();

        /* =========================
           UC5: Booking Request Queue
           ========================= */

        BookingService bookingService = new BookingService(inventory);

        bookingService.addRequest(new Reservation("Alice", "Single"));
        bookingService.addRequest(new Reservation("Bob", "Double"));
        bookingService.addRequest(new Reservation("Charlie", "Suite"));
        bookingService.addRequest(new Reservation("David", "Suite"));

        /* =========================
           UC6: Process Reservations
           ========================= */

        System.out.println("\nProcessing Reservations...\n");

        bookingService.processRequests();

        inventory.displayInventory();
        bookingService.displayAllocations();
    }
}