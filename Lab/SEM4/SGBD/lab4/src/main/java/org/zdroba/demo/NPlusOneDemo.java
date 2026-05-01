package org.zdroba.demo;

import jakarta.persistence.EntityManager;
import org.zdroba.JPAUtil;
import org.zdroba.entity.Department;
import org.zdroba.entity.Employee;

import java.util.List;

public class NPlusOneDemo {

    public static void main(String[] args) {
        runProblem();
        runSolution();
        JPAUtil.close();
    }

    static void runProblem(){
        EntityManager em = JPAUtil.getEntityManager();
        System.out.println("\n\nN+1 Problem");

        long start = System.currentTimeMillis();
        int queryCount = 0;

        List<Department> departments = em.createQuery(
                "SELECT d from Department d", Department.class
        ).setHint("hibernate.query.passDistinctThrough", false).getResultList(); // 1 query
        queryCount ++;

        for(Department department: departments){
            List<Employee> employees = department.getEmployees();
            queryCount ++;
            System.out.println(department.getName() + " size: "+employees.size());
        }//N queries

        long end = System.currentTimeMillis();
        System.out.println("Query count: "+queryCount);
        System.out.println("Time: "+(end-start) + " ms");
        em.close();
    }

    static void runSolution(){
        EntityManager em = JPAUtil.getEntityManager();
        System.out.println("\n\n\nN+1 Solution");

        long start = System.currentTimeMillis();
        int queryCount = 0;

        List<Department> departments = em.createQuery(
                "SELECT DISTINCT d from Department d LEFT JOIN FETCH d.employees",Department.class
        ).getResultList(); // 1 interogare
        queryCount ++;

        for (Department department : departments) {
            System.out.println(department.getName() + " size: " + department.getEmployees().size());
        }

        long end = System.currentTimeMillis();
        System.out.println("Total queries: "+queryCount);
        System.out.println("Time: " + (end - start) + " ms");
        em.close();
    }
}
