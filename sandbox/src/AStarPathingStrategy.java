import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.HashMap;

class AStarPathingStrategy
        implements PathingStrategy
{


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        List<Point> path = new LinkedList<Point>();

        Function<Point, Integer> calcH = (p) -> Math.abs(p.x - end.x) + Math.abs(p.y - end.y);
        PriorityQueue<Node> openList = new PriorityQueue<Node>();
        HashMap<Point, Node> openHash = new HashMap<Point, Node>();
        HashMap<Point, Node> closedList = new HashMap<Point, Node>();
        Node current = new Node(start, null);
        current.setH(calcH.apply(current.getPos()));
        current.setG(0);
        current.calcF();
        openList.add(current);
        openHash.put(current.getPos(), current);

        while (!withinReach.test(current.getPos(), end) && !openList.isEmpty())
        {

            // Get neighbors and filter them into possible nodes that aren't on the closed list
            Predicate<Point> notInClosed = (p) -> {return !closedList.containsKey(p);};
            List<Point> listP = potentialNeighbors.apply(current.getPos()).filter(canPassThrough).filter(notInClosed).collect(Collectors.toList());
            ArrayList<Node> listN = new ArrayList<Node>();
            for (Point p : listP){ listN.add(new Node(p, current)); }

            // For each valid neighbor node
            for (Node neighbor : listN)
            {
                int newG = current.getG()+1;
                if (!closedList.containsKey(neighbor.getPos())) // If not in closedList
                {
                    if(!openHash.containsKey(neighbor.getPos())) // And not in openList
                    {
                        openHash.put(neighbor.getPos(), neighbor);
                        openList.add(neighbor);                     // Add to openList
                    }
        
                    if (newG < neighbor.getG()) // if not in closedList and new g value is better than old one
                    {
                        neighbor.setG(newG);
                        neighbor.setH(calcH.apply(neighbor.getPos()));
                        neighbor.calcF();                                   // Calculate all new values and refresh spot in openList
                        openList.remove(neighbor);
                        openList.add(neighbor);
                        current = neighbor.getPriorNode();
                    }
                }
            }


            openList.remove(current);
            openHash.remove(current.getPos());                       // Move current to closed List, set new current
            closedList.put(current.getPos(), current);
            if (openList.size() != 0){current = openList.element();}

        }

        if (!withinReach.test(current.getPos(), end)){ return path; } // If no path was found, return nothing

        while (current.getPriorNode() != null)
        {
            path.add(current.getPos());
            current = current.getPriorNode();
        }
        return path;

    }
}
