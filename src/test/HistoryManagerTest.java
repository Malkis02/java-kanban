package test;

import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    HistoryManager historyManager;
    TaskManager manager;
    private Epic epic;
    private Task task;
    private SubTask subTask;


    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistory();
        manager = Managers.getDefault();
        epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        subTask = new SubTask("Купить продукты", "Закупки", epic,"2022-08-04T20:10",60);
        task = new Task("Выгулять собаку", "Погулять в парке","2022-08-04T20:15",45);
    }

    @Test
    void add() {
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(1, history.size());
    }
    @Test
    void addTwice(){
        historyManager.add(task);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(1, history.size());
    }

    @Test
    void removeFirst() {
        manager.addTask(epic);
        manager.addTask(task);
        manager.addTask(subTask);
        historyManager.add(epic);
        historyManager.add(task);
        historyManager.add(subTask);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(3,history.size());
        historyManager.remove(epic.getId());
        history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(2,history.size());
        assertEquals(task,history.get(0));
        assertEquals(subTask,history.get(1));
    }
    @Test
    void removeMiddle(){
        manager.addTask(epic);
        manager.addTask(task);
        manager.addTask(subTask);
        historyManager.add(epic);
        historyManager.add(task);
        historyManager.add(subTask);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(3,history.size());
        historyManager.remove(task.getId());
        history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(2,history.size());
        assertEquals(epic,history.get(0));
        assertEquals(subTask,history.get(1));
    }

    @Test
    void removeLast(){
        manager.addTask(epic);
        manager.addTask(task);
        manager.addTask(subTask);
        historyManager.add(epic);
        historyManager.add(task);
        historyManager.add(subTask);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(3,history.size());
        historyManager.remove(subTask.getId());
        history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(2,history.size());
        assertEquals(epic,history.get(0));
        assertEquals(task,history.get(1));
    }
    @Test
    void removeOnce(){
        manager.addTask(epic);
        historyManager.add(epic);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(1,history.size());
        historyManager.remove(epic.getId());
        history = historyManager.getHistory();
        assertTrue(history.isEmpty());

    }

    @Test
    void getHistory() {
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history);
        assertTrue(history.isEmpty());
    }
}