package org.zdroba.repository;

import org.zdroba.entity.User;

import java.util.List;

public interface UserRepository {

    User find(Long id);

    User find(String email);

    List<User> getAll();

    void add(User user);
}
