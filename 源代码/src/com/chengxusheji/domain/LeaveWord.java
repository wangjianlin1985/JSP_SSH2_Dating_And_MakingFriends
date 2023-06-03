package com.chengxusheji.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class LeaveWord {
    /*记录id*/
    private int leaveWordId;
    public int getLeaveWordId() {
        return leaveWordId;
    }
    public void setLeaveWordId(int leaveWordId) {
        this.leaveWordId = leaveWordId;
    }

    /*留言标题*/
    private String title;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    /*留言内容*/
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    /*留言时间*/
    private String addTime;
    public String getAddTime() {
        return addTime;
    }
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    /*留言人*/
    private UserInfo userObj;
    public UserInfo getUserObj() {
        return userObj;
    }
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }

    /*回复内容*/
    private String replyContent;
    public String getReplyContent() {
        return replyContent;
    }
    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    /*回复时间*/
    private String replyTime;
    public String getReplyTime() {
        return replyTime;
    }
    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonLeaveWord=new JSONObject(); 
		jsonLeaveWord.accumulate("leaveWordId", this.getLeaveWordId());
		jsonLeaveWord.accumulate("title", this.getTitle());
		jsonLeaveWord.accumulate("content", this.getContent());
		jsonLeaveWord.accumulate("addTime", this.getAddTime());
		jsonLeaveWord.accumulate("userObj", this.getUserObj().getName());
		jsonLeaveWord.accumulate("userObjPri", this.getUserObj().getUser_name());
		jsonLeaveWord.accumulate("replyContent", this.getReplyContent());
		jsonLeaveWord.accumulate("replyTime", this.getReplyTime());
		return jsonLeaveWord;
    }}