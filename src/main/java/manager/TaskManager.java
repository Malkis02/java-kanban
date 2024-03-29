package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    ArrayList<Task> getAllTasks();

    ArrayList<SubTask> getAllSubtasks();

    ArrayList<Epic> getAllEpic();

    boolean removeById(int id);

    void removeAllTasks();

    void removeAllSubTask();

    void removeAllEpic();

    void addTask(Task task);

    boolean updateTask(int curId, Task task);

    List<SubTask> getListOfSubTasksByEpicId(int id);

    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubTaskById(int id);

    List<Task> getHistory();

    void setNextId(int nId);

    List<Task> getPrioritizedTasks();

}
