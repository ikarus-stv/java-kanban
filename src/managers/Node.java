package managers;

import tasks.Task;

public class Node {
    Node prev;
    Node next;
    Task task;

    public Node(Node prev, Node next, Task task) {
        this.prev = prev;
        this.next = next;
        this.task = task;
    }

    public void unlink() {
        if (prev != null) {
            prev.next = next;
        }

        if (next != null) {
            next.prev = prev;
        }
        prev = null;
        next = null;

    }
}
