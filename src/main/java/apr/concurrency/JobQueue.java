package apr.concurrency;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * JobQueue
 */
public class JobQueue {

    Queue<Job> Q;
    Object QLock;

    public JobQueue(int initCap) {
        QLock = new Object();
        Q = new ArrayDeque<>();
    }

    public void push(Job job) {
        synchronized (QLock) {
            Q.add(job);
            QLock.notifyAll();
        }
    }

    public Job pop() {
        synchronized (QLock) {
            while (Q.isEmpty()) {
                try {
                    QLock.wait();
                } catch (InterruptedException e) {
                    return null;
                }
            }
        }
        return Q.poll();
    }

    public double totalTime() {
        if (Q.size() == 0) {
            return 0;
        }
        return Q.stream().map(j -> j.getTime()).reduce((acc, d) -> acc = acc + d).get();
    }

    @Override
    public String toString() {
        String str = String.format("JobQueue[%d jobs, %.1f time avg., %.1f time total]",
                Q.size(), totalTime() / Q.size(), totalTime());

        str += String.format("%n\tJobs:");
        for (var j : Q) {
            str += String.format("%n\t\t%s", j.toString());
        }

        return str;
    }

}
