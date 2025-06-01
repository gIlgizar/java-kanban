package FZ4.service;

import FZ4.model.Epic;
import FZ4.model.Subtask;
import FZ4.model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    ArrayList<Task> getAllTask();

    void deleteAllTask();

    Task getTaskById(int id);

    void createTask(Task task);

    void updateTask(Task task);

    void deleteTaskById(int id);

    ArrayList<Epic> getAllEpics();

    ArrayList<Subtask> getAllSubtasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    void createEpic(Epic epic);

    void createSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void deleteEpicById(int id);

    Subtask deleteSubtaskById(int id);

    void updateEpicStatus(Epic epic);

    List<Task> getHistory();
}
