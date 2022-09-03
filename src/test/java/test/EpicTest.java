package test;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private InMemoryTaskManager memory = new InMemoryTaskManager();
    private Epic epic;
    private Epic epic1;
    private SubTask subTask;
    private SubTask subTask1;
    private SubTask subTask2;

    @BeforeEach
     void setUp() {
        memory = new InMemoryTaskManager();
        epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        epic1 = new Epic("Устроить детский праздник", "Для племянника");
        subTask = new SubTask("Купить продукты", "Закупки", epic.getId(), "2022-08-04T20:10", 60);
        subTask2 = new SubTask("Купить фейерверк", "Закупки", epic.getId(), "2022-08-04T22:10", 10);
        subTask1 = new SubTask("Купить подарки", "Закупки", epic1.getId(), "2022-08-04T21:10", 90);
    }


    @Test
    void getStatusEpicEmptySubsTest() {
        memory.addTask(epic);
        assertSame(epic.getStatus(), TaskStatus.NEW);
    }

    @Test
    void getStatusEpicWithOnlyNewSubsTest() {
        memory.addTask(subTask);
        memory.addTask(subTask1);
        memory.addTask(epic);
        assertSame(epic.getStatus(), TaskStatus.NEW);
    }

    @Test
    void getStatusEpicWithOnlyDoneTest() {
        Epic epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        SubTask subTask = new SubTask("Купить продукты", "Закупки", epic.getId(), "2022-08-04T20:10", 60);
        SubTask subTask1 = new SubTask("Купить подарки", "Закупки", epic.getId(), "2022-08-04T21:10", 90);
        epic.addSub(subTask);
        epic.addSub(subTask1);
        memory.addTask(subTask);
        memory.addTask(subTask1);
        subTask.setStatus(TaskStatus.DONE);
        subTask1.setStatus(TaskStatus.DONE);
        memory.addTask(epic);
        assertSame(epic.getStatus(), TaskStatus.DONE);

    }

    @Test
    void getStatusEpicWithNewAndDoneTest() {
        memory.addTask(subTask);
        memory.addTask(subTask1);
        subTask.setStatus(TaskStatus.NEW);
        subTask1.setStatus(TaskStatus.DONE);
        memory.addTask(epic);
        epic.addSub(subTask);
        epic.addSub(subTask1);
        assertSame(epic.getStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    void getStatusEpicWithInProgressSubsTest() {
        memory.addTask(subTask);
        memory.addTask(subTask1);
        subTask.setStatus(TaskStatus.IN_PROGRESS);
        subTask1.setStatus(TaskStatus.IN_PROGRESS);
        memory.addTask(epic);
        epic.addSub(subTask);
        epic.addSub(subTask1);
        assertSame(epic.getStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    void addSubTest() {
        memory.addTask(epic);
        memory.addTask(epic1);
        memory.addTask(subTask);
        memory.addTask(subTask2);
        memory.addTask(subTask1);
        epic1.addSub(subTask1);
        epic.addSub(subTask);
        epic.addSub(subTask2);
        List<SubTask> listOfSubs = epic1.getSubs();
        List<SubTask> listOfSubsByEpic = memory.getListOfSubTasksByEpicId(2);
        assertEquals(listOfSubs.size(), listOfSubsByEpic.size());
        assertEquals(90, epic1.getDuration());
        assertEquals(70, epic.getDuration());

    }

    @Test
    void addSubWithWrongIdTest() {
        memory.addTask(epic);
        memory.addTask(epic1);
        memory.addTask(subTask);
        memory.addTask(subTask2);
        memory.addTask(subTask1);
        epic1.addSub(subTask1);
        epic.addSub(subTask);
        epic.addSub(subTask2);
        List<SubTask> listOfSubs = epic1.getSubs();
        List<SubTask> listOfSubsByEpic = memory.getListOfSubTasksByEpicId(3);
        assertNotEquals(listOfSubs.size(), listOfSubsByEpic.size());
        assertEquals(0, listOfSubsByEpic.size());
        assertEquals(90, epic1.getDuration());
        assertEquals(70, epic.getDuration());
    }
}