package com.zdroba.mpp.service;

import com.zdroba.mpp.entity.Child;
import com.zdroba.mpp.exceptions.NotFoundException;

import java.time.Instant;
import java.util.List;

public interface IChildService {

    Child update(Long childId,Long checkpointId, Instant time) throws NotFoundException;

    List<Child> get();

    List<Child> getCheck(Long checkpointId);

    Child get(Long id);
}
