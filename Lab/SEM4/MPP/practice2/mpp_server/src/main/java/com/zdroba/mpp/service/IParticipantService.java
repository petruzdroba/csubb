package com.zdroba.mpp.service;

import com.zdroba.mpp.entity.Participant;

import java.util.List;

public interface IParticipantService {

    List<Participant> get();

    void ready(Long id);

    void score(Long id,Long judge, Integer score);
}
