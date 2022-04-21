package Tasks;

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

    public SubTask(String n, String d) {
        super(n, d);
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

}
