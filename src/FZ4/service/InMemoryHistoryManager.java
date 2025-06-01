package FZ4.service;

import FZ4.model.Task;
import FZ4.service.HistoryManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> history = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (task == null) {
            return; // Не добавляем null задачи
        }
        // Удаляем задачу из истории, если она уже есть
        history.remove(task);
        // Добавляем задачу в начало списка
        history.addFirst(task);
        // Если размер истории превышает 10, удаляем последнюю задачу
        if (history.size() > 10) {
            history.removeLast();
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history); // Возвращаем копию истории
    }
}
