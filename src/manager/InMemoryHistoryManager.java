package manager;

import tasks.Task;

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
    public static String toString(HistoryManager hm){
        var his = hm.getHistory();
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < his.size(); k++) {
            sb.append(his.get(k).getId());
            if(k<his.size()-1){
                sb.append(",");
            }
        }
        return sb.toString();
    }
    public static List<Integer> fromString(String value){
        System.out.println("Parse: " + value);
        if(value==null){
            return new ArrayList<>();
        }
        String[] w = value.split(",");
        var list = new ArrayList<Integer>();
        for (int i = 0; i < w.length; i++) {
            list.add(Integer.parseInt(w[i]));
        }
        return list;
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
