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

public class Json {

    // this is an example of how the program could use data from a json file to create nodes. At the moment it merely
    // tells you which nodes and edges you have specified in example.json
    public static NodeHandler readJson() throws ParseException, IOException, IllegalEdgeException {
        NodeHandler handler = new NodeHandler();

        JSONParser parser = new JSONParser();
        Reader reader = new FileReader("example.json");

        Object jsonObj = parser.parse(reader);

        JSONObject jsonObject = (JSONObject) jsonObj;

        HashMap<String, JSONArray> data = (HashMap) jsonObject.get("connections");

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

        reader.close();

        return handler;
    }

}