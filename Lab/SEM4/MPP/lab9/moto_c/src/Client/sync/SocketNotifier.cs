using moto_c.Common.sync;

namespace moto_c.sync;

using System;
using System.Collections.Generic;
using System.IO;
using System.Net.Sockets;
using System.Threading;

public class SocketNotifier
{
    private readonly int port;
    private readonly string serverHost;
    private readonly int serverPort;

    private readonly List<Action<string>> listeners = new();
    private StreamWriter? writer;
    private bool started = false;

    public SocketNotifier(int port, string serverHost, int serverPort)
    {
        this.port = port;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public void OnUpdate(Action<string> callback)
    {
        listeners.Add(callback);
    }

    public void Start()
    {
        if (started) return;
        started = true;

        new Thread(() =>
        {
            try
            {
                var socket = new TcpClient(serverHost, serverPort);
                var stream = socket.GetStream();

                writer = new StreamWriter(stream) { AutoFlush = true };

                Console.WriteLine($"[Port {port}] Connected to SyncServer at {serverHost}:{serverPort}");

                var reader = new StreamReader(stream);

                string? msg;
                while ((msg = reader.ReadLine()) != null)
                {
                    foreach (var listener in listeners)
                    {
                        listener(msg);
                    }
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        })
        {
            IsBackground = true
        }.Start();
    }

    public void NotifyPeer(string message)
    {
        if (writer != null)
        {
            Console.WriteLine($"[Port {port}] sending: {message}");
            writer.WriteLine(message);
        }
        else
        {
            Console.Error.WriteLine($"[Port {port}] Not connected to server yet");
        }
    }

    public int GetPort()
    {
        return port;
    }

    public void Respond(string requestType, ResponseType type, string message)
    {
        if (writer != null)
        {
            var response = new Response(type, requestType, message);
            writer.WriteLine(response.ToString());
        }
        else
        {
            Console.Error.WriteLine($"[Port {port}] Not connected to server yet, cannot respond");
        }
    }
}