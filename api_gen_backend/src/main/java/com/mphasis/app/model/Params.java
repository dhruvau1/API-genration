package com.mphasis.app.model;

public class Params {

    private String paramName;
	private String paramType;
	private String paramRequestType;

    public Params()
    {
        
    }

    public Params(String paramName, String paramType, String paramRequestType) {
        this.paramName = paramName;
        this.paramType = paramType;
        this.paramRequestType = paramRequestType;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getParamRequestType() {
        return paramRequestType;
    }

    public void setParamRequestType(String paramRequestType) {
        this.paramRequestType = paramRequestType;
    }

}
