using System;
using System.Net.Http;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace Client.rest;

public class RacesClient
{
    private const string BASE = "http://localhost:8080/races";

    private static readonly HttpClient HTTP = new();

    public static async Task Main()
    {
        var created = await Post("8");
        Console.WriteLine(created);

        var id = ExtractId(created);

        Console.WriteLine(await Get(""));
        Console.WriteLine(await Get("/" + id));
        Console.WriteLine(await Put("/" + id, "16"));
        Console.WriteLine(await Delete("/" + id));
    }

    static string ExtractId(string json)
    {
        var m = Regex.Match(json, "\"id\"\\s*:\\s*(\\d+)");

        if (m.Success)
            return m.Groups[1].Value;

        throw new Exception("No id in response");
    }

    static async Task<string> Get(string path)
    {
        var req = new HttpRequestMessage(
            HttpMethod.Get,
            BASE + path
        );

        req.Headers.Add("Accept", "application/json");

        return await Send(req, null);
    }

    static async Task<string> Post(string body)
    {
        var req = new HttpRequestMessage(
            HttpMethod.Post,
            BASE
        );

        req.Content = new StringContent(
            body,
            Encoding.UTF8,
            "application/json"
        );

        return await Send(req, body);
    }

    static async Task<string> Put(string path, string body)
    {
        var req = new HttpRequestMessage(
            HttpMethod.Put,
            BASE + path
        );

        req.Content = new StringContent(
            body,
            Encoding.UTF8,
            "application/json"
        );

        return await Send(req, body);
    }

    static async Task<string> Delete(string path)
    {
        var req = new HttpRequestMessage(
            HttpMethod.Delete,
            BASE + path
        );

        return await Send(req, null);
    }

    static async Task<string> Send(
        HttpRequestMessage req,
        string? body)
    {
        var res = await HTTP.SendAsync(req);

        return await LoggingInterceptor.Intercept(
            req,
            res,
            body
        );
    }
}