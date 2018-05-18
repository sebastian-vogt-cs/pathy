import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Json {

    public static String readJson() throws ParseException, IOException {
        String ret = "";

        JSONParser parser = new JSONParser();
        Reader reader = new FileReader("example.json");

        Object jsonObj = parser.parse(reader);

        JSONObject jsonObject = (JSONObject) jsonObj;

        HashMap<String, JSONArray> data = (HashMap) jsonObject.get("connections");

        for (HashMap.Entry<String, JSONArray> entry : data.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            ret = ret + key + " is connected to ";
            Iterator it = ((JSONArray) value).iterator();
            ret = ret + it.next() + " with the distance of: " + it.next() + "\n";

        }

        reader.close();

        return ret;
    }

}