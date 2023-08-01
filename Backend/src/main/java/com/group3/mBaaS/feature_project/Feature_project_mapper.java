package com.group3.mBaaS.feature_project;

import org.springframework.data.annotation.Id;

/**
 * This class represents the mapping of a project to a feature.
 */
public class Feature_project_mapper {

    @Id
    public Integer id;

    public Integer featureid;
    public Integer projectid;

    public Feature_project_mapper(Integer featureid, Integer projectid){
        this.featureid = featureid;
        this.projectid = projectid;
    }


}
