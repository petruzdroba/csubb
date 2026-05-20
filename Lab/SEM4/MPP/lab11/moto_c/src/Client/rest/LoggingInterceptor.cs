using System.Net.Http;
using System.Text;
using System.Threading.Tasks;

namespace Client.rest;

public static class LoggingInterceptor
{
    public static async Task<string> Intercept(
        HttpRequestMessage req,
        HttpResponseMessage res,
        string? body)
    {
        var outp = new StringBuilder();

        outp.AppendLine($"{req.Method} {req.RequestUri}");

        foreach (var h in req.Headers)
        {
            outp.AppendLine($"{h.Key}: {string.Join(", ", h.Value)}");
        }

        if (req.Content != null)
        {
            foreach (var h in req.Content.Headers)
            {
                outp.AppendLine($"{h.Key}: {string.Join(", ", h.Value)}");
            }
        }

        if (!string.IsNullOrWhiteSpace(body))
        {
            outp.AppendLine();
            outp.AppendLine("Request Body:");
            outp.AppendLine(body);
        }

        outp.AppendLine();
        outp.AppendLine("Sending request...");
        outp.AppendLine();

        outp.AppendLine("Response Status:");
        outp.AppendLine(((int)res.StatusCode).ToString());

        outp.AppendLine();

        outp.AppendLine("Response Body:");
        outp.AppendLine(await res.Content.ReadAsStringAsync());

        return outp.ToString();
    }
}