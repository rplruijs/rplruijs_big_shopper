package nl.hu.bep.shopping.webservices;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import nl.hu.bep.shopping.model.MyUser;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.Key;
import java.util.AbstractMap;
import java.util.Calendar;

@Path("authenticate")
public class AuthenticationResource {

    public static Key key = MacProvider.generateKey();


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(LogonRequest logonRequest) {

        try{
            MyUser myUser = MyUser.validateLogin(logonRequest.getUserName(), logonRequest.getPassword());

            if(myUser == null) {
                throw new IllegalArgumentException("No user found");
            } else {
                String token = createToken(myUser.getName(), myUser.getRole());
                return Response.ok(new AbstractMap.SimpleEntry<>("JWT", token)).build();
            }

        } catch (JwtException | IllegalArgumentException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

    }

    private String createToken(String userName, String role) throws JwtException {
        Calendar expiration = Calendar.getInstance();
        expiration.add(Calendar.MINUTE, 30);

        return Jwts.builder()
                .setSubject(userName)
                .setExpiration(expiration.getTime())
                .claim("role", role)
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }
}
