package com.group3.mBaaS.feature_project;


import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


/**
 * This class represents the FeatureProjectMappings repository.
 */

@Repository
public interface FeatureProjectMapperRepository extends R2dbcRepository<Feature_project_mapper, Integer> {
    Flux<Feature_project_mapper> findAllByFeatureid(Integer featureid);
    Flux<Feature_project_mapper> findAllByProjectid(Integer projectid);
}
