package resource;

import java.util.Set;
import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(auth.FilterAuthentication.class);
        resources.add(resource.AuthenticationResource.class);
        resources.add(resource.CityResource.class);
        resources.add(resource.PurchaseResource.class);
        resources.add(resource.ScheduleResource.class);
    }
    
}
