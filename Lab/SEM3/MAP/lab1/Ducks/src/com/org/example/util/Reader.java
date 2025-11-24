package com.org.example.util;

import com.org.example.containers.RaceTrackContainer;
import com.org.example.containers.DuckRaceContainer;
import com.org.example.domain.Duck;
import com.org.example.domain.Culoar;

import java.util.Scanner;

public class Reader {

    public RaceTrackContainer read() {
        Scanner sc = new Scanner(System.in);
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
    }
}
