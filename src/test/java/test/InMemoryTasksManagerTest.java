package test;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

public class InMemoryTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    private void setUp() throws IOException {
        manager = new InMemoryTaskManager();
        setUpTaskManager();
    }
}
