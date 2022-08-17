package tasks;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task implements Comparable {
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Task task = (Task) o;
        return id == task.id && name.equals(task.name) && Objects.equals(description,
                task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status);
    }

    private String name;
    private String description;
    private int id;
    private TaskStatus status;
    protected int duration;
    protected LocalDateTime startTime;


    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    static TaskTypeName typeName = TaskTypeName.TASK;
    public Task(String n, String d,String startdt,int durmin) {
        name = n;
        description = d;
        status = TaskStatus.NEW;
        if(startdt!=null) {
            startTime = LocalDateTime.parse(startdt);
        }
        else{
            startTime = null;
        }
        duration = durmin;

    }

    public LocalDateTime getEndTime(){
        return startTime.plusMinutes(duration);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }
    public String getTypeName(){
        return typeName.toString();
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("LLL dd HH:mm");
        String s1 = "n/a";
        String s2 = "n/a";
        if(this.startTime!=null){
             s1 = this.startTime.format(fmt);
             s2 = this.getEndTime().format(fmt);
        }

        return String.format("%s name=%s desc=%s id=%d status=%s startTime=%s duration=%d min endTime=%s", this.getClass()
                .getSimpleName(), this.name, this.description, this.id, this.getStatus(), s1, this.duration, s2);
    }
    public String toFileString(){

        return String.format("%d,%s,%s,%s,%s,%s,%d",id,getTypeName(),this.name,this.status,
                this.description,this.startTime,this.duration);
    }

    @Override
    public int compareTo( Object o) {
        Task t = (Task)o;
        if(t.startTime==null && this.startTime==null){
            return 0;
        }
        else if(this.startTime==null){
            return 1;
        }
        else if(t.startTime==null){
            return -1;
        }
        if(startTime.isBefore(t.startTime)){
            return - 1;
        }
        else if(startTime.isAfter(t.startTime)){
            return 1;
        }
        else{
            return 0;
        }
    }
}
