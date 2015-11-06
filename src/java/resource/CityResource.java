package resource;

import hibernate.Util;
import java.net.URI;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import model.City;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@Path("city")
public class CityResource {

    @Context
    private UriInfo context;

    public CityResource() {
    }

    @GET
    @Path("all")
    @Produces("application/json; charset=UTF-8")
    public Response findAll() {
        SessionFactory sf = Util.getSessionFactory();
        Session s = sf.openSession();
        List<City> cities = s.createCriteria(City.class).list();
        s.flush();
        s.close();

        GenericEntity<List<City>> entity = new GenericEntity<List<City>>(cities) {
        };

        return Response.ok(entity).build();
    }

    @GET
    @Path("/{id}")
    @Produces("application/json; charset=UTF-8")
    public Response findById(@PathParam("id") int id) {
        SessionFactory sf = Util.getSessionFactory();
        Session s = sf.openSession();
        City destination = (City) s.get(City.class, id);
        s.flush();
        s.close();                

        UriBuilder ub = context.getAbsolutePathBuilder();
        URI uriSelf = ub.build();
        
        return Response.ok(destination).link(uriSelf, "self").build();
    }
}
