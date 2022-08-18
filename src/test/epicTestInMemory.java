package test;

import static org.junit.jupiter.api.Assertions.*;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.TaskStatus;

import java.util.List;

class EpicTestInMemory {
    InMemoryTaskManager memory = new InMemoryTaskManager();
    @Test
    void getStatusEpicEmptySubsTest(){
        Epic epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        memory.addTask(epic);
        assertSame(epic.getStatus(), TaskStatus.NEW);
    }

    @Test
    void getStatusEpicWithOnlyNewSubsTest(){
        Epic epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        SubTask subTask = new SubTask("Купить продукты", "Закупки", epic,"2022-08-04T20:10",60);
        SubTask subTask1 = new SubTask("Купить подарки", "Закупки", epic,"2022-08-04T21:10",90);
        memory.addTask(subTask);
        memory.addTask(subTask1);
        memory.addTask(epic);
        assertSame(epic.getStatus(),TaskStatus.NEW);
    }

    @Test
    void getStatusEpicWithOnlyDoneTest(){
        Epic epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        SubTask subTask = new SubTask("Купить продукты", "Закупки", epic,"2022-08-04T20:10",60);
        SubTask subTask1 = new SubTask("Купить подарки", "Закупки", epic,"2022-08-04T21:10",90);
        memory.addTask(subTask);
        memory.addTask(subTask1);
        subTask.setStatus(TaskStatus.DONE);
        subTask1.setStatus(TaskStatus.DONE);
        memory.addTask(epic);
        assertSame(epic.getStatus(),TaskStatus.DONE);

    }

    @Test
    void getStatusEpicWithNewAndDoneTest(){
        Epic epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        SubTask subTask = new SubTask("Купить продукты", "Закупки", epic,"2022-08-04T20:10",60);
        SubTask subTask1 = new SubTask("Купить подарки", "Закупки", epic,"2022-08-04T21:10",90);
        memory.addTask(subTask);
        memory.addTask(subTask1);
        subTask.setStatus(TaskStatus.NEW);
        subTask1.setStatus(TaskStatus.DONE);
        memory.addTask(epic);
        assertSame(epic.getStatus(),TaskStatus.IN_PROGRESS);
    }
    @Test
    void getStatusEpicWithInProgressSubsTest(){
        Epic epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        SubTask subTask = new SubTask("Купить продукты", "Закупки", epic,"2022-08-04T20:10",60);
        SubTask subTask1 = new SubTask("Купить подарки", "Закупки", epic,"2022-08-04T21:10",90);
        memory.addTask(subTask);
        memory.addTask(subTask1);
        subTask.setStatus(TaskStatus.IN_PROGRESS);
        subTask1.setStatus(TaskStatus.IN_PROGRESS);
        memory.addTask(epic);
        assertSame(epic.getStatus(),TaskStatus.IN_PROGRESS);
    }

    @Test
    void addSubTest(){
        Epic epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        Epic epic1 = new Epic("Устроить детский праздник", "Для племянника");
        SubTask subTask = new SubTask("Купить продукты", "Закупки", epic,"2022-08-04T20:10",60);
        SubTask subTask2 = new SubTask("Купить фейерверк", "Закупки", epic,"2022-08-04T22:10",10);
        SubTask subTask1 = new SubTask("Купить подарки", "Закупки", epic1,"2022-08-04T21:10",90);
        memory.addTask(epic);
        memory.addTask(epic1);
        memory.addTask(subTask);
        memory.addTask(subTask2);
        memory.addTask(subTask1);
        epic1.addSub(subTask1);
        List<SubTask> listOfSubs = epic1.getSubs();
        List<SubTask> listOfSubsByEpic = memory.getListOffSubTasksByEpicId(2);
        assertEquals(listOfSubs.size(),listOfSubsByEpic.size());
        assertEquals(epic1.getDuration(),90);
        assertEquals(epic.getDuration(),70);

    }

    @Test
    void addSubWithWrongIdTest(){

    }
}