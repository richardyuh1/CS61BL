/* A doubly-linked list supporting various sorting algorithms. */
public class DLList<T extends Comparable<T>> {

    private class Node {

        T item;
        Node prev;
        Node next;

        Node(T item) {
            this.item = item;
            this.prev = this.next = null;
        }

        Node(T item, Node prev, Node next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    /* The sentinel of this DLList. */
    Node sentinel;
    /* The number of items in this DLList. */
    int size;

    /* Creates an empty DLList. */
    public DLList() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        this.size = 0;
    }

    /* Creates a copy of DLList represented by LST. */
    public DLList(DLList<T> lst) {
        Node ptr = lst.sentinel.next;
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        this.size = 0;
        while (ptr != lst.sentinel) {
            addLast(ptr.item);
            ptr = ptr.next;
        }
    }

    /* Returns true if this DLList is empty. Otherwise, returns false. */
    public boolean isEmpty() {
        return size == 0;
    }

    /* Adds a new Node with item ITEM to the front of this DLList. */
    public void addFirst(T item) {
        Node newNode = new Node(item, sentinel, sentinel.next);
        sentinel.next.prev = newNode;
        sentinel.next = newNode;
        size += 1;
    }

    /* Adds a new Node with item ITEM to the end of this DLList. */
    public void addLast(T item) {
        Node newNode = new Node(item, sentinel.prev, sentinel);
        sentinel.prev.next = newNode;
        sentinel.prev = newNode;
        size += 1;
    }

    /* Removes the Node referenced by N from this DLList. */
    private void remove(Node n) {
        n.prev.next = n.next;
        n.next.prev = n.prev;
        n.next = null;
        n.prev = null;
        size -= 1;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Node ptr = sentinel.next; ptr != sentinel; ptr = ptr.next) {
            s.append(ptr.item.toString());
            s.append(" ");
        }
        return s.toString();
    }

    /* Returns a copy of this DLList sorted using insertion sort. Does not
       modify the original DLList. */
    public DLList<T> insertionSort() {
        DLList<T> toReturn = new DLList<>();
        for (Node ptr = sentinel.next; ptr != sentinel; ptr = ptr.next) {
            toReturn.insertionSortHelper(ptr.item);
        }
        return toReturn;
    }

    /* Inserts ITEM into this DLList such that the values of this DLList are in
       increasing order. */
    private void insertionSortHelper(T item) {
        //System.out.println("For item: "+item);
        //Node n = sentinel.next;
        if (this.isEmpty()) {
            //System.out.println("Base case:" + item);
            this.addLast(item);
        } else if (item.compareTo(sentinel.next.item) < 0) {
            this.addFirst(item);
        } else if (item.compareTo(sentinel.prev.item) > 0) {
            this.addLast(item);
        } else {
            Node n = sentinel.next;
            while (item.compareTo(n.item) < 0 || item.compareTo(n.next.item) > 0) {
                n = n.next;
            }
            Node insert = new Node(item, n, n.next);
            n.next = insert;
            n.next.next.prev = insert;
            //System.out.println("iteration:" + n.item);
        }
    }


    /* Returns a copy of this DLList sorted using selection sort. Does not
       modify the original DLList. */
    public DLList<T> selectionSort() {
        DLList<T> copy = new DLList<>(this);
        DLList<T> toReturn = new DLList<>();
        while (copy.size != 0) {
            Node min = copy.sentinel.next;
            for (Node p = copy.sentinel.next; p != copy.sentinel; p = p.next) {
                if (p.item.compareTo(min.item) < 0) {
                    min = p;
                }
            }
            toReturn.addLast(min.item);
            copy.remove(min);
        }
        return toReturn;
    }

    /* Returns a copy of this DLList sorted using merge sort. Does not modify
       the original DLList. */
    public DLList<T> mergeSort() {
        if (size <= 1) {
            return this;
        }
        Node n = sentinel.next;
        DLList<T> oneHalf = splitLL(n, 0, this.size / 2);
        DLList<T> otherHalf = splitLL(n, this.size / 2, this.size);

        return oneHalf.mergeSort().merge(otherHalf.mergeSort());
    }

    public DLList<T> splitLL(Node n, int start, int end) {
        DLList<T> half = new DLList<>();
        int i = 0;
        while (i < start) {
            n = n.next;
            i++;
        }
        while (i < end) {
            half.addLast(n.item);
            n = n.next;
            i++;
        }
        return half;
    }

