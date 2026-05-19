import java.util.concurrent.Semaphore;

public class Main {

    private static final int BUFFER_SIZE = 50;
    private static final Semaphore mutex = new Semaphore(1);
    private static final Semaphore consumerSemaphore = new Semaphore(0);
    private static final Semaphore producerSemaphore = new Semaphore(BUFFER_SIZE);
    private static Buffer buffer;

    static class ConsumerWork implements Runnable {
        private final Buffer buffer;
        private final int sleepTime;
        private final int id;

        public ConsumerWork(int id, Buffer buffer, int sleepTime) {
            this.id = id;
            this.buffer = buffer;
            this.sleepTime = sleepTime;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    consumerSemaphore.acquire();
                    mutex.acquire();
                    int item = buffer.remove();
                    System.out.println("Consumer " + id + " consumed item " + item);
                    producerSemaphore.release();
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    mutex.release();
                }
            }
        }
    }

    static class ProducerWork implements Runnable {
        private final Buffer buffer;
        private final int maxItems;
        private final int sleepTime;
        private final int id;

        public ProducerWork(int id, Buffer buffer, int maxItems, int sleepTime) {
            this.id = id;
            this.buffer = buffer;
            this.maxItems = maxItems;
            this.sleepTime = sleepTime;
        }

        @Override
        public void run() {
            for (int i = 0; i < maxItems; i++) {
                try {
                    producerSemaphore.acquire();
                    Thread.sleep(sleepTime);
                    int item = (int) (Math.random() * 100);
                    System.out.println("Producer " + id + " produced item " + item);
                    mutex.acquire();
                    buffer.put(item);
                    consumerSemaphore.release();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    mutex.release();
                }
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 5) {
            System.out.println(
                    "Use: java Main <num_producers> <max_items_per_producer> <producing_time> <num_consumers> <consuming_time>");
            return;
        }

        int numProducers = Integer.parseInt(args[0]);
        int maxItemsPerProducer = Integer.parseInt(args[1]);
        int producingTime = Integer.parseInt(args[2]);
        int numConsumers = Integer.parseInt(args[3]);
        int consumingTime = Integer.parseInt(args[4]);

        buffer = new Buffer();

        for (int i = 1; i <= numProducers; i++) {
            ProducerWork producerWork = new ProducerWork(i, buffer, maxItemsPerProducer, producingTime);
            new Thread(producerWork).start();
        }

        for (int i = 1; i <= numConsumers; i++) {
            ConsumerWork consumerWork = new ConsumerWork(i, buffer, consumingTime);
            new Thread(consumerWork).start();
        }
    }
}
