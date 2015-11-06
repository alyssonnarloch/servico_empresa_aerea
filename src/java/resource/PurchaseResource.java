package resource;

import hibernate.Util;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import model.Purchase;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@Path("purchase")
public class PurchaseResource {

    @Context
    private UriInfo context;

    public PurchaseResource() {
    }

    @GET
    @Path("/client_id/{client_id}")
    @Produces("application/json; charset=UTF-8")
    public Response getByClient(@PathParam("client_id") int clientId) {
        SessionFactory sf = Util.getSessionFactory();
        Session s = sf.openSession();

        Query query = s.createQuery("FROM Purchase WHERE client_id = :clientId");
        query.setInteger("clientId", clientId);

        GenericEntity<List<Purchase>> entity = new GenericEntity<List<Purchase>>(query.list()) {
        };

        s.flush();
        s.close();

        return Response.ok(entity).build();
    }

}
