package ap_f2025.threads.opgave_4;

import java.util.Random;

/**
 * Consumer
 */
public class Consumer implements Runnable {
    SBuffer Q;

    public Consumer(SBuffer sb) {
        Q = sb;
    }

    @Override
    public void run() {
        Random rng = new Random();
        try {
            String str = Q.pop();
            System.out.printf("[%s]: popped string \"%s\"%n", Thread.currentThread().getName(), str);
            Thread.sleep(rng.nextInt(1500, 3500));
        } catch (Exception e) {
            return;
        }
    }

}
