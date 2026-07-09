package com.zdroba.mpp.repository;

import com.zdroba.mpp.entity.User;

public interface IUserRepository {

    User find(String email);

    User find(Long id);

    Long save(User user);
}
