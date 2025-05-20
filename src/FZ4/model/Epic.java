package FZ4.model;

import java.util.ArrayList;
public class Epic extends Task {
    private ArrayList<Integer> subtaskIds;
    public Epic(int id, String name, String description) {
        super(id, name, description);
        this.subtaskIds = new ArrayList<>();
    }
    public void addSubtaskId(int subtaskId) {
        subtaskIds.add(subtaskId);
    }
    public void removeSubtaskId(int subtaskId) {
        subtaskIds.remove((Integer) subtaskId);
    }
    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }
}