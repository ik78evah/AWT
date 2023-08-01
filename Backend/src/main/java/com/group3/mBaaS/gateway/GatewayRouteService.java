package com.group3.mBaaS.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * This class represents the service which triggers the update of the current routes.
 */
@Service
public class GatewayRouteService {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * Method for refreshing the current routes.
     */
    public void refreshRoutes(){
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }
}
