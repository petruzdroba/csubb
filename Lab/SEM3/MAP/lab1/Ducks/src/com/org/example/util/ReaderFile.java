package com.org.example.util;

import com.org.example.containers.DuckRaceContainer;
import com.org.example.containers.RaceTrackContainer;
import com.org.example.domain.Culoar;
import com.org.example.domain.Duck;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReaderFile  extends Reader{
    private final String filePath;

    public ReaderFile(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public RaceTrackContainer read(){
        try {
            Scanner sc = new Scanner(new File(filePath));
            int N = sc.nextInt();
            int M = sc.nextInt();

            Duck[] ducks = new Duck[N];
            for (int i = 0; i < N; i++) {
                int speed = sc.nextInt();
                int resistance = sc.nextInt();
                ducks[i] = new Duck(resistance, speed, i + 1);
            }

            Culoar[] tracks = new Culoar[M];
            for (int i = 0; i < M; i++) {
                int dist = sc.nextInt();
                tracks[i] = new Culoar(dist, i + 1);
            }

            return new DuckRaceContainer(N, M, ducks, tracks);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + filePath, e);
        }
    }
}
