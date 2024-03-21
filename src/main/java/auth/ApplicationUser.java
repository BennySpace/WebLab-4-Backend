package auth;

import jakarta.enterprise.context.SessionScoped;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;


@Entity
@Table(name="ApplicationUsers")
@SessionScoped
public class ApplicationUser implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotBlank
    @Column(unique = true)
    private String login;

    @NotNull
    @NotBlank
    private String password;

    @NotNull
    @NotBlank
    private String salt;

    public ApplicationUser() {
    }

    public ApplicationUser(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public ApplicationUser(String login, String password, String salt) {
        this.login = login;
        this.password = password;
        this.salt = salt;
    }

    public ApplicationUser(Long id, String login, String password, String salt) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.salt = salt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
