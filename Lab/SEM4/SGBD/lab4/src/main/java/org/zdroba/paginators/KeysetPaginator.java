package org.zdroba.paginators;

import jakarta.persistence.EntityManager;
import org.zdroba.JPAUtil;
import org.zdroba.entity.Employee;
import org.zdroba.entity.Page;

import java.util.List;

public class KeysetPaginator {
    public Page<Employee> getEmployeesAfter(Integer lastId, int pageSize){
        EntityManager em = JPAUtil.getEntityManager();

        List<Employee> employees = em
                .createQuery("SELECT e FROM Employee e WHERE e.id > :lastId ORDER BY e.id", Employee.class)
                .setParameter("lastId", lastId)
                .setMaxResults(pageSize)
                .getResultList();

        em.close();
        return new Page<Employee>(employees, pageSize);
    }
}
