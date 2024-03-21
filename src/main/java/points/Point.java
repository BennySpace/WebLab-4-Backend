package points;

import jakarta.enterprise.context.SessionScoped;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Entity
@Table(name = "points")
@SessionScoped
public class Point implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    private Double x;
    private Double y;
    private Double r;
    private Boolean status;
    private String time;
    private long scriptTime;
    private String ownerLogin;

    public Point() {
    }

    public Point(Double x, Double y, Double r, String ownerLogin) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.status = checkStatus(x, y, r);
        this.time = DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now());
        this.ownerLogin = ownerLogin;
    }

    public Point(Long id, Double x, Double y, Double r, Boolean status, String time, long scriptTime, String ownerLogin) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.r = r;
        this.status = status;
        this.time = time;
        this.scriptTime = scriptTime;
        this.ownerLogin = ownerLogin;
    }

    public static boolean validateInput(Double x, Double y, Double r) {
        if (x == null || y == null || r == null) return false;
        if (x > 4 || x < -4) return false;
        if (y > 3 || y < -5) return false;
        if (r > 4 || r <= 0) return false;

        return true;
    }

    public static boolean checkStatus(double x, double y, double r) {
        if (x >= 0 && y >= 0 && x * x + y * y <= r * r) return true;
        if (x >= 0 && y <= 0 && x <= r / 2 && y >= -r) return true;
        if (x <= 0 && y <= 0 && y >= -2 * x - r) return true;

        return false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getR() {
        return r;
    }

    public void setR(Double r) {
        this.r = r;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getScriptTime() {
        return scriptTime;
    }

    public void setScriptTime(long scriptTime) {
        this.scriptTime = scriptTime;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    @Override
    public String toString() {
        return "Point{" +
                "id=" + id + ",\n" +
                "x=" + x + ",\n" +
                "y=" + y + ",\n" +
                "r=" + r + ",\n" +
                "status=" + status + ",\n" +
                "time='" + time + "',\n" +
                "scriptTime=" + scriptTime + ",\n" +
                "ownerLogin='" + ownerLogin + "',\n" +
                '}';
    }
}
