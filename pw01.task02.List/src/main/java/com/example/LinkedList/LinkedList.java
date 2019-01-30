package com.example.LinkedList;

import java.util.*;

class LinkedList<T> extends AbstractSequentialList<T> implements List<T> {
    private Node head;
    private Node tail;
    private int size;

    @Override
    public ListIterator<T> listIterator(int i) {
        return new ListIterator<T>() {
            LinkedList<T>.Node currentNode = head;
            int position = 0;

            @Override
            public boolean hasNext() {
                return currentNode.next != null;
            }

            @Override
            public T next() {
                currentNode = currentNode.next;
                return currentNode.element;
            }

            @Override
            public boolean hasPrevious() {
                return currentNode.previous != null;
            }

            @Override
            public T previous() {
                currentNode = currentNode.previous;
                return currentNode.element
            }

            @Override
            public int nextIndex() {
                return position + 1;
            }

            @Override
            public int previousIndex() {
                return position - 1;
            }

            @Override
            public void remove() {
                var previousNode = currentNode.previous;
                var nextNode = currentNode.next;

            }

            @Override
            public void set(T t) {

            }

            @Override
            public void add(T t) {

            }
        }
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

