package resource;

import hibernate.Util;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
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
    public Response findByClient(
            @PathParam("start_destination_id") int startDestinationId,
            @PathParam("end_destination_id") int endDestinationId,
            @PathParam("date") String startDate) {
        
        SessionFactory sf = Util.getSessionFactory();
        Session s = sf.openSession();
        Transaction t = s.getTransaction();

        try {
            t.begin();

            String sql = "FROM Schedule WHERE start_destination_id = :startDestinationId AND end_destination_id = :endDestinationId";
            
            if(startDate != null && !startDate.equals("")) {
                sql += " AND DATE(start_at) = :startDate";
            }
            System.out.println(sql);
            Query query = s.createQuery(sql);
            query.setInteger("startDestinationId", startDestinationId);
            query.setInteger("endDestinationId", endDestinationId);
            
            if(startDate != null && !startDate.equals("")) {
                query.setString("startDate", startDate);
            }
            
            GenericEntity<List<Schedule>> entity = new GenericEntity<List<Schedule>>(query.list()) {
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
