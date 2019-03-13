package ru.hse.surkov.pw08;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyList<V> implements Iterable {

    @Nullable private Node tail;
    @Nullable private Node head;

    @NotNull
    @Override
    public Iterator<V> iterator() {
        return new Iterator<>() {

            private Node current = tail;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public V next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("Next element has not been found");
                }
                V result = current.value;
                current = current.next;
                return result;
            }
        };
    }

    public void clear() {
        tail = null;
        head = null;
    }

    public void add(@Nullable V value) {
        if (tail == null) {
            tail = new Node(value);
            head = tail;
        } else {
            Node oldHead = head;
            head = new Node(value);
            oldHead.next = head;
        }
    }

    private class Node {
        private V value;
        private Node next;

        public Node(V value) {
            this.value = value;
        }
    }
}
