import java.util.*;

public class Graph implements Iterable<Integer> {

    private LinkedList<Edge>[] adjLists;
    private int vertexCount;
    private HashMap<Integer, Integer> weightMap;

    /* Initializes a graph with NUMVERTICES vertices and no Edges. */
    public Graph(int numVertices) {
        adjLists = (LinkedList<Edge>[]) new LinkedList[numVertices];
        for (int k = 0; k < numVertices; k++) {
            adjLists[k] = new LinkedList<Edge>();
        }
        vertexCount = numVertices;
    }

    /* Adds a directed Edge (V1, V2) to the graph. */
    public void addEdge(int v1, int v2) {
        addEdge(v1, v2, 0);
    }

    /* Adds an undirected Edge (V1, V2) to the graph. */
    public void addUndirectedEdge(int v1, int v2) {
        addUndirectedEdge(v1, v2, 0);
    }

    /* Adds a directed Edge (V1, V2) to the graph with weight WEIGHT. If the
       Edge already exists, replaces the current Edge with a new Edge with
       weight WEIGHT. */
    public void addEdge(int v1, int v2, int weight) {
        Edge e = new Edge(v1, v2, weight);
        LinkedList<Edge> list = adjLists[v1];

        if (list.size() == 0) {
            list.add(e);
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).to == v2) {
                list.set(i, e);
            }
        }
        list.addLast(e);
    }

    /* Adds an undirected Edge (V1, V2) to the graph with weight WEIGHT. If the
       Edge already exists, replaces the current Edge with a new Edge with
       weight WEIGHT. */
    public void addUndirectedEdge(int v1, int v2, int weight) {
        addEdge(v1, v2, weight);
        addEdge(v2, v1, weight);
    }

    /* Returns true if there exists an Edge from vertex FROM to vertex TO.
       Returns false otherwise. */
    public boolean isAdjacent(int from, int to) {
        LinkedList<Edge> list = adjLists[from];
        for (Edge e: list) {
            if (e.to == to) {
                return true;
            }
        }
        return false;
    }

    /* Returns a list of all the vertices u such that the Edge (V, u)
       exists in the graph. */
    public List<Integer> neighbors(int v) {
        List<Integer> list = new ArrayList<>();
        LinkedList<Edge> linkedList = adjLists[v];
        for (int i = 0; i < linkedList.size(); i++) {
            list.add(linkedList.get(i).to);
        }
        return list;

    }
    /* Returns the number of incoming Edges for vertex V. */
    public int inDegree(int v) {
        int degree = 0;
        for (LinkedList<Edge> edgeList: adjLists) {
            for (Edge e: edgeList) {
                if (e.to == v) {
                    degree++;
                }
            }
        }
        return degree;
    }

    /* Returns an Iterator that outputs the vertices of the graph in topological
       sorted order. */
    public Iterator<Integer> iterator() {
        return new TopologicalIterator();
    }

    /* A class that iterates through the vertices of this graph, starting with
       vertex START. If the iteration from START has no path from START to some
       vertex v, then the iteration will not include v. */
    private class DFSIterator implements Iterator<Integer> {

        private PriorityQueue<Node> fringe;
        private HashSet<Integer> visited;

        DFSIterator(int start) {
            fringe = new PriorityQueue<Node>();
            visited = new HashSet<Integer>();
            Node s = new Node(start, start, 0);
            fringe.add(s);
        }

        public boolean hasNext() {
            return !fringe.isEmpty();
        }

        public Integer next() {
            Node n = fringe.poll();
            Integer v = n.value;
            for (Integer neighbor : neighbors(v)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    Node next = new Node(neighbor, v, n.dist + getEdge(v, neighbor).weight);
                    fringe.add(next);
                }
            }
            visited.add(v);
            return v;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    /* Returns the collected result of performing a depth-first search on this
       graph's vertices starting from V. */
    public List<Integer> dfs(int v) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new DFSIterator(v);

        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    /* Returns true iff there exists a path from START to STOP. Assumes both
       START and STOP are in this graph. If START == STOP, returns true. */
    public boolean pathExists(int start, int stop) {
        if (start == stop) {
            return true;
        } else {
            return dfs(start).contains(stop);
        }
    }


    /* Returns the path from START to STOP. If no path exists, returns an empty
       List. If START == STOP, returns a List with START. */
    public List<Integer> path(int start, int stop) {
        List<Integer> list = new ArrayList<>();
        int end = stop;
        if (start == stop) {
            list.add(start);
            return list;
        }
        if (pathExists(start, stop)) {
            List<Integer> depthList = dfs(start);
            for (int i = depthList.size() - 1; i >= 0; i--) {
                if (isAdjacent(depthList.get(i), stop)) {
                    list.add(0, depthList.get(i));
                    stop = depthList.get(i);
                }
            }
            list.add(end);
            return list;
        }
        return list;
    }

    public List<Integer> shortestPath(int start, int stop) {
        //Initializsation
        List<Integer> shortestPath = new ArrayList<>();
        if (!pathExists(start, stop)) {
            return shortestPath;
        }
        HashSet<Integer> visited = new HashSet<>();
        Stack<Integer> fringe = new Stack();
        fringe.push(start);
        HashMap<Integer, Integer> weightmap = new HashMap<Integer, Integer>();
        weightMap.put(start, 0);
        int accumDistance = 0;

        //Loop
        while (!fringe.isEmpty()) {
            Integer v = fringe.pop();
            if (!visited.contains(v)) {
                for (Integer neighbor: neighbors(v)) {
                    if (visited.contains(neighbor)) {
                        continue;
                    }
                    if (!fringe.contains(neighbor)) {
                        fringe.push(neighbor);
                    }
                    Edge e = getEdge(v, neighbor);
                    fringe.push(e.to);
                }
                visited.add(v);
            }
            shortestPath.add(v);
        }

        return shortestPath;
    }

    public Edge getEdge(int u, int v) {
        if (u >= adjLists.length) {
            return null;
        }
        LinkedList<Edge> list = adjLists[u];
        for (Edge e: list) {
            if (e.to == v) {
                return e;
            }
        }
        return null;
    }

    public List<Integer> topologicalSort() {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new TopologicalIterator();
        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    private class TopologicalIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;

        TopologicalIterator() {
            fringe = new Stack<Integer>();
        }

        public boolean hasNext() {
            return false;
        }

        public Integer next() {
            return 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    private class Node {
        private int value;
        private int pre;
        private int dist;

        Node(int v, int p, int d) {
            value = v;
            pre = p;
            dist = d;
        }
    }

    private class Edge {

        private int from;
        private int to;
        private int weight;

        Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public String toString() {
            return "(" + from + ", " + to + ", weight = " + weight + ")";
        }

    }

    private void generateG1() {
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(0, 4);
        addEdge(1, 2);
        addEdge(2, 0);
        addEdge(2, 3);
        addEdge(4, 3);
    }

    private void generateG2() {
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(0, 4);
        addEdge(1, 2);
        addEdge(2, 3);
        addEdge(4, 3);
    }

    private void generateG3() {
        addUndirectedEdge(0, 2);
        addUndirectedEdge(0, 3);
        addUndirectedEdge(1, 4);
        addUndirectedEdge(1, 5);
        addUndirectedEdge(2, 3);
        addUndirectedEdge(2, 6);
        addUndirectedEdge(4, 5);
    }

    private void generateG4() {
        addEdge(0, 1);
        addEdge(1, 2);
        addEdge(2, 0);
        addEdge(2, 3);
        addEdge(4, 2);
    }

    private void printDFS(int start) {
        System.out.println("DFS traversal starting at " + start);
        List<Integer> result = dfs(start);
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
    }

    private void printPath(int start, int end) {
        System.out.println("Path from " + start + " to " + end);
        List<Integer> result = path(start, end);
        if (result.size() == 0) {
            System.out.println("No path from " + start + " to " + end);
            return;
        }
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
    }

    private void printTopologicalSort() {
        System.out.println("Topological sort");
        List<Integer> result = topologicalSort();
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
    }

    public static void main(String[] args) {

        Graph g3 = new Graph(7);
        g3.generateG3();
        g3.printDFS(0);
        g3.printPath(0, 6);




    }
}
