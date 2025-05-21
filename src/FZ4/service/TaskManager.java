package FZ4.service;

import FZ4.model.*;


import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private static int nextId = 0;

    public static int createId() {
        nextId++;
        return nextId;
    }

    public ArrayList<Task> getAllTask() {
        return new ArrayList<>(tasks.values());
    }

    public void deleteAllTask() {
        tasks.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public void createTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void deleteAllEpics() {
        epics.clear();
        deleteAllSubtasks();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public void createEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void createSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.setSubtasksId(subtask.getId());
            updateEpicStatus(epic);
        }
    }


    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                updateEpicStatus(epic);
            }
        }
    }

    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (int subId : epic.getSubtasksId()) {
                subtasks.remove(subId);
            }
        }
    }

    public Subtask deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.getSubtasksId().remove((Integer) id);
            }
        }
        return subtask;
    }
    public void updateEpicStatus(Epic epic) {
        ArrayList<Integer> subtaskIds = epic.getSubtasksId();
        if (subtaskIds.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            boolean allDone = true;
            boolean anyInProgress = false;
            for (Integer subId : subtaskIds) {
                Subtask subtask = (Subtask) getSubtaskById(subId);
                if (subtask != null) {
                    if (subtask.getStatus() != TaskStatus.DONE) {
                        allDone = false;
                    }
                    if (subtask.getStatus() == TaskStatus.IN_PROGRESS) {
                        anyInProgress = true;
                    }
                }
            }
            if (allDone) {
                epic.setStatus(TaskStatus.DONE);
            } else if (anyInProgress || !allDone) {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            } else {
                epic.setStatus(TaskStatus.NEW);
            }
        }
    }
}


