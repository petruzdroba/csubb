package main.java.com.containers;


import main.java.com.domain.Culoar;
import main.java.com.domain.Duck;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class DuckRaceContainer {

    private final Collection<Duck> racers;
    private final Collection<Culoar> culoare;

    public DuckRaceContainer(Collection<Duck> racers, Collection<Culoar> culoare) {
        this.racers = new ArrayList<>(racers);
        this.culoare = new ArrayList<>(culoare);
        sortByResistance();
    }

    public Collection<Duck> getDucks() {
        return new ArrayList<>(racers);
    }

    public int getRacerCount() {
        return racers.size();
    }

    public Collection<Culoar> getCuloare() {
        return new ArrayList<>(culoare);
    }

    public int getTrackCount() {
        return culoare.size();
    }

    private void sortByResistance() {
        ArrayList<Duck> sorted = new ArrayList<>(racers);
        sorted.sort(Comparator.comparingDouble(Duck::getRezistenta).reversed());
        racers.clear();
        racers.addAll(sorted);
    }
}
