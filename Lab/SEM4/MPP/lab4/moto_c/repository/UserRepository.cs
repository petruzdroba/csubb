using moto_c.entity;

namespace moto_c.repository;

public interface UserRepository
{
    User? find(long id);

    User? find(string email);

    List<User> getAll();

    void add(User user);
}