
using System;
using System.Globalization;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace lab4.downloaders;

public class EventDrivenDownloader : IDownloader
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
        private readonly TaskCompletionSource<bool> _completion;

        private Socket _socket;
        private FileStream _file;
        private byte[] _buffer;
        private byte[] _request;
        private int _headerEnd = -1;
        private int _contentLength = -1;
        private int _receivedBody = 0;

        public DownloadSession(Uri url, string outputPath)
        {
            _url = url;
            _outputPath = outputPath;
            _buffer = new byte[4096];

            _completion = new TaskCompletionSource<bool>(
                TaskCreationOptions.RunContinuationsAsynchronously);
        }

        public Task Start()
        {
            try
            {
                if (_url.Scheme != "http")
                    throw new Exception("Only HTTP URLs are supported.");

                IPAddress[] addresses = Dns.GetHostAddresses(_url.Host);
                IPAddress address = Array.Find(addresses,
                    ip => ip.AddressFamily == AddressFamily.InterNetwork);

                if (address == null)
                    throw new Exception("No IPv4 address found.");

                _socket = new Socket(address.AddressFamily,
                    SocketType.Stream, ProtocolType.Tcp);

                _socket.BeginConnect(
                    new IPEndPoint(address, _url.Port),
                    OnConnected,
                    null);
            }
            catch (Exception ex)
            {
                Fail(ex);
            }

            return _completion.Task;
        }

        private void OnConnected(IAsyncResult ar)
        {
            try
            {
                _socket.EndConnect(ar);

                string host = _url.IsDefaultPort
                    ? _url.Host
                    : $"{_url.Host}:{_url.Port}";

                string request =
                    $"GET {_url.PathAndQuery} HTTP/1.1\r\n" +
                    $"Host: {host}\r\n" +
                    "Connection: close\r\n\r\n";

                _request = Encoding.ASCII.GetBytes(request);

                SendRequest(0);
            }
            catch (Exception ex)
            {
                Fail(ex);
            }
        }

        private void SendRequest(int offset)
        {
            _socket.BeginSend(
                _request,
                offset,
                _request.Length - offset,
                SocketFlags.None,
                OnSent,
                offset);
        }

        private void OnSent(IAsyncResult ar)
        {
            try
            {
                int offset = (int)ar.AsyncState;
                int sent = _socket.EndSend(ar);

                if (sent == 0)
                    throw new Exception("Connection closed while sending.");

                offset += sent;

                if (offset < _request.Length)
                {
                    SendRequest(offset);
                    return;
                }

                ReceiveData();
            }
            catch (Exception ex)
            {
                Fail(ex);
            }
        }

        private void ReceiveData()
        {
            _socket.BeginReceive(
                _buffer,
                0,
                _buffer.Length,
                SocketFlags.None,
                OnReceived,
                null);
        }

        private void OnReceived(IAsyncResult ar)
        {
            try
            {
                int received = _socket.EndReceive(ar);

                if (received == 0)
                    throw new Exception(
                        "Connection closed before download completed.");

                int bodyOffset = 0;

                if (_headerEnd == -1)
                {
                    _headerBuffer.Write(_buffer, 0, received);

                    byte[] headers = _headerBuffer.ToArray();

                    int index = FindHeaderEnd(headers);

                    if (index == -1)
                    {
                        if (headers.Length > 65536)
                            throw new Exception("HTTP headers are too large.");

                        ReceiveData();
                        return;
                    }

                    _headerEnd = index + 4;
                    ParseHeaders(headers, index);

                    string directory = Path.GetDirectoryName(
                        Path.GetFullPath(_outputPath));

                    Directory.CreateDirectory(directory);

                    _file = File.Create(_outputPath);

                    int bodyLength = headers.Length - _headerEnd;

                    if (bodyLength > 0)
                    {
                        _file.Write(headers, _headerEnd, bodyLength);
                        _receivedBody += bodyLength;
                    }
                }
                else
                {
                    _file.Write(_buffer, 0, received);
                    _receivedBody += received;
                }

                if (_receivedBody >= _contentLength)
                {
                    _file.Dispose();
                    _file = null;

                    Console.WriteLine("Downloaded: " + _outputPath);

                    _socket.Close();
                    _completion.TrySetResult(true);
                    return;
                }

                ReceiveData();
            }
            catch (Exception ex)
            {
                Fail(ex);
            }
        }

        private readonly MemoryStream _headerBuffer = new MemoryStream();

        private int FindHeaderEnd(byte[] data)
        {
            for (int i = 0; i <= data.Length - 4; i++)
            {
                if (data[i] == 13 && data[i + 1] == 10 &&
                    data[i + 2] == 13 && data[i + 3] == 10)
                    return i;
            }

            return -1;
        }

        private void ParseHeaders(byte[] data, int index)
        {
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

        private void Fail(Exception ex)
        {
            Console.WriteLine(
                $"Download failed ({_url}): {ex.Message}");

            _file?.Dispose();
            _socket?.Close();

            _completion.TrySetException(ex);
        }
    }
}
