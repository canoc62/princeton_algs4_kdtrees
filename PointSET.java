import java.util.TreeSet;
import java.util.Iterator;
import java.util.Comparator;
import java.util.ArrayList;
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
        pointTreeSet.add(p);
    }
    
    public boolean contains(Point2D p) {
        return pointTreeSet.contains(p);
    }
    
    public void draw() {
        //Iterator<Point2D> pointIterator = pointTreeSet.iterator();
        while (pointIterator().hasNext()) {
            pointIterator().next().draw();
        }
    }
    
    public Iterable<Point2D> range(RectHV rect) {
        //Iterator<Point2D> pointIterator = pointTreeSet.iterator();
        rangeSet = new TreeSet<Point2D>();
        while (pointIterator().hasNext()) {
            Point2D point = pointIterator().next();
            if (rect.contains(point)) {
                rangeSet.add(point);
            }
        }
        return rangeSet;
    }
    
    public Point2D nearest(Point2D p) {
        if (this.isEmpty()) return null;
        
        TreeSet<Point2D> byDistanceSet = new TreeSet<Point2D>(p.distanceToOrder());
        while (pointIterator().hasNext()) {
            byDistanceSet.add(pointIterator().next());
        }
                                                                        
        //ArrayList<Point2D> byDistanceList = new ArrayList<Point2D>(treeSet
        //Point2D[] byDistanceArr = pointTreeSet.toArray(new Point2D[this.size()]);
        //return byDistanceArr[1];
        return byDistanceSet.last();
    }
    
    private Iterator<Point2D> pointIterator() {
        return pointTreeSet.iterator();
    }
    
    //Unit testing
    public static void main(String[] args) {
        
    }
    
}