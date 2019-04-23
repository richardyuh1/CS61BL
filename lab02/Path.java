/** A class that represents a path via pursuit curves. */
public class Path {
	public Point curr;
	public Point next; 
    // TODO
    public Path(double x, double y) {
    	next = new Point(x,y);
    	curr = new Point(1.0,1.0); 
    }

    public void iterate(double dx, double dy) {

    	curr = new Point(next); 
    	next.x = curr.x + dx;
        next.y = curr.y + dy; 
    
    }

}
