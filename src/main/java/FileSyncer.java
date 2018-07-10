import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

class FileSyncer {

    private Type type;
    private String fileName;

    FileSyncer(Type type) {
        this.type = type;
        fileName = "new-config";
    }

    void setType(Type type) {
        this.type = type;
    }

    void setFileName(String fileName) {
        this.fileName = fileName;
        try {
            createFile();
        } catch(Exception ex) {
            System.out.print("Unhandled exception in FileSyncer constructor" + ex.getMessage());
        }
    }

    String getFileName() {
        return fileName;
    }

    boolean exists(String fileName) {
        return Files.exists(Paths.get(fileName + ".json"));
    }

    private void createFile() {
        new File(fileName + ".json");
    }

    private void startFile() throws IOException {
        writeln("{");
        writeln("  \"connections\": {");
    }

    private void write(Node node, boolean comma) throws IOException {
        int len = node.getEdges().size();
        int i = 0;
        StringBuilder line = new StringBuilder();
        if(len != 1) {
            line.append("    \"").append(node.getName()).append("\"").append(": [");
        } else {
            line.append("    \"").append(node.getName()).append("\"").append(": ");
        }
        for (Object nd: node.getEdges().keySet()) {
            i++;
            Node n = (Node) nd;
            line.append("[" + "\"").append(n.getName()).append("\"").append(", ").append(node.getEdges().get(n)).append("]");
            if(len != i) {
                line.append(", ");
            }
        }
        if(len != 1) {
            line.append("]");
        }
        if(comma) {
            line.append(",");
        }
        writeln(line.toString());
    }
    private void write(Node n) throws IOException {
        write(n, false);
    }

    private void endFile() throws IOException {
        writeln("  },");
        writeln("  \"type\": \""+ type.toString() +"\"");
        writeln("}");

    }

    private void writeln(String text) throws IOException {
        ArrayList<String> list = new ArrayList<>();
        list.add(text);
        Files.write(Paths.get(fileName + ".json"), list, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    void handlerToFile(NodeHandler handler) throws IOException {
        resetFile();
        startFile();
        int i = 0;
        for(Object nd: handler.getNodes()) {
            i++;
            Node n = (Node) nd;
            if(i == handler.getNodes().size()) {
                write(n);
            } else {
                write(n, true);
            }
        }
        endFile();
    }

    void resetFile() throws IOException {
        Files.deleteIfExists(Paths.get(fileName + ".json"));
        createFile();
    }


}


