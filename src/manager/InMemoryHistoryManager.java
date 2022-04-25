package manager;

import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    // private List<Task> history = new LinkedList<>();
    private HashMap<Integer,Node> idToNode = new HashMap<>();
    private CustomLinkedList taskList = new CustomLinkedList();



    @Override
    public void remove(int id){
        Node q = idToNode.get(id);
        if(q != null){
            taskList.removeNode(q);
            idToNode.remove(id);
        }
    }

    @Override
    public void add(Task task) {
      /*  if(history.contains(task)){
            history.remove(0);
        }else if(history.size()==10) {
            history.remove(0);
        }
            history.add(task);
*/
        remove(task.getId());
        taskList.linkLast(task);
        idToNode.put(task.getId(),taskList.tail);

    }

    @Override
    public List<Task> getHistory() {
       return taskList.getTasks();
    }

}
class CustomLinkedList{

    Node head;
    Node tail;
    int total;
/*
    public void addToHead(Task t){
      Node q = new Node(t);
      if(head == null){
          head = tail = q;
          total = 1;
      }
      else{
          head.prev = q;
          q.next = head;
          head = q;
          total++;
      }


    }
    */
    public List<Task> getTasks(){
        List<Task> result = new ArrayList<>();
        Node q = head;
        while(q!=null){
            result.add(q.core);

            q = q.next;
        }
        return result;
    }

    public void linkLast(Task t){
        Node q = new Node(t);
        if(tail == null){
            tail = head = q;
            total = 1;
        }
        else{
            tail.next = q;
            q.prev = tail;
            tail = q;
            total++;
        }
    }
    public void removeNode(Node q){
        if(q == null){
            return;
        }
        if(total == 1){
            head = tail = null;
            total = 0;
        }
        else if(q == head){
            head.next.prev = null;
            head = head.next;
            total--;
        }
        else if(q == tail){
            tail.prev.next = null;
            tail = tail.prev;
            total--;
        }
        else{
            q.prev.next = q.next;
            q.next.prev = q.prev;
            total--;
        }
    }
}
