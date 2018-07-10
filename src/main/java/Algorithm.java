import java.util.*;

public class Algorithm<T extends Number> {
    ArrayList<Node<T>> path;

    private ArrayList<Node<T>> openList;

    private NodeHandler<T> nodeHandler;

    private Node<T> startNode;
    private Node<T> endNode;

    Algorithm(NodeHandler nodeHandler) {
        this.nodeHandler = nodeHandler;
        openList = new ArrayList<>();
    }

    ArrayList<Node<T>> run(Node<T> startNode, Node<T> endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
        openList.add(startNode);

        while (openList.size() > 0) {
            walk(openList.get(0));
        }

        if (endNode.getPredecessor() != null) {
            // found endNode
            path = new ArrayList<>();
            storePath(endNode);
            return path;
        } else {
            // no connection
            System.out.println("DEBUG: endNode: " + endNode.getName());
            return null;
        }
    }

    void walk(Node<T> here){
        here.setVisited(true);
        removeFromOpenList(here);
        if (here != endNode) {
            for (Node<T> next : here.getEdges().keySet()) {
                if (here == startNode) {
                    next.setDistance(Optional.of(here.getEdges().get(next)));
                    next.setPredecessor(here);
                    openList.add(next);
                } else if (!next.equals(here.getPredecessor())){
                    Optional<T> newDistance = Optional.of(Type.add(here.getEdges().get(next), here.getDistance().get()));
                    Optional<T> oldDistance = next.getDistance();

                    if (!oldDistance.isPresent() || Type.lessThan(newDistance.get(), oldDistance.get())) {
                        next.setDistance(newDistance);
                        next.setPredecessor(here);
                        openList.add(next);
                    }
                }
            }
            sortOpenList();
        } else {
            openList.clear();
        }
    }

    private void addToOpenList(Node<T> n) {
        if (!openList.contains(n)) {
            openList.add(n);
        }
    }

    private void removeFromOpenList(Node<T> n) {
        openList.remove(n);
    }

    private void sortOpenList() {
        Collections.sort(openList, new Comparator<Node<T>>() {
            @Override
            public int compare(Node<T> t1, Node<T> t2) {
                return Type.compare(t1.getDistance().get(), t2.getDistance().get());
            }
        });
    }

    private void storePath(Node<T> n){
        if (n != startNode) {
            storePath(n.getPredecessor());
        }
        path.add(n);
    }
}
