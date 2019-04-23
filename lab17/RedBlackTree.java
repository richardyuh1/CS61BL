public class RedBlackTree<T extends Comparable<T>> {

    /* Root of the tree. */
    RBTreeNode<T> root;

    /* Creates an empty RedBlackTree. */
    public RedBlackTree() {
        root = null;
    }

    /* Creates a RedBlackTree from a given BTree (2-3-4) TREE. */
    public RedBlackTree(BTree<T> tree) {
        Node<T> btreeRoot = tree.root;
        root = buildRedBlackTree(btreeRoot);
    }

    /* Builds a RedBlackTree that has isometry with given 2-3-4 tree rooted at
       given node R, and returns the root node. */
    RBTreeNode<T> buildRedBlackTree(Node<T> r) {
        if (r == null) {
            return null;
        }

        if (r.getChildrenCount() == 0) {
            return new RBTreeNode<T>(true, r.getItemAt(0));
        }

        if (r.getChildrenCount() == 2) {
            RBTreeNode<T> tree = new RBTreeNode<T>(true, r.getItemAt(0),
                    buildRedBlackTree(r.getChildAt(0)), buildRedBlackTree(r.getChildAt(1)));
            return tree;
        }

        if (r.getChildrenCount() == 3) {
            RBTreeNode<T> tree = new RBTreeNode<T>(true, r.getItemAt(0),
                    buildRedBlackTree(r.getChildAt(0)), new RBTreeNode<T>(false, r.getItemAt(1)));
            return tree;
        }

        if (r.getChildrenCount() == 4) {
            RBTreeNode<T> tree = new RBTreeNode<T>(true, r.getItemAt(1),
                    buildRedBlackTree(r.getChildAt(0)), new RBTreeNode<T>(false, r.getItemAt(2)));
            return tree;
        }
        return null;
    }

    /* Flips the color of NODE and its children. Assume that NODE has both left
       and right children. */
    void flipColors(RBTreeNode<T> node) {
        node.isBlack = !node.isBlack;
        node.left.isBlack = !node.left.isBlack;
        node.right.isBlack = !node.right.isBlack;
    }

    /* Rotates the given node NODE to the right. Returns the new root node of
       this subtree. */
    RBTreeNode<T> rotateRight(RBTreeNode<T> node) {

        RBTreeNode<T> l = node.left;
        flipColors(node);
        flipColors(l);
        node.left = l.right;
        l.right = node;
        return l;
    }

    /* Rotates the given node NODE to the left. Returns the new root node of
       this subtree. */
    RBTreeNode<T> rotateLeft(RBTreeNode<T> node) {

        RBTreeNode<T> r = node.right;
        flipColors(node);
        flipColors(r);
        node.right = r.left;
        r.left = node;
        return r;
    }

    /* Insert ITEM into the red black tree, rotating
       it accordingly afterwards. */
    void insert(T item) {
        return;
    }

    /* Returns whether the given node NODE is red. Null nodes (children of leaf
       nodes are automatically considered black. */
    private boolean isRed(RBTreeNode<T> node) {
        return node != null && !node.isBlack;
    }

    static class RBTreeNode<T> {

        final T item;
        boolean isBlack;
        RBTreeNode<T> left;
        RBTreeNode<T> right;

        /* Creates a RBTreeNode with item ITEM and color depending on ISBLACK
           value. */
        RBTreeNode(boolean isBlack, T item) {
            this(isBlack, item, null, null);
        }

        /* Creates a RBTreeNode with item ITEM, color depending on ISBLACK
           value, left child LEFT, and right child RIGHT. */
        RBTreeNode(boolean isBlack, T item, RBTreeNode<T> left,
                   RBTreeNode<T> right) {
            this.isBlack = isBlack;
            this.item = item;
            this.left = left;
            this.right = right;
        }
    }

}
