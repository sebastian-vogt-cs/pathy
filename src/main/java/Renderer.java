import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;


class Renderer<T extends Number> {

    private String styleSheet =
            "node {" +
                    "	fill-color: black;" +
                    "}" +
                    "node.marked {" +
                    "	fill-color: red;" +
                    "}" +
                    "edge.path {" +
                    "	fill-color: yellow;" +
                    "}";
    private Graph graph;
    private boolean isRendered;

    Renderer() {
        graph = new SingleGraph("graph");
        graph.setStrict(false);
        graph.setAutoCreate( true );
        graph.display();
    }

    void render(NodeHandler handler) {

        graph.clear();
        graph.addAttribute("ui.stylesheet", styleSheet);
        for(Object nd : handler.getNodes()){
            @SuppressWarnings("unchecked")
            Node<T> node = (Node) nd;
            graph.addNode(node.getName());
            HashMap<Node<T>, T> edges = node.getEdges();
            for(Object ed : edges.keySet()) {
                @SuppressWarnings("unchecked")
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

    }

    void renderBlank() {
        graph.clear();
    }

    boolean mark(String name) {
        try{
            graph.getNode(name).addAttribute("ui.class", "marked");
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    boolean markEdge(String name) {
        try{
            graph.getEdge(name).addAttribute("ui.class", "path");
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    void addEdge(String name, String name2, String len) {
        graph.addEdge(name + name2, name, name2);
        graph.getEdge(name + name2).addAttribute("ui.label", len);
        graph.getNode(name).addAttribute("ui.label", name);
        graph.getNode(name2).addAttribute("ui.label", name2);
    }

    void addFileName(String name) {
        graph.addNode("fileName").addAttribute("ui.label", name);
    }

    void setStyleSheet(String filename) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(filename));
        styleSheet = new String(encoded, Charset.forName("UTF-8"));
        graph.changeAttribute("ui.stylesheet", styleSheet);
    }

}
