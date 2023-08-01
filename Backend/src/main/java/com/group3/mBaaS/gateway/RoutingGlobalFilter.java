package com.group3.mBaaS.gateway;

import com.group3.mBaaS.authentication.JWTUtil;
import com.group3.mBaaS.feature_project.FeatureProjectMapperService;
import com.group3.mBaaS.user.UserRepository;
import com.group3.mBaaS.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;


/**
 * This class holdes the filter for evaluating whether a request shall be routed.
 */
@RefreshScope
@Component
public class RoutingGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    private Cerberus cerberus;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private FeatureProjectMapperService featureProjectMapperService;

    /**
     * This filter evaluates, whether a request ca be made to a requested feature.
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter (ServerWebExchange exchange, GatewayFilterChain chain){
        ServerHttpRequest request = exchange.getRequest();

        if (Cerberus.isSecured.test(request)) {
            if (this.isAuthMissing(request)) {
                return this.onError(exchange, "Authorization header is missing in request", HttpStatus.FORBIDDEN);
            }
            final String service = this.getService(request);
            final String token = this.getAuthHeader(request).substring(7); // delet the "Bearer"
            System.out.println(service);
            String username = jwtUtil.getUsernameFromToken(token);
            System.out.println(username);
            return userRepository.findByUsername(username)
                    .switchIfEmpty(Mono.error(new RuntimeException()))
                    .map(mbuser -> {
                        System.out.println(mbuser.getUsername());
                        return mbuser;
                    })
                    .map(user -> user.getProjectid())
                    .map(projectid -> featureProjectMapperService.findFeaturesOfProjectid(projectid)
                    )
                    .flatMap(features -> features.filter(feature -> {
                        System.out.println(feature.path);
                        if (feature.path.contains(service)) {
                            System.out.println("Feature found");
                            return true;
                        } else {
                            System.out.println("Feature not found");
                            return false;
                        }
                    }).singleOrEmpty())
                    .switchIfEmpty(Mono.error(new RuntimeException()))
                    .flatMap(feature -> {
                        System.out.println("Test");
                        return userRepository.findByUsername(username).map(user ->  {
                                            String path = exchange.getRequest().getPath().value();
                                            System.out.println(path);
                                            String newPath = path.replace(feature.getPath(), "");
                                            System.out.println(newPath);
                                            return exchange.getRequest().mutate()
                                                    .header("ProjectId", String.valueOf(user.getProjectid()))
                                                    .header("UserId", String.valueOf(user.getId()))
                                                    .path(newPath)
                                                    .build();
                                        })
                                        .map(request1 -> exchange.mutate().request(request1).build())
                                        .flatMap(exchange1 -> chain.filter(exchange1));
                            }

                    )
                    .onErrorResume(error -> this.onError(exchange, "Not allowed to access Service", HttpStatus.FORBIDDEN));

        }
        System.out.println("This would be a problem");
        return chain.filter(exchange);
        };


    /**
     * This mehtod genereates an error response.
     * @param exchange
     * @param err
     * @param httpStatus
     * @return
     */
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    /**
     * This method returns the subpath of the path which indicates the feature accessed.
     * @param request
     * @return
     */
    private String getService(ServerHttpRequest request){
        return "/" + request.getPath().subPath(1,2).value();
    }

    /**
     * This method returns the value of the "Authorization" Header.
     * @param request
     * @return
     */
    private String getAuthHeader(ServerHttpRequest request){
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }

    /**
     * This method checks whether the "Authorization" Header is present.
     * @param request
     * @return
     */
    private boolean isAuthMissing (ServerHttpRequest request){
        return !request.getHeaders().containsKey("Authorization");
    }


    @Override
    public int getOrder() {
        return -1;
    }
}
