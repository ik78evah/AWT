package com.group3.mBaaS.analytics.backend;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface BackendLogStorage extends R2dbcRepository<BackendLogEvent, Integer> {

    // Find all rows with the same hash
    Mono<Integer> countByHashEquals(Integer hash);

}
