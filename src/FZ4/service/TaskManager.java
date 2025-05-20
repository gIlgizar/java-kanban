package FZ4.service;

import FZ4.model.*;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int nextId = 1;

    private int generateId() {
        return nextId++;
    }

    public Task createTask(String name, String description) {
        int id = generateId();
        Task task = new Task(id, name, description);
        tasks.put(id, task);
        return task;
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public void updateTask(Task task) {
        int id = task.getId();
        tasks.put(id, task);
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public Epic createEpic(String name, String description) {
        int id = generateId();
        Epic epic = new Epic(id, name, description);
        epics.put(id, epic);
        return epic;
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public void deleteAllEpics() {
        for (Epic epic : epics.values()) {
            ArrayList<Integer> toRemove = new ArrayList<>(epic.getSubtaskIds());
            for (int subtaskId : toRemove) {
                subtasks.remove(subtaskId);
                tasks.remove(subtaskId);
            }
            tasks.remove(epic.getId());
        }
        epics.clear();
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public void updateEpic(Epic epic) {
        int id = epic.getId();
        epics.put(id, epic);
    }

    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            ArrayList<Integer> remove = new ArrayList<>(epic.getSubtaskIds());
            for (int subtaskId : remove) {
                subtasks.remove(subtaskId);
                tasks.remove(subtaskId);
            }
            tasks.remove(id);
        }
    }

    public Subtask createSubtask(String name, String description, int epicId) {
        int id = generateId();
        Subtask subtask = new Subtask(id, name, description, epicId);
        subtasks.put(id, subtask);
        tasks.put(id, subtask);
        epics.get(epicId).addSubtaskId(id);
        return subtask;
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void deleteAllSubtasks() {
        for (Subtask sub : subtasks.values()) {
            tasks.remove(sub.getId());
        }
        subtasks.clear();

        for (Epic epic : epics.values()) {
            epic.getSubtaskIds().clear();
            epic.setStatus(Status.NEW);
        }
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public void updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        subtasks.put(id, subtask);
        tasks.put(id, subtask);
    }

    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            tasks.remove(id);
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtaskId(id);
            }
        }
    }

    public ArrayList<Subtask> getSubtasksForEpic(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Subtask> result = new ArrayList<>();
        if (epic == null) return result;

        for (Integer subtaskId : epic.getSubtaskIds()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask != null) {
                result.add(subtask);
            }
        }
        return result;
    }

    public void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) return;

        ArrayList<Integer> subtaskIds = epic.getSubtaskIds();
        if (subtaskIds.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allDone = true;
        boolean anyInProgress = false;
        boolean anyNew = false;

        for (Integer subId : subtaskIds) {
            Subtask subtask = subtasks.get(subId);
            if (subtask != null) {
                Status st = subtask.getStatus();
                if (st != Status.DONE) {
                    allDone = false;
                }
                if (st == Status.IN_PROGRESS) {
                    anyInProgress = true;
                }
                if (st == Status.NEW) {
                    anyNew = true;
                }
            }
        }

        if (allDone) {
            epic.setStatus(Status.DONE);
        } else if (anyInProgress || (anyNew && !allDone)) {
            epic.setStatus(Status.IN_PROGRESS);
        } else {
            epic.setStatus(Status.NEW);
        }
    }
}

