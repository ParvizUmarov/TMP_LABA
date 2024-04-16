package app.server.session;

import app.transport.message.Message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.lang.Thread.sleep;

// TODO session expiration
public class SessionService {
    private final Map<Token, Session> map = new ConcurrentHashMap<>();
    private final ConcurrentLinkedQueue<Object> queue = new ConcurrentLinkedQueue<>();

    public TokenSession create() {
        var token = new Token();
        var session = new Session();
        map.put(token, session);
        return new TokenSession(token, session);
    }

    public Session get(Token token) {
        return map.get(token);
    }

    public void remove(Token token) {
        map.remove(token);

    }

    public void hasRequest(Message request) {
        addRequest(request);
        System.out.println(STR."added new request\{queue}");
    }

    public void addRequest(Object element) {
        queue.add(element);
        new Thread(() -> {
            while (!queue.isEmpty()) {
                try {
                    sleep(10000);
                    queue.poll();
                    System.out.println("REMOVED REQUEST");
                    System.out.println(queue);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //remove(token);
        }).start();
    }

    public ConcurrentLinkedQueue<Object> getQueue() {
        return queue;
    }

    public record TokenSession(Token token, Session session) {
    }

    {
        var t = new Thread(() -> {
            while (true) {
                try {
                    if (map != null) {
                        var iter = map.entrySet().iterator();
                        while (iter.hasNext()) {
                            var session = iter.next().getValue();
//                            if (session.isExpired()) {
//                                iter.remove();
//                            }
                        }
                    }
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }
}
