package com.group3.mBaaS.analytics;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

// General logging event
public abstract class LogEvent {

    // Source where logging call was triggered from
    public enum Source {
        API,
        WEB,
        APP
    }

    @Id
    public Integer id;

    // Title of the event
    public String name;

    // Description of the event
    public String description;

    // Source where event was triggered
    public Source source;

    // Additional parameters
    public Map<String, Object> parameters;

    // Log identifier
    public UUID identifier;

    // Creation date of LogEvent
    public String creationDate;

    // Number of occurrences
    public Integer count = 1;

    // The project the log occurred in
    public Integer project;

    // Row hash used for counting occurrences
    public Integer hash;

    public LogEvent(String name, String description, Source source, Map<String, Object> parameters, Integer project) {
        this.identifier = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.source = source;
        this.parameters = parameters;
        this.project = project;
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.creationDate = myDateObj.format(myFormatObj);
    }

    public LogEvent(Source source) {
        this.source = source;
        this.identifier = UUID.randomUUID();
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.creationDate = myDateObj.format(myFormatObj);
    }

    // Hash values to compare equality of multiple logs (needed for the count calculation)
    @Override
    public int hashCode() {
        return Objects.hash(name, description, source, project);
    }

}
