import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Subtask> subtasks;

    public Epic(int id, String name, String description) {
        super(id, name, description);
        this.subtasks =new ArrayList<>();
    }
    public void addSubtask(Subtask subtask){
        subtasks.add(subtask);
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }
    public void updateStatus(){
        if (subtasks.isEmpty()){
            status=Status.NEW;
            return;
        }
        boolean done=true;
        boolean progress=false;

        for (Subtask sss:subtasks){
            if(sss.getStatus()!=Status.DONE){
                done=false;
            }
            if (sss.getStatus()==Status.IN_PROGRESS){
                progress=true;
            }
        }
        if (done){
            status=Status.DONE;
        } else if (progress) {
status=Status.IN_PROGRESS;
        }else {
            status=Status.NEW;
        }
    }
}