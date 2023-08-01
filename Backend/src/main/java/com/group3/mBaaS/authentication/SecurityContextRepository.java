package com.group3.mBaaS.authentication;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * This class holds the loader for ServerWebExchange which checks the Json Web Token of a request
 */
@AllArgsConstructor
@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {

    private AuthenticationManager authenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange swe, SecurityContext sc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * checks the authentication of a server web exchange
     * @param swe Server Web Exchange for with the authentication should be checked
     * @return Mono containing the security context
     */
    @Override
    public Mono<SecurityContext> load(ServerWebExchange swe) {
        return Mono.justOrEmpty(swe.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                //searches for an authorisation header which starts with "Bearer"
                .filter(authHeader -> authHeader.startsWith("Bearer "))
                .flatMap(authHeader -> {
                    //deletes the String "Bearer" from the authHeader
                    String authToken = authHeader.substring(7);
                    //creates an Authentication via a UsernamePasswordAuthenticationToken out of the Json Web Token
                    Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken);

                    return this.authenticationManager
                            //checks the UsernamePasswordAuthenticationToken
                            .authenticate(auth)
                            //creates a SecurityContextImpl with the UsernamePasswordAuthenticationToken as a parameter
                            .map(SecurityContextImpl::new);
                });
    }
}
