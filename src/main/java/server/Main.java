package server;

import manager.HttpTaskManager;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws IOException {
        KVServer kvServer = Managers.getDefaultKVServer();
        kvServer.start();
        TaskManager manager = Managers.getDefault();

        Task task = new Task("Выгулять собаку", "Погулять в парке","2022-08-04T20:15",45);
        Task task2 = new Task("Позвонить маме", "Попросить рецепт торта","2022-08-04T22:10",60);
        Epic epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        SubTask subTask = new SubTask("Купить продукты", "Закупки", 3,"2022-08-04T20:10",60);
        SubTask subTask1 = new SubTask("Купить подарки", "Закупки", 3,"2022-08-04T21:10",90);
        SubTask subTask2 = new SubTask("Купить фейерверк", "Закупки", 7,"2022-08-04T22:10",10);
        Epic epic1 = new Epic("Устроить детский праздник", "Для племянника");
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.addTask(task);
        manager.addTask(task2);
        manager.addTask(epic);
        manager.addTask(subTask);
        manager.addTask(subTask1);
        manager.addTask(subTask2);
        manager.addTask(epic1);
        epic.addSub(subTask);
        epic.addSub(subTask1);
        epic1.addSub(subTask2);
        manager.removeById(3);
        manager.removeById(7);
        manager = new HttpTaskManager(KVServer.PORT,true);
        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getTaskById(2));
        System.out.println(manager.getSubTaskById(4));
        System.out.println(manager.getSubTaskById(5));
        System.out.println(manager.getSubTaskById(6));
        System.out.println(manager.getEpicById(3));
        System.out.println(manager.getEpicById(7));

    }
}
