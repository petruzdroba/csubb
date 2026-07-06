using moto_c.entity;
using moto_c.exceptions;
using moto_c.repository;
using BCrypt.Net;   

namespace moto_c.service;

public class AuthServiceImpl:AuthService
{
    private UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public User? logIn(string email, string password)
    {
        User? user = userRepository.find(email);

        if (user == null)
            throw new NotFoundException("User not found");
        
        if(!BCrypt.Net.BCrypt.Verify(password, user.password))
            throw new InvalidPasswordException("Password is incorrect");

        return user;
    }

    public User? register(string email, string password)
    {
        User? user = userRepository.find(email);

        if (user != null)
            throw new AlreadyExistsException("User with this email already exists");

        user = new User(email, BCrypt.Net.BCrypt.HashPassword(password));
        userRepository.add(user);

        return user;
    }
}