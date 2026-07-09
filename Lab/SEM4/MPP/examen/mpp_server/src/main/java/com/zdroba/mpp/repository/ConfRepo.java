package com.zdroba.mpp.repository;

import com.zdroba.mpp.entity.Configuration;
import org.springframework.stereotype.Repository;

@Repository
public class ConfRepo extends GenericRepo<Configuration, Long>{
    public ConfRepo() {
        super(Configuration.class);
    }
}
