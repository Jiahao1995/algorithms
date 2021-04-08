package kdtree;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

import static edu.princeton.cs.algs4.StdDraw.BLACK;
import static edu.princeton.cs.algs4.StdDraw.BLUE;
import static edu.princeton.cs.algs4.StdDraw.RED;

public class KdTree {

    private class Node {
        Point2D p;
        boolean isX;
        RectHV rect;
        Node left;
        Node right;

        public Node(Point2D p, boolean isX, RectHV rect) {
            this.p = p;
            this.isX = isX;
            this.rect = rect;
        }
    }

    private Node root;
    private int N;

    public KdTree() {
        root = null;
        N = 0;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        root = insert(root, null, p);
    }

    private Node insert(Node x, Node parent, Point2D p) {
        if (isEmpty()) {
            N++;
            return new Node(p, true, new RectHV(0, 0, 1, 1));
        }
        if (x == null) {
            N++;
            return newNode(parent, p);
        }
        int cmp = compare(p, x);
        if (cmp < 0) {
            x.left = insert(x.left, x, p);
        }
        else {
            x.right = insert(x.right, x, p);
        }
        return x;
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return contains(root, p);
    }

    private boolean contains(Node x, Point2D p) {
        if (x == null) {
            return false;
        }
        int cmp = compare(p, x);
        if (cmp < 0) {
            return contains(x.left, p);
        }
        else if (cmp > 0) {
            return contains(x.right, p);
        }
        else {
            return true;
        }
    }

    public void draw() {
        StdDraw.clear();
        draw(root);
    }

    private void draw(Node node) {
        if (node == null) {
            return;
        }
        double xMin = node.rect.xmin();
        double yMin = node.rect.ymin();
        double xMax = node.rect.xmax();
        double yMax = node.rect.ymax();
        double x = node.p.x();
        double y = node.p.y();

        StdDraw.setPenColor(BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(x, y);

        StdDraw.setPenRadius(0.004);
        if (node.isX) {
            StdDraw.setPenColor(RED);
            StdDraw.line(x, yMin, x, yMax);
        }
        else {
            StdDraw.setPenColor(BLUE);
            StdDraw.line(xMin, y, xMax, y);
        }
        draw(node.left);
        draw(node.right);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        List<Point2D> list = new ArrayList<>();
        if (isEmpty()) {
            return list;
        }
        range(root, list, rect);
        return list;
    }

    private void range(Node node, List<Point2D> list, RectHV rect) {
        if (node == null) {
            return;
        }
        if (rect.contains(node.p)) {
            list.add(node.p);
        }
        if (rect.intersects(node.rect)) {
            range(node.left, list, rect);
            range(node.right, list, rect);
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            return null;
        }
        return nearest(root, root.p, p);
    }

    private Point2D nearest(Node node, Point2D nearest, Point2D p) {
        if (node == null) {
            return nearest;
        }
        if (nearest.distanceSquaredTo(p) < node.rect.distanceSquaredTo(p)) {
            return nearest;
        }
        if (node.p.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
            nearest = node.p;
        }
        if (node.right != null && node.right.rect.contains(p)) {
            nearest = nearest(node.right, nearest, p);
            nearest = nearest(node.left, nearest, p);
        }
        else {
            nearest = nearest(node.left, nearest, p);
            nearest = nearest(node.right, nearest, p);
        }
        return nearest;
    }

    private static int compare(Point2D p, Node node) {
        if (node.isX) {
            if (Double.compare(p.x(), node.p.x()) == 0) {
                return Double.compare(p.y(), node.p.y());
            }
            return Double.compare(p.x(), node.p.x());
        }
        else {
            if (Double.compare(p.y(), node.p.y()) == 0) {
                return Double.compare(p.x(), node.p.x());
            }
            return Double.compare(p.y(), node.p.y());
        }
    }

    private Node newNode(Node parent, Point2D p) {
        double xMin = parent.rect.xmin();
        double yMin = parent.rect.ymin();
        double xMax = parent.rect.xmax();
        double yMax = parent.rect.ymax();
        double x = parent.p.x();
        double y = parent.p.y();
        int cmp = compare(p, parent);
        if (parent.isX) {
            if (cmp < 0) {
                return new Node(p, false, new RectHV(xMin, yMin, x, yMax));
            }
            else {
                return new Node(p, false, new RectHV(x, yMin, xMax, yMax));
            }
        }
        else {
            if (cmp < 0) {
                return new Node(p, true, new RectHV(xMin, yMin, xMax, y));
            }
            else {
                return new Node(p, true, new RectHV(xMin, y, xMax, yMax));
            }
        }
    }

}
