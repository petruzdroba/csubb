package org.zdroba.repository;

import org.zdroba.entity.Tag;

import java.util.List;

public class TagRepository implements ITagRepository{
    private static TagRepository instance;

    private TagRepository() {}

    public static TagRepository getInstance(){
        if(instance == null)
            instance = new TagRepository();
        return instance;
    }

    @Override
    public void add(Tag entity) {

    }

    @Override
    public void delete(Long key) {

    }

    @Override
    public void update(Long key, Tag entity) {

    }

    @Override
    public Tag find(Long key) {
        return null;
    }

    @Override
    public List<Tag> getAll() {
        return List.of();
    }

    @Override
    public List<Long> getKeys() {
        return List.of();
    }
}
