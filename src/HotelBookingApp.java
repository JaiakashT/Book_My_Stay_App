import java.util.*;
import java.io.*;

/**
 * Hotel Booking Management System
 * Book My Stay App
 * Demonstrates UC1–UC12
 */

abstract class Room {

    protected String type;
    protected int beds;
    protected double price;

    public Room(String type, int beds, double price) {
        this.type = type;
        this.beds = beds;
        this.price = price;
    }

    public void displayDetails() {
        System.out.println(type + " | Beds: " + beds + " | Price: $" + price);
    }
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single",1,100);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double",2,180);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite",3,300);
    }
}

/* =========================
   UC3 Inventory
   ========================= */

class RoomInventory {

    HashMap<String,Integer> availability = new HashMap<>();

    public RoomInventory() {
        availability.put("Single",3);
        availability.put("Double",2);
        availability.put("Suite",1);
    }

    public int getAvailability(String type){
        return availability.getOrDefault(type,0);
    }

    public void decrementRoom(String type){
        availability.put(type, availability.get(type)-1);
    }

    public void incrementRoom(String type){
        availability.put(type, availability.get(type)+1);
    }

    public void displayInventory(){

        System.out.println("\nInventory:");

        for(String key : availability.keySet()){
            System.out.println(key+" -> "+availability.get(key));
        }
    }
}

/* =========================
   Reservation
   ========================= */

class Reservation implements Serializable {

    String guestName;
    String roomType;

