package test;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    private void setUp() {
        manager = new InMemoryTaskManager();
        setUpTaskManager();
    }
}
