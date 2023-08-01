package com.group3.mBaaS.analytics.web;

import com.group3.mBaaS.analytics.LogEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class WebLogEvent extends LogEvent {

    // Operating system of client app
    public String operatingSystem;

    // Browser name
    public String browser;

    // Language and local setting of app
    public String locale;

    // Web version that sent the event
    public String webVersion;

    // Page the user is currently on
    public String page;

    public WebLogEvent(String name, String description, String operatingSystem, String browser, String locale, String webVersion, String page, Map<String, Object> parameters, Integer project) {
        super(name, description, LogEvent.Source.WEB, parameters, project);
        this.operatingSystem = operatingSystem;
        this.browser = browser;
        this.locale = locale;
        this.webVersion = webVersion;
        this.page = page;
        this.hash = this.hashCode();
    }

    public WebLogEvent() {
        super(Source.WEB);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), operatingSystem, browser, locale, webVersion, page);
    }

}
