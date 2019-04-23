import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class CodingChallenges {

    /**
     * Return the missing number from an array of length N - 1 containing all
     * the values from 0 to N except for one missing number.
     */
    public static int missingNumber(int[] values) {
        // TODO
        List list = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            list.add(values[i]);
        }
        for (int i = 0; i < values.length + 1; i++) {
            if (!list.contains(i)) {
                return i;
            }
        }
        return 0;
    }

    /** Returns true if and only if two integers in the array sum up to n. */
    public static boolean sumTo(int[] values, int n) {
        // TODO
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values.length; j++) {
                if (values[i] + values[j] == n) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if and only if s1 is a permutation of s2. s1 is a
     * permutation of s2 if it has the same number of each character as s2.
     */
    public static boolean isPermutation(String s1, String s2) {
        // TODO
        Map<String, Integer> map1 = new HashMap<>();
        Map<String, Integer> map2 = new HashMap<>();
        int count = 1;
        int count2 = 1;
        for (int i = 0; i < s1.length(); i++) {
            count = 1;
            if (map1.containsKey(s1.substring(i, i + 1))) {
                count = map1.get(s1.substring(i, i + 1)) + 1;
            }
            map1.put(s1.substring(i, i + 1), count);
        }
        for (int j = 0; j < s2.length(); j++) {
            count2 = 1;
            if (map2.containsKey(s2.substring(j, j + 1))) {
                count2 = map2.get(s2.substring(j, j + 1)) + 1;
            }
            map2.put(s2.substring(j, j + 1), count2);
        }
        if (map1.equals(map2)) {
            return true;
        }
        return false;
    }
}
