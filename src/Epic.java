import java.util.ArrayList;
import java.util.List;

class Epic extends Task {

    private List<SubTask> subs = new ArrayList<>();

    public Epic(String n, String d) {
        super(n, d);
    }

    public void addSub(SubTask sb) {
        if(sb == null || subs.contains(sb)){
            return;
        }
        subs.add(sb);
        sb.setMaster(this);
    }
    public List<SubTask> getSubs(){
        return subs;
    }

    @Override
    public String getStatus(){
        if(subs.size() == 0){
            return Manager.STATUS_NEW_NAME;
        }
        int totalNew = 0;
        int totalProg = 0;
        int totalDone = 0;
        for(int k =0; k< subs.size();k++){
            if(subs.get(k).getStatus().equals(Manager.STATUS_NEW_NAME)) totalNew++;
            if(subs.get(k).getStatus().equals(Manager.STATUS_IN_PROGRESS_NAME)) totalProg++;
            if(subs.get(k).getStatus().equals(Manager.STATUS_DONE_NAME)) totalDone++;
        }
        if(totalNew == subs.size()){
            return Manager.STATUS_NEW_NAME;
        }
        else if(totalDone == subs.size()){
            return Manager.STATUS_DONE_NAME;
        }
        else{
            return Manager.STATUS_IN_PROGRESS_NAME;
        }
    }

    @Override
    public String toString() {
       String s = super.toString();
       return String.format("%s subcount=%d",s,subs.size());
    }

}
