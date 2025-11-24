import com.org.example.containers.RaceTrackContainer;
import com.org.example.logic.BacktrackOptimiser;
import com.org.example.logic.Optimiser;
import com.org.example.util.Reader;
import com.org.example.util.ReaderFile;
import com.org.example.util.Writer;
import com.org.example.util.WriterFile;

public class Main{
    public static void main(String[] args){
        Reader read = new ReaderFile("natatie.in");
        RaceTrackContainer race = read.read();

        Optimiser optimiser = new BacktrackOptimiser(race);

        Writer write = new WriterFile("natatie.out");
        write.write(optimiser.findMinimumTime());
    }
}