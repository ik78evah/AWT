package com.group3.mBaaS.analytics.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.*;

@RestController
@RequestMapping("/logs/backend")
public class BackendLogger {

    @Autowired
    BackendLogStorage backendStorage;

    // Get all logs with type BACKEND (project-independently)
    @GetMapping("/all")
    public Mono<ResponseEntity<List<BackendLogEvent>>> getAllEntries() {
        Set<Integer> hashSet = new HashSet<>();

        return backendStorage.findAll()
                .flatMap(item -> backendStorage.countByHashEquals(item.hash).map(value -> {
                    item.count = value;
                    return item;
                }))
                .filter(item -> {
                    if (hashSet.contains(item.hash)) {
                        return false;
                    } else {
                        hashSet.add(item.hash);
                        return true;
                    }
                })
                .collectList()
                .zipWith(backendStorage.save(new BackendLogEvent("Get all backend logs", "All logs for BACKEND requested", "/logs/backend/all", new HashMap<>())))
                .map(Tuple2::getT1)
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .onErrorResume(error -> {
                    System.out.println(error);
                    return Mono.just(new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }

    // Create a new BACKEND Log
    @PostMapping("/log")
    public Mono<ResponseEntity<String>> captureEvent(@RequestBody BackendLogEvent event) {
        event.hash = event.hashCode();
        return backendStorage.countByHashEquals(event.hash)
                .map(result -> event.count = result)
                .flatMap(item -> backendStorage.save(event))
                .flatMap(value -> backendStorage.save(new BackendLogEvent("Backend log captured", "New log created for BACKEND", "/logs/backend/log", new HashMap<>())))
                .map(result -> new ResponseEntity<>("Successfully added a new Backend event", HttpStatus.CREATED))
                .onErrorResume(error -> {
                    System.out.println(error);
                    return Mono.just(new ResponseEntity<>("Failed to store BackendLogEvent", HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }
}
