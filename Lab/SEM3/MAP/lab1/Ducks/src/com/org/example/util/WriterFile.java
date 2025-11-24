package com.org.example.util;

import com.org.example.domain.Racer;
import com.org.example.logic.OptimiserResult;

import java.io.FileWriter;
import java.io.IOException;

public class WriterFile extends Writer{
    private final String filePath;

    public WriterFile(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void write(OptimiserResult result) {
        try{
            FileWriter out = new FileWriter(filePath);
            out.write(String.format("%.4f\n", result.getBestTime()));
            for (int i = 0; i < result.getBestAssignment().length; i++) {
                int racerIdx = result.getBestAssignment()[i] + 1;
                int trackIdx = i + 1;
                out.write(String.format( "Track %d -> Racer %s%n", trackIdx, racerIdx));

            }
            out.close();
        }catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }
}
