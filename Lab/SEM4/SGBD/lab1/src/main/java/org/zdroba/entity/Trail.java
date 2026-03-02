package org.zdroba.entity;

import java.util.List;

public class Trail {
    private Long id;
    private String name;
    private double length;
    private Park park;
    private  List<Tag> tags;

    public Trail() {
    }

    public Trail(String name, double length, Park park, List<Tag> tags) {
        this.name = name;
        this.length = length;
        this.park = park;
        this.tags = tags;
    }

    public Trail(Long id, String name, double length, Park park, List<Tag> tags) {
        this.id = id;
        this.name = name;
        this.length = length;
        this.park = park;
        this.tags = tags;
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

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public Park getPark() {
        return park;
    }

    public void setPark(Park park) {
        this.park = park;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
