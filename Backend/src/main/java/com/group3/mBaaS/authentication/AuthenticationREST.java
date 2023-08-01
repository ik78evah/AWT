package com.group3.mBaaS.authentication;

import com.group3.mBaaS.analytics.backend.BackendLogEvent;
import com.group3.mBaaS.analytics.backend.BackendLogStorage;
import com.group3.mBaaS.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.HashMap;
import java.util.Optional;


@AllArgsConstructor
@RestController
/**
 * provides an rest endpoint for authentication
 */
public class AuthenticationREST {

    private JWTUtil jwtUtil;
    private PBKDF2Encoder passwordEncoder;
    private UserService userService;

    @Autowired
    BackendLogStorage backendLogger;

    /**
     * provides a login rest endpoint which checks the credentials of the login requester
     * @param projectId contained in the RequestHeader of the request
     * @param ar RequestBody auf the request
     * @return A Mono containing the ResponseEntity and witch successful authentication the Json Web Token for the user
     */
    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestHeader("ProjectId") Integer projectId, @RequestBody AuthRequest ar) {
        return userService
                //searches for the specified user
                .findByUsername(ar.getUsername())
                .zipWith(backendLogger.save(new BackendLogEvent("Login requested", "User tried to login", "/login", new HashMap<>())))
                .map(Tuple2::getT1)
                //filters the user in case there are multiple users with the same username and different projects
                .filter(user -> user.getProjectid() == projectId)
                .last()
                //encodes the password of the request and compares it with the stored password
                .filter(userDetails -> passwordEncoder.encode(ar.getPassword()).equals(userDetails.getPassword()))
                //creates a ResponseEntity in case of a successful authentication with a Json Web Token for the user
                .map(userDetails -> {
                            String token = jwtUtil.generateToken(userDetails);
                            String roles = jwtUtil.getRolesFromToken(token);
                            return ResponseEntity.ok(new AuthResponse(token, roles, jwtUtil.getUsernameFromToken(token)));
                        }
                )
                //in case of unsuccessful authentication returns ResponseEntity with UNAUTHORIZED
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }

}