package auth;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.json.JSONException;
import org.json.JSONObject;


@Path("")
@ApplicationScoped
public class AuthController {
    @EJB
    private AuthService authService;

    @GET
    @Path("/login")
    public Response checkAuth(@HeaderParam("Authorization") String authorization) {
        ApplicationUser user = this.authService.parseAuthorization(authorization);
        if(user == null) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), "Not valid authentication data").build();
        }

        return authService.login(user.getLogin(), user.getPassword());
    }

    @POST
    @Path("/register")
    public Response register(String json){
        String username, password;

        try{
            JSONObject object = new JSONObject(json);
            username = object.getString("username");
            password = object.getString("password");
        } catch (JSONException e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return authService.register(username, password);
    }
}
