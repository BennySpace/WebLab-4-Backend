package auth;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;

import java.io.Serializable;


@ApplicationScoped
public class UserDao implements Serializable {
    @PersistenceContext(unitName = "ApplicationUsers")
    private EntityManager entityManager;
    private EntityManagerFactory entityManagerFactory;

    public UserDao() {
        entityManagerFactory = Persistence.createEntityManagerFactory("ApplicationUsers");
        entityManager = entityManagerFactory.createEntityManager();
    }

    public void addUser(ApplicationUser user) {
        entityManager.getTransaction().begin();

        try {
            entityManager.persist(user);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }

        entityManager.getTransaction().commit();
        entityManager.refresh(user);
    }

    public ApplicationUser getUserByLogin(String login){
        Query query = entityManager.createQuery("select u from ApplicationUser u where u.login = :login", ApplicationUser.class);
        query.setParameter("login", login);

        try{
            return (ApplicationUser) query.getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }
}
