package com.group3.mBaaS.feature;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

/**
 * This class represents the feature repository.
 */

public interface FeatureRepository extends R2dbcRepository<Feature, Integer> {
}
