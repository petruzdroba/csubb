namespace moto_c.exceptions;

public class InvalidPasswordException: Exception
{
    public  InvalidPasswordException(string msg) : base(msg)
    {
    }
}