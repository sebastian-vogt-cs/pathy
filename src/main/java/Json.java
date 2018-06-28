import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static java.lang.Math.toIntExact;

class Json {

    // this is an example of how the program could use data from a json file to create nodes. At the moment it merely
    // tells you which nodes and edges you have specified in example.json
    static NodeHandler readJson(String filename) throws ParseException, IOException {
        NodeHandler returnHandler = new NodeHandler();

        JSONParser parser = new JSONParser();
        Reader reader = new FileReader(filename);

        Object jsonObj = parser.parse(reader);

        JSONObject jsonObject = (JSONObject) jsonObj;

        @SuppressWarnings("unchecked")
        HashMap<String, JSONArray> data = (HashMap) jsonObject.get("connections");

        @SuppressWarnings("unchecked")
        String type = (String) jsonObject.get("type");

        if(type.equals("Integer")) {
            NodeHandler<Integer> handler = new NodeHandler<>();
            for (HashMap.Entry<String, JSONArray> entry : data.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                Iterator it = ((JSONArray) value).iterator();
                Iterator it_save = ((JSONArray) value).iterator();
                try {
                    handler.connect(key, (String) it.next(), toIntExact(((long) it.next())));
                } catch(Exception ex) {
                    while(it_save.hasNext()) {
                        Iterator it2 = ((JSONArray) it_save.next()).iterator();
                        handler.connect(key, (String) it2.next(), toIntExact(((long) it2.next())));
                    }
                }
            }
            returnHandler = handler;
        } else if(type.equals("Long")) {
            NodeHandler<Long> handler = new NodeHandler<>();
            for (HashMap.Entry<String, JSONArray> entry : data.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                Iterator it = ((JSONArray) value).iterator();
                Iterator it_save = ((JSONArray) value).iterator();
                try {
                    handler.connect(key, (String) it.next(), ((long) it.next()));
                } catch(Exception ex) {
                    while(it_save.hasNext()) {
                        Iterator it2 = ((JSONArray) it_save.next()).iterator();
                        handler.connect(key, (String) it2.next(), ((long) it2.next()));
                    }
                }
            }
            returnHandler = handler;
        } else if(type.equals("Double")) {
            NodeHandler<Double> handler = new NodeHandler<>();
            for (HashMap.Entry<String, JSONArray> entry : data.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                Iterator it = ((JSONArray) value).iterator();
                Iterator it_save = ((JSONArray) value).iterator();
                try {
                    handler.connect(key, (String) it.next(), ((Double) it.next()));
                } catch(Exception ex) {
                    while(it_save.hasNext()) {
                        Iterator it2 = ((JSONArray) it_save.next()).iterator();
                        handler.connect(key, (String) it2.next(), ((Double) it2.next()));
                    }
                }
            }
            returnHandler = handler;
        } else {
            System.out.println("wrong type");
        }

        reader.close();

        return returnHandler;
    }

}