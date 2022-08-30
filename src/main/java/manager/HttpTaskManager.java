package manager;

import client.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import tasks.Task;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager{

    private KVTaskClient client;
    private Gson gson;
    private int generatedId = 0;

    private int generatedId(){
        return ++generatedId;
    }

    public HttpTaskManager(int port) {
        super(null);
        gson = Managers.getGson();
        client = new KVTaskClient(port);
    }


    protected void load(){
        Type tasksType = new TypeToken<ArrayList<Task>>(){
        }.getType();

        ArrayList<Task> tasks = gson.fromJson(client.load("tasks"),tasksType);
        tasks.forEach(task -> {
            int id = task.getId();
            this.tasksById.put(id,task);
            if(id > generatedId){
                generatedId = id;
            }

        });



        Type historyType = new TypeToken<ArrayList<Task>>(){
        }.getType();
        ArrayList<Integer> history = gson.fromJson(client.load("history"),historyType);
        for(Integer taskid:history){
            historyManager.add(this.getTaskById(taskid));
        }
    }
    public void save(){
        String tasksJson = gson.toJson(getAllTasks());
        client.put("tasks",tasksJson);

        String subtasksJson = gson.toJson(getAllSubtasks());
        client.put("subtasks",subtasksJson);

        String epicsJson = gson.toJson(getAllEpic());
        client.put("epics",epicsJson);
        List<Integer> history = getHistory().stream()
                .map(Task::getId)
                .collect(Collectors.toList());
        String historyJson = gson.toJson(history);
        client.put("history",historyJson);
    }
}
