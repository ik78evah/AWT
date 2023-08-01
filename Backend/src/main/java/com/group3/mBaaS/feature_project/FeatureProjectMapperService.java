package com.group3.mBaaS.feature_project;

import com.group3.mBaaS.feature.Feature;
import com.group3.mBaaS.feature.FeatureRepository;
import com.group3.mBaaS.projects.Project;
import com.group3.mBaaS.projects.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is the interface for the interaction with the FeatureProjectMapperRepository.
 */
@Service
public class FeatureProjectMapperService {

    @Autowired
    FeatureProjectMapperRepository featureProjectMapperRepository;
    @Autowired
    FeatureRepository featureRepository;
    @Autowired
    ProjectRepository projectRepository;

    /**
     * Adds a new mapping of a feature with the featureId to a project with the projectId.
     * @param featureId
     * @param projectId
     * @return
     */
    public Mono<Void> addFeatureIdToProjectId(Integer featureId, Integer projectId){
        Feature_project_mapper newMapper = new Feature_project_mapper(featureId, projectId);
        return featureRepository.findById(featureId)
                .switchIfEmpty(Mono.error(
                        new RuntimeException(String.format("There is no feature with the id %d", featureId))
                ))
                .flatMap( feature -> projectRepository.findById(projectId))
                .switchIfEmpty(Mono.error(
                        new RuntimeException(String.format("There is no project with the id %d", projectId))
                ))
                .flatMap(project -> featureProjectMapperRepository.findAllByProjectid(project.getId())
                        .filter(feature_project_mapper -> featureId.equals(feature_project_mapper.featureid))
                        .singleOrEmpty())
                .switchIfEmpty(featureProjectMapperRepository.save(newMapper).flatMap(newmapping -> Mono.empty()))
                .flatMap(mapping -> Mono.error(new RuntimeException("The mapping of feature " + featureId + " to the project " + projectId + " already exists")))
                .then();
    }

    /**
     * Returns all mappings.
     * @return
     */
    public Flux<Feature_project_mapper> getAllMappings() {
        return featureProjectMapperRepository.findAll();
    }

    /**
     * Returns all mappings of the project with the projectId.
     *
     * @param projectid
     * @return
     */
    public Flux<Feature> findFeaturesOfProjectid(Integer projectid){
        return featureProjectMapperRepository.findAllByProjectid(projectid)
                .switchIfEmpty(Mono.error(
                        new RuntimeException(String.format("There are no features for project with the id %d", projectid))
                ))
                .flatMap(mapping -> featureRepository.findById(mapping.featureid));

    }

    /**
     * Delets all mappings.S
     * @return
     */
    public Mono<Void> deleteAll(){
        return featureProjectMapperRepository.deleteAll();
    }

    /**
     * Deletes all mappings for a project with the projectId.
     * @param projectid
     * @return
     */
    public Mono<Void> deleteAllMappingsForProject(Integer projectid){
        return featureProjectMapperRepository.findAllByProjectid(projectid)
                .flatMap(feature_project_mapper ->
                    featureProjectMapperRepository.delete(feature_project_mapper)
                )
                .then();
    }

    /**
     * Deletes all mappings for a feature with the featureId.
     * @param featureid
     * @return
     */
    public Mono<Void> deleteAllMappingsForFeature(Integer featureid){
        return featureProjectMapperRepository.findAllByFeatureid(featureid)
                .flatMap(feature_project_mapper ->
                        featureProjectMapperRepository.delete(feature_project_mapper)
                )
                .then();
    }

    /**
     * Deletes the mapping of a feature with the featureId to a project with the projectId.
     * @param featureid
     * @param projectid
     * @return
     */

    public Mono<Void> deleteMapping(Integer featureid, Integer projectid){
        return featureProjectMapperRepository.findAllByFeatureid(featureid)
                .filter(feature_project_mapper -> projectid.equals(feature_project_mapper.projectid))
                .singleOrEmpty()
                .switchIfEmpty(Mono.error(
                        new RuntimeException(String.format("There is no mapping for feature %d and project %s", featureid, projectid))
                ))
                .flatMap(mapping -> featureProjectMapperRepository.delete(mapping));
    }

}
