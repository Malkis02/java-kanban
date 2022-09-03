package test;

import manager.HttpTaskManager;
import manager.ManagerLoadException;
import manager.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.KVServer;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {



    @BeforeEach
    public void setUpHttpTaskManager() throws IOException, ManagerLoadException {
        kvServer = Managers.getDefaultKVServer();
        kvServer.start();
        manager = new HttpTaskManager(KVServer.PORT);
        setUpTaskManager();

    }
    @AfterEach
    void stop() {
        kvServer.stop();
    }

    @Test
    protected void load() throws IOException, InterruptedException {
        manager.getTaskById(task.getId());
        manager.getEpicById(epic.getId());
        manager.getSubTaskById(subTask.getId());
        manager.getSubTaskById(subTask1.getId());

        List<Task> tasks = manager.getAllTasks();
        List<Epic> epics = manager.getAllEpic();
        List<SubTask> subtasks = manager.getAllSubtasks();
        assertNotNull(tasks,"Получили пустой список");
        assertEquals(1,tasks.size(),"Не верное количество задач");
        assertEquals(task,tasks.get(0),"Задачи не совпадают");

        assertNotNull(epics,"Получили пустой список");
        assertEquals(1,epics.size(),"Не верное количество задач");
        assertEquals(epic,epics.get(0),"Задачи не совпадают");

        assertNotNull(subtasks,"Получили пустой список");
        assertEquals(2,subtasks.size(),"Не верное количество задач");
        assertEquals(subTask,subtasks.get(0),"Задачи не совпадают");
        assertEquals(subTask1,subtasks.get(1),"Задачи не совпадают");
    }
}