package com.zdroba.mpp.repository;

import com.zdroba.mpp.entity.Child;
import com.zdroba.mpp.utils.JpaUtil;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChildRepository implements IChildRepository{

    private final JpaUtil jpaUtil;

    public ChildRepository(JpaUtil jpaUtil) {
        this.jpaUtil = jpaUtil;
    }

    @Override
    public List<Child> getAll() {
         try(EntityManager em = jpaUtil.em()){
            return em.createQuery("SELECT c FROM Child c ORDER BY c.hour DESC ").getResultList();
         }catch (Exception e){
             throw new RuntimeException(e.getMessage());
         }
    }

    @Override
    public Child get(Long id) {
        try(EntityManager em = jpaUtil.em()){
            return em.find(Child.class, id);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Child> getCheck(Long checkpoitId) {
        try(EntityManager em = jpaUtil.em()){
            return em.createQuery("SELECT c FROM Child c WHERE c.checkpointId = :check ORDER BY c.hour DESC ")
                    .setParameter("check", checkpoitId)
                    .getResultList();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void update(Child child) {
        try(EntityManager em = jpaUtil.em()){
            em.getTransaction().begin();
            em.merge(child);
            em.getTransaction().commit();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
