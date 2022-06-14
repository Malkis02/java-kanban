package manager;

import Tasks.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class FileBackedTasksManager extends InMemoryTaskManager  {
    private String fileName;
    boolean restored = false;

    public boolean isRestored() {
        return restored;
    }

    public void setRestored(boolean restored) {
        this.restored = restored;
    }



    public FileBackedTasksManager(String fName) {
        super();
        fileName = fName;
    }

    public void save() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(String.format("%s",fileName)));
            bw.write("id,type,name,status,description,epic\n");
            for (int i = 0; i < 10; i++) {
                if (tasksById.containsKey(i)) {
                    bw.write(String.format("%s\n", tasksById.get(i).toFileString()));
                } else if (subtaskById.containsKey(i)) {
                    bw.write(String.format("%s\n", subtaskById.get(i).toFileString()));
                } else if (epicsById.containsKey(i)) {
                    bw.write(String.format("%s\n", epicsById.get(i).toFileString()));
                }

            }
            bw.write("\n");
            bw.write(InMemoryHistoryManager.toString(historyManager));
            bw.close();
        } catch (IOException e) {
            throw new ManagerSaveException();

        }

    }
    @Override
    public void addTask(Task task){
        if(!restored){
            super.addTask(task);
            save();
        }
         else{
             int curId = task.getId();
             if(curId>nextId){
                 nextId = curId+1;
             }
            if (task instanceof SubTask) {
                subtaskById.put(curId, (SubTask) task);
            } else if (task instanceof Epic) {
                epicsById.put(curId, (Epic) task);
            } else {
                tasksById.put(curId, task);
            }
        }

    }

    @Override
    public Epic getEpicById(int id){
        var e = super.getEpicById(id);
        save();
        return e;
    }

    @Override
    public SubTask getSubTaskById(int id){
        var st = super.getSubTaskById(id);
        save();
        return st;
    }

    @Override
    public Task getTaskById(int id){
        var t = super.getTaskById(id);
        save();
        return t;
    }
    public static Task fromString(String s){
        String[] w = s.split(",");
        System.out.println(Arrays.toString(w));
        Task t = null;
        if(w[1].equals(TaskTypeName.TASK.toString())){
             t = new Task(w[2],w[4]);
        }
        else if(w[1].equals(TaskTypeName.EPIC.toString())){
            t = new Epic(w[2],w[4]);
        }
        else if(w[1].equals(TaskTypeName.SUBTASK.toString())){
            t = new SubTask(w[2],w[4],null);
            ((SubTask)t).setMasterId(Integer.parseInt(w[5]));
        }
        else{
            System.out.format("Wrong type name");
            return null;
        }
        t.setId(Integer.parseInt(w[0]));
        if(w[3].equals(TaskStatus.NEW.toString())){
            t.setStatus(TaskStatus.NEW);
        }
        else if(w[3].equals(TaskStatus.DONE.toString())){
            t.setStatus(TaskStatus.DONE);
        }
        else if(w[3].equals(TaskStatus.IN_PROGRESS.toString())){
            t.setStatus(TaskStatus.IN_PROGRESS);
        }
        else {
            System.out.format("Wrong status name");
            return null;
        }
        return t;
    }
    public static FileBackedTasksManager loadFromFile(File file){
        FileBackedTasksManager result = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String fn = file.getName();
            int pos = fn.indexOf(".");
            String leftPart = fn.substring(0,pos);
            String newLeftPart = leftPart + "_next";
            result = new FileBackedTasksManager(newLeftPart + fn.substring(pos));
            result.setRestored(true);
            var list_t = new ArrayList<Task>();
            var list_e = new ArrayList<Epic>();
            var list_st = new ArrayList<SubTask>();
            br.readLine();
            while(true){
                String s = br.readLine();
                if(s==null){
                    break;
                }
                if(s.length()==0){
                    break;
                }
                Task t = fromString(s);
                if(t instanceof SubTask){
                    list_st.add((SubTask) t);
                }
                else if(t instanceof Epic){
                    list_e.add((Epic) t);
                }
                else{
                    list_t.add(t);
                }
            }
            var hist = br.readLine();
            result.historyManager = new InMemoryHistoryManager();
            var list = InMemoryHistoryManager.fromString(hist);

            br.close();
            for (int k = 0; k < list_e.size(); k++) {
                for (int j = 0; j < list_st.size(); j++) {
                    var subT = list_st.get(j);
                    if(subT.getMasterId() == list_e.get(k).getId()){
                        list_e.get(k).addSub(subT);
                    }
                }
            }
            for (int i = 0; i < list_t.size(); i++) {
                result.addTask(list_t.get(i));
                System.out.format("{%s}\n",list_t.get(i).toFileString());
            }
            for (int k = 0; k < list_e.size(); k++) {
                result.addTask(list_e.get(k));
                System.out.format("{%s}\n",list_e.get(k).toFileString());
            }
            for (int j = 0; j < list_st.size(); j++) {
                result.addTask(list_st.get(j));
                System.out.format("{%s}\n",list_st.get(j).toFileString());
            }
            for (int i = 0; i < list.size(); i++) {
                int id = list.get(i);
                if(result.tasksById.containsKey(id)){
                    result.historyManager.add(result.tasksById.get(id));
                }
                else if(result.epicsById.containsKey(id)){
                    result.historyManager.add(result.epicsById.get(id));
                }
                else{
                    result.historyManager.add(result.subtaskById.get(id));
                }
            }
            result.setRestored(false);
            System.out.format("Reloaded %d\n",list_e.size() + list_st.size() + list_t.size());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
    @Override
    public boolean removeById(int id) {
        var x =  super.removeById(id);
        save();
        return x;
    }
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        Task task = new Task("Выгулять собаку", "Погулять в парке");
        Task task2 = new Task("Позвонить маме", "Попросить рецепт торта");
        Epic epic = new Epic("Закупиться к новому году", "Ничего не забыть");
        SubTask subTask = new SubTask("Купить продукты", "Закупки",epic);
        SubTask subTask1 = new SubTask("Купить подарки", "Закупки",epic);
        SubTask subTask2 = new SubTask("Купить фейерверк", "Закупки",epic);
        Epic epic1 = new Epic("Устроить детский праздник", "Для племянника");
        System.out.println(Arrays.toString(manager.getHistory().toArray()));
        manager.addTask(task);
        manager.addTask(task2);
        manager.addTask(epic);
        manager.addTask(subTask);
        manager.addTask(subTask1);
        manager.addTask(subTask2);
        subTask2.setStatus(TaskStatus.IN_PROGRESS);
        manager.addTask(epic1);
        manager.getTaskById(1);
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
        var fb = FileBackedTasksManager.loadFromFile(new File("data.csv"));
        fb.save();
        fb.removeById(1);
        var fbNew = FileBackedTasksManager.loadFromFile(new File("data_next.csv"));
        fbNew.save();
    }

}
