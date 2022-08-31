package test;

import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import server.KVServer;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected HistoryManager historyManager;
    private HttpTaskServer taskServer;
    protected KVServer kvServer;
    protected T manager;
    protected Epic epic;
    protected Task task;
    protected SubTask subTask;
    private SubTask subTask1;


    protected void setUpTaskManager() throws IOException {
        taskServer = new HttpTaskServer(manager);
        historyManager = Managers.getDefaultHistory();
        kvServer = Managers.getDefaultKVServer();
        task = new Task("Выгулять собаку", "Погулять в парке", "2022-08-05T20:15", 45);
        manager.addTask(task);
        epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        manager.addTask(epic);
        subTask = new SubTask("Купить продукты", "Закупки", epic, "2022-08-04T20:10", 60);
        manager.addTask(subTask);
        subTask1 = new SubTask("Купить подарки", "Закупки", epic, "2022-08-04T21:10", 90);
        manager.addTask(subTask1);
        taskServer.start();
        kvServer.start();
    }

    @AfterEach
    void stop() {
        taskServer.stop();
        kvServer.stop();
    }

    @Test
    protected void getAllTasks() {
        List<Task> listOfTasks = manager.getAllTasks();
        assertEquals(1, listOfTasks.size());
        assertEquals(task, listOfTasks.get(0));

    }

    @Test
    protected void getAllSubtasks() {
        List<SubTask> listOfSubTasks = manager.getAllSubtasks();
        assertEquals(2, listOfSubTasks.size());
        assertEquals(subTask, listOfSubTasks.get(0));
        assertEquals(subTask1, listOfSubTasks.get(1));
        assertEquals(subTask.getMaster(), epic);
        assertEquals(subTask1.getMaster(), epic);
    }

    @Test
    protected void getAllEpic() {
        List<Epic> listOfEpic = manager.getAllEpic();
        assertEquals(1, listOfEpic.size());
        assertEquals(epic, listOfEpic.get(0));
        assertEquals(epic.getStatus(), TaskStatus.NEW);
    }

    @Test
    protected void getAllEpicWithEmptySubs() {
        manager.removeAllSubTask();
        assertEquals(epic.getStatus(), TaskStatus.NEW);
    }

    @Test
    protected void getAllEpicWithOnlyNewSubs() {
        subTask.setStatus(TaskStatus.NEW);
        subTask1.setStatus(TaskStatus.NEW);
        assertEquals(epic.getStatus(), TaskStatus.NEW);
    }

    @Test
    protected void getAllEpicWithOnlyDoneSubs() {
        subTask.setStatus(TaskStatus.DONE);
        subTask1.setStatus(TaskStatus.DONE);
        assertEquals(epic.getStatus(), TaskStatus.DONE);
    }

    @Test
    protected void getAllEpicWithNewAndDoneSubs() {
        subTask.setStatus(TaskStatus.NEW);
        subTask1.setStatus(TaskStatus.DONE);
        assertEquals(epic.getStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    protected void getAllEpicWithInProgressSubsTest() {
        subTask.setStatus(TaskStatus.IN_PROGRESS);
        subTask1.setStatus(TaskStatus.IN_PROGRESS);
        assertEquals(epic.getStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    protected void removeById() {
        manager.removeById(1);
        manager.removeById(2);
        manager.removeById(3);
        manager.removeById(4);
        assertNull(manager.getEpicById(1));
        assertNull(manager.getSubTaskById(2));
        assertNull(manager.getSubTaskById(3));
        assertNull(manager.getTaskById(4));
    }

    @Test
    protected void removeByIdWithWrongId() {
        assertFalse(manager.removeById(9));
    }

    @Test
    protected void removeByIdWithEmptyList() {
        manager.removeAllEpic();
        manager.removeAllTasks();
        manager.removeAllSubTask();
        assertFalse(manager.removeById(1));
    }

    @Test
    protected void removeAllTasks() {
        manager.removeAllTasks();
        List<Task> listOfTasks = manager.getAllTasks();
        assertEquals(0, listOfTasks.size());
    }

    @Test
    protected void removeAllSubTask() {
        manager.removeAllSubTask();
        List<SubTask> listOfSubTasks = manager.getAllSubtasks();
        List<Epic> listOfEpic = manager.getAllEpic();
        assertEquals(0, listOfSubTasks.size());
        assertEquals(1, listOfEpic.size());
        assertNotNull(subTask.getMaster());
        assertEquals(TaskStatus.NEW,epic.getStatus());
    }

    @Test
    protected void removeAllEpic() {
        manager.removeAllEpic();
        List<SubTask> listOfSubTasks = manager.getAllSubtasks();
        List<Epic> listOfEpic = manager.getAllEpic();
        assertEquals(0, listOfSubTasks.size());
        assertEquals(0, listOfEpic.size());

    }

    @Test
    protected void addTask() {
        final int taskId = task.getId();
        Task savedTask = manager.getTaskById(taskId);
        assertNotNull(savedTask);
        assertEquals(task, savedTask);
        final List<Task> tasks = manager.getAllTasks();
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals(task, tasks.get(0));
    }

    @Test
    protected void updateTask() {
        manager.updateTask(1, task);
        assertNull(manager.getTaskById(1));
        assertNotNull(manager.getTaskById(5));
        assertEquals(task, manager.getTaskById(5));
    }

    @Test
    protected void updateTaskByWrongId() {
        manager.updateTask(6, task);
        assertNotNull(manager.getTaskById(1));
        assertEquals(task, manager.getTaskById(1));
    }

    @Test
    protected void updateTaskByEmptyList() {
        manager.removeAllSubTask();
        manager.removeAllTasks();
        manager.removeAllEpic();
        manager.updateTask(1, task);
        assertNull(manager.getTaskById(4));
    }

    @Test
    protected void getListOfSubTasksByEpicId() {
        final List<SubTask> subTasks = manager.getListOfSubTasksByEpicId(2);
        assertEquals(subTasks.size(), 2);
    }

    @Test
    protected void getListOfSubTasksByEpicIdWithWrongId() {
        final List<SubTask> subTasks = manager.getListOfSubTasksByEpicId(3);
        assertEquals(0, subTasks.size());
    }

    @Test
    protected void getListOfSubTasksByEpicIdTestWithEmptyList() {
        manager.removeAllEpic();
        manager.removeAllTasks();
        manager.removeAllSubTask();
        final List<SubTask> subTasks = manager.getListOfSubTasksByEpicId(2);
        assertEquals(0, subTasks.size());
    }

    @Test
    protected void getTaskById() {
        assertNotNull(manager.getTaskById(1));
        assertEquals(task, manager.getTaskById(1));
    }

    @Test
    protected void getTaskByIdWithWrongId() {
        assertNull(manager.getTaskById(5));
    }

    @Test
    protected void getTaskByIdWithEmptyList() {
        manager.removeAllEpic();
        manager.removeAllSubTask();
        manager.removeAllTasks();
        assertNull(manager.getTaskById(1));
    }

    @Test
    protected void getEpicById() {
        assertNotNull(manager.getEpicById(2));
        assertEquals(epic, manager.getEpicById(2));
    }

    @Test
    protected void getEpicByIdWithWrongId() {
        assertNull(manager.getEpicById(5));
    }

    @Test
    protected void getEpicByIdWithEmptyList() {
        manager.removeAllEpic();
        manager.removeAllSubTask();
        manager.removeAllTasks();
        assertNull(manager.getEpicById(2));
    }

    @Test
    protected void getSubTaskById() {
        assertNotNull(manager.getSubTaskById(3));
        assertEquals(subTask, manager.getSubTaskById(3));
        assertEquals(epic, subTask.getMaster());
    }

    @Test
    protected void getSubTaskByIdWithWrongId() {
        assertNull(manager.getSubTaskById(5));
    }

    @Test
    protected void getSubTaskByIdWithEmptyList() {
        manager.removeAllEpic();
        manager.removeAllSubTask();
        manager.removeAllTasks();
        assertNull(manager.getSubTaskById(3));
    }

    @Test
    protected void getHistory() {
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
    }


    @Test
    protected void getPrioritizedTasksTest() {
        var list = manager.getPrioritizedTasks();
        System.out.println(list);
        for (int k = 0; k < list.size(); k++) {
            if (k == list.size() - 1) {
                break;
            }
            if (list.get(k).getStartTime() == null && list.get(k + 1).getStartTime() != null) {
                assertTrue(false);
            } else if (list.get(k).getStartTime() != null && list.get(k + 1)
                    .getStartTime() != null && list.get(k).getStartTime()
                    .isAfter(list.get(k + 1).getStartTime())) {
                System.out.format("[%d] \n ", list.get(k).compareTo(list.get(k + 1)));
                System.out.println(k);
                assertTrue(false);
            }
        }
        assertTrue(true);
    }

}