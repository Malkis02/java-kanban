package Tasks;

public class Task {

    private String name;
    private String description;
    private int id;
    private String status;

    public Task(String n, String d) {
        name = n;
        description = d;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        //       return "Tasks.Task{" + "name='" + name + '\'' + ", description='" + description + '\''
        //       + ", " + "id=" + id + ", status='" + status + '\'' + '}';
        return String.format("%s name=%s desc=%s id=%d status=%s", this.getClass()
                .getSimpleName(), this.name, this.description, this.id, this.getStatus());
    }
}
