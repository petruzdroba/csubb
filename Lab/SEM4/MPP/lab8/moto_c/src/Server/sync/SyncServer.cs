namespace moto_c.Server.sync;

using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Threading;

public class SyncServer
{
    private static SyncServer? instance;
    private readonly int port;

    private readonly List<StreamWriter> clients = new();

    private SyncServer(int port)
    {
        this.port = port;
    }

    public static SyncServer GetInstance(int port)
    {
        if (instance == null)
        {
            instance = new SyncServer(port);
        }
        return instance;
    }

    public void Start()
    {
        new Thread(() =>
        {
            try
            {
                var server = new TcpListener(IPAddress.Any, port);
                server.Start();

                Console.WriteLine($"SyncServer started on port {port}");

                while (true)
                {
                    var socket = server.AcceptSocket();
                    var stream = new NetworkStream(socket);

                    var writer = new StreamWriter(stream) { AutoFlush = true };
                    var reader = new StreamReader(stream);

                    lock (clients)
                    {
                        clients.Add(writer);
                    }

                    Console.WriteLine($"Client connected. Total: {clients.Count}");
                    Console.Out.Flush();

                    new Thread(() =>
                    {
                        try
                        {
                            string? msg;
                            while ((msg = reader.ReadLine()) != null)
                            {
                                Broadcast(msg, writer);
                            }
                        }
                        catch (Exception e)
                        {
                            Console.WriteLine(e);
                        }
                        finally
                        {
                            lock (clients)
                            {
                                clients.Remove(writer);
                            }
                        }
                    })
                    { IsBackground = true }.Start();
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        })
        { IsBackground = true }.Start();
    }

    private void Broadcast(string message, StreamWriter sender)
    {
        lock (clients)
        {
            foreach (var client in clients)
            {
                if (client != sender)
                {
                    client.WriteLine(message);
                }
            }
        }
    }
}