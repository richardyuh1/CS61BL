import java.util.LinkedList;

public class SimpleNameMap {

    /* Instance variables here? */
    LinkedList<Entry>[] entryArray;
    int size; //number of total entries

    public SimpleNameMap() {
        entryArray = new LinkedList[10];
        size = 0;
    }

    /* public int hash(String key) {
        if (isValidName(key)) {
            return (int) key.charAt(0) - 'A';
        } else {
            return -1;
        }
    } */

    /* Returns true if the given KEY is a valid name that starts with A - Z. */
    private static boolean isValidName(String key) {
        return 'A' <= key.charAt(0) && key.charAt(0) <= 'Z';
    }

    /* Returns true if the map contains the KEY. */
    boolean containsKey(String key) {
        if (isValidName(key)) {
            int index = Math.floorMod(key.hashCode(), entryArray.length);
            return entryArray[index] != null;
        } else {
            return false;
        }
    }

    /* Returns the value for the specified KEY. If KEY is not found, return
       null. */
    String get(String key) {
        if (isValidName(key)) {
            int index = Math.floorMod(key.hashCode(), entryArray.length);
            LinkedList<Entry> list = entryArray[index];
            if (list != null) {
                int i = 0;
                while (i < list.size()) {
                    if (list.get(i).key.equals(key)) {
                        return list.get(i).value;
                    }
                    i++;
                }
                return null;
            }
        } else {
            return null;
        }
        return null;
    }

    /* Puts a (KEY, VALUE) pair into this map. If the KEY already exists in the
       SimpleNameMap, replace the current corresponding value with VALUE. */
    void put(String key, String value) {
        if (isValidName(key)) {
            if (size() / entryArray.length >= 0.75) {
                int original = entryArray.length;
                LinkedList<Entry>[] copy = entryArray;
                entryArray = new LinkedList[original];
                for (int i = 0; i < copy.length; i++) {
                    int linkedIndex = 0;
                    while (copy[i].get(linkedIndex) != null) {
                        Entry add = new Entry(copy[i].get(linkedIndex).key,
                                              copy[i].get(linkedIndex).value);
                        entryArray[i].add(linkedIndex, add);
                    }
                }
            }
            int index = Math.floorMod(key.hashCode(), entryArray.length);
            LinkedList<Entry> list = entryArray[index];
            if (list != null) {
                int i = 0;
                boolean match = false;
                while (i < list.size()) {
                    if (list.get(i).key.equals(key)) {
                        match = true;
                        list.add(i, new Entry(key, value));
                    }
                    i++;
                }
                if (!match) {
                    list.addLast(new Entry(key, value));
                    size += 1;
                }
            } else {
                list = new LinkedList<Entry>();
                list.add(new Entry(key, value));
                size += 1;
            }
        } else {
            return;
        }
    }

    /* Removes a single entry, KEY, from this table and return the VALUE if
       successful or NULL otherwise. */
    String remove(String key) {
        if (isValidName(key)) {
            int index = Math.floorMod(key.hashCode(), entryArray.length);
            LinkedList<Entry> list = entryArray[index];
            if (list != null) {
                int i = 0;
                while (i < list.size()) {
                    if (list.get(i).key.equals(key)) {
                        String returnValue = list.get(i).value;
                        list.remove(list.get(i));
                        return returnValue;
                    }
                }
                return null;
            }
            return null;

        } else {
            return null;
        }
    }

    public int size() {
        return size;
    }

    private static class Entry {

        private String key;
        private String value;

        Entry(String key, String value) {
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
