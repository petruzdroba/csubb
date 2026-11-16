using System;
using System.Threading.Tasks;
namespace lab4.downloaders;

public interface IDownloader
{
    Task DownloadAsync(Uri url, string outputPath);
}