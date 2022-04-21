package manager;

import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import Tasks.TaskStatus;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Task1", "asdf");
        manager.addTask(task);
        Task task1 = new Task("Task2", "kdsfjn");
        manager.addTask(task1);
        System.out.printf("Normal tasks: %s\nTasks.Epic Tasks: %s\nSub Tasks: %s\n --------\n",
                manager.getAllTasks(), manager.getAllEpic(), manager.getAllSubtasks());
        task.setStatus(TaskStatus.IN_PROGRESS);
        Epic epic = new Epic("Epic1", "kfjkdhgld");
        SubTask sub = new SubTask("Sub1", "odildg");
        SubTask sub2 = new SubTask("Sub2", "ksugs");
        epic.addSub(sub);
        epic.addSub(sub2);
        manager.addTask(epic);
        manager.addTask(sub);
        manager.addTask(sub2);
        System.out.println("History: " + Arrays.toString(manager.getHistory().toArray()));
        manager.getTaskById(1);
        manager.getSubTaskById(4);
        manager.getEpicById(3);
        manager.getEpicById(3);
        manager.getEpicById(10);
        System.out.println("History: " + Arrays.toString(manager.getHistory().toArray()));
        System.out.printf("Normal Tasks: %s\nTasks.Epic Tasks: %s\nSub Tasks: %s\n --------\n",
                manager.getAllTasks(), manager.getAllEpic(), manager.getAllSubtasks());
        sub.setStatus(TaskStatus.IN_PROGRESS);
        System.out.printf("Normal tasks: %s\nTasks.Epic Tasks: %s\nSub Tasks: %s\n --------\n",
                manager.getAllTasks(), manager.getAllEpic(), manager.getAllSubtasks());
        sub.setStatus(TaskStatus.DONE);
        sub2.setStatus(TaskStatus.DONE);
        System.out.printf("Normal Tasks: %s\nTasks.Epic Tasks: %s\nSub Tasks: %s\n --------\n",
                manager.getAllTasks(), manager.getAllEpic(), manager.getAllSubtasks());
        List<SubTask> subTaskOfOldEpic = manager.getListOffSubTasksByEpicId(3);
        Epic epic2 = new Epic("epic2", "asdfsv");
        manager.getSubTaskById(5);
        manager.getSubTaskById(5);
        manager.getEpicById(3);
        manager.getTaskById(2);
        manager.getEpicById(3);
        manager.getTaskById(2);
        manager.getSubTaskById(5);
        manager.updateTask(3, epic2);
        System.out.printf("Normal Tasks: %s\nTasks.Epic Tasks: %s\nSub Tasks: %s\n --------\n",
                manager.getAllTasks(), manager.getAllEpic(), manager.getAllSubtasks());
        System.out.println(Arrays.toString(subTaskOfOldEpic.toArray()));
        subTaskOfOldEpic.add(new SubTask("sub3", "owifklsknf"));
        System.out.println(Arrays.toString(subTaskOfOldEpic.toArray()));
        System.out.println(Arrays.toString(epic.getSubs().toArray()));
        manager.removeById(1);
        System.out.printf("Normal Tasks: %s\nTasks.Epic Tasks: %s\nSub Tasks: %s\n --------\n",
                manager.getAllTasks(), manager.getAllEpic(), manager.getAllSubtasks());
        manager.removeById(4);
        System.out.printf("Normal Tasks: %s\nTasks.Epic Tasks: %s\nSub Tasks: %s\n --------\n",
                manager.getAllTasks(), manager.getAllEpic(), manager.getAllSubtasks());
        manager.removeAllEpic();
        task1.setStatus(TaskStatus.DONE);
        System.out.printf("Normal Tasks: %s\nTasks.Epic Tasks: %s\nSub Tasks: %s\n --------\n",
                manager.getAllTasks(), manager.getAllEpic(), manager.getAllSubtasks());
        manager.removeAllTasks();
        System.out.printf("Normal Tasks: %s\nTasks.Epic Tasks: %s\nSub Tasks: %s\n --------\n",
                manager.getAllTasks(), manager.getAllEpic(), manager.getAllSubtasks());
        System.out.println("History: " + Arrays.toString(manager.getHistory().toArray()));

    }
}
