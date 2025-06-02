import FZ4.model.*;
import FZ4.service.*;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class Test {
    private TaskManager taskManager;

    @BeforeEach
    public void setup() {
        taskManager = Managers.getDefault();
    }

    @org.junit.jupiter.api.Test
    public void createAndRetrieveSubtask() {
        Epic epic = new Epic("Epic1", "Epic Desc");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask1", "Subtask Desc", epic.getId());
        taskManager.createSubtask(subtask);

        Subtask retrieved = taskManager.getSubtaskById(subtask.getId());
        assertNotNull(retrieved, "Subtask should be retrievable by ID");
        assertEquals(subtask.getName(), retrieved.getName(), "Names should match");
        assertEquals(epic.getId(), retrieved.getEpicId(), "Epic ID should match");
    }

    @org.junit.jupiter.api.Test
    public void deleteEpicAlsoDeletesSubtasks() {
        Epic epic = new Epic("EpicToDelete", "Desc");
        taskManager.createEpic(epic);
        Subtask sub1 = new Subtask("Sub1", "DescSub1", epic.getId());
        Subtask sub2 = new Subtask("Sub2", "DescSub2", epic.getId());
        taskManager.createSubtask(sub1);
        taskManager.createSubtask(sub2);

        taskManager.deleteEpicById(epic.getId());

        assertNull(taskManager.getEpicById(epic.getId()), "Epic should be deleted");
        assertNull(taskManager.getSubtaskById(sub1.getId()), "Subtask 1 should be deleted");
        assertNull(taskManager.getSubtaskById(sub2.getId()), "Subtask 2 should be deleted");
    }

    @org.junit.jupiter.api.Test
    public void updateEpicStatusWhenAllSubtasksDone() {
        Epic epic = new Epic("Epic1", "Desc");
        taskManager.createEpic(epic);

        Subtask sub1 = new Subtask("Sub1", "Desc1", epic.getId());
        sub1.setStatus(TaskStatus.DONE);
        Subtask sub2 = new Subtask("Sub2", "Desc2", epic.getId());
        sub2.setStatus(TaskStatus.DONE);

        taskManager.createSubtask(sub1);
        taskManager.createSubtask(sub2);

        taskManager.updateEpicStatus(epic);
        assertEquals(TaskStatus.DONE, taskManager.getEpicById(epic.getId()).getStatus(), "Epic status should be DONE");
    }

    @org.junit.jupiter.api.Test
    public void updateEpicStatusWhenSubtasksInProgress() {
        Epic epic = new Epic("Epic2", "Desc");
        taskManager.createEpic(epic);

        Subtask sub1 = new Subtask("Sub1", "Desc1", epic.getId());
        sub1.setStatus(TaskStatus.IN_PROGRESS);
        Subtask sub2 = new Subtask("Sub2", "Desc2", epic.getId());
        sub2.setStatus(TaskStatus.NEW);

        taskManager.createSubtask(sub1);
        taskManager.createSubtask(sub2);

        taskManager.updateEpicStatus(epic);
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpicById(epic.getId()).getStatus(), "Epic status should be IN_PROGRESS");
    }

    @org.junit.jupiter.api.Test
    public void deleteSubtaskRemovesFromEpic() {
        Epic epic = new Epic("Epic", "Desc");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Sub", "Desc", epic.getId());
        taskManager.createSubtask(subtask);

        Subtask deleted = taskManager.deleteSubtaskById(subtask.getId());
        assertNotNull(deleted, "Deleted subtask should not be null");
        assertFalse(taskManager.getEpicById(epic.getId()).getSubtasksId().contains(subtask.getId()), "Subtask ID should be removed from epic");
    }

    @org.junit.jupiter.api.Test
    public void createIdIncrements() {
        int id1 = InMemoryTaskManager.createId();
        int id2 = InMemoryTaskManager.createId();
        assertTrue(id2 > id1, "второй id больше первого");
    }

    @org.junit.jupiter.api.Test
    public void idNotUpdateWhenUpdateTask() {
        Task task = new Task("Task", "Desc");
        taskManager.createTask(task);
        int originalId = task.getId();

        task.setName("Updated Name");
        taskManager.updateTask(task);

        Task updated = taskManager.getTaskById(originalId);
        assertEquals(originalId, updated.getId(), "Task ID should not change on update");
        assertEquals("Updated Name", updated.getName(), "Task name should be updated");
    }
}
