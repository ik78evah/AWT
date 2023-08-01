package com.group3.mBaaS.gateway;

import com.group3.mBaaS.feature.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class the Gateway configuration which enables the routing functionality.
 */

@Configuration
public class GatewayConfig {


    @Bean
    public RouteLocator routeLocator(FeatureService featureService, RouteLocatorBuilder routeLocatorBuilder){
        return new FeatureRouteLocator(featureService, routeLocatorBuilder);
    }
}
