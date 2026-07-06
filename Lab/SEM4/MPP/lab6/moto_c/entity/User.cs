namespace moto_c.entity;

public class User
{
    public long id { get; set; }
    public string email { get; set; }
    public string password { get; }

    public User()
    {
    }

    public User(string email, string password)
    {
        this.email = email;
        this.password = password;
    }

    public bool checkPw(string password)
    {
        return this.password == password;  
    }
}