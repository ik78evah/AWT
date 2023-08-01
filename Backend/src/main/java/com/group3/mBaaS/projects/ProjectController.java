package com.group3.mBaaS.projects;

import com.group3.mBaaS.analytics.backend.BackendLogEvent;
import com.group3.mBaaS.analytics.backend.BackendLogStorage;
import com.group3.mBaaS.feature.CreateOrUpdateFeatureRequest;
import com.group3.mBaaS.feature.FeatureResponse;
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
import java.util.UUID;


/**
 * This class represents the API endpoint for managing the projects.
 */

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    ProjectService projectService;
    @Autowired
    BackendLogStorage backendLogger;

    // Get all projects unfiltered or filtered by projectId
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<List<Project>>> getAllProjects() {
        return projectService.findProjects()
                .collectList()
                .zipWith(backendLogger.save(new BackendLogEvent("Get all projects", "All projects requested", "/projects/all", new HashMap<>())))
                .map(Tuple2::getT1)
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .onErrorReturn(new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR));
    }

    // Get specific project
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<Project>> getProject(@PathVariable("id") Integer id){
        return projectService.findProject(id)
                .zipWith(backendLogger.save(new BackendLogEvent("Get project", "Requested a project with the id " + String.valueOf(id), "/projects/{id}", new HashMap<>())))
                .map(Tuple2::getT1)
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .onErrorReturn(new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    // Add new project with name
    @PostMapping(path ="/add/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<String>> addProject(@PathVariable("name") String name) {
        return projectService.createProject(name)
                .thenReturn(name)
                .zipWith(backendLogger.save(new BackendLogEvent("Add project", "Add new project requested", "/projects/add", new HashMap<>())))
                .map(result -> new ResponseEntity<>("Successfully added a new project", HttpStatus.CREATED))
                .onErrorReturn(new ResponseEntity<>("Could not add project", HttpStatus.INTERNAL_SERVER_ERROR));
    }

    // Update exsiting project
    @PutMapping(path = "/update/{id}/{newName}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<String>> updateProject(@PathVariable("id") Integer id, @PathVariable("newName")String newName){
        return projectService.updateProject(id, newName)
                .thenReturn(id)
                .zipWith(backendLogger.save(new BackendLogEvent("Update project", "Update project with the id " + String.valueOf(id), "/projects/update/{id}", new HashMap<>())))
                .map(Tuple2::getT1)
                .map(result -> new ResponseEntity<>("Successfully updated project with the id " + String.valueOf(id), HttpStatus.OK))
                .onErrorReturn(new ResponseEntity<>("Could not update project", HttpStatus.INTERNAL_SERVER_ERROR));
    }

    // Delete project
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<String>> deleteProject(@PathVariable("id") Integer id) {
        return projectService.deleteProject(id)
                .thenReturn(id)
                .zipWith(backendLogger.save(new BackendLogEvent("Delete project", "Project delete requested", "/projects/delete", new HashMap<>())))
                .map(Tuple2::getT1)
                .map(result -> new ResponseEntity<>("Successfully deleted project with the id " + String.valueOf(id), HttpStatus.OK))
                .onErrorReturn(new ResponseEntity<>("Could not delete project", HttpStatus.INTERNAL_SERVER_ERROR));

    }

}
