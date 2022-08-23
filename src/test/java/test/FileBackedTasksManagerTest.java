package test;

import manager.FileBackedTasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {


    @BeforeEach
    private void setUp() {
        String file;
        file = "src\\test\\data.csv";
        manager = new FileBackedTasksManager(file);
        setUpTaskManager();
    }

    @Test
    private void loadFromFileTest() {
        var file1 = new File("src\\test\\testdata.csv");
        manager.getTaskById(task.getId());
        FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(file1);
        List<Task> taskList = manager.getAllTasks();
        assertNotNull(taskList);
        assertEquals(2, taskList.size());
        assertEquals(task, taskList.get(0));
    }
}