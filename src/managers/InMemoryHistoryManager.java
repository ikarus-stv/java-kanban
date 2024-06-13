package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private Node firstNode;
    private Node lastNode;

    private final Map<Integer, Node> nodeMap = new HashMap<>();

    private Node linkLast(Task task) {
        Node result = new Node(lastNode, null, task);
        if (lastNode != null) {
            lastNode.next = result;
        }
        lastNode = result;
        if (firstNode == null) {
            firstNode = result;
        }
        return result;
    }

    private List<Task> getTasks() {
        List<Task> result = new ArrayList<>();
        for (Node current = firstNode; current != null; current = current.next) {
            result.add(current.task);
        }
        return result;
    }

    private void removeNode(Node node) {
        if (firstNode == node) {
            firstNode = node.next;
        }
        if (lastNode == node) {
            lastNode = node.prev;
        }

        node.unlink();

    }

    public int size() {
        return nodeMap.size();
    }

    @Override
    public void add(Task task) {
        if (task == null) return;
        int id = task.getId();
        remove(id);
        Node newNode = linkLast(task);
        nodeMap.put(id, newNode);
    }

    @Override
    public void remove(int id) {
        final Node oldNode = nodeMap.get(id);
        if (oldNode != null) {
            removeNode(oldNode);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public String toString() {
        return "InMemoryHistoryManager{" +
                "history=" + getHistory() +
                '}';
    }
}
