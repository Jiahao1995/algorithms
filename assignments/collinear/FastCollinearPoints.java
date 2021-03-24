package collinear;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FastCollinearPoints {
    private final Point[] points;
    private final List<LineSegment> segments;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        Point[] copy = points.clone();
        Arrays.sort(copy);
        int N = copy.length;
        for (int i = 0; i < N; i++) {
            if (copy[i] == null) {
                throw new IllegalArgumentException();
            }
            if (i > 0 && points[i].compareTo(points[i - 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }
        this.points = copy;
        segments = new ArrayList<>();
        collinear();
    }

    private void collinear() {
        int N = points.length;
        if (N < 4) {
            return;
        }
        Point[] temp;
        for (Point p : points) {
            temp = points.clone();
            Arrays.sort(temp, p.slopeOrder());
            List<Point> list = new ArrayList<>();
            double slope = p.slopeTo(temp[1]);
            for (int i = 1; i < N; i++) {
                if (p.slopeTo(temp[i]) == slope) {
                    list.add(temp[i]);
                    continue;
                }
                if (list.size() >= 3) {
                    Collections.sort(list);
                    if (p.compareTo(list.get(0)) < 0) {
                        segments.add(new LineSegment(p, list.get(list.size() - 1)));
                    }
                }
                list.clear();
                list.add(temp[i]);
                slope = p.slopeTo(temp[i]);
            }
            if (list.size() >= 3) {
                Collections.sort(list);
                if (p.compareTo(list.get(0)) < 0) {
                    segments.add(new LineSegment(p, list.get(list.size() - 1)));
                }
            }
        }
    }

    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[0]);
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(StdIn.readString());
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
