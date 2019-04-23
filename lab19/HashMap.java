import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class HashMap<K, V> implements Map61BL<K, V> {
    /* Instance variables here? */
    LinkedList<Entry<K, V>>[] entryArray;
    int size; //number of total entries
    float loadFactor = 0.75f;

    public HashMap() {
        entryArray = new LinkedList[16];
        for (int i = 0; i < entryArray.length; i++) {
            entryArray[i] = new LinkedList<Entry<K, V>>();
        }
        size = 0;
    }

    public HashMap(int initialCapacity) {
        //entryArray = (LinkedList<Entry<K,V>>[]) new Object[initialCapacity];
        entryArray = new LinkedList[initialCapacity];
        for (int i = 0; i < entryArray.length; i++) {
            entryArray[i] = new LinkedList<Entry<K, V>>();
        }
        size = 0;
    }

    public HashMap(int initialCapacity, float loadFactor) {
        //entryArray = (LinkedList<Entry<K,V>>[]) new Object[initialCapacity];
        entryArray = new LinkedList[initialCapacity];
        for (int i = 0; i < entryArray.length; i++) {
            entryArray[i] = new LinkedList<Entry<K, V>>();
        }
        size = 0;
        this.loadFactor = loadFactor;
    }



    public int capacity() {
        return entryArray.length;
    }

    /* Returns true if the map contains the KEY. */
    public boolean containsKey(K key) {
        int index = Math.floorMod(key.hashCode(), capacity());
        LinkedList<Entry<K, V>> list = entryArray[index];
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).key.equals(key)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /* Returns the value for the specified KEY. If KEY is not found, return
       null. */
    public V get(K key) {
        int index = Math.floorMod(key.hashCode(), capacity());
        LinkedList<Entry<K, V>> list = entryArray[index];
        if (list != null) {
            int i = 0;
            while (i < list.size()) {
                if (list.get(i).key.equals(key)) {
                    return (V) list.get(i).value;
                }
                i++;
            }
            return null;
        }
        return null;
    }

    /* Puts a (KEY, VALUE) pair into this map. If the KEY already exists in the
       SimpleNameMap, replace the current corresponding value with VALUE. */
    public void put(K key, V value) {
        if ((float) (size() + 1) / (float) capacity() > loadFactor) {
            int original = entryArray.length;
            LinkedList<Entry<K, V>>[] copy = entryArray;
            entryArray = new LinkedList[original * 2];
            for (int j = 0; j < entryArray.length; j++) {
                entryArray[j] = new LinkedList<Entry<K, V>>();
            }
            for (int i = 0; i < copy.length; i++) {
                int linkedIndex = 0;
                while (linkedIndex < copy[i].size() && copy[i].get(linkedIndex) != null) {
                    Entry add = new Entry(copy[i].get(linkedIndex).key,
                                          copy[i].get(linkedIndex).value);
                    int index = Math.floorMod(add.key.hashCode(), entryArray.length);
                    //System.out.println("OG LinkedList size: " + copy[i].size());
                    entryArray[index].addLast(add);
                    linkedIndex += 1;
                }
            }
            int keyIndex = Math.floorMod(key.hashCode(), entryArray.length);
            entryArray[keyIndex].addLast(new Entry<K, V>(key, value));
            size += 1;
            return;

        }

        int index = Math.floorMod(key.hashCode(), capacity());
        LinkedList<Entry<K, V>> list = entryArray[index];
        if (list != null) {
            int i = 0;
            boolean match = false;
            while (i < list.size()) {
                if (list.get(i).key.equals(key)) {
                    match = true;
                    list.set(i, new Entry(key, value));
                }
                i++;
            }
            if (!match) {
                list.addLast(new Entry(key, value));
                size += 1;
                return;
            }
        } else {
            list = new LinkedList<>();
            list.add(new Entry(key, value));
            size += 1;
            return;
        }

    }

    /* Removes a single entry, KEY, from this table and return the VALUE if
       successful or NULL otherwise. */
    public V remove(K key) {
        int index = Math.floorMod(key.hashCode(), entryArray.length);
        LinkedList<Entry<K, V>> list = entryArray[index];
        if (list != null) {
            int i = 0;
            while (i < list.size()) {
                if (list.get(i).key.equals(key)) {
                    V returnValue = list.get(i).value;
                    list.remove(list.get(i));
                    size -= 1;
                    return returnValue;
                }
            }
            return null;
        }
        return null;

    }

    public int size() {
        return size;
    }

    public void clear() {
        int capacity = capacity();
        entryArray = new LinkedList[capacity];
        size = 0;
    }

    public boolean remove(K key, V value) {
        return false;
    }

    public Iterator<K> iterator() {
        return new HashMapIterator();
    }

    private class HashMapIterator implements Iterator<K> {
        int index;
        int linkedIndex;

        HashMapIterator() {
            index = 0;
            linkedIndex = 0;
        }

        public boolean hasNext() {
            System.out.println("Current array index: " + index);
            System.out.println("Array length " + entryArray.length);
            System.out.println("Has Next:");
            System.out.println(index < entryArray.length);
            boolean elementsLeft = false;
            int lI = 0;
            for (int i = index; i < entryArray.length; i++) {
                if (entryArray[i].get(lI) != null) {
                    elementsLeft = true;
                }
                lI++;
            }
            return (index < entryArray.length) && elementsLeft;
        }

        @Override
        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (linkedIndex < entryArray[index].size()) {
                K key = entryArray[index].get(linkedIndex).key;
                linkedIndex += 1;
                return key;
            } else {
                index += 1;
                linkedIndex = 0;
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return this.next();
            }
        }
    }


    private static class Entry<K, V> {

        private K key;
        private V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /* Returns true if this key matches with the OTHER's key. */
        public boolean keyEquals(Entry other) {
            return key.equals(other.key);
        }

        /* Returns true if both the KEY and the VALUE match. */
        @Override
        public boolean equals(Object other) {
            return (other instanceof Entry
                    && key.equals(((Entry) other).key)
                    && value.equals(((Entry) other).value));
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }
}



