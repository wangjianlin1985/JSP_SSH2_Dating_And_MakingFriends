package com.chengxusheji.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityType {
    /*活动类型id*/
    private int typeId;
    public int getTypeId() {
        return typeId;
    }
    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    /*活动类型名称*/
    private String typeName;
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonActivityType=new JSONObject(); 
		jsonActivityType.accumulate("typeId", this.getTypeId());
		jsonActivityType.accumulate("typeName", this.getTypeName());
		return jsonActivityType;
    }}