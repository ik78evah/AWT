package com.group3.mBaaS.user;


import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<Mbuser, Integer> {
    Mono<Mbuser> findByUsername(String username);
    Mono<Mbuser> deleteByUsername(String username);
    Flux<Mbuser> findAllByUsername(String username);
    Flux<Mbuser> findAllByProjectid(Integer projectId);
}
