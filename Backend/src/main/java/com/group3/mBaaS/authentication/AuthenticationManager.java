package com.group3.mBaaS.authentication;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
/**
 * This class implements the ReactiveAuthenticationManager to determine if a provided authentication can be authenticated
 */
public class AuthenticationManager implements ReactiveAuthenticationManager {


    private JWTUtil jwtUtil;

    /**
     * checks if an authentication is valid by checking the json web token which is contained in the authentication
     * @param authentication which will be checked for validity
     * @return a Mono containing the verified authentication as a UsernamePasswordAuthenticationToken
     */
    @Override
    @SuppressWarnings("unchecked")
    public Mono<Authentication> authenticate(Authentication authentication) {
        //extracts the json web token from the authentication
        String authToken = authentication.getCredentials().toString();
        //extracts the username from the json web token
        String username = jwtUtil.getUsernameFromToken(authToken);

        return Mono.just(jwtUtil
                        //checks if the json web token of the authentication is valid by checking the exploration
                        .validateToken(authToken)
                )
                .filter(valid -> valid)
                .switchIfEmpty(Mono.empty())
                .map(valid -> {
                    //extract the clams from the JWT
                    Claims claims = jwtUtil.getAllClaimsFromToken(authToken);
                    //list containing the extracted roles from the claims
                    List<String> rolesMap = claims.get("role", List.class);
                    //returns  an Authentication via a UsernamePasswordAuthenticationToken containing the roles of the JWT
                    return new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            rolesMap.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                    );
                });

    }
}
