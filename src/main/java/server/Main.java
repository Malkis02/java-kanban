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
        SubTask subTask = new SubTask("Купить продукты", "Закупки", epic.getId(),"2022-08-04T20:10",60);
        SubTask subTask1 = new SubTask("Купить подарки", "Закупки", epic.getId(),"2022-08-04T21:10",90);
        SubTask subTask2 = new SubTask("Купить фейерверк", "Закупки", epic.getId(),"2022-08-04T22:10",10);
        Epic epic1 = new Epic("Устроить детский праздник", "Для племянника");
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.addTask(task);
        manager.addTask(task2);
        manager.addTask(epic);
        manager.addTask(subTask);
        manager.addTask(subTask1);
        manager.addTask(subTask2);
        epic.addSub(subTask);
        epic.addSub(subTask1);
        epic1.addSub(subTask2);
        subTask2.setStatus(TaskStatus.IN_PROGRESS);
        manager.addTask(epic1);
        manager = new HttpTaskManager(KVServer.PORT,true);
        
    }
}
