package org.zdroba.paginators;

import jakarta.persistence.EntityManager;
import org.zdroba.JPAUtil;
import org.zdroba.entity.Employee;
import org.zdroba.entity.Page;

import java.util.List;

public class OffsetPaginator {
    public Page<Employee> getEmployeesPage(int pageNumber, int pageSize){
        EntityManager em = JPAUtil.getEntityManager();
        int offset = pageNumber * pageSize;

        List<Employee> employees = em.
                createQuery("SELECT e FROM Employee e ORDER BY e.id",Employee.class)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();

        Long total = countEmployees();

        em.close();
        return new Page<Employee>(employees, pageNumber, pageSize, total);
    }

    public long countEmployees() {
        EntityManager em = JPAUtil.getEntityManager();
        Long total = em.createQuery("SELECT COUNT(e) FROM Employee e", Long.class)
                .getSingleResult();
        em.close();
        return total;
    }
}
