import java.util.*;
import edu.princeton.cs.algs4.*;

public class KdTree {
    
    private static final double xmin = 0.0;
    private static final double xmax = 1.0;
    private static final double ymin = 0.0;
    private static final double ymax = 1.0;
    private int level;
    private int size;
    //private ArrayList<Point2D> range;
    private Node root;
    private static double queryToClosest = 0;
    
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
        
        if (!contains(p)) {
            level = 0;
            root = insert(root, p, xmin, ymin, xmax, ymax, level + 1);
            size++;
        }
    }
    
    private Node insert(Node n, Point2D p, double xmin, double ymin, double xmax, double ymax, int level) {
        if (n == null) {
            Node newNode = new Node(p, new RectHV(xmin, ymin, xmax, ymax)); 
            return newNode;
        }
        
        int cmp;
        double nodePointX = n.p.x();
        double nodePointY = n.p.y();
        
        if (level % 2 == 1) {
            cmp = comparePoints(nodePointX, p.x());
            if (cmp == 0) {
                cmp = comparePoints(nodePointY, p.y());
            }
        }
        else {
            cmp = comparePoints(nodePointY, p.y());
            if (cmp == 0) {
                cmp = comparePoints(nodePointX, p.x());
            }
        }
        
        if (cmp > 0) {
            if (level % 2 == 1) {
                n.leftBottom = insert(n.leftBottom, p, xmin, ymin, nodePointX, ymax, level +1);
            }
            else {
                n.leftBottom = insert(n.leftBottom, p, xmin, ymin, xmax, nodePointY, level +1);
            }
        } 
        else if (cmp < 0) {
            if (level % 2 == 1) {
                n.rightTop = insert(n.rightTop, p, nodePointX, ymin, xmax, ymax, level + 1);
            }
            else {
                n.rightTop = insert(n.rightTop, p, xmin, nodePointY, xmax, ymax, level + 1);
            }
        }
      
        return n;
    }
    
    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException();
        level = 0;
        return contains(root, p, level +1) != null;
    }
    
    private Point2D contains(Node n, Point2D p, int level) {
        if (n == null) return null;
        
        int cmp;
        if (level % 2 == 1) {
            cmp = comparePoints(n.p.x(), p.x());
            if (cmp == 0) {
                cmp = comparePoints(n.p.y(), p.y());
            }
        }
        else {
            cmp = comparePoints(n.p.y(), p.y());
            if (cmp == 0) {
                cmp = comparePoints(n.p.x(), p.x());
            }
        }
 
        if (cmp > 0) {
            return contains(n.leftBottom, p, level + 1);
        } 
        else if (cmp < 0) {
            return contains(n.rightTop, p, level + 1);
        }
        else {
            return n.p;
        }
    }
    
    public void draw() {
        StdDraw.clear();
        level = 0;
        draw(root, level + 1);
    }
    
    private void draw(Node n, int level) {
        
        if (n != null) {
            draw(n.leftBottom, level + 1);
            
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            n.p.draw();
            
            if (level % 2 == 1) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.setPenRadius();
                StdDraw.line(n.p.x(), n.rect.ymin(), n.p.x(), n.rect.ymax());
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.setPenRadius();
                StdDraw.line(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.p.y());
            }
            
            draw(n.rightTop, level + 1);
        }
    }
    
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException();
        
        ArrayList<Point2D> range = new ArrayList<Point2D>();
        intersects(root, rect, range); 
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
        //queryToClosest = p.distanceSquaredTo(closest);
        return nearest(root, p, closest); 
    }
    
    private Point2D nearest(Node n, Point2D queryP, Point2D closest) {
        //queryToClosest = currentClosest;
        
        if (n != null) {
            if (closest == null) {
                closest = n.p;
                queryToClosest = queryP.distanceSquaredTo(closest);
            }
            
            //double queryToClosest = queryP.distanceSquaredTo(closest);
            double queryToRect = n.rect.distanceSquaredTo(queryP); 
             if (queryToClosest >= queryToRect) {//n.rect.distanceSquaredTo(queryP)) {
            if (queryP.distanceSquaredTo(n.p) < queryToClosest) {
                closest = n.p;
                queryToClosest = queryP.distanceSquaredTo(closest);
            }
            
            //double queryToRect = n.rect.distanceSquaredTo(queryP);
            
            if (n.leftBottom != null && n.leftBottom.rect.contains(queryP)) {
                
                closest = nearest(n.leftBottom, queryP, closest);
                //if (queryToClosest > queryToRect) {
                    closest = nearest(n.rightTop, queryP, closest);
                //}
            }
            else {
                //if (n.rightTop != null) {
                
                closest = nearest(n.rightTop, queryP, closest);
               // if (queryToClosest > queryToRect) {
                    closest = nearest(n.leftBottom, queryP, closest);
                //}
                //}
            }
           }
            
        }
        
        return closest;
    }
    
    //Unit tests
    public static void main(String[] args) {
      
        In in = new In(args[0]);
        KdTree tree = new KdTree();
        
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            System.out.println(p);
            tree.insert(p);
        }
        
        Point2D testPoint = new Point2D(.81,.30);
       
        Point2D containsTest1 = new Point2D(0.206107, 0.095492);
        Point2D containsTest2 = new Point2D(0.975528, 0.654508);
        Point2D containsTest3 = new Point2D(0.024472, 0.345492);
        Point2D containsTest4 = new Point2D(0.793893, 0.095492);
        Point2D containsTest5 = new Point2D(0.793893, 0.904508);
        Point2D containsTest6 = new Point2D(0.975528, 0.345492);
        Point2D containsTest7 = new Point2D(0.206107, 0.904508);
        Point2D containsTest8 = new Point2D(0.500000, 0.000000);
        Point2D containsTest9 = new Point2D(0.024472, 0.654508);
        Point2D containsTest10 = new Point2D(0.500000, 1.000000);
     
        
        //Test insert/search
        System.out.println("Size: " + tree.size());
        System.out.println("Contains test1: " + tree.contains(containsTest1));
        System.out.println("Contains test2: " + tree.contains(containsTest2));
        System.out.println("Contains test3: " + tree.contains(containsTest3));
        System.out.println("Contains test4: " + tree.contains(containsTest4));
        System.out.println("Contains test5: " + tree.contains(containsTest5));
        System.out.println("Contains test6: " + tree.contains(containsTest6));
        System.out.println("Contains test7: " + tree.contains(containsTest7));
        System.out.println("Contains test8: " + tree.contains(containsTest8));
        System.out.println("Contains test9: " + tree.contains(containsTest9));
        System.out.println("Contains test10: " + tree.contains(containsTest10));
     
        System.out.println("nearest point: " + tree.nearest(testPoint)); 
        tree.draw();
    }
                                            
                                            
}