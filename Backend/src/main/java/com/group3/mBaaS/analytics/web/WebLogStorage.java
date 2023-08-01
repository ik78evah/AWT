package com.group3.mBaaS.analytics.web;

import com.group3.mBaaS.analytics.mobile.MobileLogEvent;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WebLogStorage extends R2dbcRepository<WebLogEvent, Integer> {

    // Find all rows with the same hash
    Mono<Integer> countByHashEquals(Integer hash);

    // Filter logs by project
    Flux<WebLogEvent> findAllByProject(Integer project);
}