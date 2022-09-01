package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Objects;


import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {

    public static final int PORT = 8080;
    private HttpServer server;
    private Gson gson;
    private TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handler);
    }

    public static void main(String[] args) throws IOException {
        final HttpTaskServer server = new HttpTaskServer();
        server.start();
    }

    private void handler(HttpExchange h) {
        try {
            System.out.println("\n/tasks: " + h.getRequestURI());
            final String path = h.getRequestURI().getPath().replaceFirst("/tasks/", "");
            switch (path) {
                case "": {
                    if (!h.getRequestMethod().equals("GET")) {
                        System.out.println("/ Ждёт GET-запрос, а получил: " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                    }
                    final String response = gson.toJson(taskManager.getPrioritizedTasks());
                    sendText(h, response);
                    break;
                }
                case "history": {
                    if (!h.getRequestMethod().equals("GET")) {
                        System.out.println("/history ждёт GET-запрос, а получил: " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                    }
                    final String response = gson.toJson(taskManager.getHistory());
                    sendText(h, response);
                    break;
                }
                case "task":
                    handleTask(h);
                    break;
                case "subtask":
                    handleSubtask(h);
                    break;
                case "subtask/epic": {
                    if (!h.getRequestMethod().equals("GET")) {
                        System.out.println("/subtask/epic ждёт GET-запрос, а получил: " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                    }
                    final String query = h.getRequestURI().getQuery();
                    String idParam = query.substring(3);
                    final int id = Integer.parseInt(idParam);
                    final List<SubTask> subtasks = taskManager.getListOfSubTasksByEpicId(id);
                    final String response = gson.toJson(subtasks);
                    System.out.println("Получили подзадачи эпика id=" + id);
                    sendText(h, response);
                    break;
                }
                case "epic":
                    handleEpic(h);
                    break;
                default: {
                    System.out.println("Неизвестный зарос: " + h.getRequestURI());
                    h.sendResponseHeaders(404, 0);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            h.close();
        }
    }

    private void handleTask(HttpExchange h) throws IOException {
        final String query = h.getRequestURI().getQuery();
        switch (h.getRequestMethod()) {
            case "GET": {
                if (query == null) {
                    final List<Task> tasks = taskManager.getAllTasks();
                    final String response = gson.toJson(tasks);
                    System.out.println("Получили все задачи");
                    sendText(h, response);
                    return;
                }
                String idParam = query.substring(3);
                final int id = Integer.parseInt(idParam);
                final Task task = taskManager.getTaskById(id);
                final String response = gson.toJson(task);
                System.out.println("Получили задачу id=" + id);
                sendText(h, response);
                break;
            }
            case "DELETE": {
                if (query == null) {
                    taskManager.removeAllTasks();
                    System.out.println("Удалили все задачи");
                    h.sendResponseHeaders(200, 0);
                    return;
                }
                String idParam = query.substring(3);
                final int id = Integer.parseInt(idParam);
                taskManager.removeById(id);
                System.out.println("Удалили задачу id=" + id);
                h.sendResponseHeaders(200, 0);
                break;
            }
            case "POST": {
                String json = readText(h);
                if (json.isEmpty()) {
                    System.out.println("Body c задачей  пустой. указывается в теле запроса");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                final Task task = gson.fromJson(json, Task.class);
                final Integer id =  task.getId();
                if (id!=0) {
                    taskManager.updateTask(id,task);
                    System.out.println("Обновили задачу id=" + id);
                    h.sendResponseHeaders(200, 0);
                } else {
                    taskManager.addTask(task);
                    System.out.println("Создали задачу id=" + id);
                    final String response = gson.toJson(task);
                    sendText(h, response);
                }
                break;
            }
            default: {
                System.out.println("/task получил: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
                break;
            }
        }
    }

    private void handleSubtask(HttpExchange h) throws IOException {
        final String query = h.getRequestURI().getQuery();
        switch (h.getRequestMethod()) {
            case "GET": {
                if (query == null) {
                    final List<SubTask> subtasks = taskManager.getAllSubtasks();
                    final String response = gson.toJson(subtasks);
                    System.out.println("Получили все подзадачи");
                    sendText(h, response);
                    return;
                }
                String idParam = query.substring(3);
                final int id = Integer.parseInt(idParam);
                final SubTask task = taskManager.getSubTaskById(id);
                final String response = gson.toJson(task);
                System.out.println("Получили подзадачу id=" + id);
                sendText(h, response);
                break;
            }
            case "DELETE": {
                if (query == null) {
                    taskManager.removeAllSubTask();
                    System.out.println("Удалили все подзадачи");
                    h.sendResponseHeaders(200, 0);
                    return;
                }
                String idParam = query.substring(3);// ?id=
                final int id = Integer.parseInt(idParam);
                taskManager.removeById(id);
                System.out.println("Удалили подзадачу id=" + id);
                h.sendResponseHeaders(200, 0);
                break;
            }
            case "POST": {
                String json = readText(h);
                if (json.isEmpty()) {
                    System.out.println("Body c задачей  пустой. указывается в теле запроса");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                final SubTask subTask = gson.fromJson(json, SubTask.class);
                final Integer id = subTask.getId();
                if (id!=0) {
                    taskManager.updateTask(id, subTask);
                    System.out.println("Обновили подзадачу id=" + id);
                    h.sendResponseHeaders(200, 0);
                } else {
                    taskManager.addTask(subTask);
                    System.out.println("Создали подзадачу id=" + id);
                    final String response = gson.toJson(subTask);
                    sendText(h, response);
                }
                break;
            }
            default: {
                System.out.println("/subtask получил: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
                break;
            }
        }
    }

    private void handleEpic(HttpExchange h) throws IOException {
        final String query = h.getRequestURI().getQuery();
        switch (h.getRequestMethod()) {
            case "GET": {
                if (query == null) {
                    final List<Epic> tasks = taskManager.getAllEpic();
                    final String response = gson.toJson(tasks);
                    System.out.println("Получили все эпики");
                    sendText(h, response);
                    return;
                }
                String idParam = query.substring(3);
                final int id = Integer.parseInt(idParam);
                final Epic epic = taskManager.getEpicById(id);
                final String response = gson.toJson(epic);
                System.out.println("Получили эпик id=" + id);
                sendText(h, response);
                break;
            }
            case "DELETE": {
                if (query == null) {
                    taskManager.removeAllEpic();
                    System.out.println("Удалили все эпики");
                    h.sendResponseHeaders(200, 0);
                    return;
                }
                String idParam = query.substring(3);//
                final int id = Integer.parseInt(idParam);
                taskManager.removeById(id);
                System.out.println("Удалили эпик id=" + id);
                h.sendResponseHeaders(200, 0);
                break;
            }
            case "POST": {
                String json = readText(h);
                if (json.isEmpty()) {
                    System.out.println("Body c эпиком  пустой. указывается в теле запроса");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                final Epic epic = gson.fromJson(json, Epic.class);
                final Integer id = epic.getId();
                if (id!=0) {
                    taskManager.updateTask(id, epic);
                    System.out.println("Обновили эпик id=" + id);
                    h.sendResponseHeaders(200, 0);
                } else {
                    taskManager.addTask(epic);
                    System.out.println("Создали эпик id=" + id);
                    final String response = gson.toJson(epic);
                    sendText(h, response);
                }
                break;
            }
            default: {
                System.out.println("/epic получил: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
                break;
            }
        }
    }

    public void start() {
        System.out.println("Started TaskServer " + PORT);
        System.out.println("http://localhost:" + PORT + "/tasks");
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

    private String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    private void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}
