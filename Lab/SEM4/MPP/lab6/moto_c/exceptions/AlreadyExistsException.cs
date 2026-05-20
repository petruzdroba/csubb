namespace moto_c.exceptions;

public class AlreadyExistsException: Exception
{
    public  AlreadyExistsException(string msg) : base(msg)
    {
    }
}