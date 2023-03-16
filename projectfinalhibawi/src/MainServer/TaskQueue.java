package MainServer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TaskQueue {
    private BlockingQueue<Task> queue;

    public TaskQueue() {
        queue = new LinkedBlockingQueue<>();
    }
    public void add(Task task) {
        try {
            queue.put(task);
        } catch (InterruptedException e) {
        }
    }
    public Task take() throws InterruptedException {
        return queue.take();
    }
}