    /* Returns the result of merging this DLList with LST. Does not modify the
       two DLLists. Assumes that this DLList and LST are in sorted order. */
    private DLList<T> merge(DLList<T> lst) {
        DLList<T> toReturn = new DLList<T>();
        Node thisPtr = sentinel.next;
        Node lstPtr = lst.sentinel.next;
        while (thisPtr != sentinel && lstPtr != lst.sentinel) {
            if (thisPtr.item.compareTo(lstPtr.item) < 0) {
                toReturn.addLast(thisPtr.item);
                thisPtr = thisPtr.next;
            } else {
                toReturn.addLast(lstPtr.item);
                lstPtr = lstPtr.next;
            }
        }
        while (thisPtr != sentinel) {
            toReturn.addLast(thisPtr.item);
            thisPtr = thisPtr.next;
        }
        while (lstPtr != lst.sentinel) {
            toReturn.addLast(lstPtr.item);
            lstPtr = lstPtr.next;
        }
        return toReturn;
    }

    /* Returns a copy of this DLList sorted using quicksort. The first element
       is used as the pivot. Does not modify the original DLList. */
    public DLList<T> quicksort() {
        if (size <= 1) {
            return this;
        }
        // Assume first element is the divider.
        DLList<T> smallElements = new DLList<>();
        DLList<T> equalElements = new DLList<>();
        DLList<T> largeElements = new DLList<>();
        T pivot = sentinel.next.item;
        Node n = sentinel.next;
        while (n != sentinel) {
            if (n.item.compareTo(pivot) < 0) {
                smallElements.addLast(n.item);
            }
            if (n.item.compareTo(pivot) == 0) {
                equalElements.addLast(n.item);
            }
            if (n.item.compareTo(pivot) > 0) {
                largeElements.addLast(n.item);
            }

            n = n.next;
        }
        /*System.out.println("Small: " + smallElements + ", size: " + smallElements.size);
        System.out.println("Equal: " + equalElements + ", size: " + equalElements.size);
        System.out.println("Large: " + largeElements + ", size: " + largeElements.size);*/
        DLList<T> copy = new DLList<>();
        copy.append(smallElements.quicksort());
        copy.append(equalElements);
        copy.append(largeElements.quicksort());

        return copy;
    }

    /* Appends LST to the end of this DLList. */
    public void append(DLList<T> lst) {
        if (lst.isEmpty()) {
            return;
        }
        if (isEmpty()) {
            sentinel = lst.sentinel;
            size = lst.size;
            return;
        }
        sentinel.prev.next = lst.sentinel.next;
        lst.sentinel.next.prev = sentinel.prev;
        sentinel.prev = lst.sentinel.prev;
        lst.sentinel.prev.next = sentinel;
        size += lst.size;
    }

    /* Returns a random integer between 0 and 99. */
    private static int randomInt() {
        return (int) (100 * Math.random());
    }

    private static DLList<Integer> generateRandomIntegerDLList(int N) {
        DLList<Integer> toReturn = new DLList<>();
        for (int k = 0; k < N; k++) {
            toReturn.addLast((int) (100 * Math.random()));
        }
        return toReturn;
    }

    public static void main(String[] args) {
        DLList values;
        DLList sortedValues;

        DLList<Integer> test = new DLList<>();
        int[] copy = new int[]{0, 0, 1, 2, 3, 4, 4, 5, 6, 7, 8, 9, 9};
        for (int i: copy) {
            test.addFirst(i);
        }

        System.out.println(test);
        System.out.println(test.quicksort());


        System.out.print("Before insertion sort: ");
        values = generateRandomIntegerDLList(10);
        System.out.println(values);
        sortedValues = values.insertionSort();
        System.out.print("After insertion sort: ");
        System.out.println(sortedValues);

        System.out.print("Before selection sort: ");
        values = generateRandomIntegerDLList(10);
        System.out.println(values);
        sortedValues = values.selectionSort();
        System.out.print("After selection sort: ");
        System.out.println(sortedValues);

        System.out.print("Before merge sort: ");
        values = generateRandomIntegerDLList(10);
        System.out.println(values);
        sortedValues = values.mergeSort();
        System.out.print("After merge sort: ");
        System.out.println(sortedValues);

        System.out.print("Before quicksort: ");
        values = generateRandomIntegerDLList(10);
        System.out.println(values);
        sortedValues = values.quicksort();
        System.out.print("After quicksort: ");
        System.out.println(sortedValues);
    }
}
