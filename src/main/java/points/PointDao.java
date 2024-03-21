package points;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Root;

import java.io.Serializable;
import java.util.List;


@Named
@SessionScoped
public class PointDao implements Serializable {
    @PersistenceContext(unitName = "points")
    private EntityManager entityManager;
    private EntityManagerFactory entityManagerFactory;

    public PointDao() {
        entityManagerFactory = Persistence.createEntityManagerFactory("points");
        entityManager = entityManagerFactory.createEntityManager();
    }

    public void addPoint(Point point) {
        entityManager.getTransaction().begin();

        try {
            entityManager.persist(point);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }

        entityManager.getTransaction().commit();
    }

    public List<Point> getPointsByLogin(String login) {
        Query query = entityManager.createQuery(
                "select s from Point s where s.ownerLogin=:login order by s.id desc",
                Point.class
        );

        query.setParameter("login", login);

        return query.getResultList();
    }

    public void removePointsByLogin(String login){
        entityManager.getTransaction().begin();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Point> criteriaDelete = criteriaBuilder.createCriteriaDelete(Point.class);
        Root<Point> root = criteriaDelete.from(Point.class);
        criteriaDelete.where(root.get("ownerLogin").in(List.of(login)));

        try {
            entityManager.createQuery(criteriaDelete).executeUpdate();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }

        entityManager.getTransaction().commit();
    }
}
