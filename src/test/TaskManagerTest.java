package test;

import manager.TaskManager;

public abstract class TaskManagerTest<T extends TaskManager> {
    public abstract void testNormal();
    public abstract void testEmptyList();
    public abstract void testWrongId();
}
