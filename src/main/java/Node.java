import java.util.HashMap;

public class Node {

    private String name;
    private HashMap<Node, Integer> edges;

    public Node(String name) {
        this.name = name;
    }

    public void connect_node(Node node, int length) throws IllegalEdgeException {
        for (Node key : edges.keySet()) {
            if(key.getName().equals(node.getName())) {
                throw new IllegalEdgeException("You cannot define an edge twice!");
            }
        }
        edges.put(node, length);
        node.connect_node_back(this, length);
    }

    public String getName() {
        return name;
    }

    private void connect_node_back(Node node, int length) throws IllegalEdgeException {
        for (Node key : edges.keySet()) {
            if(key.getName().equals(node.getName())) {
                throw new IllegalEdgeException("You cannot define an edge twice!");
            }
        }
        edges.put(node, length);
    }

    public HashMap<Node, Integer> getEdges() {
        return edges;
    }

}
