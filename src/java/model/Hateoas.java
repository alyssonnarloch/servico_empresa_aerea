package model;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

public class Hateoas {
    
    private UriInfo context;
    private boolean absolutePath;
    private Class className;
    private LinkedHashMap params;
    
    public Hateoas(UriInfo context, Class className) {
        this.context = context;
        this.className = className;
        this.params = new LinkedHashMap();
    }
    
    public void addParam(String paramName, String paramValue) {
        this.params.put(paramName, paramValue);
    }
    
    public String getUri() {
        UriBuilder ub = this.context.getBaseUriBuilder().path(this.className);
        Map<String, String> paramsAux = new HashMap();
        String buildParams = "";
        
        Set set = this.params.entrySet();
        Iterator i = set.iterator();
        
        while(i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next(); 
            if(!entry.getKey().toString().trim().isEmpty()) {
                buildParams += "/" + entry.getKey();
                
                if(!entry.getValue().toString().trim().isEmpty()) {
                    buildParams += "/";
                }
            }
            buildParams += entry.getValue();
        }                
        
        URI uri = ub.path(buildParams).build();
        
        return uri.toString();
    }
    
}
