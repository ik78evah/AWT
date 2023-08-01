package com.group3.mBaaS.analytics;

import com.group3.mBaaS.analytics.backend.BackendLogEvent;
import com.group3.mBaaS.analytics.backend.BackendLogStorage;
import com.group3.mBaaS.analytics.mobile.MobileLogEvent;
import com.group3.mBaaS.analytics.mobile.MobileLogStorage;
import com.group3.mBaaS.analytics.web.WebLogEvent;
import com.group3.mBaaS.analytics.web.WebLogStorage;
import com.group3.mBaaS.feature.CreateOrUpdateFeatureRequest;
import com.group3.mBaaS.feature.Feature;
import com.group3.mBaaS.feature.FeatureRepository;
import com.group3.mBaaS.feature.FeatureService;
import com.group3.mBaaS.projects.Project;
import com.group3.mBaaS.projects.ProjectRepository;
import com.group3.mBaaS.user.Mbuser;
import com.group3.mBaaS.user.Role;
import com.group3.mBaaS.user.UserRepository;
import com.group3.mBaaS.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/debug")
public class SampleGenerator {

    @Autowired
    BackendLogStorage backendStorage;

    @Autowired
    MobileLogStorage mobileStorage;

    @Autowired
    WebLogStorage webStorage;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    FeatureRepository featureRepository;

    @Autowired
    FeatureService featureService;

    Map<String, Object> emptyParams = new HashMap<>();
    private Project project1 = new Project("Analytics App");
    private Project project2 = new Project("Multiplayer Card Game");
    private Project project3 = new Project("Water Tracker");


