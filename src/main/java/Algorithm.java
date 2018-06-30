/* I commented it out so the program will compile


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class Algorithm<T> {
    ArrayList<Node<T>> openList;
    NodeHandler<T> nodeHandler;

    Node<T> startNode;
    Node<T> endNode;

    Algorithm(NodeHandler nodeHandler) {
        this.nodeHandler = nodeHandler;
    }

    void run(Node<T> startNode, Node<T> endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
        walk(startNode);
    }

    void walk(Node<T> here){
        if (here != endNode) {
            for (Node<T> next : here.getEdges().keySet()) {
                if (here == startNode) {
                    next.setDistance(Optional.of(here.getEdges().get(next)));
                }
                else{
                    next.setDistance(Optional.of(here.getEdges().get(next) + here.getDistance().get()));

                }
            }
        }
    }
}
*/
