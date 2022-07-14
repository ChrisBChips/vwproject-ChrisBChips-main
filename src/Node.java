import java.util.List;

public class Node implements Comparable<Node>{

    private int g = 99999999;
    private int h;
    private int f;
    private Node priorNode;
    private Point pos;

    public Node(Point pos, Node priorNode)
    {
        this.pos = pos;
        this.priorNode = priorNode;
    }

    public int getG(){ return g; }
    public void setG(int g){ this.g = g; }
    public int getH(){ return h; }
    public void setH(int h){this.h = h; }
    public int getF(){ return f; }
    public void calcF(){ this.f = this.g+this.h; }
    public Point getPos(){ return pos; }
    public Node getPriorNode(){ return priorNode; }
    public void setPriorNode(Node prior){ this.priorNode = prior; }
    
    public List<Node> getNeighbors()
    {
        Node down = new Node(new Point(pos.x, pos.y+1), this);
        Node up = new Node(new Point(pos.x, pos.y-1), this);
        Node left = new Node(new Point(pos.x-1, pos.y), this);
        Node right = new Node(new Point(pos.x+1, pos.y), this);
        return List.of(down, up, left, right);
    }

    @Override
    public int compareTo(Node o) {
        if (this.f > o.f){
            return 1;
        }
        else if (this.f < o.f){
            return -1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o){
        Node other = (Node) o;
        return this.pos.equals(other.pos);
    }

    @Override
    public int hashCode(){
        return this.pos.hashCode();
    }
}
