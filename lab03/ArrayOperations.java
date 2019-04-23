public class ArrayOperations {
    /**
     * Delete the value at the given position in the argument array, shifting
     * all the subsequent elements down, and storing a 0 as the last element of
     * the array.
     */
    public static void delete(int[] values, int pos) {
        if (pos < 0 || pos >= values.length) {
            return;
        }
        // TODO: YOUR CODE HERE
        int[] newvalues = new int[values.length];
        for (int i = 0; i < pos; i++){
            newvalues[i] = values[i];
        }
        for (int j = pos; j < values.length-1; j++){
            newvalues[j] = values[j+1];
        }
        newvalues[values.length-1] = 0; 
        for (int k = 0; k < newvalues.length; k++){
            values[k] = newvalues[k];
        }
    }

    /**
     * Insert newInt at the given position in the argument array, shifting all
     * the subsequent elements up to make room for it. The last element in the
     * argument array is lost.
     */
    public static void insert(int[] values, int pos, int newInt) {
        if (pos < 0 || pos >= values.length) {
            return;
        }
        // TODO: YOUR CODE HERE
        int[] newvalues = new int[values.length];
        for (int i = 0; i < pos; i++){
            newvalues[i] = values[i];
        }
        newvalues[pos] = newInt; 
        for (int j = pos+1; j < newvalues.length; j++){
            newvalues[j] = values[j-1];
        }
        for (int k = 0; k < newvalues.length; k++){
            values[k] = newvalues[k];
        }
    }
}
