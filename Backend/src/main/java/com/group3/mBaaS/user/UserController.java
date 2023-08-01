package com.group3.mBaaS.user;

import com.group3.mBaaS.analytics.backend.BackendLogEvent;
import com.group3.mBaaS.analytics.backend.BackendLogStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    BackendLogStorage backendLogger;

    @Autowired
    UserService userService;

    @GetMapping("/getUsername/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<List<Mbuser>>> getByUsername(@PathVariable("username") String username) {
        return userService.findByUsername(username)
                .collectList()
                .zipWith(backendLogger.save(new BackendLogEvent("getByUsername", "Requests all users with the Username: " + username, "/user/getUsername/{username}", new HashMap<>())))
                .map(Tuple2::getT1)
                .map(users -> new ResponseEntity<>(users, HttpStatus.OK))
                .onErrorReturn(new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @GetMapping("/getId/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<Mbuser>> getById(@PathVariable("id") Integer id) {
        return userService.findById(id)
                .zipWith(backendLogger.save(new BackendLogEvent("getById", "Requests all users with the id: " +id, "/user/getId/{id}", new HashMap<>())))
                .map(Tuple2::getT1)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .onErrorReturn(new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @PostMapping("/add")
    public Mono<ResponseEntity<String>>createUser(@RequestBody Mbuser mbUser) {
        return userService.createUser(mbUser)
                .thenReturn(mbUser)
                .zipWith(backendLogger.save(new BackendLogEvent("createUser", "creates and adds the user: " +mbUser, "/user/add", new HashMap<>())))
                .map(Tuple2::getT1)
                .thenReturn(new ResponseEntity<String>("Successfully created new user", HttpStatus.OK))
                .onErrorResume(error -> Mono.just(new ResponseEntity<>(error.toString(),HttpStatus.INTERNAL_SERVER_ERROR)));
    }

    @GetMapping({"/getAll", "/getAll/{projectId}"})
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<List<Mbuser>>> getAllUsers(@PathVariable("projectId") Optional<Integer> projectId) {
        return userService.getAllUsers(projectId)
                .collectList()
                .zipWith(backendLogger.save(new BackendLogEvent("getAllUsers", "Retrieves all users", "/user/getAll", new HashMap<>())))
                .map(Tuple2::getT1)
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .onErrorResume(error -> {
                    System.out.println(error);
                    return Mono.just(new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR));
                    });
    }

    @PostMapping("/update/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<String>>updateUser(@PathVariable("userId") Integer userId, @RequestBody Mbuser mbUser){
        return userService.updateUser(userId,mbUser)
                .thenReturn(userId)
                .zipWith(backendLogger.save(new BackendLogEvent("updateUser", "Updates the user of the id: + " + userId, "user/update/{username}", new HashMap<>())))
                .map(Tuple2::getT1)
                .thenReturn(new ResponseEntity<>("Successfully updated user with id: "+userId , HttpStatus.OK))
                .onErrorReturn(new ResponseEntity<>("Failed to update user with id: "+userId, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @GetMapping("/deleteId/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<String>> deleteUserById(@PathVariable("id") Integer id) {
        return userService.deleteUserById(id)
                .thenReturn(id)
                .zipWith(backendLogger.save(new BackendLogEvent("deleteUserById", "Deletes the user with the id: " +id, "user/deleteId/{id}", new HashMap<>())))
                .map(Tuple2::getT1)
                .thenReturn(new ResponseEntity<>("Successfully deleted the user with id " + id, HttpStatus.OK))
                .onErrorReturn(new ResponseEntity<>("Could not deleted the user with id " + id, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @GetMapping("/deleteUsername/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<String>> deleteUserByUsername(@PathVariable("username") String username) {
        return userService.deleteUserByUsername(username)
                .thenReturn(username)
                .zipWith(backendLogger.save(new BackendLogEvent("deleteUserByUsername", "Deletes the user with username: " +username, "user/deleteUsername/{username}", new HashMap<>())))
                .map(Tuple2::getT1)
                .thenReturn(new ResponseEntity<>("Successfully deleted the user with username " + username, HttpStatus.OK))
                .onErrorReturn(new ResponseEntity<>("Could not deleted the user with username " + username, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @GetMapping("/deleteAll")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<String>> deleteAll() {
        return userService.deleteAllUsers()
                .thenReturn(true)
                .zipWith(backendLogger.save(new BackendLogEvent("deleteAll", "Deletes all users", "user/deleteAll", new HashMap<>())))
                .map(Tuple2::getT1)
                .thenReturn(new ResponseEntity<>("Successfully deleted all users", HttpStatus.OK))
                .onErrorReturn(new ResponseEntity<>("Not able to delete all users", HttpStatus.INTERNAL_SERVER_ERROR));
    }

}
