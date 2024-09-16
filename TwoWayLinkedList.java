import java.util.ListIterator;
import java.util.NoSuchElementException;

public class TwoWayLinkedList<E> implements MyList<E> {
    private Node<E> head, tail;
    private int size = 0; // Number of elements in the list

    /** Create an empty list */
    public TwoWayLinkedList() {
    }

    /** Create a list from an array of objects */
    public TwoWayLinkedList(E[] objects) {
        for (int i = 0; i < objects.length; i++) {
            add(objects[i]);
        }
    }

    /** Return the head element in the list */
    public E getFirst() {
        if (size == 0) {
            return null;
        } else {
            return head.element;
        }
    }

    /** Return the last element in the list */
    public E getLast() {
        if (size == 0) {
            return null;
        } else {
            return tail.element;
        }
    }

    /** Add an element to the beginning of the list */
    public void addFirst(E e) {
        Node<E> newNode = new Node<>(e);
        if (head == null) { // List is empty
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.previous = newNode;
            head = newNode;
        }
        size++;
    }

    /** Add an element to the end of the list */
    public void addLast(E e) {
        Node<E> newNode = new Node<>(e);
        if (tail == null) { // List is empty
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.previous = tail;
            tail = newNode;
        }
        size++;
    }

    @Override /** Add a new element at the specified index in this list. The index of the head element is 0 */
    public void add(int index, E e) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (index == 0) {
            addFirst(e);
        } else if (index == size) {
            addLast(e);
        } else {
            Node<E> current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            Node<E> newNode = new Node<>(e);
            newNode.next = current;
            newNode.previous = current.previous;
            current.previous.next = newNode;
            current.previous = newNode;
            size++;
        }
    }

    /** Remove the head node and return the object that is contained in the removed node. */
    public E removeFirst() {
        if (head == null) {
            throw new NoSuchElementException("List is empty");
        }
        E element = head.element;
        if (head == tail) { // Only one element in the list
            head = tail = null;
        } else {
            head = head.next;
            head.previous = null;
        }
        size--;
        return element;
    }

    /** Remove the last node and return the object that is contained in the removed node. */
    public E removeLast() {
        if (tail == null) {
            throw new NoSuchElementException("List is empty");
        }
        E element = tail.element;
        if (head == tail) { // Only one element in the list
            head = tail = null;
        } else {
            tail = tail.previous;
            tail.next = null;
        }
        size--;
        return element;
    }

    @Override /** Remove the element at the specified position in this list. Return the element that was removed from the list. */
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (index == 0) {
            return removeFirst();
        } else if (index == size - 1) {
            return removeLast();
        } else {
            Node<E> current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            E element = current.element;
            current.previous.next = current.next;
            current.next.previous = current.previous;
            size--;
            return element;
        }
    }

    @Override /** Override toString() to return elements in the list */
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        Node<E> current = head;
        while (current != null) {
            result.append(current.element);
            current = current.next;
            if (current != null) {
                result.append(", ");
            }
        }
        result.append("]");
        return result.toString();
    }

    @Override /** Clear the list */
    public void clear() {
        size = 0;
        head = tail = null;
    }

    @Override /** Return true if this list contains the element e */
    public boolean contains(Object e) {
        Node<E> current = head;
        while (current != null) {
            if (current.element.equals(e)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override /** Return the element at the specified index */
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<E> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.element;
    }

    @Override /** Return the index of the first matching element in this list. Return −1 if no match. */
    public int indexOf(Object e) {
        Node<E> current = head;
        for (int i = 0; i < size; i++) {
            if (current.element.equals(e)) {
                return i;
            }
            current = current.next;
        }
        return -1;
    }

    @Override /** Return the index of the last matching element in this list. Return −1 if no match. */
    public int lastIndexOf(E e) {
        Node<E> current = tail;
        for (int i = size - 1; i >= 0; i--) {
            if (current.element.equals(e)) {
                return i;
            }
            current = current.previous;
        }
        return -1;
    }

    @Override /** Replace the element at the specified position in this list with the specified element. */
    public E set(int index, E e) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<E> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        E oldElement = current.element;
        current.element = e;
        return oldElement;
    }

    @Override /** Override iterator() defined in Iterable */
    public java.util.Iterator<E> iterator() {
        return new LinkedListIterator();
    }

    /** Return a list iterator starting from the head of the list */
    public ListIterator<E> listIterator() {
        return new ListIteratorImpl(0);
    }

    /** Return a list iterator starting from the specified index */
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return new ListIteratorImpl(index);
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private class ListIteratorImpl implements ListIterator<E> {
        private Node<E> current;
        private int index;

        public ListIteratorImpl(int index) {
            this.index = index;
            if (index == size) {
                current = null;
            } else {
                current = head;
                for (int i = 0; i < index; i++) {
                    current = current.next;
                }
            }
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E element = current.element;
            current = current.next;
            index++;
            return element;
        }

        @Override
        public boolean hasPrevious() {
            return index > 0;
        }

        @Override
        public E previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            current = (current == null) ? tail : current.previous;
            E element = current.element;
            index--;
            return element;
        }

        @Override
        public int nextIndex() {
            return index;
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        @Override
        public void remove() {
            if (current == null) {
                throw new IllegalStateException();
            }
            Node<E> nodeToRemove = (current.previous != null) ? current.previous : current;
            TwoWayLinkedList.this.remove(index - 1);
            if (nodeToRemove == head) {
                current = head;
            } else {
                current = nodeToRemove.previous;
            }
            size--;
        }

        @Override
        public void set(E e) {
            if (current == null) {
                throw new IllegalStateException();
            }
            current.element = e;
        }

        @Override
        public void add(E e) {
            throw new UnsupportedOperationException("Add is not supported in this iterator.");
        }
    }

    private static class Node<E> {
        E element;
        Node<E> next;
        Node<E> previous;

        public Node(E element) {
            this.element = element;
        }
    }
}

