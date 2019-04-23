/**
 * A data structure to represent a Linked List of Integers.
 * Each IntList represents one node in the overall Linked List.
 */
public class IntList {
    public int first;
    public IntList rest;

    public IntList(int f, IntList r) {
        first = f;
        rest = r;
    }

    /** Returns an IntList consisting of the given values. */
    public static IntList of(int... values) {
        if (values.length == 0) {
            return null;
        }
        IntList p = new IntList(values[0], null);
        IntList front = p;
        for (int i = 1; i < values.length; i++) {
            p.rest = new IntList(values[i], null);
            p = p.rest;
        }
        return front;
    }

    /** Returns the size of the list. */
    public int size() {
        if (rest == null) {
            return 1;
        }
        return 1 + rest.size();
    }

    /** Returns [position]th value in this list. */
    public int get(int position) {
        if (position == 0) {
            return first; 
        } else {
            return rest.get(position - 1);
        }
    }

    /** Returns the string representation of the list. */
    public String toString() {
        if (size() == 1) {
            return Integer.toString(first);
        } else {
            return Integer.toString(first) + " " + rest.toString();
        }
    }

    /** Returns whether this and the given list or object are equal. */
    public boolean equals(Object o) {
        IntList other = (IntList) o;
        return this.size() == other.size() && this.toString().equals(other.toString()); 
    }

    public void add(int value) {
        if (rest == null) {
            rest = new IntList(value, null);
        } else {
            rest.add(value);
        }
    }

    public int smallest() {
        int smallest = first;
        while (rest != null) {
            if (smallest > rest.first) {
                smallest = rest.first;
            }
            rest = rest.rest;
        }
        return smallest;
    }

    public int squaredSum() {
        if (rest == null) {
            return first * first;
        }
        int sum = first * first;
        while (rest != null) {
            sum += rest.first * rest.first;
            rest = rest.rest;
        }
        return sum;
    }

    public static void dSquareList(IntList L) {
        while (L != null) {
            L.first = L.first * L.first;
            L = L.rest;
        }
    }

    public static IntList dcatenate(IntList A, IntList B) {
        if (A == null) {
            return B;
        }
        if (B == null) {
            return A;
        }

        IntList temp = A;
        while (temp.rest != null) {
            temp = temp.rest;
        }
        temp.rest = B;
        return A;
    }

    public static IntList catenate(IntList A, IntList B) {
        if (A == null) {
            return B;
        }
        if (B == null) {
            return A;
        }
        IntList catenated = new IntList(A.first, null);
        IntList pointer = catenated;
        A = A.rest;
        while (A != null) {
            pointer.rest = new IntList(A.first, null);
            A = A.rest;
            pointer = pointer.rest;
        }
        while (B != null) {
            pointer.rest = new IntList(B.first, null);
            B = B.rest;
            pointer = pointer.rest;
        }
        return catenated;
    }

}
