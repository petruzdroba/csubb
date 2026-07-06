
using moto_c.Server.sync;

internal class Program
{
    public static void Main(string[] args)
    {
        SyncServer.GetInstance(6000).Start();
        Console.WriteLine("Server running on 6000");
        Thread.CurrentThread.Join();
    }
}
