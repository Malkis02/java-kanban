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
        return Objects.equals(masterId, subTask.masterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), masterId);
    }

    static TaskTypeName typeName = TaskTypeName.SUBTASK;
    private Integer masterId;

    public Integer getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }



    public SubTask(String n, String d, Integer mId,String startdt,int durmin) {
        super(n, d,startdt,durmin);
        if(mId!=null){
            masterId = mId;
        }
    }
    @Override
    public String getTypeName(){
        return typeName.toString();
    }

    @Override
    public String toString() {
        String s = super.toString();
        return String.format("%s masterId=%s",s,masterId == null ? "null":masterId);
    }
    @Override
    public String toFileString(){
        String s = super.toFileString();
        s+=",";
        System.out.println(this.getName() + ": " + masterId);
        s+= masterId;
        return s;
    }

}
