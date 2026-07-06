package org.zdroba.repository;

import org.zdroba.entity.User;

import java.util.List;

public interface UserRepository extends Repository<Long, User> {

    User find(String email);

    void add(User user);
}
