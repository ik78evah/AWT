package com.group3.mBaaS.analytics.mobile;

import com.group3.mBaaS.analytics.backend.BackendLogEvent;
import com.group3.mBaaS.analytics.backend.BackendLogStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.*;

@RestController
@RequestMapping("/logs/mobile")
public class MobileLogger {

    @Autowired
    MobileLogStorage mobileStorage;

    @Autowired
    BackendLogStorage backendLogger;


    // Get all logs with type APP filtered by project or project-independently
    @GetMapping({"/all", "all/{projectId}"})
    public Mono<ResponseEntity<List<MobileLogEvent>>> getAllEntries(@PathVariable("projectId") Optional<Integer> projectId) {
        Set<Integer> hashSet = new HashSet<>();

        Flux<MobileLogEvent> items = (projectId.isPresent()) ? mobileStorage.findAllByProject(projectId.get()) : mobileStorage.findAll();

        return items
                .flatMap(item -> mobileStorage.countByHashEquals(item.hash).map(value -> {
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
                .zipWith(backendLogger.save(new BackendLogEvent("Get all mobile logs", "All logs for MOBILE requested", "/logs/mobile/all", new HashMap<>())))
                .map(Tuple2::getT1)
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .onErrorResume(error -> {
                    System.out.println(error);
                    return Mono.just(new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }

    // Create a new APP Log
    @PostMapping("/log")
    public Mono<ResponseEntity<String>> captureEvent(@RequestBody MobileLogEvent event) {
        event.hash = event.hashCode();
        return mobileStorage.countByHashEquals(event.hash)
                .map(result -> event.count = result)
                .flatMap(item -> mobileStorage.save(event))
                .flatMap(value -> backendLogger.save(new BackendLogEvent("App log captured", "New log created for APP", "/logs/mobile/log", new HashMap<>())))
                .map(result -> new ResponseEntity<>("Successfully added a App event", HttpStatus.CREATED))
                .onErrorResume(error -> {
                    System.out.println(error);
                    return Mono.just(new ResponseEntity<>("Failed to store MobileLogEvent", HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }
}
