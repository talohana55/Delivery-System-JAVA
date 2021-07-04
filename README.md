# Delivery-System-JAVA
Development Enviroment - Eclipse IDE

## Description
The system simulates the work of a courier company. The company has several branches, 
a sorting center and a head office. Each branch is associated with distribution 
vehicles - the branches have purchases of the Van type,
and the sorting center of stylish purchases "standard truck" and one additional
vehicle stylish "non-standard truck" for the benefit of transporting unusual cargo.
The system's customers produce delivery packages at random. 
Customers track packages using a tracking.txt file

## Multi-Threads
The system was built in a multi-process environment, the system works synchronously between all the objects.
* Thread-Pool - limited customer creation to 2 at the same time until the end of their operation.
* Observer / Listeners- Each branch monitors its vans that report status on each change of condition.
* PropertyChangeListener - Each branch reports to a sorting center about a package that needs to be collected.
* Read/Write Lock - Any customer at the time can read from a file to check package status,
Sort Center writes to file to update package status(prevention of race condition).

## Design Patterns
* Prototype - The system can expand and replicate a new, independent branch on top of another prototype branch.
* Singletone - A main office object is created only once, initialized in a voletile variable and has a private constructor.
* Using the principle - Double-Check-Locking.
* Memento - The system can be restored to a specific point in time after branch duplication(clone option).

## GUI - System View
* Using the JAVA GUI view
* Buttons to select
* Displays a simulation of the system operation from the beginning of the operation until all packages are shipped.
