namespace moto_c.Server.sync;

using Grpc.Core;
using System.Collections.Concurrent;

public class SyncServiceImpl : SyncService.SyncServiceBase
{
    private static readonly ConcurrentDictionary<string, IServerStreamWriter<SyncMessage>> Clients = new();

    public override async Task Subscribe(SubscribeRequest request,
        IServerStreamWriter<SyncMessage> responseStream,
        ServerCallContext context)
    {
        var clientId = request.ClientId;
        Clients[clientId] = responseStream;
        Console.WriteLine($"Client subscribed: {clientId}. Total: {Clients.Count}");

        // stream alive while connected
        try
        {
            await Task.Delay(Timeout.Infinite, context.CancellationToken);
        }
        catch (OperationCanceledException)
        {
            Clients.TryRemove(clientId, out _);
            Console.WriteLine($"Client disconnected: {clientId}. Total: {Clients.Count}");
        }
    }

    public override async Task<NotifyAck> Notify(SyncMessage request, ServerCallContext context)
    {
        var senderId = context.RequestHeaders.GetValue("client-id") ?? "";
        Console.WriteLine($"Broadcasting: {request.Payload}");

        var tasks = Clients
            .Where(kv => kv.Key != senderId)
            .Select(async kv =>
            {
                try { await kv.Value.WriteAsync(request); }
                catch { Clients.TryRemove(kv.Key, out _); }
            });

        await Task.WhenAll(tasks);
        return new NotifyAck { Ok = true };
    }
}
