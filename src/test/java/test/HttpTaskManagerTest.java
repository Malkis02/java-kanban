package test;

import manager.HttpTaskManager;
import manager.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.KVServer;
import test.TaskManagerTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {



    @BeforeEach
    void setUp() throws IOException {
        kvServer = Managers.getDefaultKVServer();
        manager = new HttpTaskManager(KVServer.PORT);
        setUpTaskManager();
    }



    @Test
    protected void load(){
        manager.getTaskById(task.getId());

    }
}