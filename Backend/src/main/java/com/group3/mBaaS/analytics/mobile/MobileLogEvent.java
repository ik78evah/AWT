package com.group3.mBaaS.analytics.mobile;

import com.group3.mBaaS.analytics.LogEvent;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class MobileLogEvent extends LogEvent {

    // Operating system of client app
    public String operatingSystem;

    // Language and local setting of app
    public String locale;

    // App version that sent the event
    public String appVersion;

    // Screen the user is currently on
    public String screen;

    public MobileLogEvent(String name, String description, String operatingSystem, String locale, String appVersion, String screen, Map<String, Object> parameters, Integer project) {
        super(name, description, Source.APP, parameters, project);
        this.operatingSystem = operatingSystem;
        this.locale = locale;
        this.appVersion = appVersion;
        this.screen = screen;
        this.hash = this.hashCode();
    }

    public MobileLogEvent() {
        super(Source.APP);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), operatingSystem, appVersion, locale, screen);
    }

}
