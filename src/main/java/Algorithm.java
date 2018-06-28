import java.util.ArrayList;

public class Algorithm<T> {
    ArrayList<Node<T>> openList;
    NodeHandler<T> nodeHandler;

    Algorithm(NodeHandler nodeHandler) {
        this.nodeHandler = nodeHandler;
    }

    private void initNodes() {

    }
}
