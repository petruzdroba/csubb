package org.zdroba.service;

import org.zdroba.entity.User;
import org.zdroba.exceptions.AlreadyExistsException;
import org.zdroba.exceptions.InvalidPasswordException;
import org.zdroba.exceptions.NotFoundException;

public interface AuthService {
    User logIn(String email, String password) throws NotFoundException, InvalidPasswordException;

    User register(String email, String password) throws AlreadyExistsException;
}
