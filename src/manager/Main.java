package manager;

import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        Task task = new Task("Выгулять собаку", "Погулять в парке");
        Task task2 = new Task("Позвонить маме", "Попросить рецепт торта");
        Epic epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        SubTask subTask = new SubTask("Купить продукты", "Закупки");
        SubTask subTask1 = new SubTask("Купить подарки", "Закупки");
        SubTask subTask2 = new SubTask("Купить фейерверк", "Закупки");
        Epic epic1 = new Epic("Устроить детский праздник", "Для племянника");
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.addTask(task);
        manager.addTask(task2);
        manager.addTask(epic);
        manager.addTask(subTask);
        manager.addTask(subTask1);
        manager.addTask(subTask2);
        manager.addTask(epic1);
        manager.getTaskById(1);
        epic.addSub(subTask);
        epic.addSub(subTask1);
        epic.addSub(subTask2);
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.getTaskById(2);
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.getTaskById(1);
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.getSubTaskById(4);
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.getSubTaskById(5);
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.getSubTaskById(6);
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.getSubTaskById(4);
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.getSubTaskById(6);
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.getEpicById(3);
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.getEpicById(7);
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.getEpicById(3);
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.getEpicById(7);
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.removeById(2);
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.removeById(3);
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
    }
}
