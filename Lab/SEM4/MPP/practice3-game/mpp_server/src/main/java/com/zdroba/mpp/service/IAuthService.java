package com.zdroba.mpp.service;

import com.zdroba.mpp.entity.User;
import com.zdroba.mpp.exceptions.AlreadyExistsException;
import com.zdroba.mpp.exceptions.InvalidPasswordException;
import com.zdroba.mpp.exceptions.NotFoundException;

public interface IAuthService {

    User login(String email, String password) throws NotFoundException, InvalidPasswordException, IllegalArgumentException;

    User register(String email, String name, String password) throws AlreadyExistsException, IllegalArgumentException;
}
