package org.zdroba.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="trails")
public class Trail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double length;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="park_id")
    private Park park;
    @ManyToMany(fetch =  FetchType.EAGER)
    @JoinTable(
            name="trail_tags",
            joinColumns = @JoinColumn(name="trail_id"),
            inverseJoinColumns = @JoinColumn(name="tag_id")
    )
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
