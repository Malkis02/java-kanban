package manager;

import Tasks.Task;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> history = new LinkedList<>();

    @Override
    public void add(Task task) {
        if(history.contains(task)){
            history.remove(0);
        }else if(history.size()==10) {
            history.remove(0);
        }
            history.add(task);

    }

    @Override
    public List<Task> getHistory() {
        return history;
    }

}
