import java.util.Scanner;

public class Simulation {

    private static final int NUM_NODES = 3;
    private static final String SHARED_RESOURCE = "Shared Resource";
    private static final ThreadGroup threadGroup = new ThreadGroup("MyThreadGroup");

    public static void main(String[] args) {
        displayMenu();
    }

    static void displayMenu() {

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n\nChoose the scenario: ");
                System.out.println("1 - Multiple simultaneous nodes (full concurrency)");
                System.out.println("2 - Correct use of the semaphore");
                System.out.println("3 - Incorrect use of the semaphore");
                System.out.println("0 - Exit");
                System.out.print("\nEnter your choice: ");

                int scenario = scanner.nextInt();
                scanner.nextLine();

                System.out.println();

                switch (scenario) {
                    case 0:
                        System.out.println("Ending the simulation.");
                        return;
                    case 1:
                        executeScenario1();
                        break;
                    case 2:
                        executeScenario2();
                        break;
                    case 3:
                        executeScenario3();
                        break;
                    default:
                        System.out.println("Invalid scenario. Choose 0, 1, 2, or 3.");
                }
                waitForThreadsToFinish();
            }
        }
    }

    static void executeScenario1() {

        for (int i = 0; i < NUM_NODES; i++) {
            Thread thread = new Thread(threadGroup, new NodeBehavior(i));
            thread.start();
        }
    }

    static void executeScenario2() {

        BinarySemaphore semaphore = new BinarySemaphore(1);

        for (int i = 0; i < NUM_NODES; i++) {
            Thread thread = new Thread(threadGroup, new SynchronizedBehavior(i, semaphore));
            thread.start();
        }
    }

    static void executeScenario3() {

        BinarySemaphore semaphore = new BinarySemaphore(1);
        for (int i = 0; i < NUM_NODES; i++) {
            Thread thread = new Thread(threadGroup, new IncorrectNodeBehavior(i, semaphore));
            thread.start();
        }
    }

    static void waitForThreadsToFinish() {

        while (threadGroup.activeCount() > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class BinarySemaphore {

        private int value;

        public BinarySemaphore(int initial) {
            value = initial;
        }

        public synchronized void acquire() throws InterruptedException {
            while (value <= 0) {
                wait();
            }
            value--;
        }

        public synchronized void release() {
            value++;
            notify();
        }
    }

    static class NodeBehavior implements Runnable {

        private final int nodeId;

        public NodeBehavior(int nodeId) {
            this.nodeId = nodeId;
        }

        @Override
        public void run() {
            System.out.println("Node " + nodeId + " trying to access shared resource.");
            try {
                Thread.sleep(1000);

                System.out.println("Node " + nodeId + " accessing the shared resource: " + SHARED_RESOURCE);
                Thread.sleep(2000);

                System.out.println("Node " + nodeId + " finished accessing the shared resource.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class IncorrectNodeBehavior implements Runnable {

        private final int nodeId;
        private final BinarySemaphore semaphore;

        public IncorrectNodeBehavior(int nodeId, BinarySemaphore semaphore) {
            this.nodeId = nodeId;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            System.out.println("Node " + nodeId + " trying to access shared resource.");
            try {
                Thread.sleep(1000);
                System.out.println("Node " + nodeId + " trying to acquire the semaphore.");
                semaphore.acquire();
                System.out.println("Node " + nodeId + " acquired the semaphore. Accessing the shared resource: " + SHARED_RESOURCE);
                Thread.sleep(2000);

                System.out.println("Node " + nodeId + " finished accessing the shared resource and did not release the semaphore (INCORRECT).");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class SynchronizedBehavior implements Runnable {
        
        private final int nodeId;
        private final BinarySemaphore semaphore;

        public SynchronizedBehavior(int nodeId, BinarySemaphore semaphore) {
            this.nodeId = nodeId;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            System.out.println("Node " + nodeId + " trying to access shared resource.");
            try {
                Thread.sleep(1000);

                System.out.println("Node " + nodeId + " trying to acquire the semaphore.");
                semaphore.acquire();
                System.out.println("Node " + nodeId + " acquired the semaphore. Accessing the shared resource: " + SHARED_RESOURCE);
                Thread.sleep(2000); 
                System.out.println("Node " + nodeId + " finished accessing the shared resource and released the semaphore.");
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}