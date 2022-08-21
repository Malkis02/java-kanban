package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected static int nextId;
    protected final HashMap<Integer, Task> tasksById;
    protected final HashMap<Integer, SubTask> subtaskById;
    protected final HashMap<Integer, Epic> epicsById;
    protected  HistoryManager historyManager;
    protected TreeSet<Task> taskSet;

    public InMemoryTaskManager() {
        nextId = 1;
        this.tasksById = new HashMap<>();
        this.subtaskById = new HashMap<>();
        this.epicsById = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
        this.taskSet = new TreeSet<>();
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
            var elem = tasksById.get(id);
            tasksById.remove(id);
            taskSet.remove(elem);
            return true;
        } else if (epicsById.containsKey(id)) {
            Epic ep = epicsById.get(id);
            List<SubTask> children = ep.getSubs();
            for (SubTask sb : children) {
                historyManager.remove(sb.getId());
                subtaskById.remove(sb.getId());
                taskSet.remove(sb);
            }
            epicsById.remove(id);
            taskSet.remove(ep);
            historyManager.remove(id);
            return true;
        } else if (subtaskById.containsKey(id)) {
            SubTask sb = subtaskById.get(id);
            Epic ep = sb.getMaster();
            List<SubTask> subs = ep.getSubs();
            subs.remove(sb);
            taskSet.remove(sb);
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
    public List<Task> getPrioritizedTasks(){
        var list = new ArrayList<Task>();
        for(var t: tasksById.entrySet()){
            list.add(t.getValue());
        }
        for(var t: epicsById.entrySet()){
            list.add(t.getValue());
        }
        for(var t: subtaskById.entrySet()){
            list.add(t.getValue());
        }
        Collections.sort(list);
        return list;
    }

    @Override
    public void removeAllTasks() {
        for (var e : tasksById.entrySet()) {
            historyManager.remove(e.getKey());
            taskSet.remove(e.getValue());
        }
        tasksById.clear();
    }

    @Override
    public void removeAllSubTask() {
        for (var e : subtaskById.entrySet()) {
            historyManager.remove(e.getKey());
            taskSet.remove(e.getValue());
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
            taskSet.remove(e.getValue());
        }
        for (var e : subtaskById.entrySet()) {
            historyManager.remove(e.getKey());
            taskSet.remove(e.getValue());
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
    public List<SubTask> getListOfSubTasksByEpicId(int id) {
        ArrayList<SubTask> copyList = new ArrayList<>();
        if (epicsById.containsKey(id)) {
            List<SubTask> origList = epicsById.get(id).getSubs();
            for (int k = 0; k < origList.size(); k++) {
                copyList.add(origList.get(k));
            }
        }
        return copyList;
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



