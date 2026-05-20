using System;
using Avalonia;
using moto_c.sync;

namespace moto_c;

internal class Program
{
    public static void Main(string[] args)
    {
        if (args.Length == 0)
        {
            Console.Error.WriteLine("Usage: moto_c <clientPort>");
            Environment.Exit(1);
        }

        int clientPort = int.Parse(args[0]);

        AppConfig.ClientPort = clientPort;

        BuildAvaloniaApp()
            .StartWithClassicDesktopLifetime(args);
    }

    public static AppBuilder BuildAvaloniaApp()
        => AppBuilder.Configure<App>()
            .UsePlatformDetect()
            .LogToTrace();
}