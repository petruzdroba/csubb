package org.example.service;

import org.example.domain.*;
import org.example.repo.EvenimentRepo;
import org.example.repo.MeciRepo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EvenimentService implements Observable {
    private final EvenimentRepo evenimentRepo;
    private final MeciRepo meciRepo;

    List<Observer> observer = new ArrayList<>();

    public EvenimentService(EvenimentRepo evenimentRepo, MeciRepo meciRepo) {
        this.evenimentRepo = evenimentRepo;
        this.meciRepo = meciRepo;
    }

    public List<Eveniment> get(Long key){
        return evenimentRepo.getAll(key);
    }

    public void add(Long matchId, Team team, int rata, Action action) throws SQLException {
        evenimentRepo.add(new Eveniment(matchId, team, rata, action));

        if(action.equals(Action.GOL)){
            Meci meci = meciRepo.find(matchId);
            if(team.equals(Team.GAZDA))
                meci.setScorGazda(meci.getScorGazda() + 1);
            else meci.setScorOaspete(meci.getScorOaspete() + 1);

            meciRepo.modify(meci);
        }

        notifyO();
    }

    @Override
    public void notifyO() {
        observer.forEach(Observer::update);
    }

    @Override
    public void add(Observer o) {
        if(!observer.contains(o))
            observer.add(o);
    }

    @Override
    public void remove(Observer o) {
        observer.remove(o);
    }
}
