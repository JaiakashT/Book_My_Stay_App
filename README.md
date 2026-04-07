# 🏨 Book My Stay App

## 📌 Overview
The **Book My Stay App** is a Java-based console application designed to simulate a **Hotel Booking Management System**.  
This project demonstrates how **Core Java, Data Structures, and Object-Oriented Design** are used to build scalable and real-world booking systems.

The application is developed incrementally through multiple use cases, each introducing a new concept such as inventory management, booking workflows, validation, concurrency, and persistence.

---

## 🎯 Objective
The goal of this project is to:

- Apply **data structures in real-world scenarios**
- Build a **modular and scalable system**
- Demonstrate **clean object-oriented design**
- Simulate **real hotel booking workflows**
- Understand system evolution from simple to advanced design

---

## 🧠 Concepts Covered

### 🔹 Core Java
- Classes & Objects
- Inheritance & Abstraction
- Encapsulation
- Exception Handling
- File Handling (Serialization)

### 🔹 Data Structures
- HashMap (Inventory Management)
- Queue (Booking Requests - FIFO)
- Set (Unique Room Allocation)
- List (Booking History)
- Stack (Cancellation Rollback)

### 🔹 Advanced Concepts
- Multithreading (Concurrency)
- Synchronization (Thread Safety)
- Defensive Programming
- System Design Principles
- Persistence & Recovery

---

## ⚙️ Use Cases Implemented (UC1–UC12)

| Use Case | Description |
|--------|------------|
| UC1 | Application Entry & Welcome Message |
| UC2 | Room Modeling using OOP |
| UC3 | Centralized Inventory using HashMap |
| UC4 | Room Search (Read-Only Access) |
| UC5 | Booking Request Queue (FIFO) |
| UC6 | Reservation Confirmation & Allocation |
| UC7 | Add-On Services (One-to-Many Mapping) |
| UC8 | Booking History & Reporting |
| UC9 | Error Handling & Validation |
| UC10 | Booking Cancellation & Rollback |
| UC11 | Concurrent Booking Simulation |
| UC12 | Data Persistence & Recovery |

---

## 🏗️ Project Structure
Book_My_Stay_App/
│
├── src/
│ └── HotelBookingApp.java
│
├── booking.dat (generated at runtime)
│
└── README.md

## ▶️ How to Run

### 🔹 Compile
```bash
javac src/HotelBookingApp.java
🔹 Run
java -cp src HotelBookingApp
💡 Sample Output
BOOK MY STAY SYSTEM v12.0

Single | Beds: 1 | Price: $100.0
Double | Beds: 2 | Price: $180.0
Suite | Beds: 3 | Price: $300.0

Inventory:
Single -> 3
Double -> 2
Suite -> 1

Booking request from Alice
Reservation confirmed for Alice | Single-12345

Add-on cost: 60.0

Booking History:
Alice booked Single

Cancelled reservation R1

Processing concurrent booking for Bob
Recovered:
Alice booked Single
🚀 Key Highlights
✔ Real-world hotel booking simulation
✔ Covers complete booking lifecycle
✔ Demonstrates proper use of data structures
✔ Thread-safe booking implementation
✔ Clean separation of concerns
✔ Extensible and scalable design

🔄 Git Workflow Used
main → Final stable version

develop → Integration branch

feature/UCx → Each use case implemented separately

📚 Learning Outcome
After completing this project, you will:

Understand how data structures solve real problems

Learn system design thinking

Build modular and maintainable applications

Gain confidence for technical interviews & viva

👨‍💻 Author
Jai Akash T

⭐ If you like this project
Give it a ⭐ on GitHub!
