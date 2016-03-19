import java.util.*;
import edu.princeton.cs.algs4.*;

public class KdTree {
    
    private final double xmin = 0.0;
    private final double xmax = 1.0;
    private final double ymin = 0.0;
    private final double ymax = 0.0;
    private int level;
    private int size;
    private ArrayList<Point2D> points;
    private Node root;
    
    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node leftBottom;
        private Node rightTop;
       
        public Node(Point2D point, RectHV rectangle) {
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
        level = 0;
        root = insert(root, p, level);
        size++;
    }
    
    private Node insert(Node n, Point2D p, int level) {
        if (n == null) {
            Node newNode = new Node(p, new RectHV(xmin, ymin, xmax, ymax)); 
            return newNode;
        }
        
        int cmp;
        if (level % 2 == 1) {
            cmp = comparePoints(n.p.x(), p.x());
        }
        else {
            cmp = comparePoints(n.p.y(), p.y());
        }
 
        if (cmp > 0) {
            n.leftBottom = insert(n.leftBottom, p, ++level);
        } 
        else if (cmp < 0) {
            n.rightTop = insert(n.rightTop, p, ++level);
        }
        else {
            n.p = p;
        }
        
        return n;
    }
    
    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException();
        level = 0;
        return contains(p);
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
        points = new ArrayList<Point2D>();
        return points; // to compile
    }
    
    public Point2D nearest(Point2D p) {
        return p; //to compile
    }
    
    public static void main(String[] args) {
        
    }
                                            
                                            
}