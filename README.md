# Discrete Event Simulator

This project is a Discrete Event Simulator designed to model events and processes in a controlled simulation environment. The simulator is written in Java and aims to handle a variety of events, manage queues, and simulate outcomes based on defined rules and conditions.

## Project Structure

![image](https://github.com/user-attachments/assets/beee7b85-9398-481c-8ff6-6286991c5e73)


The simulator comprises several key components:

- **Event**: The basic unit of the simulation. Events are triggered and processed in sequence based on their defined times and dependencies.
- **Event Queue**: A immutable priority queue that holds and manages events according to their scheduled times, ensuring that events are processed in the correct order.
- **Entities**: Objects that interact within the simulation environment, such as servers or customers, which are defined based on the simulation’s needs.
- **Simulation Engine**: Controls the flow of the simulation, processing events in the event queue, updating states, and generating new events as needed.

## How It Works

1. **Initialize Simulation**: The simulation is initialized with a set of parameters, including the number of entities, event types, and time constraints.
2. **Generate Events**: Initial events are created and added to the event queue.
3. **Process Events**: The simulation engine processes each event in the queue, updating entity states and generating new events if necessary.
4. **End Simulation**: The simulation ends when all events are processed or a specified condition is met.

## Requirements

- Java Development Kit (JDK) 8 or higher

## Running the Simulator

To run the simulator, compile the Java files and execute the main class:
```bash
javac *.java
java Main
