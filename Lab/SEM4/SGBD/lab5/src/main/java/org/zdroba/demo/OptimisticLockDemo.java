package org.zdroba.demo;

import jakarta.persistence.*;
import org.zdroba.JPAUtil;
import org.zdroba.entity.Employee;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class OptimisticLockDemo {

    private static final EntityManagerFactory emf = JPAUtil.getEntityManagerFactory();

    public static void main(String[] args) {
        runDemo();
    }

    public static void runDemo() {

        CountDownLatch startLatch = new CountDownLatch(1);

        Thread threadA = new Thread(() -> {
            try (EntityManager emA = emf.createEntityManager()) {

                Employee a = emA.find(Employee.class, 1L);
                System.out.println("A loaded version: " + a.getVersion());

                startLatch.await();

                emA.getTransaction().begin();
                a.setSalary(a.getSalary().add(new BigDecimal(1000)));

                Thread.sleep(1000);

                emA.getTransaction().commit();

                System.out.println("A committed update");

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Thread threadB = new Thread(() -> {

            Employee b;

            try (EntityManager emB = emf.createEntityManager()) {

                b = emB.find(Employee.class, 1L);
                System.out.println("B loaded version: " + b.getVersion());

                startLatch.await();

                Thread.sleep(1500);

                emB.getTransaction().begin();
                b.setSalary(b.getSalary().add(new BigDecimal(2000)));

                System.out.println("B attempting stale update");

                emB.getTransaction().commit();

                System.out.println("B committed (unexpected)");

            } catch (OptimisticLockException | RollbackException ex) {

                System.out.println("\nCONFLICT DETECTED");

                printEmployee("\nSTATE AFTER CONFLICT", 1L);

                System.out.println("1. Reload data");
                System.out.println("2. Force update");
                System.out.println("3. Cancel");

                System.out.println("\n>>>");
                Scanner scanner = new Scanner(System.in);
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> reload();
                    case 2 -> forceUpdate(1L);
                    case 3 -> System.out.println("Update cancelled");
                }
                printEmployee("FINAL STATE", 1L);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        threadA.start();
        threadB.start();

        startLatch.countDown();

        try {
            threadA.join();
            threadB.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void reload() {
        try (EntityManager em = emf.createEntityManager()) {

            Employee fresh = em.find(Employee.class, 1L);
            System.out.println("Reloaded version: " + fresh.getVersion());

            em.getTransaction().begin();
            fresh.setSalary(fresh.getSalary().add(new BigDecimal(2000)));
            em.getTransaction().commit();

            System.out.println("Reload + retry successful");
        }
    }

    private static void forceUpdate(Long id) {
        try (EntityManager em = emf.createEntityManager()) {

            em.getTransaction().begin();

            em.createQuery("""
                UPDATE Employee e
                SET e.salary = e.salary + 2000
                WHERE e.id = :id
            """)
                    .setParameter("id", id)
                    .executeUpdate();

            em.getTransaction().commit();

            System.out.println("Force update applied (version ignored)");
        }
    }

    private static void printEmployee(String label, Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            Employee e = em.find(Employee.class, id);
            System.out.println(label +
                    " | salary=" + e.getSalary() +
                    " | version=" + e.getVersion());
        }
    }
}