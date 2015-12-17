package resource;

import hibernate.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import model.Client;
import model.Hateoas;
import model.Link;
import model.Purchase;
import model.Schedule;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@Path("purchase")
public class PurchaseResource {

    @Context
    private UriInfo context;

    public PurchaseResource() {
    }

    @GET
    @Produces("application/json")
    public String test() {
        return "{oieeee : 1}";
    }
    
    @GET
    @Path("/client/{client_id}")
    @Produces("application/json; charset=UTF-8")
    public Response getByClient(@PathParam("client_id") int clientId) {
        SessionFactory sf = Util.getSessionFactory();
        Session s = sf.openSession();

        Query query = s.createQuery("FROM Purchase WHERE client_id = :clientId ORDER BY created_at DESC");
        query.setInteger("clientId", clientId);

        GenericEntity<List<Purchase>> entity = new GenericEntity<List<Purchase>>(query.list()) {
        };

        s.flush();
        s.close();

        return Response.ok(entity).build();
    }

    @POST
    @Path("/save")
    @Produces("application/json; charset=UTF-8")
    public Response save(@FormParam("schedule_id") int scheduleId,
            @FormParam("client_id") int clientId,
            @FormParam("account") int account,
            @FormParam("agency") int agency) {

        SessionFactory sf = Util.getSessionFactory();
        Session s = sf.openSession();
        Transaction t = s.beginTransaction();

        try {
            Client client = (Client) s.get(Client.class, clientId);
            Schedule schedule = (Schedule) s.get(Schedule.class, scheduleId);

            Purchase purchase = new Purchase();
            purchase.setClient(client);
            purchase.setSchedule(schedule);
            purchase.setPrice(schedule.getPrice());
            purchase.setStatus(Purchase.EFFECTED);
            purchase.setAccount(account);
            purchase.setAgency(agency);
            purchase.setCreatedAt(new Date());

            s.save(purchase);

            t.commit();
            
            s.flush();
            s.close();

            Hateoas hSelf = new Hateoas(context, PurchaseResource.class);
            hSelf.addParam("", String.valueOf(purchase.getId()));

            List<Link> links = new ArrayList();
            links.add(new Link("self", hSelf.getUri()));

            purchase.setLinks(links);

            GenericEntity<Purchase> entity = new GenericEntity<Purchase>(purchase) {
            };

            return Response.ok(entity).build();
        } catch (Exception ex) {
            t.rollback();

            ex.printStackTrace();

            s.flush();
            s.close();

            return Response.serverError().build();
        }
    }

    @PUT
    @Path("/cancel")
    @Produces("application/json; charset=UTF-8")
    public Response cancel(@FormParam("id") int id) {

        SessionFactory sf = Util.getSessionFactory();
        Session s = sf.openSession();
        Transaction t = s.beginTransaction();

        try {
            Purchase purchase = (Purchase) s.get(Purchase.class, id);

            purchase.setStatus(Purchase.CANCELED);

            s.update(purchase);

            s.flush();
            s.close();

            GenericEntity<Purchase> entity = new GenericEntity<Purchase>(purchase) {
            };
            
            return Response.ok(entity).build();
        } catch (Exception ex) {
            t.rollback();

            ex.printStackTrace();

            s.flush();
            s.close();

            return Response.serverError().build();
        }
    }
}
