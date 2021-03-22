package queues;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

public class RandomizedQueue<Item> implements Iterable<Item> {

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

    public RandomizedQueue() {
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

    public void enqueue(Item item) {
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

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("No item in the dequeue");
        }
        Node node = get(new Random().nextInt(N) + 1);
        Node prev = node.prev;
        Node next = node.next;
        prev.next = next;
        next.prev = prev;
        N--;
        return node.item;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("No item in the dequeue");
        }
        return get(new Random().nextInt(N) + 1).item;
    }

    private Node get(int num) {
        Node curr;
        if (num < N - num) {
            curr = head;
            for (int i = 0; i < num; i++) {
                curr = curr.next;
            }
        }
        else {
            curr = tail;
            for (int i = 0; i < num; i++) {
                curr = curr.prev;
            }
        }
        return curr;
    }

    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            boolean[] visited = new boolean[N];
            int count = 0;

            @Override
            public boolean hasNext() {
                return count < N;
            }

            @Override
            public Item next() {
                int rand;
                while (true) {
                    rand = new Random().nextInt(N);
                    if (!visited[rand]) {
                        visited[rand] = true;
                        count++;
                        break;
                    }
                }
                return get(rand + 1).item;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();

        try {
            rq.dequeue();
        }
        catch (NoSuchElementException e) {
            e.printStackTrace();
        }

        try {
            rq.sample();
        }
        catch (NoSuchElementException e) {
            e.printStackTrace();
        }

        rq.enqueue(1);
        System.out.println(rq.size() == 1);

        rq.enqueue(2);
        System.out.println(rq.size() == 2);

        int sample = rq.sample();
        System.out.println(sample == 1 || sample == 2);
        System.out.println(rq.size() == 2);

        int num = rq.dequeue();
        System.out.println(num == 1 || num == 2);
        System.out.println(rq.size() == 1);
    }
}
