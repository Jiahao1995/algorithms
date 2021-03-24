package collinear;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private final Point[] points;
    private final List<LineSegment> segments;

    public BruteCollinearPoints(Point[] points) {
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
        for (int i = 0; i < N - 3; i++) {
            for (int j = i + 1; j < N - 2; j++) {
                for (int m = j + 1; m < N - 1; m++) {
                    for (int n = m + 1; n < N; n++) {
                        Point p0 = points[i];
                        Point p1 = points[j];
                        Point p2 = points[m];
                        Point p3 = points[n];
                        if (p0.slopeTo(p1) == p0.slopeTo(p2) && p0.slopeTo(p2) == p0.slopeTo(p3)) {
                            segments.add(new LineSegment(p0, p3));
                        }
                    }
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
