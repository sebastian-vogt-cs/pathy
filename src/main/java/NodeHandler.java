import java.util.HashMap;
import java.util.Vector;

public class NodeHandler {

    // this vector stores all the nodes
    private Vector<Node> nodes = new Vector<Node>();

    public void addNode(Node n) throws IllegalEdgeException {
        if(getNodeByName(n.getName()) != null) {
            throw new IllegalEdgeException("You tried to create an existing node");
        }
        nodes.add(n);
    }

    public void connect(String name1, String name2, Integer len) throws IllegalEdgeException {
        Node node1 = getNodeByName(name1);
        Node node2 = getNodeByName(name2);
        if (node1 == null && node2 == null) {
            node1 = new Node(name1);
            node2 = new Node(name2);
            nodes.add(node1);
            nodes.add(node2);
            node1.connect_node(node2, len);
        } else if (node1 != null && node2 == null) {
            node2 = new Node(name2);
            nodes.add(node2);
            node1.connect_node(node2, len);
        } else if (node1 == null) {
            node1 = new Node(name1);
            nodes.add(node1);
            node1.connect_node(node2, len);
        } else {
            node1.connect_node(node2, len);
        }
    }

    public Node getNodeByName(String name) {
        for(Node node : nodes) {
            if(node.getName().equals(name)) {
                return node;
            }
        }
        return null;
    }

    public String toString() {
        String ret = "";
        for(Node n : nodes) {
            ret = ret + n.getName() + "->";
            for (HashMap.Entry<Node, Integer> entry : n.getEdges().entrySet()) {
                Node key = entry.getKey();
                Integer value = entry.getValue();
                ret = ret + " " + key.getName() + "," + value + ";";
            }
            ret = ret + "\n";
        }
        return ret;
    }

}
