package resource;

import hibernate.Util;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@Path("authentication")
public class AuthenticationResource {

    @Context
    private UriInfo context;

    public AuthenticationResource() {
    }

    @POST
    @Path("/verification")
    @Produces("application/json; charset=UTF-8")
    public Response verification(@FormParam("email") String email, @FormParam("password") String password) {
        
        SessionFactory sf = Util.getSessionFactory();
        Session s = sf.openSession();
        Transaction t = s.beginTransaction();
        
        Query query = s.createQuery("FROM User WHERE email = :email AND password = :password");
        query.setString("email", email);
        query.setString("password", password);
        query.setMaxResults(1);
        
        User user = (User) query.uniqueResult();
        
        if(user == null) {
            user = new User();
        } 
        
        s.flush();
        s.close();
        t.commit();
        
        user.setPassword("********************");
        GenericEntity<User> entity = new GenericEntity<User> (user) {
        };

        return Response.ok(entity).build();
    }

}
