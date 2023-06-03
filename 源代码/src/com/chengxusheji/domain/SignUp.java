package com.chengxusheji.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUp {
    /*报名id*/
    private int signId;
    public int getSignId() {
        return signId;
    }
    public void setSignId(int signId) {
        this.signId = signId;
    }

    /*报名的活动*/
    private ActivityInfo activityObj;
    public ActivityInfo getActivityObj() {
        return activityObj;
    }
    public void setActivityObj(ActivityInfo activityObj) {
        this.activityObj = activityObj;
    }

    /*相亲用户*/
    private UserInfo userObj;
    public UserInfo getUserObj() {
        return userObj;
    }
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }

    /*报名时间*/
    private String signUpTime;
    public String getSignUpTime() {
        return signUpTime;
    }
    public void setSignUpTime(String signUpTime) {
        this.signUpTime = signUpTime;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonSignUp=new JSONObject(); 
		jsonSignUp.accumulate("signId", this.getSignId());
		jsonSignUp.accumulate("activityObj", this.getActivityObj().getTitle());
		jsonSignUp.accumulate("activityObjPri", this.getActivityObj().getActivityId());
		jsonSignUp.accumulate("userObj", this.getUserObj().getName());
		jsonSignUp.accumulate("userObjPri", this.getUserObj().getUser_name());
		jsonSignUp.accumulate("signUpTime", this.getSignUpTime());
		return jsonSignUp;
    }}