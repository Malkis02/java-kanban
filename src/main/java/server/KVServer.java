package server;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import manager.FileBackedTasksManager;
import manager.HttpTaskManager;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

/**
 * Постман: https://www.getpostman.com/collections/a83b61d9e1c81c10575c
 */
public class KVServer {
    public static final int PORT = 8078;
    private final String apiToken;
    private final HttpServer server;
    private final Map<String, String> data = new HashMap<>();

    public KVServer() throws IOException {
        apiToken = generateApiToken();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/register", this::register);
        server.createContext("/save", this::save);
        server.createContext("/load", this::load);
    }
    public static void main(String[] args) throws IOException {
        KVServer kvServer = Managers.getDefaultKVServer();
        TaskManager manager = new HttpTaskManager(KVServer.PORT);

/*
        Task task = new Task("Выгулять собаку", "Погулять в парке","2022-08-04T20:15",45);
        Task task2 = new Task("Позвонить маме", "Попросить рецепт торта","2022-08-04T22:10",60);
        Epic epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        SubTask subTask = new SubTask("Купить продукты", "Закупки", epic,"2022-08-04T20:10",60);
        SubTask subTask1 = new SubTask("Купить подарки", "Закупки", epic,"2022-08-04T21:10",90);
        SubTask subTask2 = new SubTask("Купить фейерверк", "Закупки", epic,"2022-08-04T22:10",10);
        Epic epic1 = new Epic("Устроить детский праздник", "Для племянника");
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.addTask(task);
        manager.addTask(task2);
        manager.addTask(epic);
        manager.addTask(subTask);
        manager.addTask(subTask1);
        manager.addTask(subTask2);
        subTask2.setStatus(TaskStatus.IN_PROGRESS);
        manager.addTask(epic1);
        manager.getTaskById(1);
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.getTaskById(2);
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.getTaskById(1);
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.getSubTaskById(4);
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.getSubTaskById(5);
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.getSubTaskById(6);
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.getSubTaskById(4);
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.getSubTaskById(6);
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.getEpicById(3);
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.getEpicById(7);
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.getEpicById(3);
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.getEpicById(7);
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.removeById(2);
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.removeById(3);
        System.out.println(Arrays.toString(manager.getHistory().toArray()));

 */
    }

    private void load(HttpExchange h) {
        try {
            System.out.println("\n/load");
            if ("GET".equals(h.getRequestMethod())) {
                String key = h.getRequestURI().getPath().substring("/load/".length());
                sendText(h, data.get(key));
            } else {
                System.out.println("/load ждёт GET-запрос, а получил " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            h.close();
        }
    }

    private void save(HttpExchange h) throws IOException {
        try {
            System.out.println("\n/save");
            if (!hasAuth(h)) {
                System.out.println("Запрос неавторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
                h.sendResponseHeaders(403, 0);
                return;
            }
            if ("POST".equals(h.getRequestMethod())) {
                String key = h.getRequestURI().getPath().substring("save/".length());
                if (key.isEmpty()) {
                    System.out.println("Key для сохранения пустой. key указывается в пути: /save/{key}");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                String value = readText(h);
                if (value.isEmpty()) {
                    System.out.println("Value для сохранения пустой. value указывается в теле запроса");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                data.put(key, value);
                System.out.println("Значение для ключа " + key + " успешно обновлено!");
                h.sendResponseHeaders(200, 0);
            } else {
                System.out.println("/save ждёт POST-запрос, а получил: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    private void register(HttpExchange h) throws IOException {
        try {
            System.out.println("\n/register");
            if ("GET".equals(h.getRequestMethod())) {
                sendText(h, apiToken);
            } else {
                System.out.println("/register ждёт GET-запрос, а получил " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        System.out.println("API_TOKEN: " + apiToken);
        server.start();
    }

    public void stop(){
        server.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

    private String generateApiToken() {
        return "" + System.currentTimeMillis();
    }

    protected boolean hasAuth(HttpExchange h) {
        String rawQuery = h.getRequestURI().getRawQuery();
        return rawQuery != null && (rawQuery.contains("API_TOKEN=" + apiToken) || rawQuery.contains("API_TOKEN=DEBUG"));
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}
