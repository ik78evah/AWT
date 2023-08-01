package com.group3.mBaaS.feature_project;


import com.group3.mBaaS.analytics.backend.BackendLogEvent;
import com.group3.mBaaS.analytics.backend.BackendLogStorage;
import com.group3.mBaaS.feature.Feature;
import com.group3.mBaaS.projects.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * This class represents the API endpoint for managing the mapping of projects to features.
 */

@RestController
@RequestMapping("/f2p")
public class FeatureProjectMapperController {

    @Autowired
    FeatureProjectMapperService featureProjectMapperService;
    @Autowired
    BackendLogStorage backendLogger;

    // Get all mappings
    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<List<Feature_project_mapper>>> getAllMappings (){
        return featureProjectMapperService.getAllMappings()
                .collectList()
                .zipWith(backendLogger.save(new BackendLogEvent("Get all mappings", "All mappings requested", "/f2p/all", new HashMap<>())))
                .map(Tuple2::getT1)
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .onErrorResume(error -> {
                    System.out.println(error);
                    return Mono.just(new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }

    //Get all mappings of a project
    @GetMapping("/get/{projectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<List<Feature>>> getProjectMappings (@PathVariable("projectId")Integer projectId){
        return featureProjectMapperService.findFeaturesOfProjectid(projectId)
                .collectList()
                .zipWith(backendLogger.save(new BackendLogEvent("Get all mappings for project " + String.valueOf(projectId), "All mappings for project requested", "/f2p/get/{projectid}", new HashMap<>())))
                .map(Tuple2::getT1)
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .onErrorResume(error -> {
                    System.out.println(error);
                    return Mono.just(new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }

    //Add mapping of a feature to a project
    @PostMapping("/add/{featureId}/{projectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<String>> createMapping(@PathVariable("featureId")Integer featureId, @PathVariable("projectId")Integer projectId){
        return featureProjectMapperService.addFeatureIdToProjectId(featureId, projectId)
                .thenReturn(featureId)
                .zipWith(backendLogger.save(new BackendLogEvent("Add feature", "Add new feature requested", "/features/add", new HashMap<>())))
                .map(Tuple2::getT1)
                .map(result -> new ResponseEntity<>("Successfully added mapping of feature " + featureId + " to project " + projectId, HttpStatus.CREATED))
                .onErrorResume(error -> {
                    System.out.println(error);
                    return Mono.just(new ResponseEntity<>("Could not add mapping: " + error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }

    //Delete mapping of a feature to a project
    @DeleteMapping("/delete/{featureId}/{projectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<String>> deleteMapping(@PathVariable("featureId")Integer featureId, @PathVariable("projectId")Integer projectId){
        return featureProjectMapperService.deleteMapping(featureId, projectId)
                .thenReturn(featureId)
                .zipWith(backendLogger.save(new BackendLogEvent("Delete mapping", "mapping delete requested", "/f2p/delete/{featureid}/{projectid}", new HashMap<>())))
                .map(Tuple2::getT1)
                .map(result -> new ResponseEntity<>("Successfully deleted mapping of feature " + featureId + " to project " + projectId, HttpStatus.OK))
                .onErrorResume(error -> {
                    System.out.println(error);
                    return Mono.just(new ResponseEntity<>("Could not delete mapping: " + error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }

    //Delete all mappings
    @DeleteMapping("/deleteMapping/all")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<String>> deleteAllMapping(){
        return featureProjectMapperService.deleteAll()
                .thenReturn(true)
                .map(result -> new ResponseEntity<>(
                "Successfully deleted all mappings" , HttpStatus.CREATED))
                .onErrorResume(error -> Mono.just(new ResponseEntity<>(
                        "Could not delete all mappings: " + error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR)));
    }

}
