package org.zdroba.demo;

import org.zdroba.cache.DepartmentService;
import org.zdroba.entity.Department;

import java.util.Random;

public class CacheDemo {
    public static void main(String[] args) throws InterruptedException {

        DepartmentService service = new DepartmentService();

        Department department = new Department();
        department.setName("CACHE 1");

        Integer key = service.save(department);

        System.out.println("First query (MISS):");
        long start1 = System.nanoTime();
        service.getById(key);
        long end1 = System.nanoTime();
        System.out.println("Time: " + (end1 - start1) / 1_000_000.0 + " ms\n");

        System.out.println("Second query (HIT):");
        long start2 = System.nanoTime();
        service.getById(key);
        long end2 = System.nanoTime();
        System.out.println("Time: " + (end2 - start2) / 1_000_000.0 + " ms\n");

        System.out.println("Update:");
        Department newDept = new Department();
        newDept.setId(key);
        newDept.setName("Cache 2");
        service.update(newDept);

        System.out.println("After update (MISS):");
        long start3 = System.nanoTime();
        service.getById(key);
        long end3 = System.nanoTime();
        System.out.println("Time: " + (end3 - start3) / 1_000_000.0 + " ms\n");

        System.out.println("Waiting for TTL...");
        Thread.sleep(14000);

        System.out.println("After TTL (MISS):");
        long start4 = System.nanoTime();
        service.getById(key);
        long end4 = System.nanoTime();
        System.out.println("Time: " + (end4 - start4) / 1_000_000.0 + " ms\n");

        service.delete(key);

        benchmark(service);
    }

    static void benchmark(DepartmentService service){
        Random random = new Random();

        for( int i=0; i<100; ++i){
            int id = 1 + random.nextInt(10);
            service.getById(id);
        }

        service.printCacheStats();
    }
}