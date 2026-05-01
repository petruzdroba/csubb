package org.zdroba.demo;

import jakarta.persistence.EntityManager;
import org.zdroba.JPAUtil;
import org.zdroba.entity.Employee;

import java.math.BigDecimal;
import java.util.List;

public class Actualizari {
    public static void main(String[] args) {
        individual();
//        mass();
//        batch();
    }

    static void individual(){
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();

        long start = System.currentTimeMillis();
        List<Employee> employees = em
                .createQuery("SELECT e from Employee e where e.department.id= :deptId", Employee.class)
                .setParameter("deptId", 5)
                .getResultList();

        for (Employee emp : employees) {
            emp.setSalary(emp.getSalary().multiply(new BigDecimal("1.1")));
            em.merge(emp);
        }

        em.getTransaction().commit();
        long end = System.currentTimeMillis();
        System.out.println((end-start) + " ms");

        em.close();
    }

    static void mass(){
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();

        long start = System.currentTimeMillis();
        em.createQuery(
                        "UPDATE Employee e SET e.salary = e.salary * 1.1 WHERE e.department.id = :deptId")
                .setParameter("deptId", 5)
                .executeUpdate();

        em.getTransaction().commit();
        long end = System.currentTimeMillis();
        System.out.println((end-start) + " ms");

        em.close();
    }

    static void batch() {

        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();

        long start = System.currentTimeMillis();

        List<Employee> employees = em
                .createQuery("SELECT e FROM Employee e WHERE e.department.id = :deptId", Employee.class)
                .setParameter("deptId", 5)
                .getResultList();

        int batchSize = 50;

        for (int i = 0; i < employees.size(); i++) {
            Employee emp = employees.get(i);

            emp.setSalary(emp.getSalary().multiply(new BigDecimal("1.1")));
            em.merge(emp);

            if (i % batchSize == 0 && i > 0) {
                em.flush();
                em.clear();
            }
        }
        em.getTransaction().commit();

        long end = System.currentTimeMillis();

        System.out.println("Batch update: " + (end - start) + " ms");

        em.close();
    }
}
