using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.DependencyInjection;
using moto_c.Server.sync;

internal class Program
{
    public static void Main(string[] args)
    {
        var builder = WebApplication.CreateBuilder(args);
        builder.Services.AddGrpc();
        builder.WebHost.ConfigureKestrel(options =>
        {
            options.ListenAnyIP(6000, listenOptions =>
            {
                listenOptions.Protocols = Microsoft.AspNetCore.Server.Kestrel.Core.HttpProtocols.Http2;
            });
        });

        var app = builder.Build();
        app.MapGrpcService<SyncServiceImpl>();
        
        Console.WriteLine("gRPC SyncServer running on port 6000");
        app.Run();
    }
}
