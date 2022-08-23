package tasks;

import java.util.Objects;

public class SubTask extends Task {
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        SubTask subTask = (SubTask) o;
        return Objects.equals(master, subTask.master);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), master);
    }

    private Epic master;
    static TaskTypeName typeName = TaskTypeName.SUBTASK;
    private int masterId;

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }



    public SubTask(String n, String d, Epic e,String startdt,int durmin) {
        super(n, d,startdt,durmin);
        if(e!=null){
            e.addSub(this);
            master = e;
        }
    }
    @Override
    public String getTypeName(){
        return typeName.toString();
    }

    public Epic getMaster() {
        return master;
    }

    public void setMaster(Epic master) {
        this.master = master;
    }
    @Override
    public String toString() {
        String s = super.toString();
        return String.format("%s master=%s",s,master == null ? "null":master.getName());
    }
    @Override
    public String toFileString(){
        String s = super.toFileString();
        s+=",";
        System.out.println(this.getName() + ": " + master);
        s+= master.getId();
        return s;
    }

}
