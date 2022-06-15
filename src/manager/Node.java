package manager;

import Tasks.Task;

public class Node {
    Task core;
    Node prev;
    Node next;

    public Node(Task t) {
        this.core = t;
        this.prev = null;
        this.next = null;

    }
}
