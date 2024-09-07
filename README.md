# Simulation of Node Behavior with Semaphores

This Java project simulates multiple nodes accessing a shared resource, using various synchronization techniques. The project demonstrates how threads behave in concurrent scenarios and how semaphores can be used correctly or incorrectly to control access to a shared resource.

## Description

The simulation includes three scenarios:
1. **Multiple simultaneous nodes (full concurrency)**: All nodes access the shared resource concurrently without any control mechanism.
2. **Correct use of the semaphore**: Nodes use a binary semaphore to ensure only one node accesses the shared resource at a time, and the semaphore is properly released.
3. **Incorrect use of the semaphore**: Nodes acquire the semaphore but do not release it, causing incorrect behavior in the system.

## Scenarios

1. **Multiple Simultaneous Nodes (Full Concurrency)**  
   In this scenario, all nodes try to access the shared resource concurrently, leading to possible race conditions.

2. **Correct Use of Semaphore**  
   Nodes are synchronized using a binary semaphore. Only one node can access the shared resource at a time, and the semaphore is properly released after use.

3. **Incorrect Use of Semaphore**  
   Nodes acquire the semaphore but fail to release it after accessing the shared resource, simulating a deadlock or resource lock issue.

## Installation

1. Clone the repository or download the source code.
2. Compile the Java files:
   ```bash
   javac Simulation.java
   ```
3. Run the program:
    ```bash
    java Simulation
    ```

## Usage
When running the program, you'll be presented with a menu to choose between the available scenarios:

- Scenario 1 - Multiple simultaneous nodes (full concurrency).
- Scenario 2 - Correct use of semaphore.
- Scenario 3 - Incorrect use of semaphore.
- Exit - Exits the simulation.

You can select a scenario by entering the corresponding number.

## Contribution

Feel free to open issues or submit pull requests. All contributions are welcome!

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.