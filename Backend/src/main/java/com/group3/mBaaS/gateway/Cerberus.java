package com.group3.mBaaS.gateway;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

/**
 * This class holds util methods used for the routing.
 */
@Component
public class Cerberus {

    // holed the prefixes for the open endpoints
    public static final List<String> openEndpoints = List.of(
            "services",
            "auth"
    );

    //evaluates whether a request was made to one of the secured endpoints
    public static Predicate<ServerHttpRequest> isSecured =
            request -> openEndpoints
                    .stream()
                    .noneMatch(uri ->
                    request.getURI().getPath().contains(uri));
}
