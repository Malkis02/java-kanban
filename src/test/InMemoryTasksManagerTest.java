package test;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

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
        manager.removeById(0);
        manager.removeById(1);
        manager.removeById(2);
        manager.removeById(3);
        assertNull(manager.getEpicById(0));
        assertNull(manager.getSubTaskById(1));
        assertNull(manager.getSubTaskById(2));
        assertNull(manager.getTaskById(3));
    }
}
