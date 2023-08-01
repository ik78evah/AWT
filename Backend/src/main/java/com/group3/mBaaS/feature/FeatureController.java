package com.group3.mBaaS.feature;

import com.group3.mBaaS.analytics.backend.BackendLogEvent;
import com.group3.mBaaS.analytics.backend.BackendLogStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * This class represents the API endpoint for managing the features.
 */


@RestController
@RequestMapping("/features")
public class FeatureController {

    @Autowired
    FeatureService featureService;
    @Autowired
    BackendLogStorage backendLogger;

    //Get all features
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<List<FeatureResponse>>> getAllFeatures() {
        return featureService.findFeatures()
                .map(this::generateResponse)
                .collectList()
                .zipWith(backendLogger.save(new BackendLogEvent("Get all features", "All features requested", "/features/all", new HashMap<>())))
                .map(Tuple2::getT1)
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .onErrorResume(error -> {
                    System.out.println(error);
                    return Mono.just(new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }

    //Get feature by id
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<FeatureResponse>> getFeature (@PathVariable("id") Integer id){
        return featureService.findFeature(id)
                .map(this::generateResponse)
                .zipWith(backendLogger.save(new BackendLogEvent("Get feature", "Requested a feature with the id " + String.valueOf(id), "/features/{id}", new HashMap<>())))
                .map(Tuple2::getT1)
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .onErrorResume(error -> {
                    System.out.println(error);
                    return Mono.just(new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }


    //Add new feature based on request
    @PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<String>> addFeature(@RequestBody @Validated CreateOrUpdateFeatureRequest request) {
        return featureService.createFeature(request)
                .thenReturn(request)
                .zipWith(backendLogger.save(new BackendLogEvent("Add feature", "Add new feature requested", "/features/add", new HashMap<>())))
                .map(Tuple2::getT1)
                .map(result -> new ResponseEntity<>("Successfully added a new feature", HttpStatus.CREATED))
                .onErrorResume(error -> {
                    System.out.println(error);
                    return Mono.just(new ResponseEntity<>("Could not add feature", HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }

    //update feature based on request
    @PutMapping(path = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<String>> updateFeature(@PathVariable("id") Integer id, @RequestBody @Validated CreateOrUpdateFeatureRequest request){
        return featureService.updateFeature(id, request)
                .thenReturn(id)
                .zipWith(backendLogger.save(new BackendLogEvent("Update feature", "Update feature with the id " + id, "/features/update/{id}", new HashMap<>())))
                .map(Tuple2::getT1)
                .map(result -> new ResponseEntity<>("Successfully updated feature with the id " + String.valueOf(id), HttpStatus.OK))
                .onErrorResume(error -> {
                    System.out.println(error);
                    return Mono.just(new ResponseEntity<>("Could not update feature", HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }

    // Delete feature with id
    @DeleteMapping(path = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<String>> deleteFeature(@PathVariable("id") Integer id) {
        return featureService.deleteFeature(id)
                .thenReturn(id)
                .zipWith(backendLogger.save(new BackendLogEvent("Delete feature", "Feature delete requested", "/features/delete/{id}", new HashMap<>())))
                .map(Tuple2::getT1)
                .map(result -> new ResponseEntity<>("Successfully deleted feature with the id " + String.valueOf(id), HttpStatus.OK))
                .onErrorResume(error -> {
                    System.out.println(error);
                    return Mono.just(new ResponseEntity<>("Could not delete feature", HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }


    private FeatureResponse generateResponse(Feature feature){
        return FeatureResponse.builder()
                .id(feature.getId())
                .path(feature.getPath())
                .uri(feature.getUri())
                .build();
    }
}
