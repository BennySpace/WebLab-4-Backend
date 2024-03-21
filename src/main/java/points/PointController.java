package points;

import auth.ApplicationUser;
import auth.AuthService;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONException;
import org.json.JSONObject;


@Path("/dots")
@ApplicationScoped
public class PointController {
    @EJB
    private AuthService authService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPoint(@HeaderParam("Authorization") String authorization, String json) throws JSONException {
        ApplicationUser user = authService.parseAuthorization(authorization);

        if(user == null){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), "Invalid authorization").build();
        }

        long timer = System.nanoTime();

        Double x, y, r;

        try{
            JSONObject object = new JSONObject(json);
            x = object.getDouble("x");
            y = object.getDouble("y");
            r = object.getDouble("r");
        } catch (JSONException e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if(authService.login(user.getLogin(), user.getPassword()).getStatusInfo() != Response.Status.OK){
            return Response.status(Response.Status.UNAUTHORIZED.getStatusCode(), "Invalid login").build();
        }

        if(!Point.validateInput(x, y, r)){
            return Response.status(Response.Status.NOT_ACCEPTABLE.getStatusCode(), "Not valid x, y, r values").build();
        }

        Point point = new Point(x, y, r, user.getLogin());
        PointDao pointDao = new PointDao();
        point.setScriptTime((System.nanoTime() - timer) / 1000);
        pointDao.addPoint(point);

        return Response.ok(point, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPoints(@HeaderParam("Authorization") String authorization) throws JSONException {
        ApplicationUser user = authService.parseAuthorization(authorization);

        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), "Invalid authorization").build();
        }

        if (authService.login(user.getLogin(), user.getPassword()).getStatusInfo() != Response.Status.OK) {
            return Response.status(Response.Status.UNAUTHORIZED.getStatusCode(), "Invalid login").build();
        }

        PointDao pointDao = new PointDao();

        return Response.ok(pointDao.getPointsByLogin(user.getLogin()), MediaType.APPLICATION_JSON).build();
    }

    @DELETE
    public Response clearPoints(@HeaderParam("Authorization") String authorization) throws JSONException{
        ApplicationUser user = authService.parseAuthorization(authorization);

        if(user == null){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), "Invalid authorization").build();
        }

        if(authService.login(user.getLogin(), user.getPassword()).getStatusInfo() != Response.Status.OK){
            return Response.status(Response.Status.UNAUTHORIZED.getStatusCode(), "Invalid login").build();
        }

        PointDao pointDao = new PointDao();
        pointDao.removePointsByLogin(user.getLogin());

        return Response.ok().build();
    }
}
