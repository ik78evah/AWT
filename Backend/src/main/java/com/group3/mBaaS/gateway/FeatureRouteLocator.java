package com.group3.mBaaS.gateway;

import com.group3.mBaaS.feature.Feature;
import com.group3.mBaaS.feature.FeatureService;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.BooleanSpec;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import reactor.core.publisher.Flux;

/**
 * This calss handes the dynamic creation of the routs to the backend features.
 */

@AllArgsConstructor
public class FeatureRouteLocator implements RouteLocator {

    private final FeatureService featureService;

    private final RouteLocatorBuilder routeLocatorBuilder;


    /**
     * Generates the routes based on the current features.
     * @return
     */
    @Override
    public Flux<Route> getRoutes() {
        RouteLocatorBuilder.Builder routsBuilder = routeLocatorBuilder.routes();
        return featureService.findFeatures()
                .map(feature -> routsBuilder.route(String.valueOf(feature.getId()), predicateSpec -> setPredicateSpec(feature, predicateSpec)))
                .collectList()
                .flatMapMany(builders -> routsBuilder.build().getRoutes());
    }

    private Buildable<Route> setPredicateSpec(Feature feature, PredicateSpec predicateSpec){
        BooleanSpec booleanSpec = predicateSpec.path(feature.getPath());
        return booleanSpec.uri(feature.getUri());
    }
}
