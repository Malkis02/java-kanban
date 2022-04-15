import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Manager {
    private static int nextId;
    HashMap<Integer, Task> tasksById;
    HashMap<Integer, SubTask> subtaskById;
    HashMap<Integer, Epic> epicsById;

    public static final String STATUS_NEW_NAME = "NEW";
    public static final String STATUS_IN_PROGRESS_NAME = "IN_PROGRESS";
    public static final String STATUS_DONE_NAME = "DONE";


    public Manager() {
        nextId = 1;
        this.tasksById = new HashMap<>();
        this.subtaskById = new HashMap<>();
        this.epicsById = new HashMap<>();
    }

    ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasksById.values());
    }

    ArrayList<SubTask> getAllSubtasks() {
        return new ArrayList<>(subtaskById.values());
    }

    ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epicsById.values());
    }

    boolean removeById(int id){
        if(tasksById.containsKey(id)){
            tasksById.remove(id);
            return true;
        }
        else if(epicsById.containsKey(id)){
            Epic ep = epicsById.get(id); // вытащим все подзадачи этого обьекта
            List<SubTask> children = ep.getSubs();
            for(SubTask sb : children){
                subtaskById.remove(sb.getId());
            }
            epicsById.remove(id);
            return true;
        }
        else if(subtaskById.containsKey(id)){
            SubTask sb = subtaskById.get(id);
            //требуется удалить подзадачу значит она должна быть удалена у соответсвуещего эпика
            Epic ep = sb.getMaster();
            List<SubTask> subs = ep.getSubs();
            subs.remove(sb);
            if(subs.size() == 0){
                ep.setStatus(STATUS_NEW_NAME);
            }
            subtaskById.remove(id);
            return true;
        }
            return false;
    }
    void removeAllTasks() {
        tasksById.clear();
    }

    void removeAllSubTask() {
        Collection<Epic> allEpics =  epicsById.values();
        for(Epic epic: allEpics){
            epic.getSubs().clear();
            epic.setStatus(STATUS_NEW_NAME);
        }
        subtaskById.clear();
    }

    void removeAllEpic() {

        epicsById.clear();
        subtaskById.clear();
    }


    void addTask(Task task) {
        task.setStatus(STATUS_NEW_NAME);
        task.setId(nextId);
        if (task instanceof SubTask ) {
            subtaskById.put(nextId, (SubTask) task);
        } else if (task instanceof Epic ) {
            epicsById.put(nextId, (Epic) task);
        } else {
            tasksById.put(nextId, task);
        }
        nextId++;
    }

    boolean updateTask(int curId, Task task){
       if(removeById(curId)){
           task.setId(curId);
           task.setStatus(STATUS_NEW_NAME);
           if (task instanceof SubTask ) {
               subtaskById.put(nextId, (SubTask) task);
           } else if (task instanceof Epic ) {
               epicsById.put(nextId, (Epic) task);
           } else {
               tasksById.put(nextId, task);
           }return true;
       }
       return false;
    }

    List<SubTask> getListOffSubTasksByEpicId(int id){
        if(epicsById.containsKey(id)){
            List<SubTask> origList = epicsById.get(id).getSubs();
            ArrayList<SubTask> copyList = new ArrayList<>();
            for(int k = 0;k<origList.size(); k++){
                copyList.add(origList.get(k));
            }
            return copyList;
        }
        return null;
    }


  /*  void addSubTask(SubTask subtask) {
        subtask.setId(nextId++);
        subtaskById.put(nextId, subtask);
    }

    void addEpic(Epic epic) {
        epic.setId(nextId++);
        epicsById.put(nextId, epic);
    }
*/
}


