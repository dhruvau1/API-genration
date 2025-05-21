package com.mphasis.app.model;

import java.util.List;

public class Services {

    private List<String> field;
    private String mapping;
    private String action;
    private String orderBy;
    private String groupBy;
    private String returnType;
    private List<Params> params;

    public Services() {

    }

    public Services(List<String> field, String mapping, String action, String orderBy, String groupBy, String returnType, List<Params> params) {
        this.field = field;
        this.mapping = mapping;
        this.action = action;
        this.orderBy = orderBy;
        this.groupBy = groupBy;
        this.returnType = returnType;
        this.params = params;
    }

    public List<Params> getParams() {
        return params;
    }

    public void setParams(List<Params> params) {
        this.params = params;
    }

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public List<String> getField() {
        return field;
    }

    public void setField(List<String> field) {
        this.field = field;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public void setReturnType(String retType) {
        this.returnType = retType;
    }

    public String getReturnType() {
        return this.returnType;
    }

    @Override
    public String toString() {
        return "SpecialFunctions [field=" + field + ", action=" + action + ", order=" + orderBy + "]";
    }
}
