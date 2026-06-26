package com.zdroba.mpp.repository;

import com.zdroba.mpp.entity.Participant;

import java.util.List;

public interface IParticipantRepo {

    List<Participant> get();

    Participant get(Long id);

    void modify(Participant participant);
}
