import java.util.*;
import edu.princeton.cs.algs4.*;

public class KdTree {
    
    private final double xmin = 0.0;
    private final double xmax = 1.0;
    private final double ymin = 0.0;
    private final double ymax = 1.0;
    private int level;
    private int size;
    private ArrayList<Point2D> range;
    private Node root;
    
    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node leftBottom;
        private Node rightTop;
       
        private Node(Point2D point, RectHV rectangle) {
            p = point;
            rect = rectangle;
        }
    }
    
    private int comparePoints(double coordinate1, double coordinate2) {
        return Double.compare(coordinate1, coordinate2);
    }
    
    public KdTree() {
        
        size = 0;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
                              
    public int size() {
        return size;
    }
    
    public void insert(Point2D p) {
        if (p == null) throw new NullPointerException();
        
        //if (!contains(p)) {
            level = 0;
            root = insert(root, p, xmin, ymin, xmax, ymax, ++level);
            size++;
        //}
    }
    
    private Node insert(Node n, Point2D p, double xmin, double ymin, double xmax, double ymax, int level) {
        if (n == null) {
            Node newNode = new Node(p, new RectHV(xmin, ymin, xmax, ymax)); 
            return newNode;
        }
        
        int cmp;
        if (level % 2 == 1) {
            cmp = comparePoints(n.p.x(), p.x());
            if (cmp == 0) {
                cmp = -1;// go right subtree? //*/cmp = comparePoints(n.p.y(), p.y());
            }
        }
        else {
            cmp = comparePoints(n.p.y(), p.y());
            if (cmp == 0) {
                cmp = -1; //go right subtree? //*/cmp = comparePoints(n.p.x(), p.x());
            }
        }
        
        if (cmp > 0) {
            if (level % 2 == 1) {
                n.leftBottom = insert(n.leftBottom, p, xmin, ymin, n.p.x(), ymax, ++level);
            }
            else {
                n.leftBottom = insert(n.leftBottom, p, xmin, ymin, xmax, n.p.y(), ++level);
            }
        } 
        else if (cmp < 0) {
            if (level % 2 == 1) {
                n.rightTop = insert(n.leftBottom, p, n.p.x(), ymin, xmax, ymax, ++level);
            }
            else {
                n.rightTop = insert(n.leftBottom, p, xmin, n.p.y(), xmax, ymax, ++level);
            }
        }
        
        return n;
    }
    
    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException();
        level = 0;
        return contains(root, p, ++level) != null;
    }
    
    private Point2D contains(Node n, Point2D p, int level) {
        if (n == null) return null;
        
        int cmp;
        if (level % 2 == 1) {
            cmp = comparePoints(n.p.x(), p.x());
        }
        else {
            cmp = comparePoints(n.p.y(), p.y());
        }
 
        if (cmp > 0) {
            return contains(n.leftBottom, p, ++level);
        } 
        else if (cmp < 0) {
            return contains(n.rightTop, p, ++level);
        }
        else {
            return n.p;
        }
    }
    
    public void draw() {
        
    }
    
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException();
        
        range = new ArrayList<Point2D>();
        intersects(root, rect, range); //points; // to compile
        return range;
    }
    
    private void intersects(Node n, RectHV queryRect, ArrayList<Point2D> range) {
        if (n != null && queryRect.intersects(n.rect)) {
            if (queryRect.contains(n.p)) {
                range.add(n.p);
            }
        
            intersects(n.leftBottom, queryRect, range);
            intersects(n.rightTop, queryRect, range);
        }
    }
    
    public Point2D nearest(Point2D p) {
        Point2D closest = null;
        return nearest(root, p, closest); //to compile
    }
    
    private Point2D nearest(Node n, Point2D queryP, Point2D closest) {
        if (n != null) {
            if (closest == null) closest = n.p;
            
            double queryToClosest = queryP.distanceSquaredTo(closest);
            
            //if the closest point discovered so far is closer than the distance between the query point and the rectangle corresponding to a node, there is no need to explore that node (or its subtrees). 
            if (queryToClosest >= n.rect.distanceSquaredTo(queryP)) {
                if (queryP.distanceSquaredTo(n.p) < queryToClosest) {
                    closest = n.p;
                }
            }
            
            //choose the subtree that is on the same side of the splitting line as the query point as the first subtree to exploreÑthe closest point found while exploring the first subtree may enable pruning of the second subtree.
            if (n.leftBottom != null && n.leftBottom.rect.contains(queryP)) {
                closest = nearest(n.leftBottom, queryP, closest);
                closest = nearest(n.rightTop, queryP, closest);
            }
            else {
                if (n.rightTop != null) {
                    closest = nearest(n.rightTop, queryP, closest);
                    closest = nearest(n.leftBottom, queryP, closest);
                }
            }
                
        }
        /*else {
            if (n != null) closest = n.p;
           
            if (n.leftBottom != null && n.leftBottom.rect.contains(queryP)) {
                 closest = nearest(n.leftBottom, queryP, closest);
                 closest = nearest(n.rightTop, queryP, closest);
            }
            else {
                 closest = nearest(n.rightTop, queryP, closest);
                 closest = nearest(n.leftBottom, queryP, closest);
            }
        }*/
        
        return closest;
    }
    
    public static void main(String[] args) {
        
        //KdTree tree = new KdTree();
        
        //tree.insert(new Point2D(.3
                                    
        In in = new In(args[0]);
        KdTree tree = new KdTree();
        
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            System.out.println(p);
            //Point2D point = new Point2D(x,y);
            tree.insert(p);
           
        }
        
        Point2D testPoint = new Point2D(.81,.30);
        Point2D containsTest = new Point2D(0.206107, 0.095492);
        Point2D containsTest2 = new Point2D(0.206107, 0.904508);
        //StdDraw.clear();
        //StdDraw.setPenColor(StdDraw.BLACK);
       // StdDraw.setPenRadius(.01);
        //setOfPoints.draw();
        
        //Test insert/search
        System.out.println("Size: " + tree.size());
        System.out.println("Contains 0.206107, 0.095492: " + tree.contains(containsTest));
        System.out.println("Contains 0.206107, 0.904508: " + tree.contains(containsTest2));
        //System.out.println(setOfPoints.isEmpty());
        //System.out.println("nearest point: " + tree.nearest(testPoint));                              
    }
                                            
                                            
}