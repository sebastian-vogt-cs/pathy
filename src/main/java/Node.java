import java.util.HashMap;

public class Node {

    // each node has a name and all the nodes it's connected to
    private String name;
    private HashMap<Node, Integer> edges;

    public Node(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public HashMap<Node, Integer> getEdges() {
        return edges;
    }

    // if you want to connect a specified node with a specified edge length...
    public void connect_node(Node node, int length) throws IllegalEdgeException {
        // ...it first looks for already connected nodes with the same name and throws an exception if it exists...
        for (Node key : edges.keySet()) {
            if(key.getName().equals(node.getName())) {
                throw new IllegalEdgeException("You cannot define an edge twice!");
            }
        }
        // ...pushes the new node to the edges Hash...
        edges.put(node, length);
        //...and lets the other node know about the new connection.
        node.connect_node_back(this, length);
    }

    // connect_node method, but without calling it on the other node again, the other node already has the edge.
    private void connect_node_back(Node node, int length) throws IllegalEdgeException {
        for (Node key : edges.keySet()) {
            if(key.getName().equals(node.getName())) {
                throw new IllegalEdgeException("You cannot define an edge twice!");
            }
        }
        edges.put(node, length);
    }

}
