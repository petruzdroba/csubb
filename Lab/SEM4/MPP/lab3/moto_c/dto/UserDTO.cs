using moto_c.entity;

namespace moto_c.dto;

public interface UserDTO
{
    User find(long id);

    User find(string email);

    List<User> getAll();

    void add(User user);
}