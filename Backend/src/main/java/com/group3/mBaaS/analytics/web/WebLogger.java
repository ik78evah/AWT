package com.group3.mBaaS.analytics.web;

import com.group3.mBaaS.analytics.backend.BackendLogEvent;
import com.group3.mBaaS.analytics.backend.BackendLogStorage;
import com.group3.mBaaS.analytics.mobile.MobileLogEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.*;

@RestController
@RequestMapping("/logs/web")
public class WebLogger {

    @Autowired
    WebLogStorage webStorage;

    @Autowired
    BackendLogStorage backendLogger;

    // Get all logs with type WEB filtered by project or project-independently
    @GetMapping({"/all", "all/{projectId}"})
    public Mono<ResponseEntity<List<WebLogEvent>>> getAllEntries(@PathVariable("projectId") Optional<Integer> projectId) {
        Set<Integer> hashSet = new HashSet<>();

        Flux<WebLogEvent> items = (projectId.isPresent()) ? webStorage.findAllByProject(projectId.get()) : webStorage.findAll();

        return items
                .flatMap(item -> webStorage.countByHashEquals(item.hash).map(value -> {
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
                .zipWith(backendLogger.save(new BackendLogEvent("Get all web logs", "All logs for WEB requested", "/logs/web/all", new HashMap<>())))
                .map(Tuple2::getT1)
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .onErrorResume(error -> {
                    System.out.println(error);
                    return Mono.just(new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }

    // Create a new WEB Log
    @PostMapping("/log")
    public Mono<ResponseEntity<String>> captureEvent(@RequestBody WebLogEvent event) {
        event.hash = event.hashCode();
        return webStorage.countByHashEquals(event.hash)
                .map(result -> event.count = result)
                .flatMap(item -> webStorage.save(event))
                .flatMap(value -> backendLogger.save(new BackendLogEvent("Web log captured", "New log created for WEB", "/logs/web/log", new HashMap<>())))
                .map(result -> new ResponseEntity<>("Successfully added a Web event", HttpStatus.CREATED))
                .onErrorResume(error -> {
                    System.out.println(error);
                    return Mono.just(new ResponseEntity<>("Failed to store WebLogEvent", HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }
}
