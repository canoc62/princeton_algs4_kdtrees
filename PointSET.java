import java.util.TreeSet;
import java.util.Iterator;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Scanner;
import edu.princeton.cs.algs4.*;

public class PointSET {
    
    private TreeSet<Point2D> pointTreeSet; 
    private TreeSet<Point2D> rangeSet;
    
    public PointSET() {
        pointTreeSet = new TreeSet<Point2D>();
    }
    
    public boolean isEmpty() {
        return pointTreeSet.isEmpty();
    }
    
    public int size() {
        return pointTreeSet.size();
    }
    
    public void insert(Point2D p) {
        checkForNull(p);
        pointTreeSet.add(p);
    }
    
    public boolean contains(Point2D p) {
        checkForNull(p);
        return pointTreeSet.contains(p);
    }
    
    public void draw() {
        Iterator<Point2D> pointIterator = pointTreeSet.iterator();
        while (pointIterator.hasNext()) {
            pointIterator.next().draw();
        }
    }
    
    public Iterable<Point2D> range(RectHV rect) {
        checkForNull(rect);
        
        Iterator<Point2D> pointIterator = pointTreeSet.iterator();
        rangeSet = new TreeSet<Point2D>();
        while (pointIterator.hasNext()) {
            Point2D point = pointIterator.next();
            if (rect.contains(point)) {
                rangeSet.add(point);
            }
        }
        return rangeSet;
    }
    
    public Point2D nearest(Point2D p) {
        checkForNull(p);
        
        if (this.isEmpty()) return null;
        
        Point2D closest = null;
        Point2D next;// = null;
        Iterator<Point2D> it = pointTreeSet.iterator();
        while (it.hasNext()) {
            next = it.next();
            //System.out.println(next);
            if (closest == null || next.distanceSquaredTo(p) < closest.distanceSquaredTo(p)) {
                closest = next;
                //System.out.println("hello");
            }
            //System.out.println("hello2");
                                           
        }
        return closest;
    }
    
    private void checkForNull(Object item) {
        if (item == null) { 
            throw new NullPointerException();
        }
    }
    
    //Unit testing
    public static void main(String[] args) {
        
        In in = new In(args[0]);
        PointSET setOfPoints = new PointSET();
        
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            
            Point2D point = new Point2D(x,y);
            setOfPoints.insert(point);
           
        }
        
        
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        setOfPoints.draw();
        
        //System.out.println(setOfPoints.isEmpty());
        //System.out.println(setOfPoints.nearest(testPoint));                      
    }
    
}