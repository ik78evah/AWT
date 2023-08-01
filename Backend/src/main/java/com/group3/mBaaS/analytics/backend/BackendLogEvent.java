package com.group3.mBaaS.analytics.backend;

import com.group3.mBaaS.analytics.LogEvent;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class BackendLogEvent extends LogEvent {

    // Fixed UUID for backend logs
    static private final Integer backendUUID = 0;

    // Path of the API
    public String apiPath;

    public BackendLogEvent(String name, String description, String apiPath, Map<String, Object> parameters) {
        super(name, description, Source.API, parameters, backendUUID);
        this.apiPath = apiPath;
        this.hash = this.hashCode();
    }

    public BackendLogEvent() {
        super(Source.API);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), apiPath);
    }
}
