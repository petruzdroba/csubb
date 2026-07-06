package org.zdroba.demo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.zdroba.JPAUtil;
import org.zdroba.entity.Employee;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class SoftDeleteDemo {

    private static final EntityManagerFactory emf = JPAUtil.getEntityManagerFactory();

    public static void main(String[] args) {

        Integer testId = createTestEmployee();

        listActive();

        softDelete(testId, "admin");

        listActive();
        listDeleted();

        restore(testId);

        listActive();

        hardDelete(testId);
    }

    public static Integer createTestEmployee() {
        try (EntityManager em = emf.createEntityManager()) {

            String email = "deletious@company.com";

            em.getTransaction().begin();

            em.createQuery(
                            "DELETE FROM Employee e WHERE e.email = :email"
                    )
                    .setParameter("email", email)
                    .executeUpdate();

            em.getTransaction().commit();

            em.getTransaction().begin();

            Employee e = new Employee();
            e.setName("Deletious Deleticus");
            e.setEmail(email);
            e.setSalary(new BigDecimal("50000"));
            e.setIsDeleted(false);

            em.persist(e);

            em.getTransaction().commit();

            return e.getId();
        }
    }

    public static void softDelete(Integer id, String user) {
        try (EntityManager em = emf.createEntityManager()) {

            Employee e = em.find(Employee.class, id);

            if (e == null) {
                System.out.println("Employee not found or already deleted");
                return;
            }

            em.getTransaction().begin();

            e.setIsDeleted(true);
            e.setDeletedAt(LocalDateTime.now());
            e.setDeletedBy(user);

            em.getTransaction().commit();

            System.out.println("Soft delete applied for id = " + id);
        }
    }

    public static void restore(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {

            em.getTransaction().begin();

            int updated = em.createNativeQuery(
                            "UPDATE employees " +
                                    "SET is_deleted = false, deleted_at = NULL, deleted_by = NULL " +
                                    "WHERE id = ?"
                    )
                    .setParameter(1, id)
                    .executeUpdate();

            em.getTransaction().commit();

            if (updated == 0) {
                System.out.println("Restore failed: employee not found");
            } else {
                System.out.println("Restored employee " + id);
            }
        }
    }

    public static void hardDelete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {

            em.getTransaction().begin();

            int affected = em.createNativeQuery(
                            "DELETE FROM employees WHERE id = ?"
                    )
                    .setParameter(1, id)
                    .executeUpdate();

            em.getTransaction().commit();

            System.out.println("Hard deleted employee " + id + ", rows affected = " + affected);
        }
    }

    public static void listActive() {
        try (EntityManager em = emf.createEntityManager()) {

            Long count = em.createQuery(
                    "SELECT COUNT(e) FROM Employee e WHERE e.isDeleted = false",
                    Long.class
            ).getSingleResult();

            System.out.println("\nACTIVE EMPLOYEES COUNT: " + count);
        }
    }

    public static void listDeleted() {
        try (EntityManager em = emf.createEntityManager()) {

            List<Employee> list = em.createNativeQuery(
                    "SELECT * FROM employees WHERE is_deleted = true",
                    Employee.class
            ).getResultList();

            System.out.println("\nDELETED EMPLOYEES:");
            list.forEach(e ->
                    System.out.println(e.getId() + " " + e.getName())
            );
        }
    }
}