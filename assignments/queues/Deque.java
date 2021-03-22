package queues;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private class Node {
        Item item;
        Node prev;
        Node next;

        Node(Item item) {
            this.item = item;
        }
    }

    private final Node head;
    private final Node tail;
    private int N;

    public Deque() {
        head = new Node(null);
        tail = new Node(null);
        head.next = tail;
        tail.prev = head;
        N = 0;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item is null");
        }
        Node oldFirst = head.next;
        Node newFirst = new Node(item);
        newFirst.prev = head;
        newFirst.next = oldFirst;
        head.next = newFirst;
        oldFirst.prev = newFirst;
        N++;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item is null");
        }
        Node oldLast = tail.prev;
        Node newLast = new Node(item);
        newLast.next = tail;
        newLast.prev = oldLast;
        tail.prev = newLast;
        oldLast.next = newLast;
        N++;
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("No item in the deque");
        }
        Item item = head.next.item;
        head.next.next.prev = head;
        head.next = head.next.next;
        N--;
        return item;
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("No item in the deque");
        }
        Item item = tail.prev.item;
        tail.prev.prev.next = tail;
        tail.prev = tail.prev.prev;
        N--;
        return item;
    }

    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private Node curr = head.next;

            @Override
            public boolean hasNext() {
                return curr != tail;
            }

            @Override
            public Item next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No item in the deque");
                }
                Item item = curr.item;
                curr = curr.next;
                return item;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();

        try {
            deque.removeFirst();
        }
        catch (NoSuchElementException e) {
            e.printStackTrace();
        }

        try {
            deque.removeLast();
        }
        catch (NoSuchElementException e) {
            e.printStackTrace();
        }

        deque.addLast(1);
        System.out.println(deque.size() == 1);
        int num = deque.removeFirst();
        System.out.println(num == 1);
        System.out.println(deque.size() == 0);

        deque.addFirst(2);
        System.out.println(deque.size() == 1);
        deque.addFirst(1);
        System.out.println(deque.size() == 2);
        deque.addLast(3);
        System.out.println(deque.size() == 3);
        int first = deque.removeFirst();
        System.out.println(first == 1);
        int second = deque.removeFirst();
        System.out.println(second == 2);
        int third = deque.removeFirst();
        System.out.println(third == 3);

        try {
            deque.addFirst(null);
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
