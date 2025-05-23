package com.mphasis.app.model;

public class ModfiyModel {
    
    private String modelName;
    private Services services;
    private String requirement;
    private String type;

    public ModfiyModel(String modelName, Services services, String requirement, String type) {
        this.modelName = modelName;
        this.services = services;
        this.requirement = requirement;
        this.type = type;
    }

    public ModfiyModel() {
    }


    public String getType() {
        return type;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Services getServices() {
        return services;
    }

    public void setServices(Services services) {
        this.services = services;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public void setType(String type) {
        this.type = type;
    }

}
