package test;

import manager.FileBackedTasksManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    private String file;

    @BeforeEach
    void setUp() {

        file = "src\\test\\data.csv";
        manager = new FileBackedTasksManager(file);
        setUpTaskManager();
    }

    @Test
    void loadFromFileTest(){
        var file1 = new File("src\\test\\testdata.csv");
        manager.getTaskById(task.getId());
        FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(file1);
        List<Task> taskList = manager.getAllTasks();
        assertNotNull(taskList);
        assertEquals(2,taskList.size());
        assertEquals(task,taskList.get(0));
    }
}