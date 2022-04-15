import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
  /*     System.out.println("Пришло время практики!");
        Scanner scanner = new Scanner(System.in);
        int command = scanner.nextInt();
        switch (command){
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
        }*/
        Manager manager = new Manager();
        Task task = new Task("Task1","asdf");
        manager.addTask(task);
        Task task1 = new Task("Task2","kdsfjn");
        manager.addTask(task1);
        System.out.printf("Normal tasks: %s\nEpic Tasks: %s\nSub Tasks: %s\n --------\n" ,manager.getAllTasks(),manager.getAllEpic(),manager.getAllSubtasks());
        //ArrayList<Task> allTasks = manager.getAllTasks();
        task.setStatus(Manager.STATUS_IN_PROGRESS_NAME);
        Epic epic = new Epic("Epic1","kfjkdhgld");
        SubTask sub = new SubTask("Sub1","odildg");
        SubTask sub2 = new SubTask("Sub2","ksugs");
        epic.addSub(sub);
        epic.addSub(sub2);
        manager.addTask(epic);
        manager.addTask(sub);
        manager.addTask(sub2);
        System.out.printf("Normal Tasks: %s\nEpic Tasks: %s\nSub Tasks: %s\n --------\n" ,manager.getAllTasks(),manager.getAllEpic(),manager.getAllSubtasks());
        sub.setStatus(Manager.STATUS_IN_PROGRESS_NAME);
        System.out.printf("Normal tasks: %s\nEpic Tasks: %s\nSub Tasks: %s\n --------\n" ,manager.getAllTasks(),manager.getAllEpic(),manager.getAllSubtasks());
        sub.setStatus(Manager.STATUS_DONE_NAME);
        sub2.setStatus(Manager.STATUS_DONE_NAME);
        System.out.printf("Normal Tasks: %s\nEpic Tasks: %s\nSub Tasks: %s\n --------\n" ,manager.getAllTasks(),manager.getAllEpic(),manager.getAllSubtasks());
        List<SubTask> subTaskOfOldEpic = manager.getListOffSubTasksByEpicId(3);
        Epic epic2 = new Epic("epic2","asdfsv");
        manager.updateTask(3,epic2);
        System.out.printf("Normal Tasks: %s\nEpic Tasks: %s\nSub Tasks: %s\n --------\n" ,
                manager.getAllTasks(),manager.getAllEpic(),manager.getAllSubtasks());
        System.out.println(Arrays.toString(subTaskOfOldEpic.toArray()));
        subTaskOfOldEpic.add(new SubTask("sub3","owifklsknf"));
        System.out.println(Arrays.toString(subTaskOfOldEpic.toArray()));
        System.out.println(Arrays.toString(epic.getSubs().toArray()));
        manager.removeById(1);
        System.out.printf("Normal Tasks: %s\nEpic Tasks: %s\nSub Tasks: %s\n --------\n" ,manager.getAllTasks(),manager.getAllEpic(),manager.getAllSubtasks());
        manager.removeById(4);
        System.out.printf("Normal Tasks: %s\nEpic Tasks: %s\nSub Tasks: %s\n --------\n" ,manager.getAllTasks(),manager.getAllEpic(),manager.getAllSubtasks());
        manager.removeAllEpic();
        task1.setStatus(Manager.STATUS_DONE_NAME);
        System.out.printf("Normal Tasks: %s\nEpic Tasks: %s\nSub Tasks: %s\n --------\n" ,manager.getAllTasks(),manager.getAllEpic(),manager.getAllSubtasks());
        manager.removeAllTasks();
        System.out.printf("Normal Tasks: %s\nEpic Tasks: %s\nSub Tasks: %s\n --------\n" ,manager.getAllTasks(),manager.getAllEpic(),manager.getAllSubtasks());

    }
    public static void printMenu(){
        System.out.println("1-Получить список задач");
        System.out.println("2-Удалить все задачи");
        System.out.println("3-Получить задачу по ид");
        System.out.println("4-Создать задачу");
        System.out.println("5-Обновить задачу");
        System.out.println("6-Удалить задачу по ид");
        System.out.println("7-Получить список подзадач");

    }


}