    // Create samples for BACKEND logs
    @GetMapping("/backendSamples")
    public Mono<ResponseEntity<String>> generateSampleBackendLogs() {
        Map<String, Object> params = new HashMap<>();
        params.put("token", "vehbJKKBKLHvdfbervegver");
        params.put("isAdmin", 0);

        return backendStorage.save(new BackendLogEvent("Requested all logs", "All logs have been requested", "/logs/all", emptyParams))
                .concatWith(backendStorage.save(new BackendLogEvent("Requested user login", "User tried to log in", "/users/login", emptyParams)))
                .concatWith(backendStorage.save(new BackendLogEvent("Requested user login", "User tried to log in", "/users/login", emptyParams)))
                .concatWith(backendStorage.save(new BackendLogEvent("Requested user login", "User tried to log in", "/users/login", emptyParams)))
                .concatWith(backendStorage.save(new BackendLogEvent("New log captured", "New log from source APP captured", "/log/mobile/new", params)))
                .concatWith(backendStorage.save(new BackendLogEvent("New log captured", "New log from source APP captured", "/log/mobile/new", params)))
                .collectList()
                .map(result -> new ResponseEntity<>("Successfully created sample backend logs", HttpStatus.OK))
                .onErrorResume(error -> {
                    System.out.println(error);
                    return Mono.just(new ResponseEntity<>(error.toString(), HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }

    // Create samples for WEB logs
    @GetMapping("/webSamples")
    public Mono<ResponseEntity<String>> generateSampleWebLogs() {
        Map<String, Object> params = new HashMap<>();
        params.put("username", "user123");
        params.put("isPro", 1);

        return webStorage.save(new WebLogEvent("Tapped Settings", "User opened Settings", "macOS 13.2", "Safari", "EN-us", "7.8", "settings", emptyParams, project1.id))
                .concatWith(webStorage.save(new WebLogEvent("Watched Mobile Logs", "Logs were filtered for Mobile Logs", "Windows 10", "Internet Explorer", "DE-de", "7.9.2", "analytics", emptyParams, project2.id)))
                .concatWith(webStorage.save(new WebLogEvent("Watched Mobile Logs", "Logs were filtered for Mobile Logs", "Windows 10", "Internet Explorer", "DE-de", "7.9.2", "analytics", emptyParams, project1.id)))
                .concatWith(webStorage.save(new WebLogEvent("Tapped Home", "User clicked on brand name", "Linux ABC", "Google Chrome", "EN-us", "7.2", "home", params, project2.id)))
                .concatWith(webStorage.save(new WebLogEvent("Tapped Home", "User clicked on brand name", "Linux ABC", "Google Chrome", "EN-us", "7.2", "home", params, project1.id)))
                .collectList()
                .map(result -> new ResponseEntity<>("Successfully created sample web logs", HttpStatus.OK))
                .onErrorResume(error -> Mono.just(new ResponseEntity<>(error.toString(), HttpStatus.INTERNAL_SERVER_ERROR)));
    }

    // Create samples for MOBILE logs
    @GetMapping("/mobileSamples")
    public Mono<ResponseEntity<String>> generateSampleMobileLogs() {
        Map<String, Object> params = new HashMap<>();
        params.put("username", "niklas");
        params.put("isPro", 1);
        return mobileStorage.save(new MobileLogEvent("Tapped SignUp Button", "User created a new account", "iOS 14.7", "DE-de", "2.4.3", "RegistrationView", emptyParams, project1.id))
                .concatWith(mobileStorage.save(new MobileLogEvent("Opened Settings", "User opened app settings", "iOS 15.0", "DE-de", "1.4.3", "SettingsView", emptyParams, project1.id)))
                .concatWith(mobileStorage.save(new MobileLogEvent("Opened Settings", "User opened app settings", "iOS 15.0", "DE-de", "1.4.3", "SettingsView", emptyParams, project2.id)))
                .concatWith(mobileStorage.save(new MobileLogEvent("Tapped LogIn Button", "User logged in with valid credential", "iOS 15.2", "EN-us", "2.4.3", "LogInView", params, project1.id)))
                .concatWith(mobileStorage.save(new MobileLogEvent("Tapped LogIn Button", "User logged in with valid credential", "iOS 15.2", "EN-us", "2.4.3", "LogInView", params, project2.id)))
                .collectList()
                .map(result -> new ResponseEntity<>("Successfully created sample mobile logs", HttpStatus.OK))
                .onErrorResume(error -> Mono.just(new ResponseEntity<>(error.toString(), HttpStatus.INTERNAL_SERVER_ERROR)));
    }

    // Create samples for Projects
    @GetMapping("/projects")
    public Mono<ResponseEntity<String>> generateProjects() {
        return projectRepository.save(project1)
                .concatWith(projectRepository.save(project2))
                .concatWith(projectRepository.save(project3))
                .collectList()
                .map(result -> new ResponseEntity<>("Successfully created sample projects", HttpStatus.OK))
                .onErrorResume(error -> {
                    System.out.println(error);
                    return Mono.just(new ResponseEntity<>(error.toString(), HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }

    // Create samples for Features
    @GetMapping("/features")
    public Mono<ResponseEntity<String>> generateFeatures() {
        return featureService.createFeature(new CreateOrUpdateFeatureRequest("/feature1", "http://127.0.0.1:8090/"))
                .concatWith(featureService.createFeature(new CreateOrUpdateFeatureRequest("/feature2", "http://127.0.0.1:8090/")))
                .concatWith(featureService.createFeature(new CreateOrUpdateFeatureRequest("/feature3", "http://127.0.0.1:8090/")))
                .collectList()
                .map(result -> new ResponseEntity<>("Successfully created sample features", HttpStatus.OK))
                .onErrorResume(error -> Mono.just(new ResponseEntity<>(error.toString(), HttpStatus.INTERNAL_SERVER_ERROR)));
    }

    // Create samples for Users
    @GetMapping("/users")
    public Mono<ResponseEntity<String>> generateUsers() {
        return userService.createUser(new Mbuser("niklas.amslgruber", "123", true,Role.ROLE_ADMIN, 0))
                .concatWith(userService.createUser(new Mbuser("max.mustermann", "567", true, Role.ROLE_USER, project1.id)))
                .concatWith(userService.createUser(new Mbuser("group1", "4343", true, Role.ROLE_USER, project2.id)))
                .collectList()
                .map(result -> new ResponseEntity<>("Successfully created users", HttpStatus.OK))
                .onErrorResume(error -> Mono.just(new ResponseEntity<>(error.toString(), HttpStatus.INTERNAL_SERVER_ERROR)));
    }

    // Delete ALL logs
    @GetMapping("/deleteAll")
    public Mono<ResponseEntity<String>> deleteAll() {
        return backendStorage.deleteAll()
                .concatWith(mobileStorage.deleteAll())
                .concatWith(webStorage.deleteAll())
                .collectList()
                .map(result -> new ResponseEntity<>("Successfully deleted all logs", HttpStatus.OK))
                .onErrorResume(error -> Mono.just(new ResponseEntity<>(error.toString(), HttpStatus.INTERNAL_SERVER_ERROR)));
    }

    // Delete ALL projects
    @GetMapping("/deleteAllProjects")
    public Mono<ResponseEntity<String>> deleteAllProjects() {
        project1 = new Project("Analytics App");
        project2 = new Project("Multiplayer Card Game");
        project3 = new Project("Water Tracker");
        return projectRepository.deleteAll()
                .map(result -> new ResponseEntity<>("Successfully deleted all projects", HttpStatus.OK))
                .onErrorResume(error -> Mono.just(new ResponseEntity<>(error.toString(), HttpStatus.INTERNAL_SERVER_ERROR)));
    }

    // Delete ALL projects
    @GetMapping("/deleteAllFeatures")
    public Mono<ResponseEntity<String>> deleteAllFeatures() {
        return featureRepository.deleteAll()
                .map(result -> new ResponseEntity<>("Successfully deleted all features", HttpStatus.OK))
                .onErrorResume(error -> Mono.just(new ResponseEntity<>(error.toString(), HttpStatus.INTERNAL_SERVER_ERROR)));
    }

    // Delete ALL users
    @GetMapping("/deleteAllUsers")
    public Mono<ResponseEntity<String>> deleteAllUsers() {
        return userRepository.deleteAll()
                .map(result -> new ResponseEntity<>("Successfully deleted all users", HttpStatus.OK))
                .onErrorResume(error -> Mono.just(new ResponseEntity<>(error.toString(), HttpStatus.INTERNAL_SERVER_ERROR)));
    }
}
