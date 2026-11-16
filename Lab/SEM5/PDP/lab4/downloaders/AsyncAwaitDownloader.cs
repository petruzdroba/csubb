using System;
using System.Globalization;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace lab4.downloaders;

public class AsyncAwaitDownloader : IDownloader
{
    public async Task DownloadAsync(Uri url, string outputPath)
    {
        if (url.Scheme != "http")
            throw new Exception("Only HTTP URLs are supported.");

        IPAddress[] addresses = await Dns.GetHostAddressesAsync(url.Host);
        IPAddress address = Array.Find(addresses,
            ip => ip.AddressFamily == AddressFamily.InterNetwork);

        if (address == null)
            throw new Exception("No IPv4 address found.");

        using Socket socket = new Socket(
            address.AddressFamily,
            SocketType.Stream,
            ProtocolType.Tcp);

        await ConnectAsync(socket, new IPEndPoint(address, url.Port));

        string host = url.IsDefaultPort
            ? url.Host
            : $"{url.Host}:{url.Port}";

        string request =
            $"GET {url.PathAndQuery} HTTP/1.1\r\n" +
            $"Host: {host}\r\n" +
            "Connection: close\r\n\r\n";

        byte[] requestBytes = Encoding.ASCII.GetBytes(request);

        await SendAllAsync(socket, requestBytes);

        byte[] response = await ReceiveResponseAsync(socket);

        SaveFile(response, outputPath);

        Console.WriteLine("Downloaded: " + outputPath);
    }

    private Task ConnectAsync(Socket socket, EndPoint endpoint)
    {
        TaskCompletionSource<bool> promise =
            new TaskCompletionSource<bool>();

        try
        {
            socket.BeginConnect(endpoint, ar =>
            {
                try
                {
                    socket.EndConnect(ar);
                    promise.SetResult(true);
                }
                catch (Exception ex)
                {
                    promise.SetException(ex);
                }
            }, null);
        }
        catch (Exception ex)
        {
            promise.SetException(ex);
        }

        return promise.Task;
    }

    private Task<int> SendAsync(Socket socket, byte[] data, int offset)
    {
        TaskCompletionSource<int> promise =
            new TaskCompletionSource<int>();

        try
        {
            socket.BeginSend(
                data,
                offset,
                data.Length - offset,
                SocketFlags.None,
                ar =>
                {
                    try
                    {
                        promise.SetResult(socket.EndSend(ar));
                    }
                    catch (Exception ex)
                    {
                        promise.SetException(ex);
                    }
                },
                null);
        }
        catch (Exception ex)
        {
            promise.SetException(ex);
        }

        return promise.Task;
    }

    private async Task SendAllAsync(Socket socket, byte[] data)
    {
        int offset = 0;

        while (offset < data.Length)
        {
            int sent = await SendAsync(socket, data, offset);

            if (sent == 0)
                throw new Exception("Connection closed while sending.");

            offset += sent;
        }
    }

    private Task<int> ReceiveAsync(Socket socket, byte[] buffer)
    {
        TaskCompletionSource<int> promise =
            new TaskCompletionSource<int>();

        try
        {
            socket.BeginReceive(
                buffer,
                0,
                buffer.Length,
                SocketFlags.None,
                ar =>
                {
                    try
                    {
                        promise.SetResult(socket.EndReceive(ar));
                    }
                    catch (Exception ex)
                    {
                        promise.SetException(ex);
                    }
                },
                null);
        }
        catch (Exception ex)
        {
            promise.SetException(ex);
        }

        return promise.Task;
    }

    private async Task<byte[]> ReceiveResponseAsync(Socket socket)
    {
        byte[] buffer = new byte[4096];

        using MemoryStream response = new MemoryStream();

        int headerEnd = -1;
        int contentLength = -1;

        while (true)
        {
            int received = await ReceiveAsync(socket, buffer);

            if (received == 0)
                throw new Exception(
                    "Connection closed before download completed.");

            response.Write(buffer, 0, received);

            byte[] data = response.ToArray();

            if (headerEnd == -1)
            {
                headerEnd = FindHeaderEnd(data);

                if (headerEnd != -1)
                    contentLength = ParseHeaders(data, headerEnd);
            }

            if (headerEnd != -1 &&
                data.Length - headerEnd >= contentLength)
            {
                return data;
            }
        }
    }

    private int FindHeaderEnd(byte[] data)
    {
        for (int i = 0; i <= data.Length - 4; i++)
        {
            if (data[i] == 13 && data[i + 1] == 10 &&
                data[i + 2] == 13 && data[i + 3] == 10)
                return i + 4;
        }

        if (data.Length > 65536)
            throw new Exception("HTTP headers are too large.");

        return -1;
    }

    private int ParseHeaders(byte[] data, int headerEnd)
    {
        string headers = Encoding.ASCII.GetString(
            data, 0, headerEnd - 4);

        string[] lines = headers.Split("\r\n");
        string[] status = lines[0].Split(' ');

        if (status.Length < 2 ||
            !int.TryParse(status[1], out int statusCode))
            throw new Exception("Invalid HTTP status line.");

        if (statusCode < 200 || statusCode >= 300)
            throw new Exception($"HTTP error: {statusCode}");

        int contentLength = -1;

        foreach (string line in lines)
        {
            if (line.StartsWith("Content-Length:",
                StringComparison.OrdinalIgnoreCase))
            {
                string value = line.Substring(
                    "Content-Length:".Length).Trim();

                if (!int.TryParse(value,
                    NumberStyles.None,
                    CultureInfo.InvariantCulture,
                    out contentLength))
                    throw new Exception("Invalid Content-Length.");
            }

            if (line.StartsWith("Transfer-Encoding:",
                StringComparison.OrdinalIgnoreCase) &&
                line.Contains("chunked",
                    StringComparison.OrdinalIgnoreCase))
                throw new Exception(
                    "Chunked transfer encoding is not supported.");
        }

        if (contentLength < 0)
            throw new Exception("Missing Content-Length header.");

        return contentLength;
    }

    private void SaveFile(byte[] data, string outputPath)
    {
        int headerEnd = FindHeaderEnd(data);
        int contentLength = ParseHeaders(data, headerEnd);

        string directory = Path.GetDirectoryName(
            Path.GetFullPath(outputPath));

        Directory.CreateDirectory(directory);

        using FileStream file = File.Create(outputPath);

        file.Write(data, headerEnd, contentLength);
    }
}
