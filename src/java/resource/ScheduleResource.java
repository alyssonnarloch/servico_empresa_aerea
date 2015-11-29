package resource;

import com.google.gson.Gson;
import hibernate.Util;
import java.net.URI;
import java.util.ArrayList;
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
import model.Hateoas;
import model.Link;
import model.Schedule;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@Path("schedule")
public class ScheduleResource {

    @Context
    private UriInfo context;

    public ScheduleResource() {
    }

    @GET
    @Path("all")
    @Produces("application/json; charset=UTF-8")
    public Response findAll() {
        SessionFactory sf = Util.getSessionFactory();
        Session s = sf.openSession();
        List<Schedule> schedules = s.createCriteria(Schedule.class).list();
        s.flush();
        s.close();

        GenericEntity<List<Schedule>> entity = new GenericEntity<List<Schedule>>(schedules) {
        };

        return Response.ok(entity).build();
    }

    @GET
    @Path("/start/{start_destination_id}/end/{end_destination_id}/{date: .*}")
    @Produces("application/json; charset=UTF-8")
    public Response findByDestinationDate(
            @PathParam("start_destination_id") int startDestinationId,
            @PathParam("end_destination_id") int endDestinationId,
            @PathParam("date") String startDate) {
        
        SessionFactory sf = Util.getSessionFactory();
        Session s = sf.openSession();
        Transaction t = s.getTransaction();
        
        Hateoas h = new Hateoas();
        UriBuilder ub = context.getBaseUriBuilder().path(ScheduleResource.class);

        try {
            t.begin();

            String sql = "FROM Schedule WHERE start_destination_id = :startDestinationId AND end_destination_id = :endDestinationId";
            
            if(startDate != null && !startDate.equals("")) {
                sql += " AND DATE(start_at) = :startDate";
            }
            
            Query query = s.createQuery(sql);
            query.setInteger("startDestinationId", startDestinationId);
            query.setInteger("endDestinationId", endDestinationId);
            
            if(startDate != null && !startDate.equals("")) {
                query.setString("startDate", startDate);
            }
            
            List<Schedule> schedules = query.list();
            Gson gson = new Gson();
            for(int i = 0; i < schedules.size(); i++) {
                URI uriSelf = ub.path("").build();                                
                
                List<Link> links = new ArrayList<Link>();
                
                links.add(new Link("self", "http://oimundinholoco.com.br"));
                links.add(new Link("title", "AJUDA AE"));
                
                schedules.get(i).setLinks(links);
            }
            
            GenericEntity<List<Schedule>> entity = new GenericEntity<List<Schedule>>(schedules) {
            };

            t.commit();

            s.flush();
            s.close();

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
