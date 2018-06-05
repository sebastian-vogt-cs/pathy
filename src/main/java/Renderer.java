import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

import java.util.HashMap;
import java.util.Vector;

public class Renderer<T> {

    void render(NodeHandler handler) {

        Graph graph = new SingleGraph("graph");
        graph.setStrict(false);
        graph.setAutoCreate( true );

        for(Object nd : handler.getNodes()){
            Node<T> node = (Node) nd;
            graph.addNode(node.getName());
            HashMap<Node<T>, T> edges = node.getEdges();
            for(Object ed : edges.keySet()) {
                Node<T> node2 = (Node) ed;
                T len = edges.get(ed);
                if(node.getName().compareTo(node2.getName()) < 0) {
                    String name = node.getName();
                    String name2 = node2.getName();
                    graph.addEdge(name + name2, name, name2);
                    graph.getNode(name).addAttribute("ui.label", name);
                    graph.getNode(name2).addAttribute("ui.label", name2);
                    graph.getEdge(name + name2).addAttribute("ui.label", len.toString());
                }
            }
        }

        graph.display();
    }

}
