package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import manager.HistoryManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    protected HttpTaskServer taskServer;
    protected HistoryManager historyManager;
    protected TaskManager taskManager;
    protected Task task;
    protected Epic epic;
    protected SubTask subtask;
    Gson gson = Managers.getGson();

    @BeforeEach
    void init() throws IOException {
        historyManager = Managers.getDefaultHistory();
        taskManager = new InMemoryTaskManager();
        taskServer = new HttpTaskServer(taskManager);

        task = new Task("Test Task", "Test Task description",LocalDateTime.now().toString(),15);
        taskManager.addTask(task);

        epic = new Epic("Test Epic", "Test Epic description");
        taskManager.addTask(epic);

        subtask = new SubTask("Test Subtask", "Test Subtask description",
                epic,task.getEndTime().toString(),50);

        taskManager.addTask(subtask);

        taskServer.start();
    }

    @AfterEach
    void stop() {
        taskServer.stop();
    }

    @Test
    void getTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        final List<Task> tasks = gson.fromJson(response.body(), taskType);

        assertNotNull(tasks, "Задачи на возвращаются");
        assertEquals(1, tasks.size(), "Не верное количество задач");
        assertEquals(task, tasks.get(0), "Задачи не совпадают");
    }

    @Test
    void getSubtasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        final List<SubTask> tasks = gson.fromJson(response.body(),
                new TypeToken<ArrayList<SubTask>>() {
        }.getType());

        assertNotNull(tasks, "Подзадачи на возвращаются");
        assertEquals(1, tasks.size(), "Не верное количество подзадач");
        assertEquals(subtask, tasks.get(0), "Подзадачи не совпадают");
    }

    @Test
    void getEpics() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        final List<Epic> tasks = gson.fromJson(response.body(), new TypeToken<ArrayList<Epic>>() {
        }.getType());

        assertNotNull(tasks, "Эпик на возвращаются");
        assertEquals(1, tasks.size(), "Не верное количество эпиков");
        assertEquals(epic, tasks.get(0), "Эпики не совпадают");
    }

    @Test
    void getEpicSubtasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/epic?id=" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        final List<SubTask> tasks = gson.fromJson(response.body(),
                new TypeToken<ArrayList<SubTask>>() {
        }.getType());

        assertNotNull(tasks, "Подзадачи на возвращаются");
        assertEquals(1, tasks.size(), "Не верное количество подзадач");
        assertEquals(subtask, tasks.get(0), "Подзадачи не совпадают");
    }

    @Test
    void getTask() {
        final Task savedTask = taskManager.getTaskById(task.getId());

        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(task, savedTask, "Задачи не совпадают");
    }

    @Test
    void getSubtask() {
        final Task savedSubtask = taskManager.getTaskById(subtask.getId());

        assertNotNull(savedSubtask, "Подзадача не найдена");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают");
    }

    @Test
    void getEpic() {
        final Task savedEpic = taskManager.getEpicById(epic.getId());

        assertNotNull(savedEpic, "Эпик не найдена");
        assertEquals(epic, savedEpic, "Эпики не совпадают");
    }

    @Test
    void addNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description",
                LocalDateTime.now().toString(), 45);
        taskManager.addTask(task);
        final int taskId = task.getId();
        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(task, savedTask, "Задачи не совпадают");

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются");
        assertEquals(2, tasks.size(), "Не верное количество задач");
        final Optional<Task> optionalTask = tasks.stream().filter(t -> t.getId() == taskId)
                .findAny();
        assertFalse(optionalTask.isEmpty(), "Задачи не найдена в списке");
        assertEquals(task, optionalTask.get(), "Задачи не совпадают");
//        assertNull(savedTask.getEpicId(), "Эпик найден");
    }

    @Test
    void addNewEpic() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        taskManager.addTask(epic);
        final int epicId = epic.getId();
        final Task savedEpic = taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "Эпик не найдена");
        assertEquals(epic, savedEpic, "Эпики не совпадают");

        final List<Epic> tasks = taskManager.getAllEpic();

        assertNotNull(tasks, "Эпик на возвращаются");
        assertEquals(2, tasks.size(), "Не верное количество эпиков");
        final Optional<Epic> optionalTask = tasks.stream().filter(t -> t.getId() == epicId)
                .findAny();
        assertFalse(optionalTask.isEmpty(), "Эпик не найдена в списке");
        final Epic actual = optionalTask.get();
        assertEquals(epic, actual, "Эпики не совпадают");
