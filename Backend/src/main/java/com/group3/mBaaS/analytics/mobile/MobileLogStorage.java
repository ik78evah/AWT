package com.group3.mBaaS.analytics.mobile;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;


public interface MobileLogStorage extends R2dbcRepository<MobileLogEvent, Integer> {

    // Find all rows with the same hash
    Mono<Integer> countByHashEquals(Integer hash);

    // Filter logs by project
    Flux<MobileLogEvent> findAllByProject(Integer project);
}
