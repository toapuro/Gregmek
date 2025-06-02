package dev.tdnpgm.gregmek.resource;

public enum GMResourceType {
    PLATE("plate");

    private final String registryPrefix;
    private final String baseTagPath;

    GMResourceType(String prefix) {
        this(prefix, prefix + "s");
    }

    GMResourceType(String prefix, String baseTagPath) {
        this.registryPrefix = prefix;
        this.baseTagPath = baseTagPath;
    }

    public String getRegistryPrefix() {
        return this.registryPrefix;
    }

    public String getBaseTagPath() {
        return this.baseTagPath;
    }
}
