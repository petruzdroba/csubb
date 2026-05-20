namespace moto_c.entity;

public class User
{
    private long id { get; set; }
    private string email { get; set; }
    private string password ;

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