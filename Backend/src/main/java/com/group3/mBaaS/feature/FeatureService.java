package com.group3.mBaaS.feature;


import com.group3.mBaaS.analytics.backend.BackendLogStorage;
import com.group3.mBaaS.feature_project.FeatureProjectMapperService;
import com.group3.mBaaS.gateway.GatewayRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * this class represents the interface for the interaction with the featureRepository.
 */

@Service
public class FeatureService {

    @Autowired
    private FeatureRepository featureRepository;

    @Autowired
    BackendLogStorage backendLogger;

    @Autowired
    private FeatureProjectMapperService featureProjectMapperService;

    @Autowired
    private GatewayRouteService gatewayRouteService;

    /**
     * Returns all features in the repository.
     *
     * @return Flux<Feature>
     */
    public Flux<Feature> findFeatures(){
        return featureRepository.findAll();
    }

    /**
     * Returns the feature, which has the provided id.
     *
     * @param id
     * @return Mono<Feature>
     */
    public Mono<Feature> findFeature(Integer id){
        return findAndValidateFeature(id);
    }

    /**
     * Creates a new feature based on the provided CreateOrUpdateFeatureRequest.
     *
     * @param createOrUpdateFeatureRequest
     * @return Mono<Void>
     */
    public Mono<Void> createFeature(CreateOrUpdateFeatureRequest createOrUpdateFeatureRequest){
        Feature feature = setNewFeature (new Feature(), createOrUpdateFeatureRequest);
        return featureRepository.save(feature)
                .doOnSuccess(success -> gatewayRouteService.refreshRoutes())
                .then();
    }

    /**
     * Updates the feature corresponding to the provided id with the provided CreateOrUpdateFeatureRequest.
     *
     * @param createOrUpdateFeatureRequest
     * @return Mono<Void>
     */
    public Mono<Void> updateFeature (Integer id, CreateOrUpdateFeatureRequest request){
        return findAndValidateFeature(id)
                .map(feature -> setNewFeature(feature, request))
                .flatMap(featureRepository::save)
                .doOnSuccess(success -> gatewayRouteService.refreshRoutes())
                .then();
    }

    /**
     * Detelt the feature corresponding to the provided id.
     * @param id
     * @return
     */
    public Mono<Void> deleteFeature(Integer id){
        return findAndValidateFeature(id)
                .flatMap(feature -> featureProjectMapperService.deleteAllMappingsForFeature(feature.getId())
                        .thenReturn(id)
                        .flatMap(x -> featureRepository.deleteById(feature.getId()))
                )
                .doOnSuccess(success -> gatewayRouteService.refreshRoutes());
    }

    /**
     * Creatues a new Feature object based on the provided CreateOrUpdateFeatureRequest.
     * @param feature
     * @param request
     * @return
     */
    private Feature setNewFeature(Feature feature, CreateOrUpdateFeatureRequest request) {
        feature.setPath(request.getPath());
        feature.setUri(request.getUri());
        return feature;
    }

    /**
     * Find the feature corresponding to the provided id or returns an error.
     *
     * @param id
     * @return
     */
    private Mono<Feature> findAndValidateFeature(Integer id) {
        return featureRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new RuntimeException(String.format("Feature with id %d not found", id))
                ));
    }


}
