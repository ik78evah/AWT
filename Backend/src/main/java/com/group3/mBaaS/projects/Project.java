package com.group3.mBaaS.projects;

import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents the project object.
 */



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    // SQL identifier
    @Id
    public Integer id;

    // Project name
    public String name;

    // Project creation data
    public String creationDate;

    public Project(String name) {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.creationDate = myDateObj.format(myFormatObj);
        this.name = name;
    }
}
