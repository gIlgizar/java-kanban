package FZ4.model;

import java.util.ArrayList;

public class Epic extends Task{
    ArrayList<Integer>subtasksId=new ArrayList<>();
    public Epic( String name, String description) {
        super( name, description);
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void setSubtasksId(int id) {
        subtasksId.add(id);
    }

}
