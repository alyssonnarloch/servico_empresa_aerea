package auth;

import hibernate.Util;
import java.io.IOException;
import java.util.Base64;
import java.util.StringTokenizer;
import model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class AuthenticationService {

    public boolean authenticate(String authCredentials) {
        if (null == authCredentials) {
            return false;
        }

        final String encodedUserPassword = authCredentials.replaceFirst("Basic ", "");
        String usernameAndPassword = null;
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encodedUserPassword);
            usernameAndPassword = new String(decodedBytes, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final StringTokenizer tokenizer = new StringTokenizer(
                usernameAndPassword, ":");
        final String username = tokenizer.nextToken();
        final String password = tokenizer.nextToken();

        return this.userVerification(username, password);
    }
    
    public boolean userVerification(String email, String password) {
        boolean status = true;
        
        SessionFactory sf = Util.getSessionFactory();
        Session s = sf.openSession();
        Transaction t = s.beginTransaction();
        
        Query query = s.createQuery("FROM User WHERE email = :email AND password = :password");
        query.setString("email", email);
        query.setString("password", password);
        query.setMaxResults(1);
        
        User user = (User) query.uniqueResult();
        
        if(user == null) {
            status = false;
        } 
        
        s.flush();
        s.close();
        t.commit();
        
        return status;
    }
}
