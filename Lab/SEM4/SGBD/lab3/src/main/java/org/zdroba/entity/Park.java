package org.zdroba.entity;

import jakarta.persistence.*;

@Entity
@Table(name="parks")
public class Park {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String county;

    public Park() {
    }

    public Park(String name, String county) {
        this.name = name;
        this.county = county;
    }

    public Park(Long id, String name, String county) {
        this.id = id;
        this.name = name;
        this.county = county;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }
}
