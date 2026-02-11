package server;

import java.util.LinkedList;

public class HistoryManager {
    private final int MAX = 10;
    private LinkedList<DocumentMemento> history = new LinkedList<>();

    public void push(DocumentMemento m) {
        if (history.size() == MAX) {
            history.removeFirst();
        }
        history.addLast(m);
    }

    public DocumentMemento undo() {
        if (history.isEmpty()) return null;
        return history.removeLast();
    }

    public boolean hasHistory() {
        return !history.isEmpty();
    }
}