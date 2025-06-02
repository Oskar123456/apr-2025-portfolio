package ap_f2025.threads.opgave_3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Main
 */
public class Main {

    static int consumerId, producerId;

    public static void main(String[] args) {
        Random rng = new Random();
        SBuffer sbuffer = new SBuffer();
        sbuffer.print();

        List<Thread> producers = new ArrayList<>();
        List<Thread> consumers = new ArrayList<>();

        int nProducers = rng.nextInt(2, 5);
        for (int i = 0; i < nProducers; i++) {
            Thread t = new Thread(new Producer(sbuffer), "Producer " + producerId++);
            t.start();
            producers.add(t);
        }

        System.out.printf("[Main]: Added %d producers...%n", producers.size());

        while (true) {
            try {
                Thread.sleep(5000);
                int sbufferSize = sbuffer.getSize();
                float sbufferLoad = sbuffer.getLoad();
                System.out.printf(
                        "[Main]: sbuffer's size and load is currently %d and %f... there are %d producers and %d consumers...",
                        sbufferSize, sbufferLoad, producers.size(), consumers.size());
                if (sbufferSize > 8) {
                    System.out.printf(" adding a consumer to keep with producers...%n");
                    Thread t = new Thread(new Consumer(sbuffer), "Consumer " + consumerId++);
                    t.start();
                    consumers.add(t);
                } else if (sbufferSize < 1) {
                    System.out.printf(" removing consumers as there is no work...%n");
                    for (int i = 0; i < consumers.size(); i++) {
                        Thread t = consumers.getLast();
                        t.interrupt();
                        t.join();
                        consumers.removeLast();
                    }
                } else {
                    System.out.printf(" going back to sleep...%n");
                }
                sbuffer.print();
            } catch (Exception e) {
                System.out.printf("[Main]: Exiting...%n");
                for (Thread thread : consumers) {
                    try {
                        thread.interrupt();
                        thread.join();
                    } catch (Exception ex) {
                    }
                }
                return;
            }
        }
    }
}
