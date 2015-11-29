package model;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

public class Hateoas {

    private Map<String, String> links;
    private Gson gson;
    
    public Hateoas() {
        links = new HashMap<String, String>();
        gson = new Gson();
    }

    public void put(String key, String value) {
        links.put(key, value);
    }
    
    public String toJson() {
        return gson.toJson(links).toString().replace("\"", "");
    }
}
