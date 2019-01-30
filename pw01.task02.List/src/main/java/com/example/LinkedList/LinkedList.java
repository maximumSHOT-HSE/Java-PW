package com.example.LinkedList;

import java.util.AbstractSequentialList;
import java.util.List;
import java.util.ListIterator;

public class LinkedList<T> extends AbstractSequentialList<T> implements List<T> {
    private Node head;
    private Node tail;
    private int size;

    @Override
    public ListIterator<T> listIterator(int i) {
        return new ListIterator<T>() {
            LinkedList<T>.Node nodeAfter = head;
            int direction = 0;
            int position = 0;

            @Override
            public boolean hasNext() {
                return nodeAfter != null;
            }

            @Override
            public T next() {
                direction = 1;
                T result = nodeAfter.element;
                nodeAfter = nodeAfter.next;
                return result;
            }

            @Override
            public boolean hasPrevious() {
                return nodeAfter.previous != null;
            }

            @Override
            public T previous() {
                direction = -1;
                nodeAfter = nodeAfter.previous;
                return nodeAfter.element;
            }

            @Override
            public int nextIndex() {
                return position;
            }

            @Override
            public int previousIndex() {
                return position - 1;
            }

            @Override
            public void remove() {
                if (direction == 1) {
                    var nodeBefore = nodeAfter.previous;
                    var nodeBeforeBefore = nodeBefore.previous;
                    nodeBeforeBefore.next = nodeAfter;
                    nodeAfter.previous = nodeBeforeBefore;
                } else if (direction == -1) {
                    var nodeAfterAfter = nodeAfter.next;
                    var nodeBefore = nodeAfter.previous;
                    nodeBefore.next = nodeAfterAfter;
                    nodeAfterAfter.previous = nodeBefore;
                    nodeAfter = nodeAfterAfter;
                } else {
                    throw new IllegalStateException();
                }
            }

            @Override
            public void set(T t) {
                if (direction == 1) {
                    nodeAfter.previous.element = t;
                } else if (direction == -1) {
                    nodeAfter.element = t;
                } else {
                    throw new IllegalStateException();
                }
            }

            @Override
            public void add(T t) {
                var nodeBefore = nodeAfter.previous;
                var newNode = new Node();
                newNode.previous = nodeBefore;
                newNode.next = nodeAfter;
                newNode.element = t;
                nodeBefore.next = newNode;
                nodeAfter.previous = newNode;
                ++position;
            }
        };
    }

    @Override
    public int size() {
        return size;
    }

    private class Node {
        private Node next;
        private Node previous;
        private T element;
    }

}

