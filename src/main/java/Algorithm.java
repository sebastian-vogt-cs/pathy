import java.util.*;

class Algorithm<T extends Number> {

    private ArrayList<Node<T>> path;

    private ArrayList<Node<T>> openList;

    private NodeHandler<T> nodeHandler;

    private Node<T> startNode;
    private Node<T> endNode;

    Algorithm(NodeHandler<T> nodeHandler) {
        this.nodeHandler = nodeHandler;
    }

    ArrayList<Node<T>> run(Node<T> startNode, Node<T> endNode) throws NoSuchElementException {
        if (startNode == null || endNode == null) {
            throw new NoSuchElementException();
        }

        nodeHandler.getNodes().forEach(node -> {
            node.setPredecessor(null);
            node.setDistance(Optional.empty());
        });

        openList = new ArrayList<>();
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
            return null;
        }
    }

    private void walk(Node<T> here) {
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

    private void removeFromOpenList(Node<T> n) {
        openList.remove(n);
    }

    private void sortOpenList() {
        Collections.sort(openList, (t1, t2) -> Type.compare(t1.getDistance().get(), t2.getDistance().get()));
    }

    private void storePath(Node<T> n){
        if (n != startNode) {
            storePath(n.getPredecessor());
        }
        path.add(n);
    }
}
