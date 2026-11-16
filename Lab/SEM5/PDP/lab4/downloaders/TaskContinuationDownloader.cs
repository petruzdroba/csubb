
using System;
using System.Globalization;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace lab4.downloaders;

public class TaskContinuationsDownloader : IDownloader
{
    public Task DownloadAsync(Uri url, string outputPath)
    {
        DownloadSession session = new DownloadSession(url, outputPath);
        return session.Start();
    }

    private class DownloadSession
    {
        private readonly Uri _url;
        private readonly string _outputPath;
        private readonly Socket _socket;
        private readonly byte[] _buffer;
        private readonly MemoryStream _response;

        private byte[] _request;
        private int _headerEnd = -1;
        private int _contentLength = -1;

        public DownloadSession(Uri url, string outputPath)
        {
            _url = url;
            _outputPath = outputPath;
            _socket = new Socket(
                AddressFamily.InterNetwork,
                SocketType.Stream,
                ProtocolType.Tcp);

            _buffer = new byte[4096];
            _response = new MemoryStream();
        }

        public Task Start()
        {
            if (_url.Scheme != "http")
                return Task.FromException(
                    new Exception("Only HTTP URLs are supported."));

            IPAddress[] addresses = Dns.GetHostAddresses(_url.Host);
            IPAddress address = Array.Find(addresses,
                ip => ip.AddressFamily == AddressFamily.InterNetwork);

            if (address == null)
                return Task.FromException(
                    new Exception("No IPv4 address found."));

            return Connect(address)
                .ContinueWith((Task t) =>
                {
                    t.GetAwaiter().GetResult();
                    PrepareRequest();
                    return SendAll(0);
                }).Unwrap()
                .ContinueWith((Task t) =>
                {
                    t.GetAwaiter().GetResult();
                    return ReceiveResponse();
                }).Unwrap()
                .ContinueWith((Task<byte[]> t) =>
                {
                    try
                    {
                        byte[] data = t.GetAwaiter().GetResult();
                        SaveFile(data);
                        Console.WriteLine("Downloaded: " + _outputPath);
                    }
                    finally
                    {
                        _socket.Close();
                    }
                });
        }

        private Task Connect(IPAddress address)
        {
            TaskCompletionSource<bool> promise =
                new TaskCompletionSource<bool>(
                    TaskCreationOptions.RunContinuationsAsynchronously);

            try
            {
                _socket.BeginConnect(
                    new IPEndPoint(address, _url.Port),
                    (IAsyncResult ar) =>
                    {
                        try
                        {
                            _socket.EndConnect(ar);
                            promise.SetResult(true);
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

        private void PrepareRequest()
        {
            string host = _url.IsDefaultPort
                ? _url.Host
                : $"{_url.Host}:{_url.Port}";

            string request =
                $"GET {_url.PathAndQuery} HTTP/1.1\r\n" +
                $"Host: {host}\r\n" +
                "Connection: close\r\n\r\n";

            _request = Encoding.ASCII.GetBytes(request);
        }

        private Task<int> Send(int offset)
        {
            TaskCompletionSource<int> promise = new TaskCompletionSource<int>();

            try
            {
                _socket.BeginSend(
                    _request,
                    offset,
                    _request.Length - offset,
                    SocketFlags.None,
                    ar =>
                    {
                        try
                        {
                            int sent = _socket.EndSend(ar);
                            promise.SetResult(sent);
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

        private Task SendAll(int offset)
        {
            Task<int> sendTask = Send(offset);

            Task<Task> continuation = sendTask.ContinueWith<Task>(
                t =>
                {
                    int sent = t.Result;

                    if (sent == 0)
                        throw new Exception("Connection closed while sending.");

                    int nextOffset = offset + sent;

                    if (nextOffset < _request.Length)
                        return SendAll(nextOffset);

                    return Task.CompletedTask;
                },
                TaskScheduler.Default
            );

            return continuation.Unwrap();
        }

        private Task<int> Receive()
        {
            TaskCompletionSource<int> promise =
                new TaskCompletionSource<int>(
                    TaskCreationOptions.RunContinuationsAsynchronously);

            try
            {
                _socket.BeginReceive(
                    _buffer,
                    0,
                    _buffer.Length,
                    SocketFlags.None,
                    (IAsyncResult ar) =>
                    {
                        try
                        {
                            promise.SetResult(_socket.EndReceive(ar));
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

        private Task<byte[]> ReceiveResponse()
        {
            return Receive().ContinueWith((Task<int> t) =>
            {
                int received = t.GetAwaiter().GetResult();

                if (received == 0)
                    throw new Exception(
                        "Connection closed before download completed.");

                _response.Write(_buffer, 0, received);

                if (_headerEnd == -1)
                    ParseHeaders();

                if (_headerEnd != -1)
                {
                    byte[] data = _response.ToArray();
                    int bodyReceived = data.Length - _headerEnd;

                    if (bodyReceived >= _contentLength)
                        return Task.FromResult(data);
                }

                return ReceiveResponse();
            }).Unwrap();
        }

        private void ParseHeaders()
        {
            byte[] data = _response.ToArray();

            if (data.Length > 65536)
                throw new Exception("HTTP headers are too large.");

            int index = -1;

            for (int i = 0; i <= data.Length - 4; i++)
            {
                if (data[i] == 13 && data[i + 1] == 10 &&
                    data[i + 2] == 13 && data[i + 3] == 10)
                {
                    index = i;
                    break;
                }
            }

            if (index == -1)
                return;

            _headerEnd = index + 4;

            string headers = Encoding.ASCII.GetString(data, 0, index);
            string[] lines = headers.Split("\r\n");
            string[] status = lines[0].Split(' ');

            if (status.Length < 2 ||
                !int.TryParse(status[1], out int statusCode))
                throw new Exception("Invalid HTTP status line.");

            if (statusCode < 200 || statusCode >= 300)
                throw new Exception($"HTTP error: {statusCode}");

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
                        out _contentLength))
                        throw new Exception("Invalid Content-Length.");
                }

                if (line.StartsWith("Transfer-Encoding:",
                    StringComparison.OrdinalIgnoreCase) &&
                    line.Contains("chunked",
                        StringComparison.OrdinalIgnoreCase))
                    throw new Exception(
                        "Chunked transfer encoding is not supported.");
            }

            if (_contentLength < 0)
                throw new Exception("Missing Content-Length header.");
        }

        private void SaveFile(byte[] data)
        {
            string directory = Path.GetDirectoryName(
                Path.GetFullPath(_outputPath));

            Directory.CreateDirectory(directory);

            using (FileStream file = File.Create(_outputPath))
            {
                file.Write(data, _headerEnd, _contentLength);
            }
        }
    }
}
