package com.zdroba.mpp.repository;

import com.zdroba.mpp.entity.Child;

import java.util.List;

public interface IChildRepository {

    List<Child> getAll();

    Child get(Long id);

    List<Child> getCheck(Long checkpoitId);

    void update(Child child);
}
