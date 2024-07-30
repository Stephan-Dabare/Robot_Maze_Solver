// Custom Liked List class
public class CustomLinkedList {
    public class Node { // Class for one Node
        public RobotPosition data;
        public Node next;

        public Node(RobotPosition data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;
    private int size;

    public boolean isEmpty() {
        return head == null;
    }  // method to check if list is empty.

    public void add(RobotPosition data) {  // Add method
        Node newNode = new Node(data);
        if (isEmpty()) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }


    public RobotPosition removeFirst() {  // Remove method
        if (isEmpty()) {
            return null;
        }
        Node temp = head;
        head = head.next;
        temp.next = null;
        size--;
        return temp.data;
    }

    public Node getHead() {
        return head;
    }   // Method to get the head node
}
