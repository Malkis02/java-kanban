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
    InMemoryTaskManager memory = new InMemoryTaskManager();
    private Epic epic;
    private Epic epic1;
    private SubTask subTask;
    private SubTask subTask1;
    private SubTask subTask2;

    @BeforeEach
    private void setUp() {
        memory = new InMemoryTaskManager();
        epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        epic1 = new Epic("Устроить детский праздник", "Для племянника");
        subTask = new SubTask("Купить продукты", "Закупки", epic, "2022-08-04T20:10", 60);
        subTask2 = new SubTask("Купить фейерверк", "Закупки", epic, "2022-08-04T22:10", 10);
        subTask1 = new SubTask("Купить подарки", "Закупки", epic, "2022-08-04T21:10", 90);
    }


    @Test
    private void getStatusEpicEmptySubsTest() {
        memory.addTask(epic);
        assertSame(epic.getStatus(), TaskStatus.NEW);
    }

    @Test
    private void getStatusEpicWithOnlyNewSubsTest() {
        memory.addTask(subTask);
        memory.addTask(subTask1);
        memory.addTask(epic);
        assertSame(epic.getStatus(), TaskStatus.NEW);
    }

    @Test
    private void getStatusEpicWithOnlyDoneTest() {
        Epic epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        SubTask subTask = new SubTask("Купить продукты", "Закупки", epic, "2022-08-04T20:10", 60);
        SubTask subTask1 = new SubTask("Купить подарки", "Закупки", epic, "2022-08-04T21:10", 90);
        memory.addTask(subTask);
        memory.addTask(subTask1);
        subTask.setStatus(TaskStatus.DONE);
        subTask1.setStatus(TaskStatus.DONE);
        memory.addTask(epic);
        assertSame(epic.getStatus(), TaskStatus.DONE);

    }

    @Test
    private void getStatusEpicWithNewAndDoneTest() {
        memory.addTask(subTask);
        memory.addTask(subTask1);
        subTask.setStatus(TaskStatus.NEW);
        subTask1.setStatus(TaskStatus.DONE);
        memory.addTask(epic);
        assertSame(epic.getStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    private void getStatusEpicWithInProgressSubsTest() {
        memory.addTask(subTask);
        memory.addTask(subTask1);
        subTask.setStatus(TaskStatus.IN_PROGRESS);
        subTask1.setStatus(TaskStatus.IN_PROGRESS);
        memory.addTask(epic);
        assertSame(epic.getStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    private void addSubTest() {
        memory.addTask(epic);
        memory.addTask(epic1);
        memory.addTask(subTask);
        memory.addTask(subTask2);
        memory.addTask(subTask1);
        epic1.addSub(subTask1);
        List<SubTask> listOfSubs = epic1.getSubs();
        List<SubTask> listOfSubsByEpic = memory.getListOfSubTasksByEpicId(2);
        assertEquals(listOfSubs.size(), listOfSubsByEpic.size());
        assertEquals(90, epic1.getDuration());
        assertEquals(70, epic.getDuration());

    }

    @Test
    private void addSubWithWrongIdTest() {
        memory.addTask(epic);
        memory.addTask(epic1);
        memory.addTask(subTask);
        memory.addTask(subTask2);
        memory.addTask(subTask1);
        epic1.addSub(subTask1);
        List<SubTask> listOfSubs = epic1.getSubs();
        List<SubTask> listOfSubsByEpic = memory.getListOfSubTasksByEpicId(3);
        assertNotEquals(listOfSubs.size(), listOfSubsByEpic.size());
        assertEquals(0, listOfSubsByEpic.size());
        assertEquals(90, epic1.getDuration());
        assertEquals(70, epic.getDuration());
    }
}