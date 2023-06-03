package com.chengxusheji.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityInfo {
    /*记录id*/
    private int activityId;
    public int getActivityId() {
        return activityId;
    }
    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    /*活动类别*/
    private ActivityType typeObj;
    public ActivityType getTypeObj() {
        return typeObj;
    }
    public void setTypeObj(ActivityType typeObj) {
        this.typeObj = typeObj;
    }

    /*活动主题*/
    private String title;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    /*活动内容*/
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    /*活动时间*/
    private String activityTime;
    public String getActivityTime() {
        return activityTime;
    }
    public void setActivityTime(String activityTime) {
        this.activityTime = activityTime;
    }

    /*已参与人数*/
    private int personNum;
    public int getPersonNum() {
        return personNum;
    }
    public void setPersonNum(int personNum) {
        this.personNum = personNum;
    }

    /*组织者*/
    private UserInfo userObj;
    public UserInfo getUserObj() {
        return userObj;
    }
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }

    /*发起时间*/
    private String addTime;
    public String getAddTime() {
        return addTime;
    }
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonActivityInfo=new JSONObject(); 
		jsonActivityInfo.accumulate("activityId", this.getActivityId());
		jsonActivityInfo.accumulate("typeObj", this.getTypeObj().getTypeName());
		jsonActivityInfo.accumulate("typeObjPri", this.getTypeObj().getTypeId());
		jsonActivityInfo.accumulate("title", this.getTitle());
		jsonActivityInfo.accumulate("content", this.getContent());
		jsonActivityInfo.accumulate("activityTime", this.getActivityTime());
		jsonActivityInfo.accumulate("personNum", this.getPersonNum());
		jsonActivityInfo.accumulate("userObj", this.getUserObj().getName());
		jsonActivityInfo.accumulate("userObjPri", this.getUserObj().getUser_name());
		jsonActivityInfo.accumulate("addTime", this.getAddTime());
		return jsonActivityInfo;
    }}