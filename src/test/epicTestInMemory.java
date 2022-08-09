package tasks;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Test;

class EpicTestInMemory {
    @Test
    void testEpicEmptySubs(){
        Epic epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        InMemoryTaskManager memory = new InMemoryTaskManager();
        memory.addTask(epic);
        assertSame(epic.getStatus(), TaskStatus.NEW);
    }

    @Test
    void testEpicWithOnlyNewSubs(){
        Epic epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        InMemoryTaskManager memory = new InMemoryTaskManager();
        SubTask subTask = new SubTask("Купить продукты", "Закупки", epic,"2022-08-04T20:10",60);
        SubTask subTask1 = new SubTask("Купить подарки", "Закупки", epic,"2022-08-04T21:10",90);
        memory.addTask(subTask);
        memory.addTask(subTask1);
        memory.addTask(epic);
        assertSame(epic.getStatus(),TaskStatus.NEW);
    }

    @Test
    void testEpicWithOnlyDone(){
        Epic epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        InMemoryTaskManager memory = new InMemoryTaskManager();
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
    void testEpicWithNewAndDone(){
        Epic epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        InMemoryTaskManager memory = new InMemoryTaskManager();
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
    void testEpicWithInProgress(){
        Epic epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        InMemoryTaskManager memory = new InMemoryTaskManager();
        SubTask subTask = new SubTask("Купить продукты", "Закупки", epic,"2022-08-04T20:10",60);
        SubTask subTask1 = new SubTask("Купить подарки", "Закупки", epic,"2022-08-04T21:10",90);
        memory.addTask(subTask);
        memory.addTask(subTask1);
        subTask.setStatus(TaskStatus.IN_PROGRESS);
        subTask1.setStatus(TaskStatus.IN_PROGRESS);
        memory.addTask(epic);
        assertSame(epic.getStatus(),TaskStatus.IN_PROGRESS);
    }

}