package manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.KVServer;
import test.TaskManagerTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    private KVServer kvServer;

    @BeforeEach
    void setUp() throws IOException {
        manager = new HttpTaskManager(KVServer.PORT);
        kvServer = Managers.getDefaultKVServer();
        setUpTaskManager();
        kvServer.start();
    }

    @AfterEach
    void tearDown() {
        kvServer.stop();
    }

    @Test
    protected void load(){
        manager.getTaskById(task.getId());

    }
}