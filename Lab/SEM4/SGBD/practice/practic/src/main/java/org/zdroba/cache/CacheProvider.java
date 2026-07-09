package org.zdroba.cache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.zdroba.entity.dale;

import java.time.Duration;

public class CacheProvider {

    private final static CacheManager cacheManager =
            CacheManagerBuilder.newCacheManagerBuilder()
                    .withCache(
                            "dale",
                            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                    Long.class,
                                    dale.class,
                                    ResourcePoolsBuilder.heap(1000) // max number of entries stored
                            ).withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(
                                    Duration.ofSeconds(30) // Time To Live
                            ))
                    )
                    .build(true);

    public static Cache<Long, dale> getCache() {
        return cacheManager.getCache("dale", Long.class, dale.class);
    }
}
