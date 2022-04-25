package manager;

import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final HashMap<Integer, Node> idToNode = new HashMap<>();
    private Node head;
    private Node tail;

    @Override
    public void remove(int id) {
        Node q = idToNode.get(id);
        if (q != null) {
            removeNode(q);
            idToNode.remove(id);
        }
    }

    @Override
    public void add(Task task) {

        remove(task.getId());
        linkLast(task);

        idToNode.put(task.getId(), tail);

    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    public List<Task> getTasks() {
        List<Task> result = new ArrayList<>();
        Node q = head;
        while (q != null) {
            result.add(q.core);

            q = q.next;
        }
        return result;
    }

    public void linkLast(Task t) {
        Node q = new Node(t);
        if (tail == null) {
            tail = head = q;
        } else {
            tail.next = q;
            q.prev = tail;
            tail = q;
        }
    }

    public void removeNode(Node q) {
        if (q == null) {
            return;
        }
        if (q.next == null) {
            head = tail = null;
        } else if (q == head) {
            head.next.prev = null;
            head = head.next;
        } else if (q == tail) {
            tail.prev.next = null;
            tail = tail.prev;
        } else {
            q.prev.next = q.next;
            q.next.prev = q.prev;
        }
    }
}
