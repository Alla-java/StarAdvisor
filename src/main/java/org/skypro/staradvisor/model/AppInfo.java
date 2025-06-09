package org.skypro.staradvisor.model;

public class AppInfo {
    private final String name;
    private final String version;

    public AppInfo(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }
}
