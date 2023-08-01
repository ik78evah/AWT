package com.group3.mBaaS.feature;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * This class represents an request to create or update a feature.
 */

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateOrUpdateFeatureRequest {

    @NotBlank
    private String path;
    @NotBlank
    private String uri;

    public CreateOrUpdateFeatureRequest(String path, String uri) {
        this.path = path;
        this.uri = uri;
    }
}
