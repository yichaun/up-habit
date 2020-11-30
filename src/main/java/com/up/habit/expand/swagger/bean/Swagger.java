package com.up.habit.expand.swagger.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Swagger {
    private String swagger = "2.0";
    private Info info;
    private String host;
    private String basePath;
    private List<Tag> tags = new ArrayList<>();
    private List<String> schemes = new ArrayList<>();
    private Map<String, Path> paths = new HashMap<>();

    public Swagger() {
    }

    public Swagger(String host, String basePath) {
        super();
        this.host = host;
        this.basePath = basePath;

    }


    public Swagger addTag(Tag Tag) {
        tags.add(Tag);
        return this;
    }

    public Swagger addPath(String key, Path path){
        paths.put(key,path);
        return this;
    }

    public String getSwagger() {
        return swagger;
    }

    public void setSwagger(String swagger) {
        this.swagger = swagger;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<String> getSchemes() {
        return schemes;
    }

    public void setSchemes(List<String> schemes) {
        this.schemes = schemes;
    }

    public Map<String, Path> getPaths() {
        return paths;
    }

    public void setPaths(Map<String, Path> paths) {
        this.paths = paths;
    }
}
