namespace moto_c.Common.sync;

public class Request
{
    public RequestType type;
    public string message;

    public Request(RequestType type, string message)
    {
        this.type = type;
        this.message = message;
    }

    public RequestType GetType()
    {
        return type;
    }

    public string GetMessage()
    {
        return message;
    }

    public override string ToString()
    {
        return type + "|" + message;
    }

    public static Request FromString(string str)
    {
        string[] parts = str.Split('|', 2);
        if (parts.Length != 2)
            throw new ArgumentException("Invalid request: " + str);

        return new Request(
            Enum.Parse<RequestType>(parts[0]),
            parts[1]
        );
    }
}