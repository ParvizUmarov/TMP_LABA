package app.server.session;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class SessionExpiration {
    private final ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

    public void addElement(String element) {
        queue.add(element);
    }

    public void removeFirstElementPeriodically() {
        new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(5); // Удалять первый элемент каждые 5 секунд
                    String removedElement = queue.poll();
                    if (removedElement != null) {
                        System.out.println("Removed element: " + removedElement);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



}
