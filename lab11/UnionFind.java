public class UnionFind {

    /* TODO: Add instance variables here. */

    int[] array;

    /* Creates a UnionFind data structure holding N vertices. Initially, all
       vertices are in disjoint sets. */
    public UnionFind(int N) {
        // TODO: YOUR CODE HERE
        array = new int[N];
        for (int i = 0; i < array.length; i++) {
            array[i] = -1;
        }
    }

    /* Returns the size of the set V belongs to. */
    public int sizeOf(int v) {
        // TODO: YOUR CODE HERE
        if (array[v] == -1) {
            return 1;
        }
        while (array[v] >= 0) {
            v = array[v];
        }
        return -array[v];
    }

    /* Returns the parent of V. If V is the root of a tree, returns the
       negative size of the tree for which V is the root. */
    public int parent(int v) {
        // TODO: YOUR CODE HERE
        if (array[v] == -1) {
            return -1;
        } else {
            return array[v];
        }
    }

    /* Returns true if nodes V1 and V2 are connected. */
    public boolean connected(int v1, int v2) {
        // TODO: YOUR CODE HERE
        int root1 = find(v1);
        int root2 = find(v2);
        if (root1 == -1 && root2 == -1) {
            return false;
        } else {
            return root1 == root2;
        }
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. If invalid vertices are passed into this
       function, throw an IllegalArgumentException. */
    public int find(int v) {
        // TODO: YOUR CODE HERE
        int root = v;
        if (v < 0 || v >= array.length) {
            throw new IllegalArgumentException();
        }
        if (parent(root) < 0) {
            return root;
        }
        while (parent(root) >= 0) {
            root = parent(root);
        }
        int compressor = parent(v);
        while (compressor != root) {
            int temp = compressor;
            compressor = parent(compressor);
            array[temp] = root;
        }
        array[v] = root;
        return root;
    }

    /* Connects two elements V1 and V2 together. V1 and V2 can be any element,
       and a union-by-size heuristic is used. If the sizes of the sets are
       equal, tie break by connecting V1's root to V2's root. Union-ing a vertex
       with itself or vertices that are already connected should not change the
       structure. */
    public void union(int v1, int v2) {
        // TODO: YOUR CODE HERE
        if (v1 == v2) {
            return;
        }
        if (parent(v1) == -1 && parent(v2) == -1) {
            array[v1] = v2;
            array[v2] -= 1;
            return;
        }
        if (find(v1) == find(v2)) {
            return;
        }
        if (sizeOf(v1) == sizeOf(v2)) {
            int root1 = find(v1);
            int root2 = find(v2);
            int size = parent(root1);
            array[root1] = root2;
            array[root2] -= size;
        }
        if (sizeOf(v1) > sizeOf(v2)) {
            int size = sizeOf(v2);
            int root1 = find(v1);
            int root2 = find(v2);
            array[root2] = root1;
            array[root1] -= size;
        }
        if (sizeOf(v1) < sizeOf(v2)) {
            int size = sizeOf(v1);
            int root1 = find(v1);
            int root2 = find(v2);
            array[root1] = root2;
            array[root2] -= size;
        }
    }
}
