package manager;

import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import Tasks.TaskStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    protected static int nextId;
    protected final HashMap<Integer, Task> tasksById;
    protected final HashMap<Integer, SubTask> subtaskById;
    protected final HashMap<Integer, Epic> epicsById;
    protected  HistoryManager historyManager;

    public InMemoryTaskManager() {
        nextId = 1;
        this.tasksById = new HashMap<>();
        this.subtaskById = new HashMap<>();
        this.epicsById = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasksById.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubtasks() {
        return new ArrayList<>(subtaskById.values());
    }

    @Override
    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epicsById.values());
    }

    @Override
    public boolean removeById(int id) {
        historyManager.remove(id);
        if (tasksById.containsKey(id)) {
            tasksById.remove(id);
            return true;
        } else if (epicsById.containsKey(id)) {
            Epic ep = epicsById.get(id);
            List<SubTask> children = ep.getSubs();
            for (SubTask sb : children) {
                historyManager.remove(sb.getId());
                subtaskById.remove(sb.getId());
            }
            epicsById.remove(id);
            historyManager.remove(id);
            return true;
        } else if (subtaskById.containsKey(id)) {
            SubTask sb = subtaskById.get(id);
            Epic ep = sb.getMaster();
            List<SubTask> subs = ep.getSubs();
            subs.remove(sb);
            if (subs.size() == 0) {
                ep.setStatus(TaskStatus.NEW);
            }
            subtaskById.remove(id);
            historyManager.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public void removeAllTasks() {
        for (var e : tasksById.entrySet()) {
            historyManager.remove(e.getKey());
        }
        tasksById.clear();
    }

    @Override
    public void removeAllSubTask() {
        for (var e : subtaskById.entrySet()) {
            historyManager.remove(e.getKey());
        }
        Collection<Epic> allEpics = epicsById.values();
        for (Epic epic : allEpics) {
            epic.getSubs().clear();
            epic.setStatus(TaskStatus.NEW);
        }

        subtaskById.clear();
    }

    @Override
    public void removeAllEpic() {
        for (var e : epicsById.entrySet()) {
            historyManager.remove(e.getKey());
        }
        for (var e : subtaskById.entrySet()) {
            historyManager.remove(e.getKey());
        }
        epicsById.clear();
        subtaskById.clear();
    }

    @Override
    public void addTask(Task task) {
        task.setStatus(TaskStatus.NEW);
        task.setId(nextId);
        if (task instanceof SubTask) {
            subtaskById.put(nextId, (SubTask) task);
        } else if (task instanceof Epic) {
            epicsById.put(nextId, (Epic) task);
        } else {
            tasksById.put(nextId, task);
        }
        nextId++;
    }

    @Override
    public boolean updateTask(int curId, Task task) {
        if (removeById(curId)) {
            task.setId(curId);
            task.setStatus(TaskStatus.NEW);
            if (task instanceof SubTask) {
                subtaskById.put(nextId, (SubTask) task);
            } else if (task instanceof Epic) {
                epicsById.put(nextId, (Epic) task);
            } else {
                tasksById.put(nextId, task);
            }
            return true;
        }
        return false;
    }

    @Override
    public List<SubTask> getListOffSubTasksByEpicId(int id) {
        if (epicsById.containsKey(id)) {
            List<SubTask> origList = epicsById.get(id).getSubs();
            ArrayList<SubTask> copyList = new ArrayList<>();
            for (int k = 0; k < origList.size(); k++) {
                copyList.add(origList.get(k));
            }
            return copyList;
        }
        return null;
    }

    @Override
    public Task getTaskById(int id) {
        Task t = tasksById.get(id);
        if (t != null) {
            historyManager.add(t);
        }
        return t;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic e = epicsById.get(id);
        if (e != null) {
            historyManager.add(e);
        }
        return e;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask s = subtaskById.get(id);
        if (s != null) {
            historyManager.add(s);
        }
        return s;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void setNextId(int nId) {
        this.nextId = nId;
    }


}



