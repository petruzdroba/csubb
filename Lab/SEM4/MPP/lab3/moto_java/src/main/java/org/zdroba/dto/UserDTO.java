package org.zdroba.dto;

import org.zdroba.entity.User;

import java.util.List;

public interface UserDTO {

    User find(Long id);

    User find(String email);

    List<User> getAll();

    void add(User user);
}