    public Reservation(String guestName,String roomType){
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

/* =========================
   UC5 & UC6 Booking Service
   ========================= */

class BookingService {

    Queue<Reservation> queue = new LinkedList<>();
    Set<String> allocatedIds = new HashSet<>();
    HashMap<String,Set<String>> allocations = new HashMap<>();

    RoomInventory inventory;

    public BookingService(RoomInventory inventory){
        this.inventory = inventory;
    }

    public void addRequest(Reservation r){
        queue.add(r);
        System.out.println("Booking request from "+r.guestName);
    }

    public void processRequests(){

        while(!queue.isEmpty()){

            Reservation r = queue.poll();

            if(inventory.getAvailability(r.roomType)>0){

                String roomId = r.roomType+"-"+UUID.randomUUID().toString().substring(0,5);

                allocatedIds.add(roomId);

                allocations.computeIfAbsent(r.roomType,k->new HashSet<>()).add(roomId);

                inventory.decrementRoom(r.roomType);

                System.out.println("Reservation confirmed for "+r.guestName+" | "+roomId);

            }else{

                System.out.println("No rooms available for "+r.guestName);
            }
        }
    }

    public void displayAllocations(){

        System.out.println("\nAllocations:");

        for(String type: allocations.keySet()){
            System.out.println(type+" -> "+allocations.get(type));
        }
    }
}

/* =========================
   UC7 Add-on Services
   ========================= */

class AddOnService{

    String name;
    double price;

    public AddOnService(String name,double price){
        this.name=name;
        this.price=price;
    }
}

class AddOnServiceManager{

    Map<String,List<AddOnService>> serviceMap = new HashMap<>();

    public void addService(String id,AddOnService service){
        serviceMap.computeIfAbsent(id,k->new ArrayList<>()).add(service);
    }

    public double calculateCost(String id){

        double total=0;

        List<AddOnService> list = serviceMap.get(id);

        if(list!=null){
            for(AddOnService s:list)
                total+=s.price;
        }

        return total;
    }
}

/* =========================
   UC8 Booking History
   ========================= */

class BookingHistory{

    List<Reservation> history = new ArrayList<>();

    public void addReservation(Reservation r){
        history.add(r);
    }

    public void displayHistory(){

        System.out.println("\nBooking History:");

        for(Reservation r:history){
            System.out.println(r.guestName+" booked "+r.roomType);
        }
    }
}

/* =========================
   UC9 Validation
   ========================= */

class InvalidBookingException extends Exception{

    public InvalidBookingException(String msg){
        super(msg);
    }
}

class BookingValidator{

    public static void validate(String type) throws InvalidBookingException{

        if(!type.equals("Single") && !type.equals("Double") && !type.equals("Suite")){
            throw new InvalidBookingException("Invalid Room Type");
        }
    }
}

/* =========================
   UC10 Cancellation
   ========================= */

class CancellationService{

    Stack<String> cancelled = new Stack<>();

    public void cancel(String id){

        cancelled.push(id);

        System.out.println("Cancelled reservation "+id);
    }
}

/* =========================
   UC11 Concurrency
   ========================= */

class ConcurrentProcessor{

    Queue<Reservation> queue = new LinkedList<>();

    public synchronized void add(Reservation r){
        queue.add(r);
    }

    public synchronized Reservation process(){
        return queue.poll();
    }
}

class BookingThread extends Thread{

    ConcurrentProcessor processor;

    public BookingThread(ConcurrentProcessor p){
        processor=p;
    }

    public void run(){

        Reservation r = processor.process();

        if(r!=null)
            System.out.println("Processing concurrent booking for "+r.guestName);
    }
}

/* =========================
   UC12 Persistence
   ========================= */

class PersistenceService{

    public static void save(List<Reservation> history){

        try{

            ObjectOutputStream out =
                    new ObjectOutputStream(new FileOutputStream("booking.dat"));

            out.writeObject(history);

            out.close();

        }catch(Exception e){
            System.out.println("Save error");
        }
    }

    public static List<Reservation> load(){

        try{

            ObjectInputStream in =
                    new ObjectInputStream(new FileInputStream("booking.dat"));

            List<Reservation> data = (List<Reservation>) in.readObject();

            in.close();

            return data;

        }catch(Exception e){

            return new ArrayList<>();
        }
    }
}

/* =========================
   MAIN APPLICATION
   ========================= */

public class HotelBookingApp {

    public static void main(String[] args) {

        /* UC1 Welcome */

        System.out.println("BOOK MY STAY SYSTEM v12.0");

        /* UC2 Rooms */

        Room single = new SingleRoom();
        Room dbl = new DoubleRoom();
        Room suite = new SuiteRoom();

        single.displayDetails();
        dbl.displayDetails();
        suite.displayDetails();

        /* UC3 Inventory */

        RoomInventory inventory = new RoomInventory();
        inventory.displayInventory();

        /* UC5 Booking Requests */

        BookingService service = new BookingService(inventory);

        service.addRequest(new Reservation("Alice","Single"));
        service.addRequest(new Reservation("Bob","Double"));
        service.addRequest(new Reservation("Charlie","Suite"));

        /* UC6 Processing */

        service.processRequests();

        inventory.displayInventory();
        service.displayAllocations();

        /* UC7 Add-ons */

        AddOnServiceManager addOns = new AddOnServiceManager();

        addOns.addService("R1",new AddOnService("Breakfast",20));
        addOns.addService("R1",new AddOnService("Airport Pickup",40));

        System.out.println("Add-on cost: "+addOns.calculateCost("R1"));

        /* UC8 History */

        BookingHistory history = new BookingHistory();
        history.addReservation(new Reservation("Alice","Single"));
        history.displayHistory();

        /* UC9 Validation */

        try{
            BookingValidator.validate("Single");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        /* UC10 Cancel */

        CancellationService cancel = new CancellationService();
        cancel.cancel("R1");

        /* UC11 Concurrency */

        ConcurrentProcessor processor = new ConcurrentProcessor();

        processor.add(new Reservation("Bob","Double"));
        processor.add(new Reservation("Charlie","Suite"));

        new BookingThread(processor).start();
        new BookingThread(processor).start();

        /* UC12 Persistence */

        PersistenceService.save(history.history);

        List<Reservation> restored = PersistenceService.load();

        System.out.println("\nRecovered:");

        for(Reservation r:restored)
            System.out.println(r.guestName+" booked "+r.roomType);
    }
}