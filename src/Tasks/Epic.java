package Tasks;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        Epic epic = (Epic) o;
        return Objects.equals(subs, epic.subs);
    }

    static TaskTypeName typeName = TaskTypeName.EPIC;

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subs);
    }

    private List<SubTask> subs = new ArrayList<>();

    public Epic(String n, String d) {
        super(n, d);
    }

    public void addSub(SubTask sb) {
        if (sb == null || subs.contains(sb)) {
            return;
        }
        subs.add(sb);
        sb.setMaster(this);
    }

    public List<SubTask> getSubs() {
        return subs;
    }

    @Override
    public TaskStatus getStatus() {
        if (subs.size() == 0) {
            return TaskStatus.NEW;
        }
        int totalNew = 0;
        int totalProg = 0;
        int totalDone = 0;
        for (int k = 0; k < subs.size(); k++) {
            if (subs.get(k).getStatus().equals(TaskStatus.NEW))
                totalNew++;
            if (subs.get(k).getStatus().equals(TaskStatus.IN_PROGRESS))
                totalProg++;
            if (subs.get(k).getStatus().equals(TaskStatus.DONE))
                totalDone++;
        }
        if (totalNew == subs.size()) {
            return TaskStatus.NEW;
        } else if (totalDone == subs.size()) {
            return TaskStatus.DONE;
        } else {
            return TaskStatus.IN_PROGRESS;
        }
    }
    @Override
    public String getTypeName(){
        return typeName.toString();
    }

    @Override
    public String toString() {
        String s = super.toString();
        return String.format("%s subcount=%d", s, subs.size());
    }
    @Override
    public String toFileString(){
        String s = super.toFileString();
        return s;
    }
}
