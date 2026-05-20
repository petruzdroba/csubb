using moto_c.entity;

namespace moto_c.service;

public interface AuthService
{
    User? logIn(string email, string password);

    User? register(string email, string password);
}