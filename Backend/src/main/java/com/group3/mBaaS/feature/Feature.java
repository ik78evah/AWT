package com.group3.mBaaS.feature;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class represents the feature object.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Feature {

    @Id
    public Integer id;

    public String path;

    public String creationDate;

    public String uri;

    public Feature(String path, String uri){
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.creationDate = myDateObj.format(myFormatObj);
        this.path = path;
        this.uri = uri;
    }


}
