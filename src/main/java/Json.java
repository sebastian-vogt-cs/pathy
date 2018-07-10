import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static java.lang.Math.toIntExact;

class Json {

    private Type type;
    private NodeHandler<Integer> handI;
    private NodeHandler<Double> handD;
    private NodeHandler<Long> handL;

    Json(String filename) throws ParseException, IOException {

        JSONParser parser = new JSONParser();
        Reader reader = new FileReader(filename);

        Object jsonObj = parser.parse(reader);

        JSONObject jsonObject = (JSONObject) jsonObj;

        @SuppressWarnings("unchecked")
        HashMap<String, JSONArray> data = (HashMap) jsonObject.get("connections");

        @SuppressWarnings("unchecked")
        String type = (String) jsonObject.get("type");

        switch (type) {
            case "Integer": {
                NodeHandler<Integer> handler = new NodeHandler<>();
                for (HashMap.Entry<String, JSONArray> entry : data.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    Iterator it = ((JSONArray) value).iterator();
                    Iterator it_save = ((JSONArray) value).iterator();
                    try {
                        handler.connect(key, (String) it.next(), toIntExact(((long) it.next())));
                    } catch (Exception ex) {
                        while (it_save.hasNext()) {
                            Iterator it2 = ((JSONArray) it_save.next()).iterator();
                            handler.connect(key, (String) it2.next(), toIntExact(((long) it2.next())));
                        }
                    }
                }
                handI = handler;
                this.type = Type.INTEGER;
                break;
            }
            case "Long": {
                NodeHandler<Long> handler = new NodeHandler<>();
                for (HashMap.Entry<String, JSONArray> entry : data.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    Iterator it = ((JSONArray) value).iterator();
                    Iterator it_save = ((JSONArray) value).iterator();
                    try {
                        handler.connect(key, (String) it.next(), ((long) it.next()));
                    } catch (Exception ex) {
                        while (it_save.hasNext()) {
                            Iterator it2 = ((JSONArray) it_save.next()).iterator();
                            handler.connect(key, (String) it2.next(), ((long) it2.next()));
                        }
                    }
                }
                handL = handler;
                this.type = Type.LONG;
                break;
            }
            case "Double": {
                NodeHandler<Double> handler = new NodeHandler<>();
                for (HashMap.Entry<String, JSONArray> entry : data.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    Iterator it = ((JSONArray) value).iterator();
                    Iterator it_save = ((JSONArray) value).iterator();
                    try {
                        handler.connect(key, (String) it.next(), ((Double) it.next()));
                    } catch (Exception ex) {
                        while (it_save.hasNext()) {
                            Iterator it2 = ((JSONArray) it_save.next()).iterator();
                            handler.connect(key, (String) it2.next(), ((Double) it2.next()));
                        }
                    }
                }
                handD = handler;
                this.type = Type.DOUBLE;
                break;
            }
            default:
                System.out.println("wrong type");
                break;
        }

        reader.close();

    }

    Optional<NodeHandler<Integer>> getHandI() {
        if(type.equals(Type.INTEGER)) {
            return Optional.of(handI);
        } else {
            return Optional.empty();
        }
    }
    Optional<NodeHandler<Double>> getHandD() {
        if(type.equals(Type.DOUBLE)) {
            return Optional.of(handD);
        } else {
            return Optional.empty();
        }
    }
    Optional<NodeHandler<Long>> getHandL() {
        if(type.equals(Type.LONG)) {
            return Optional.of(handL);
        } else {
            return Optional.empty();
        }
    }

}