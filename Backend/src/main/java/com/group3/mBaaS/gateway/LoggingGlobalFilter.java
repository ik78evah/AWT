package com.group3.mBaaS.gateway;

import com.group3.mBaaS.analytics.backend.BackendLogEvent;
import com.group3.mBaaS.analytics.backend.BackendLogStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.HashMap;


/**
 * This class holds the filter for logging the request made to a route.
 */
@Component
public class LoggingGlobalFilter implements GlobalFilter {

    @Autowired
    BackendLogStorage backendLogger;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        System.out.println("A request was made with the methode: " + exchange.getRequest().getMethod().toString()
                            + "to the path: " + exchange.getRequest().getPath().toString());
        return chain.filter(exchange)
                .zipWith(backendLogger.save(new BackendLogEvent("Request received by Router", "Method " + exchange.getRequest().getMethod().toString() + " called", "/" + exchange.getRequest().getPath(), new HashMap<>())))
                .map(Tuple2::getT1);
    }
}
