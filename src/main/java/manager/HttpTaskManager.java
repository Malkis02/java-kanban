package manager;

import client.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {

    private KVTaskClient client;
    private final Gson gson;

    public HttpTaskManager(int port,boolean isLoad){
        super("data.csv");
        gson = Managers.getGson();
        client = new KVTaskClient(port);
        if(isLoad){
            load();
        }
    }

    public HttpTaskManager(int port) {
        super(null);
        gson = Managers.getGson();
        client = new KVTaskClient(port);
    }

    public void save() {
        String tasksJson = gson.toJson(getAllTasks());
        client.put("tasks", tasksJson);

        String subtasksJson = gson.toJson(getAllSubtasks());
        client.put("subtasks", subtasksJson);

        String epicsJson = gson.toJson(getAllEpic());
        client.put("epics", epicsJson);

        List<Integer> history = historyManager.getHistory().stream().map(Task::getId)
                .collect(Collectors.toList());
        String historyJson = gson.toJson(history);
        client.put("history", historyJson);
    }


    protected void load() {
        Type tasksType = new TypeToken<ArrayList<Task>>() {
        }.getType();

        ArrayList<Task> tasks = gson.fromJson(client.load("/tasks"), tasksType);
        tasks.forEach(task -> {
            int id = task.getId();
            this.tasksById.put(id, task);
            this.taskSet.add(task);
            if (id > nextId) {
                nextId = id;
            }

        });

        Type epicsType = new TypeToken<ArrayList<Epic>>() {
        }.getType();

        ArrayList<Epic> epics = gson.fromJson(client.load("/epics"), epicsType);
        epics.forEach(epic -> {
            int id = epic.getId();
            this.epicsById.put(id, epic);
            this.taskSet.add(epic);
            if (id > nextId) {
                nextId = id;
            }

        });

        Type subtasksType = new TypeToken<ArrayList<SubTask>>() {
        }.getType();

        ArrayList<SubTask> subtasks = gson.fromJson(client.load("/subtasks"), subtasksType);
        subtasks.forEach(subtask -> {
            int id = subtask.getId();
            this.subtaskById.put(id,subtask);
            this.taskSet.add(subtask);
            if (id > nextId) {
                nextId = id;
            }

        });

        Type historyType = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        ArrayList<Integer> history = gson.fromJson(client.load("/history"), historyType);
        for (Integer id : history) {
            historyManager.add(this.findTask(id));
        }
    }

}
