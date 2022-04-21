package manager;

import Tasks.Task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private ArrayList<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if(history.contains(task)){
            return;
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }

}
