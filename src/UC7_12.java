import java.util.*;
import java.io.*;

/* =========================
   Reservation Model
   ========================= */
class Reservation implements Serializable {
    String reservationId;
    String guestName;
    String roomType;

    public Reservation(String id, String guestName, String roomType) {
        this.reservationId = id;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType;
    }
}

/* =========================
   UC7: Add-On Services
   ========================= */
class AddOnService {
    String name;
    double price;

    public AddOnService(String name, double price) {
        this.name = name;
        this.price = price;
    }
}

class AddOnServiceManager {

    Map<String, List<AddOnService>> servicesMap = new HashMap<>();

    public void addService(String reservationId, AddOnService service) {
        servicesMap.computeIfAbsent(reservationId, k -> new ArrayList<>()).add(service);
    }

    public double calculateTotalCost(String reservationId) {
        double total = 0;
        List<AddOnService> services = servicesMap.get(reservationId);

        if (services != null) {
            for (AddOnService s : services) {
                total += s.price;
            }
        }
        return total;
    }
}

/* =========================
   UC8: Booking History
   ========================= */
class BookingHistory {

    List<Reservation> history = new ArrayList<>();

    public void addReservation(Reservation r) {
        history.add(r);
    }

    public void showHistory() {
        System.out.println("\nBooking History:");
        for (Reservation r : history) {
            System.out.println(r);
        }
    }
}

/* =========================
   UC9: Error Handling
   ========================= */
class InvalidBookingException extends Exception {
    public InvalidBookingException(String msg) {
        super(msg);
    }
}

class BookingValidator {

    public static void validateRoomType(String roomType) throws InvalidBookingException {

        if (!roomType.equals("Single") &&
                !roomType.equals("Double") &&
                !roomType.equals("Suite")) {

            throw new InvalidBookingException("Invalid Room Type: " + roomType);
        }
    }
}

/* =========================
   UC10: Booking Cancellation
   ========================= */
class CancellationService {

    Stack<String> cancelledStack = new Stack<>();

    public void cancelReservation(String reservationId) {

        cancelledStack.push(reservationId);

        System.out.println("Reservation Cancelled: " + reservationId);
    }

    public void showCancelled() {

        System.out.println("\nCancelled Reservations:");

        for (String id : cancelledStack) {
            System.out.println(id);
        }
    }
}

/* =========================
   UC11: Concurrent Booking
   ========================= */
class ConcurrentBookingProcessor {

    Queue<Reservation> queue = new LinkedList<>();

    public synchronized void addRequest(Reservation r) {
        queue.add(r);
    }

    public synchronized Reservation processRequest() {
        return queue.poll();
    }
}

class BookingThread extends Thread {

    ConcurrentBookingProcessor processor;

    public BookingThread(ConcurrentBookingProcessor processor) {
        this.processor = processor;
    }

    public void run() {

        Reservation r = processor.processRequest();

        if (r != null) {
            System.out.println("Processing booking for " + r.guestName);
        }
    }
}

/* =========================
   UC12: Data Persistence
   ========================= */
class PersistenceService {

    public static void save(List<Reservation> history) {

        try {

            ObjectOutputStream out =
                    new ObjectOutputStream(new FileOutputStream("booking_data.dat"));

            out.writeObject(history);
            out.close();

            System.out.println("Data Saved Successfully");

        } catch (Exception e) {
            System.out.println("Error saving data");
        }
    }

    public static List<Reservation> load() {

        try {

            ObjectInputStream in =
                    new ObjectInputStream(new FileInputStream("booking_data.dat"));

            List<Reservation> list = (List<Reservation>) in.readObject();
            in.close();

            return list;

        } catch (Exception e) {

            System.out.println("No previous data found");
            return new ArrayList<>();
        }
    }
}

/* =========================
   MAIN PROGRAM
   ========================= */
public class UC7_12 {

    public static void main(String[] args) {

        try {

            /* UC9 Validation */
            BookingValidator.validateRoomType("Single");

            Reservation r1 = new Reservation("R101", "Alice", "Single");

            /* UC7 Add-On Service */
            AddOnServiceManager serviceManager = new AddOnServiceManager();

            serviceManager.addService("R101", new AddOnService("Breakfast", 25));
            serviceManager.addService("R101", new AddOnService("Airport Pickup", 50));

            System.out.println("Add-on Cost: " +
                    serviceManager.calculateTotalCost("R101"));

            /* UC8 Booking History */
            BookingHistory history = new BookingHistory();
            history.addReservation(r1);
            history.showHistory();

            /* UC10 Cancellation */
            CancellationService cancel = new CancellationService();
            cancel.cancelReservation("R101");
            cancel.showCancelled();

            /* UC11 Concurrent Booking */
            ConcurrentBookingProcessor processor = new ConcurrentBookingProcessor();

            processor.addRequest(new Reservation("R102","Bob","Double"));
            processor.addRequest(new Reservation("R103","Charlie","Suite"));

            new BookingThread(processor).start();
            new BookingThread(processor).start();

            /* UC12 Persistence */
            PersistenceService.save(history.history);

            List<Reservation> restored = PersistenceService.load();

            System.out.println("\nRecovered Bookings:");
            for (Reservation r : restored) {
                System.out.println(r);
            }

        } catch (InvalidBookingException e) {
            System.out.println(e.getMessage());
        }
    }
}