//        assertNull(actual.getEpicId(), "Эпик найден");
    }

    @Test
    void addNewSubtask() {
        final LocalDateTime endTime = getLastEndTime();
        Epic epic = new Epic("Test addNewSubtask", "Test addNewSubtask description");
        taskManager.addTask(epic);
        final int epicId = epic.getId();
        SubTask subtask = new SubTask("Test addNewSubtask", "Test addNewSubtask description",
                epic,LocalDateTime.now().toString(),50);
        taskManager.addTask(subtask);
        final Integer subTaskId = subtask.getId();
        assertNotNull(subTaskId, "Подзадача не создается");

        final Task savedSubtask = taskManager.getTaskById(subTaskId);

        assertNotNull(savedSubtask, "Подзадача не найдена");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают");

        final List<SubTask> tasks = taskManager.getAllSubtasks();

        assertNotNull(tasks, "Подзадачи на возвращаются");
        assertEquals(2, tasks.size(), "Не верное количество подзадач");
        final Optional<SubTask> optionalTask = tasks.stream().filter(t -> t.getId() == subTaskId)
                .findAny();
        assertFalse(optionalTask.isEmpty(), "Подзадача не найдена в списке");
        final SubTask actual = optionalTask.get();
        assertEquals(subtask, actual, "Подзадачи не совпадают");
//        assertNotNull(actual.getEpicId(), "Эпик не найден");

        final List<SubTask> subtasks = taskManager.getListOfSubTasksByEpicId(actual.getMasterId());
        assertNotNull(subtasks, "Подзадачи не найдены");
        assertEquals(1, subtasks.size(), "Не верное количество подзадач");
        assertEquals(subtask, subtasks.get(0), "Подзадачи не совпадают");
    }

    @Test
    void updateTask() {
        task.setStatus(TaskStatus.DONE);
        task.setName("Update updateTask");
        task.setDescription("Update updateTask description");
        taskManager.updateTask(5,task);


        final Task savedTask = taskManager.getTaskById(task.getId());

        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(task, savedTask, "Задачи не совпадают");
    }

    @Test
    void updateEpic() {
        epic.setName("Update updateEpic");
        epic.setDescription("Update updateEpic description");
        taskManager.updateTask(6,epic);

        final Task savedEpic = taskManager.getEpicById(epic.getId());

        assertNotNull(savedEpic, "Эпик не найдена");
        assertEquals(epic, savedEpic, "Эпик не совпадают");
    }

    @Test
    void epicWithEmptySubtasks() {
        Epic epic = new Epic("Empty epic", "Test epic description");
        taskManager.addTask(epic);
        final int epicId = epic.getId();

        final Task savedEpic = taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "Эпик не найдена");
        assertEquals(TaskStatus.NEW, savedEpic.getStatus(), "Пустой эпик статус");
    }

    @Test
    void epicWithNewSubtasks() {
        final LocalDateTime endTime = getLastEndTime();
        Epic epic = new Epic("Empty epic", "Test epic description");
        taskManager.addTask(epic);
        final int epicId = epic.getId();
        SubTask subtask = new SubTask("Test", "Test description",
                epic,LocalDateTime.now().toString(),40);
        taskManager.addTask(subtask);

        final Task savedEpic = taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "Эпик не найдена");
        assertEquals(TaskStatus.NEW, savedEpic.getStatus(), "Пустой эпик статус");
    }

    @Test
    void epicWithInProgressNewSubtasks() {
        Epic epic = new Epic("Empty epic", "Test epic description");
        taskManager.addTask(epic);
        final int epicId = epic.getId();
        final LocalDateTime endTime = getLastEndTime();
        SubTask subtask1 = new SubTask("Test", "Test description",
                epic,LocalDateTime.now().toString(),30);
        taskManager.addTask(subtask1);
        SubTask subtask2 = new SubTask("Test", "Test description",
                epic,LocalDateTime.now().toString(),20);
        taskManager.addTask(subtask2);

        final Task savedEpic = taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "Эпик не найдена");
        assertEquals(TaskStatus.IN_PROGRESS, savedEpic.getStatus(), "Пустой эпик статус");
        assertEquals(subtask1.getDuration() + subtask2.getDuration(), savedEpic.getDuration(),
                "Длительности эпика сумма длительностей подзадач");
        assertEquals(subtask1.getStartTime(), savedEpic.getStartTime(), "Начало эпика - начало " +
                "ранней подзадачи");
        assertEquals(subtask2.getEndTime(), savedEpic.getEndTime(), "Завершение эпика - " +
                "завершение последней подзадачи");
    }

    @Test
    void epicWithDoneSubtasks() {
        final LocalDateTime endTime = getLastEndTime();
        Epic epic = new Epic("Empty epic", "Test epic description");
        taskManager.addTask(epic);
        final int epicId = epic.getId();
        SubTask subtask = new SubTask("Test", "Test description",
                epic,LocalDateTime.now().toString(),10);
        subtask.setStatus(TaskStatus.DONE);
        taskManager.addTask(subtask);

        final Task savedEpic = taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "Эпик не найдена");
        assertEquals(TaskStatus.DONE, savedEpic.getStatus(), "Пустой эпик статус");
    }

    private LocalDateTime getLastEndTime() {
        final List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        return prioritizedTasks.get(prioritizedTasks.size() - 1).getEndTime();
    }

    @Test
    void updateSubtask() {
        subtask.setStatus(TaskStatus.IN_PROGRESS);
        subtask.setName("Update updateEpic");
        subtask.setDescription("Update updateEpic description");
        taskManager.updateTask(9,subtask);

        final SubTask savedTask = taskManager.getSubTaskById(subtask.getId());

        assertNotNull(savedTask, "Эпик не найдена");
        assertEquals(subtask, savedTask, "Эпик не совпадают");

        final Task savedEpic = taskManager.getEpicById(savedTask.getMasterId());
        assertNotNull(savedEpic, "Эпик подзадачи не найден");
    }

    @Test
    void deleteTask() {
        taskManager.removeById(task.getId());

        final Task savedTask = taskManager.getTaskById(task.getId());
        assertNull(savedTask, "Задачи нет после удаления");

        final List<Task> tasks = taskManager.getAllTasks();
        assertNotNull(tasks, "Задачи нет после удаления");
        assertTrue(tasks.isEmpty(), "Задачи нет после удаления");
    }

    @Test
    void deleteEpic() {
        taskManager.removeById(epic.getId());

        final Task savedEpic = taskManager.getEpicById(epic.getId());
        assertNull(savedEpic, "Эпика нет после удаления");

        final List<Epic> epics = taskManager.getAllEpic();
        assertNotNull(epics, "Эпика нет после удаления");
        assertTrue(epics.isEmpty(), "Эпика нет после удаления");

        final List<SubTask> epicSubtasks = taskManager.getListOfSubTasksByEpicId(epic.getId());
        assertNull(epicSubtasks, "Подзадач нет после удаления эпика");

        final List<SubTask> subtasks = taskManager.getAllSubtasks();
        assertNotNull(subtasks, "Подзадач нет после удаления эпика");
        assertTrue(subtasks.isEmpty(), "Подзадач нет после удаления эпика");
    }

    @Test
    void deleteSubtask() {
        taskManager.removeById(subtask.getId());

        final Task savedSubtask = taskManager.getTaskById(subtask.getId());
        assertNull(savedSubtask, "Подзадач нет после удаления");

        final List<SubTask> subtasks = taskManager.getAllSubtasks();
        assertNotNull(subtasks, "Подзадач нет после удаления");
        assertTrue(subtasks.isEmpty(), "Подзадач нет после удаления");

        final List<SubTask> epicSubtasks = taskManager.getListOfSubTasksByEpicId(subtask.getMasterId());
        assertTrue(epicSubtasks.isEmpty(), "Подзадач нет после удаления");

        final Epic epic = taskManager.getEpicById(subtask.getMasterId());
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Обновился статус эпика");
    }

    @Test
    void getHistory() {
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История возвращается");
        assertTrue(history.isEmpty(), "История пустая");

        taskManager.getTaskById(task.getId());
        historyManager.add(task);
        history = historyManager.getHistory();
        assertFalse(history.isEmpty(), "История не пустая");
        assertEquals(task, history.get(0), "История вернула последнюю задачу");

        taskManager.getEpicById(epic.getId());
        historyManager.add(epic);
        history = historyManager.getHistory();
        assertEquals(2, history.size(), "История не пустая");
        assertEquals(epic, history.get(1), "История вернула последнюю задачу");

        taskManager.getTaskById(subtask.getId());
        historyManager.add(subtask);
        history = historyManager.getHistory();
        assertEquals(3, history.size(), "История не пустая");
        assertEquals(subtask, history.get(2), "История вернула последнюю задачу");


        historyManager.remove(task.getId());
        history = historyManager.getHistory();
        assertEquals(2, history.size(), "История не пустая");
        assertEquals(epic, history.get(0), "История вернула последнюю задачу");

        historyManager.remove(subtask.getId());
        history = historyManager.getHistory();
        assertEquals(1, history.size(), "История пустая");

        historyManager.remove(epic.getId());
        history = historyManager.getHistory();
        assertEquals(0, history.size(), "История пустая");
    }

    @Test
    void getPrioritizedTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> tasks = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {
        }.getType());
        assertNotNull(tasks, "Задачи в порядке приоритета возвращаются");
        assertEquals(2, tasks.size(), "Все задачи в порядке приоритета кроме эпика");
        assertEquals(task.getId(), tasks.get(0).getId(), "Первая - обычная задача");
        assertEquals(subtask.getId(), tasks.get(1).getId(), "Вторая - подзадача");

        Task updateTask = new Task("Test addNewTask", "Test addNewTask description",
                LocalDateTime.now().toString(), 45);
        updateTask.setStartTime(subtask.getEndTime());
        taskManager.updateTask(8,updateTask);

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        tasks = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {
        }.getType());

        assertNotNull(tasks, "Задачи в порядке приоритета возвращаются");
        assertEquals(2, tasks.size(), "Все задачи в порядке приоритета");
        assertEquals(subtask.getId(), tasks.get(0).getId(), "Первая - подзадача");
        assertEquals(task.getId(), tasks.get(1).getId(), "Второй  - задача");
    }

}