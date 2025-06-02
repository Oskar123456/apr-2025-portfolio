package apr.concurrency.opgave_4;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Main
 */
public class Main {

    static int consumerId, producerId;

    public static void main(String[] args) {
        Random rng = new Random();
        SBuffer sbuffer = new SBuffer();
        sbuffer.print();

        int nProducers = rng.nextInt(1, 4);
        ExecutorService producers = Executors.newFixedThreadPool(nProducers);
        ExecutorService consumers = Executors.newFixedThreadPool(4);

        for (int i = 0; i < nProducers; i++) {
            Thread t = new Thread(new Producer(sbuffer), "Producer " + producerId++);
            t.start();
            producers.submit(t);
        }

        System.out.printf("[Main]: Added %d producers...%n", nProducers);

        while (true) {
            try {
                Thread.sleep(5000);
                int sbufferSize = sbuffer.getSize();
                float sbufferLoad = sbuffer.getLoad();
                System.out.printf("[Main]: sbuffer's size and load is currently %d and %f...", sbufferSize,
                        sbufferLoad);
                if (sbufferSize > 8) {
                    System.out.printf(" adding consumers to keep with producers...%n");
                    for (int i = 0; i < sbufferSize; i++) {
                        consumers.submit(() -> {
                            try {
                                Thread.sleep(rng.nextInt(1500, 3500));
                                String str = sbuffer.pop();
                                System.out.printf("[%s]: popped string \"%s\"%n", Thread.currentThread().getName(),
                                        str);
                            } catch (InterruptedException e) {
                            }
                        });
                    }
                } else if (sbufferSize < 1) {
                    System.out.printf(" there is no work...%n");
                } else {
                    System.out.printf(" going back to sleep...%n");
                }
                sbuffer.print();
            } catch (Exception e) {
                System.out.printf("[Main]: Exiting...%n");
                try {
                    producers.shutdownNow();
                    producers.awaitTermination(10, TimeUnit.SECONDS);
                    consumers.shutdownNow();
                    consumers.awaitTermination(10, TimeUnit.SECONDS);
                } catch (Exception ex) {
                }
                return;
            }
        }
    }
}
