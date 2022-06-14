package Tasks;

import java.util.Objects;

public class Task {
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
    static TaskTypeName typeName = TaskTypeName.TASK;
    public Task(String n, String d) {
        name = n;
        description = d;
        status = TaskStatus.NEW;

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
        return String.format("%s name=%s desc=%s id=%d status=%s", this.getClass()
                .getSimpleName(), this.name, this.description, this.id, this.getStatus());
    }
    public String toFileString(){
        return String.format("%d,%s,%s,%s,%s,",id,getTypeName(),this.name,this.status,this.description);
    }
}
