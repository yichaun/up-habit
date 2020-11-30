package com.up.habit.expand.swagger.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO:
 * <p>
 * @author 王剑洪 on 2019/10/25 14:33
 */
public class Operation {
    private List<String> tags = new ArrayList<>();
    private String summary;
    private String description;
    private String operationId;
    private List<String> consumes = new ArrayList<>();
    private List<String> produces = new ArrayList<>();
    private List<Parameter> parameters = new ArrayList<>();
    private Map<String, Response> responses = new HashMap<>();

    public Operation() {
    }

    public Operation(String summary, String description, String operationId) {
        this.summary = summary;
        this.description = description;
        this.operationId = operationId;
    }

    public Operation addTag(String tag) {
        this.tags.add(tag);
        return this;
    }

    public Operation addConsume(String consume) {
        consumes.add(consume);
        return this;
    }

    public Operation addProduce(String produce) {
        produces.add(produce);
        return this;
    }

    public Operation addParameter(Parameter parameter) {
        parameters.add(parameter);
        return this;
    }

    public Operation addResponse(String key, Response response) {
        responses.put(key, response);
        return this;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public List<String> getConsumes() {
        return consumes;
    }

    public void setConsumes(List<String> consumes) {
        this.consumes = consumes;
    }

    public List<String> getProduces() {
        return produces;
    }

    public void setProduces(List<String> produces) {
        this.produces = produces;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public Map<String, Response> getResponses() {
        return responses;
    }

    public void setResponses(Map<String, Response> responses) {
        this.responses = responses;
    }
}
