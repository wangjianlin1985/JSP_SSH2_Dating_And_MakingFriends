package com.chengxusheji.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class News {
    /*相亲资讯id*/
    private int newsId;
    public int getNewsId() {
        return newsId;
    }
    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    /*标题*/
    private String title;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    /*资讯内容*/
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    /*发布日期*/
    private String publishDate;
    public String getPublishDate() {
        return publishDate;
    }
    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonNews=new JSONObject(); 
		jsonNews.accumulate("newsId", this.getNewsId());
		jsonNews.accumulate("title", this.getTitle());
		jsonNews.accumulate("content", this.getContent());
		jsonNews.accumulate("publishDate", this.getPublishDate().length()>19?this.getPublishDate().substring(0,19):this.getPublishDate());
		return jsonNews;
    }}