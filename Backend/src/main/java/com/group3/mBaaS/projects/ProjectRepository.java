package com.group3.mBaaS.projects;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * This class represents the project repository.
 */

@Repository
public interface ProjectRepository extends R2dbcRepository<Project, Integer> {

}
