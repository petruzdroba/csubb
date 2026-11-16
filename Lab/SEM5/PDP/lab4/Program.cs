using lab4.downloaders;
using System.Diagnostics;

class Program
{
    static void Main(string[] args)
    {
        if (args.Length < 2)
        {
            Console.WriteLine("Usage: HttpDownloader <mode> <url1> [url2] ...");
            return;
        }

        IDownloader downloader = args[0].ToLower() switch
        {
            "event" => new EventDrivenDownloader(),
            "continuations" => new TaskContinuationsDownloader(),
            "async" => new AsyncAwaitDownloader(),
            _ => throw new ArgumentException("Unknown implementation")
        };

        Directory.CreateDirectory("downloads");

        List<Task> downloads = new List<Task>();

        Stopwatch stopwatch = Stopwatch.StartNew();

        for (int i = 1; i < args.Length; i++)
        {
            Uri url = new Uri(args[i]);
            string fileName = Path.GetFileName(url.LocalPath);

            if (string.IsNullOrEmpty(fileName))
                fileName = $"download_{i}.html";

            string outputPath = Path.Combine("downloads", fileName);

            downloads.Add(downloader.DownloadAsync(url, outputPath));
        }

        try
        {
            Task.WaitAll(downloads.ToArray());
            stopwatch.Stop();

            Console.WriteLine();
            Console.WriteLine("All downloads completed.");
            Console.WriteLine($"Total time: {stopwatch.Elapsed.TotalSeconds:F2} seconds");
        }
        catch (AggregateException ex)
        {
            stopwatch.Stop();

            foreach (Exception error in ex.Flatten().InnerExceptions)
                Console.WriteLine(error.Message);

            Console.WriteLine($"Total time: {stopwatch.Elapsed.TotalSeconds:F2} seconds");
        }
    }
}
