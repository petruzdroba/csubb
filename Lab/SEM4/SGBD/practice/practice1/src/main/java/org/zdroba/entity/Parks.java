package org.zdroba.entity;

import jakarta.persistence.*;

@Entity
@Table(name="parks")
public class Parks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="country", nullable = false)
    private String country;

    @Column(name="name")
    private String name;

    @Column(name="size")
    private Long size;


    public Parks() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
