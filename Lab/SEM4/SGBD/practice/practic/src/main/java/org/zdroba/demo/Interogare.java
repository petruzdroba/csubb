package org.zdroba.demo;

import org.zdroba.cache.service;

public class Interogare {
    public static void main(String[] args) {

        service service = new service();

        System.out.println(service.getById(2L));
        System.out.println(service.getById(2L));
        System.out.println(service.getById(2L));
    }
}
