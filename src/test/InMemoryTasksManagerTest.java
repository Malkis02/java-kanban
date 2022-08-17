package test;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTasksManagerTest extends TaskManagerTest {
    private final InMemoryTaskManager manager = new InMemoryTaskManager();


    @Override
    @Test
    public  void testNormal(){
        Epic epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        SubTask subTask = new SubTask("Купить продукты", "Закупки", epic,"2022-08-04T20:10",60);
        SubTask subTask1 = new SubTask("Купить подарки", "Закупки", epic,"2022-08-04T21:10",90);
        Task task = new Task("Закупиться к новому году","Ничего не забыть","2022-08-07T23:12",60);
        manager.addTask(subTask);
        manager.addTask(subTask1);
        manager.addTask(epic);
        manager.addTask(task);
        assertSame(manager.getSubTaskById(1),subTask);
        assertSame(manager.getSubTaskById(2),subTask1);
        assertSame(manager.getEpicById(3),epic);
        assertSame(manager.getTaskById(4),task);
    }

    @Override
    @Test
    public void testEmptyList(){
        Epic epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        SubTask subTask = new SubTask("Купить продукты", "Закупки", epic,"2022-08-04T20:10",60);
        SubTask subTask1 = new SubTask("Купить подарки", "Закупки", epic,"2022-08-04T21:10",90);
        Task task = new Task("Закупиться к новому году","Ничего не забыть","2022-08-07T23:12",60);
        manager.addTask(subTask);
        manager.addTask(subTask1);
        manager.addTask(epic);
        manager.addTask(task);
        manager.removeAllTasks();
        System.out.println(manager.getAllTasks());
        assertTrue(manager.getAllTasks().size()+manager.getAllEpic().size()+manager.getAllSubtasks().size()==3);
        manager.removeAllSubTask();
        assertTrue(manager.getAllTasks().size()+manager.getAllEpic().size()+manager.getAllSubtasks().size()==1);
        manager.removeAllEpic();
        assertTrue(manager.getAllTasks().size()+manager.getAllEpic().size()+manager.getAllSubtasks().size()==0);

    }

    @Override
    @Test
    public void testWrongId(){
        Epic epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        SubTask subTask = new SubTask("Купить продукты", "Закупки", epic,"2022-08-04T20:10",60);
        SubTask subTask1 = new SubTask("Купить подарки", "Закупки", epic,"2022-08-04T21:10",90);
        Task task = new Task("Закупиться к новому году","Ничего не забыть","2022-08-07T23:12",60);
        manager.addTask(subTask);
        manager.addTask(subTask1);
        manager.addTask(epic);
        manager.addTask(task);
        assertFalse(manager.getTaskById(5)!=null);
    }


    @Test
    public void removeByIdTest(){
        Epic epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        SubTask subTask = new SubTask("Купить продукты", "Закупки", epic,"2022-08-04T20:10",60);
        SubTask subTask1 = new SubTask("Купить подарки", "Закупки", epic,"2022-08-04T21:10",90);
        Task task = new Task("Закупиться к новому году","Ничего не забыть","2022-08-07T23:12",60);
        manager.addTask(subTask);
        manager.addTask(subTask1);
        manager.addTask(epic);
        manager.addTask(task);
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
    public void removeByIdWrongIdTest(){
        Epic epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        SubTask subTask = new SubTask("Купить продукты", "Закупки", epic,"2022-08-04T20:10",60);
        SubTask subTask1 = new SubTask("Купить подарки", "Закупки", epic,"2022-08-04T21:10",90);
        Task task = new Task("Закупиться к новому году","Ничего не забыть","2022-08-07T23:12",60);
        assertFalse(manager.removeById(9));
        manager.removeById(0);
    }

    @Test
    public void removeByIdWithEmptyList(){
        Epic epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        SubTask subTask = new SubTask("Купить продукты", "Закупки", epic,"2022-08-04T20:10",60);
        SubTask subTask1 = new SubTask("Купить подарки", "Закупки", epic,"2022-08-04T21:10",90);
        Task task = new Task("Закупиться к новому году","Ничего не забыть","2022-08-07T23:12",60);
        manager.removeAllEpic();
        manager.removeAllTasks();
        manager.removeAllSubTask();
        assertFalse(manager.removeById(1));
    }

    @Test
    public void addTaskTest(){
        Epic epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        SubTask subTask = new SubTask("Купить продукты", "Закупки", epic,"2022-08-04T20:10",60);
        SubTask subTask1 = new SubTask("Купить подарки", "Закупки", epic,"2022-08-04T21:10",90);
        Task task = new Task("Закупиться к новому году","Ничего не забыть","2022-08-07T23:12",60);
        manager.addTask(subTask);
        manager.addTask(subTask1);
        manager.addTask(epic);
        manager.addTask(task);
        assertNotNull(manager.getTaskById(4));
        assertEquals(task,manager.getTaskById(4));
        final List<Task> tasks = manager.getAllTasks();
        assertNotNull(tasks);
        assertEquals(1,tasks.size());
        assertEquals(task,tasks.get(0));
    }

    @Test
    public void updateTaskNormalTest(){
        Epic epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        SubTask subTask = new SubTask("Купить продукты", "Закупки", epic,"2022-08-04T20:10",60);
        SubTask subTask1 = new SubTask("Купить подарки", "Закупки", epic,"2022-08-04T21:10",90);
        Task task = new Task("Закупиться к новому году","Ничего не забыть","2022-08-07T23:12",60);
        manager.addTask(subTask);
        manager.addTask(subTask1);
        manager.addTask(epic);
        manager.addTask(task);
        manager.updateTask(4,5,task);
        System.out.println(manager.getTaskById(5));
        assertNull(manager.getTaskById(4));
        assertNotNull(manager.getTaskById(5));
    }
    @Test
    public void updateTaskWithEmptyList(){
        Epic epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        SubTask subTask = new SubTask("Купить продукты", "Закупки", epic,"2022-08-04T20:10",60);
        SubTask subTask1 = new SubTask("Купить подарки", "Закупки", epic,"2022-08-04T21:10",90);
        Task task = new Task("Закупиться к новому году","Ничего не забыть","2022-08-07T23:12",60);
        manager.addTask(epic);
        manager.addTask(subTask);
        manager.addTask(subTask1);
        manager.addTask(task);
        manager.removeAllSubTask();
        manager.removeAllTasks();
        manager.removeAllEpic();
        manager.updateTask(4,5,task);
        assertNull(manager.getTaskById(5));
    }

    @Test
    public void updateTaskWithWrongId(){
        Epic epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        SubTask subTask = new SubTask("Купить продукты", "Закупки", epic,"2022-08-04T20:10",60);
        SubTask subTask1 = new SubTask("Купить подарки", "Закупки", epic,"2022-08-04T21:10",90);
        Task task = new Task("Закупиться к новому году","Ничего не забыть","2022-08-07T23:12",60);
        manager.updateTask(5,6,task);
        assertNull(manager.getTaskById(6));
    }
    @Test
    void getListOffSubTasksByEpicIdTest(){
        Epic epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        SubTask subTask = new SubTask("Купить продукты", "Закупки", epic,"2022-08-04T20:10",60);
        SubTask subTask1 = new SubTask("Купить подарки", "Закупки", epic,"2022-08-04T21:10",90);
        Task task = new Task("Закупиться к новому году","Ничего не забыть","2022-08-07T23:12",60);
        manager.addTask(epic);
        manager.addTask(subTask);
        manager.addTask(subTask1);
        manager.addTask(task);
        final List<SubTask> subTasks = manager.getListOffSubTasksByEpicId(1);
        assertEquals(subTasks.size(),2);
    }
    @Test
    void getListOffSubTasksByEpicIdTestWithEmptyList(){
        Epic epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        SubTask subTask = new SubTask("Купить продукты", "Закупки", epic,"2022-08-04T20:10",60);
        SubTask subTask1 = new SubTask("Купить подарки", "Закупки", epic,"2022-08-04T21:10",90);
        Task task = new Task("Закупиться к новому году","Ничего не забыть","2022-08-07T23:12",60);
        manager.addTask(epic);
        manager.addTask(subTask);
        manager.addTask(subTask1);
        manager.addTask(task);
        manager.removeAllEpic();
        manager.removeAllTasks();
        manager.removeAllSubTask();
        final List<SubTask> subTasks = manager.getListOffSubTasksByEpicId(1);
        assertNull(subTasks);
    }
    @Test
    void getListOffSubTasksByEpicIdTestWithWrongId(){
        Epic epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        SubTask subTask = new SubTask("Купить продукты", "Закупки", epic,"2022-08-04T20:10",60);
        SubTask subTask1 = new SubTask("Купить подарки", "Закупки", epic,"2022-08-04T21:10",90);
        Task task = new Task("Закупиться к новому году","Ничего не забыть","2022-08-07T23:12",60);
        manager.addTask(epic);
        manager.addTask(subTask);
        manager.addTask(subTask1);
        manager.addTask(task);
        manager.removeAllEpic();
        manager.removeAllTasks();
        manager.removeAllSubTask();
        final List<SubTask> subTasks = manager.getListOffSubTasksByEpicId(2);
        assertNull(subTasks);
    }
}
