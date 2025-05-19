package apr.concurrency;

/**
 * Job
 */
public class Job implements Runnable {

    long time;

    public Job(long ms) {
        this.time = ms;
    }

    public double getTime() {
        return time / 1000D;
    }

    @Override
    public String toString() {
        return String.format("Job[time: %.1f]", time / 1000D);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            System.out.println("[Thread:" + Thread.currentThread().getName() + "]: Job.run(): Interrupted");
        }
    }

}
