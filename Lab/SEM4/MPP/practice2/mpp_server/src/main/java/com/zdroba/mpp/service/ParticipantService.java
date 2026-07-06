package com.zdroba.mpp.service;

import com.zdroba.mpp.entity.Participant;
import com.zdroba.mpp.entity.Status;
import com.zdroba.mpp.repository.IParticipantRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class ParticipantService implements IParticipantService{
    private final IParticipantRepo repository;

    public ParticipantService(IParticipantRepo repository) {
        this.repository = repository;
    }

    @Override
    public List<Participant> get() {
        return repository.get();
    }

    @Override
    public void ready(Long id) {
        Participant participant = repository.get(id);
        participant.setStatus(Status.ONGOING);
        repository.modify(participant);
    }

    @Override
    public void score(Long id,Long judge, Integer score) {
        Participant p = repository.get(id);

        if(judge ==1) {
            p.setScoreExec(score);
        }else if(judge ==2) {
            p.setScoreTeh(score);
        }else {
            p.setScoreArt(score);
        }

        if(p.getScoreExec() != 0 && p.getScoreTeh() != 0 && p.getScoreArt() != 0)
            p.setStatus(Status.FINISHED);

        repository.modify(p);
    }
}
