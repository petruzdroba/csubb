package org.zdroba.demo;

import jakarta.persistence.EntityManager;
import org.zdroba.JPAUtil;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.IOException;

import static java.lang.System.currentTimeMillis;

public class IndexBenchmark {

    private static final int iterations = 100;
    static PrintStream out;

    public static void main(String[] args) {
        try {
            out = new PrintStream(new FileOutputStream("index-results.txt"));

            out.println("Before Indexes");
            benchmarkEmail();
            benchmarkDepartment();
            benchmarkSalaryRange();
            benchmarkMultiColumn();

            createIndexes();
            out.println("After Indexes");
            benchmarkEmail();
            benchmarkDepartment();
            benchmarkSalaryRange();
            benchmarkMultiColumn();

            dropIndexes();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) out.close();
            JPAUtil.close();
        }
    }

    static void createIndexes() {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();

        safeDrop(em, "idx_email");
        safeDrop(em, "idx_department");
        safeDrop(em, "idx_salary");
        safeDrop(em, "idx_dept_salary");

        em.createNativeQuery("CREATE INDEX idx_email ON employees(email)").executeUpdate();
        em.createNativeQuery("CREATE INDEX idx_department ON employees(department_id)").executeUpdate();
        em.createNativeQuery("CREATE INDEX idx_salary ON employees(salary)").executeUpdate();
        em.createNativeQuery("CREATE INDEX idx_dept_salary ON employees(department_id, salary)").executeUpdate();

        em.getTransaction().commit();
        em.close();
    }

    static void safeDrop(EntityManager em, String indexName) {
        try {
            em.createNativeQuery(
                    "DROP INDEX " + indexName + " ON employees"
            ).executeUpdate();
        } catch (Exception ignored) {
        }
    }

    static void dropIndexes() {
        EntityManager em = JPAUtil.getEntityManager();
        em.clear();
        em.getTransaction().begin();

        em.createNativeQuery("DROP INDEX idx_email ON employees").executeUpdate();
        em.createNativeQuery("DROP INDEX idx_department ON employees").executeUpdate();
        em.createNativeQuery("DROP INDEX idx_salary ON employees").executeUpdate();
        em.createNativeQuery("DROP INDEX idx_dept_salary ON employees").executeUpdate();

        em.getTransaction().commit();
        em.close();
    }

    static void benchmarkEmail() {
        EntityManager em = JPAUtil.getEntityManager();
        em.clear();

        out.println("EXPLAIN ANALYZE - Email:");
        var explain = em.createNativeQuery(
                "EXPLAIN ANALYZE SELECT * FROM employees WHERE email = 'employee1@company.com'"
        ).getResultList();
        explain.forEach(out::println);

        long start = currentTimeMillis();

        for (int i = 0; i < iterations; i++) {
            em.createQuery(
                            "SELECT e FROM Employee e WHERE email = :email"
                    ).setParameter("email", "employee" + (i * 100 + 1) + "@company.com")
                    .getResultList();
        }

        long end = currentTimeMillis();
        out.printf("Email search       : %d ms total, %.2f ms avg%n",
                (end - start), (end - start) / (double) iterations);
        em.close();
    }

    static void benchmarkDepartment(){
        EntityManager em = JPAUtil.getEntityManager();

        out.println("EXPLAIN ANALYZE - Department:");
        var explain = em.createNativeQuery(
                "EXPLAIN ANALYZE SELECT * FROM employees WHERE department_id = 1"
        ).getResultList();
        explain.forEach(out::println);

        long start = currentTimeMillis();

        for (int i = 0; i < iterations; i++) {
            em.createNativeQuery("SELECT * FROM employees WHERE department_id = :deptId")
                    .setParameter("deptId", (i % 10) + 1)
                    .getResultList();
        }

        long end = currentTimeMillis();
        out.printf("Department search   : %d ms total, %.2f ms avg%n",
                (end - start), (end - start) / (double) iterations);
        em.close();
    }

    static void benchmarkSalaryRange() {
        EntityManager em = JPAUtil.getEntityManager();
        em.clear();

        out.println("EXPLAIN ANALYZE - Salary:");
        var explain = em.createNativeQuery(
                "EXPLAIN ANALYZE SELECT * FROM employees WHERE salary BETWEEN 50000 AND 80000"
        ).getResultList();
        explain.forEach(out::println);

        long start = currentTimeMillis();

        for (int i = 0; i < iterations; i++) {
            em.createNativeQuery("SELECT * FROM employees WHERE salary BETWEEN 50000 AND 80000")
                    .getResultList();
        }

        long end = currentTimeMillis();
        out.printf("Salary range: %d ms total, %.2f ms avg%n",
                (end - start), (end - start) / (double) iterations);
        em.close();
    }

    static void benchmarkMultiColumn() {
        EntityManager em = JPAUtil.getEntityManager();
        em.clear();

        out.println("EXPLAIN ANALYZE - Multi-column:");
        var explain = em.createNativeQuery(
                "EXPLAIN ANALYZE SELECT * FROM employees WHERE department_id = 1 AND salary > 60000"
        ).getResultList();
        explain.forEach(out::println);

        long start = currentTimeMillis();

        for (int i = 0; i < iterations; i++) {
            em.createNativeQuery("SELECT * FROM employees WHERE department_id = :deptId AND salary > 60000")
                    .setParameter("deptId", (i % 10) + 1)
                    .getResultList();
        }

        long end = currentTimeMillis();
        out.printf("Multi-column        : %d ms total, %.2f ms avg%n",
                (end - start), (end - start) / (double) iterations);
        em.close();
    }
}
