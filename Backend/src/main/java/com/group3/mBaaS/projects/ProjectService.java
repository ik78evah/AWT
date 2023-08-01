package com.group3.mBaaS.projects;

import com.group3.mBaaS.feature_project.FeatureProjectMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * This class is the interface for the interaction with the projectRepository.
 */
@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    private FeatureProjectMapperService featureProjectMapperService;

    /**
     * Returns all project in the repository.
     *
     * @return Flux<Project>
     */
   public Flux<Project> findProjects(){
       return projectRepository.findAll();
   }

    /**
     * Returns the project, which has the provided id.
     *
     * @param id
     * @return Mono<Project>
     */
   public Mono<Project> findProject(Integer id){
       return findAndValidateProject(id);
   }

    /**
     * Creates a new project with the provided name.
     *
     * @param name
     * @return Mono<Void>
     */
   public Mono<Void> createProject(String name){
       return projectRepository.save(new Project(name)).then();
   }

    /**
     * Updates the project corresponding to the provided id with the provided new name.
     * @param id
     * @param newName
     * @return Mono<Void>
     */
   public Mono<Void> updateProject(Integer id, String newName){
       return findAndValidateProject(id)
               .map(project -> {
                   project.setName(newName);
                   return project;
               })
               .flatMap(projectRepository::save)
               .then();
   }

    /**
     * Detelt the project corresponding to the provided id.
     * @param id
     * @return
     */
   public Mono<Void> deleteProject(Integer id){
       return findAndValidateProject(id)
               .flatMap(project -> featureProjectMapperService.deleteAllMappingsForProject(project.getId())
                                .thenReturn(id)
                               .flatMap(x -> projectRepository.deleteById(project.getId()))
               ).then();
   }

    /**
     * Find the project corresponding to the provided id or returns an error.
     *
     * @param id
     * @return
     */
   private Mono<Project> findAndValidateProject(Integer id) {
        return projectRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new RuntimeException(String.format("Project with id %d not found", id))
                ));
   }

}
