package org.zdroba.cache;

import jakarta.persistence.EntityManager;
import org.ehcache.Cache;
import org.zdroba.JPAUtil;
import org.zdroba.entity.Department;

public class DepartmentService {

    private final Cache<Integer, Department> cache = CacheProvider.getCache();
    private int hits = 0;
    private int misses = 0;
    public Department getById(Integer id) {

        Department cached = cache.get(id);

        if (cached != null) {
            System.out.println("CACHE HIT");
            hits++;
            return cached;
        }

        System.out.println("CACHE MISS");
        misses++;

        EntityManager em = JPAUtil.getEntityManager();
        Department dept = em.find(Department.class, id);

        em.close();
        cache.put(id, dept);

        return dept;
    }

    public Department update(Department department) {

        EntityManager em = JPAUtil.getEntityManager();

        em.getTransaction().begin();

        Department updated = em.merge(department);

        em.getTransaction().commit();
        em.close();

        cache.remove(updated.getId());
        cache.put(updated.getId(), updated);

        System.out.println("Updated in cache " + updated.getId());

        return updated;
    }

    public void delete(Integer id) {

        EntityManager em = JPAUtil.getEntityManager();

        em.getTransaction().begin();

        Department dept = em.find(Department.class, id);
        em.remove(dept);

        em.getTransaction().commit();
        em.close();

        cache.remove(id);
        System.out.println("Removed from cache " + id);
    }

    public Integer save(Department department) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(department);
        em.getTransaction().commit();
        em.close();
        return department.getId();
    }

    public void printCacheStats() {
        int total = hits + misses;

        double hitRate = total == 0 ? 0 : (hits * 100.0 / total);
        double missRate = total == 0 ? 0 : (misses * 100.0 / total);

        System.out.println("\nCACHE STATS:");
        System.out.println("Hits: " + hits);
        System.out.println("Misses: " + misses);
        System.out.println("Hit rate: " + hitRate + "%");
        System.out.println("Miss rate: " + missRate + "%\n");
    }
}
