using moto_c.entity;

namespace moto_c.repository;

public interface UserRepository: Repository<long, User>
{
    User? find(string email);

    void add(User user);
}