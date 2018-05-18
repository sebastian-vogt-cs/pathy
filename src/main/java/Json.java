import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Json {

    public static String readJson() throws ParseException, FileNotFoundException, IOException {
        String ret = "";

        JSONParser parser = new JSONParser();
        Reader reader = new FileReader("example.json");

        Object jsonObj = parser.parse(reader);

        JSONObject jsonObject = (JSONObject) jsonObj;

        String name = (String) jsonObject.get("name");
        ret = ret + "Name = " + name + "\n";

        long age = (Long) jsonObject.get("age");
        ret = ret + "Age = " + age + "\n";

        JSONArray cities = (JSONArray) jsonObject.get("cities");

        @SuppressWarnings("unchecked")
        Iterator<String> it = cities.iterator();
        while (it.hasNext()) {
            ret = ret + "City = " + it.next() + "\n";
        }
        reader.close();

        return ret;
    }

}