namespace moto_c.Common.sync;

public class Response
{
    public ResponseType type;
    public string requestType;
    public string message;

    public Response(ResponseType type, string requestType, string message)
    {
        this.type = type;
        this.requestType = requestType;
        this.message = message;
    }

    public ResponseType GetType()
    {
        return type;
    }

    public string GetRequestType()
    {
        return requestType;
    }

    public string GetMessage()
    {
        return message;
    }

    public override string ToString()
    {
        return "RESPONSE:" + type + "|" + requestType + "|" + (message ?? "");
    }

    public static Response FromString(string str)
    {
        if (!str.StartsWith("RESPONSE:"))
            throw new ArgumentException("Not a response string");

        string[] parts = str.Substring(9).Split('|', 3);

        ResponseType type = Enum.Parse<ResponseType>(parts[0]);
        string requestType = parts[1];
        string message = parts.Length > 2 ? parts[2] : null;

        return new Response(type, requestType, message);
    }
}