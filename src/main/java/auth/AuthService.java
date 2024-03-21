package auth;

import jakarta.ejb.Stateless;
import jakarta.ws.rs.core.Response;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;


@Stateless
public class AuthService {
    public static final String PEPPER = "P(*6&HjD3#";
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz0123456789<>?:@{!$%^&*()_+£$";

    public Response login(String login, String password) {
        UserDao userDao = new UserDao();
        ApplicationUser user = userDao.getUserByLogin(login);

        if(user == null){
            return Response.status(Response.Status.UNAUTHORIZED.getStatusCode(), "Login is not valid").build();
        }

        String cookedPass = PEPPER + password + user.getSalt();

        if(!user.getPassword().equals(this.getHash(cookedPass))){
            return Response.status(Response.Status.UNAUTHORIZED.getStatusCode(), "Password is not valid").build();
        }

        return Response.ok().build();
    }

    public ApplicationUser parseAuthorization(String authorization){
        if(authorization == null || authorization.isBlank() || authorization.length() <= 6) {
            return null;
        }

        String login, password;

        try{
            var base64 = authorization.substring(6);
            String[] data =  new String(Base64.getDecoder().decode(base64)).split(":", 2);

            if(data.length != 2){
                return null;
            }

            login = data[0];
            password = data[1];
        } catch (IllegalArgumentException e) {
            return null;
        }

        return new ApplicationUser(login, password);
    }

    public Response register(String login, String password){
        UserDao userDao = new UserDao();

        if(userDao.getUserByLogin(login) != null){
            return Response.status(Response.Status.NOT_ACCEPTABLE.getStatusCode(), "Login is not free").build();
        }

        if(password.isBlank()){
            return Response.status(Response.Status.NOT_ACCEPTABLE.getStatusCode(), "Password must be not blank").build();
        }

        String salt = this.generateRandomString();
        String cookedPass = PEPPER + password + salt;
        var user = new ApplicationUser(login, this.getHash(cookedPass), salt);
        userDao.addUser(user);

        return Response.ok().build();
    }

    private String generateRandomString() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        return sb.toString();
    }

    private String getHash(String input){
        byte[] inputBytes = input.getBytes();
        MessageDigest md;

        try{
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        byte[] hashBytes = md.digest(inputBytes);
        StringBuilder sb = new StringBuilder();

        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}
