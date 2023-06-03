package com.chengxusheji.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.UUID;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.chengxusheji.utils.ExportExcelUtil;
import com.chengxusheji.dao.NewsDAO;
import com.chengxusheji.domain.News;
@Controller @Scope("prototype")
public class NewsAction extends ActionSupport {

    /*界面层需要查询的属性: 标题*/
    private String title;
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return this.title;
    }

    /*界面层需要查询的属性: 发布日期*/
    private String publishDate;
    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }
    public String getPublishDate() {
        return this.publishDate;
    }

    /*当前第几页*/
    private int page;
    public void setPage(int page) {
        this.page = page;
    }
    public int getPage() {
        return page;
    }

    /*每页显示多少条数据*/
    private int rows;
    public void setRows(int rows) {
    	this.rows = rows;
    }
    public int getRows() {
    	return this.rows;
    }
    /*一共多少页*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    private int newsId;
    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }
    public int getNewsId() {
        return newsId;
    }

    /*要删除的记录主键集合*/
    private String newsIds;
    public String getNewsIds() {
		return newsIds;
	}
	public void setNewsIds(String newsIds) {
		this.newsIds = newsIds;
	}
    /*当前查询的总记录数目*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*业务层对象*/
    @Resource NewsDAO newsDAO;

    /*待操作的News对象*/
    private News news;
    public void setNews(News news) {
        this.news = news;
    }
    public News getNews() {
        return this.news;
    }

    /*ajax添加News信息*/
    @SuppressWarnings("deprecation")
    public void ajaxAddNews() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try {
            newsDAO.AddNews(news);
            success = true;
            writeJsonResponse(success, message); 
        } catch (Exception e) {
            e.printStackTrace();
            message = "News添加失败!";
            writeJsonResponse(success, message);
        }
    }

    /*向客户端输出操作成功或失败信息*/
	private void writeJsonResponse(boolean success,String message)
			throws IOException, JSONException {
		HttpServletResponse response=ServletActionContext.getResponse(); 
		response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		//将要被返回到客户端的对象 
		JSONObject json=new JSONObject();
		json.accumulate("success", success);
		json.accumulate("message", message);
		out.println(json.toString());
		out.flush(); 
		out.close();
	}

    /*查询News信息*/
    public void ajaxQueryNews() throws IOException, JSONException {
        if(page == 0) page = 1;
        if(title == null) title = "";
        if(publishDate == null) publishDate = "";
        if(rows != 0) newsDAO.setRows(rows);
        List<News> newsList = newsDAO.QueryNewsInfo(title, publishDate, page);
        /*计算总的页数和总的记录数*/
        newsDAO.CalculateTotalPageAndRecordNumber(title, publishDate);
        /*获取到总的页码数目*/
        totalPage = newsDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = newsDAO.getRecordNumber();
        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(News news:newsList) {
			JSONObject jsonNews = news.getJsonObject();
			jsonArray.put(jsonNews);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
    }

    /*查询News信息*/
    public void ajaxQueryAllNews() throws IOException, JSONException {
        List<News> newsList = newsDAO.QueryAllNewsInfo();        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONArray jsonArray = new JSONArray();
		for(News news:newsList) {
			JSONObject jsonNews = new JSONObject();
			jsonNews.accumulate("newsId", news.getNewsId());
			jsonNews.accumulate("title", news.getTitle());
			jsonArray.put(jsonNews);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
    }

    /*查询要修改的News信息*/
    public void ajaxModifyNewsQuery() throws IOException, JSONException {
        /*根据主键newsId获取News对象*/
        News news = newsDAO.GetNewsByNewsId(newsId);

        HttpServletResponse response=ServletActionContext.getResponse(); 
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		//将要被返回到客户端的对象 
		JSONObject jsonNews = news.getJsonObject(); 
		out.println(jsonNews.toString()); 
		out.flush();
		out.close();
    };

    /*更新修改News信息*/
    public void ajaxModifyNews() throws IOException, JSONException{
    	String message = "";
    	boolean success = false;
        try {
            newsDAO.UpdateNews(news);
            success = true;
            writeJsonResponse(success, message);
        } catch (Exception e) {
            message = "News修改失败!"; 
            writeJsonResponse(success, message);
       }
   }

    /*删除News信息*/
    public void ajaxDeleteNews() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try { 
        	String _newsIds[] = newsIds.split(",");
        	for(String _newsId: _newsIds) {
        		newsDAO.DeleteNews(Integer.parseInt(_newsId));
        	}
        	success = true;
        	message = _newsIds.length + "条记录删除成功";
        	writeJsonResponse(success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(success, message);
        }
    }

    /*前台查询News信息*/
    public String FrontQueryNews() {
        if(page == 0) page = 1;
        if(title == null) title = "";
        if(publishDate == null) publishDate = "";
        List<News> newsList = newsDAO.QueryNewsInfo(title, publishDate, page);
        /*计算总的页数和总的记录数*/
        newsDAO.CalculateTotalPageAndRecordNumber(title, publishDate);
        /*获取到总的页码数目*/
        totalPage = newsDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = newsDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("newsList",  newsList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("page", page);
        ctx.put("title", title);
        ctx.put("publishDate", publishDate);
        return "front_query_view";
    }

    /*查询要修改的News信息*/
    public String FrontShowNewsQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键newsId获取News对象*/
        News news = newsDAO.GetNewsByNewsId(newsId);

        ctx.put("news",  news);
        return "front_show_view";
    }

    /*删除News信息*/
    public String DeleteNews() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            newsDAO.DeleteNews(newsId);
            ctx.put("message", "News删除成功!");
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error", "News删除失败!");
            return "error";
        }
    }

    /*后台导出到excel*/
    public String queryNewsOutputToExcel() { 
        if(title == null) title = "";
        if(publishDate == null) publishDate = "";
        List<News> newsList = newsDAO.QueryNewsInfo(title,publishDate);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "News信息记录"; 
        String[] headers = { "相亲资讯id","标题","发布日期"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<newsList.size();i++) {
        	News news = newsList.get(i); 
        	dataset.add(new String[]{news.getNewsId() + "",news.getTitle(),news.getPublishDate()});
        }
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		HttpServletResponse response = null;//创建一个HttpServletResponse对象 
		OutputStream out = null;//创建一个输出流对象 
		try { 
			response = ServletActionContext.getResponse();//初始化HttpServletResponse对象 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"News.xls");//filename是下载的xls的名，建议最好用英文 
			response.setContentType("application/msexcel;charset=UTF-8");//设置类型 
			response.setHeader("Pragma","No-cache");//设置头 
			response.setHeader("Cache-Control","no-cache");//设置头 
			response.setDateHeader("Expires", 0);//设置日期头  
			String rootPath = ServletActionContext.getServletContext().getRealPath("/");
			ex.exportExcel(rootPath,title,headers, dataset, out);
			out.flush();
		} catch (IOException e) { 
			e.printStackTrace(); 
		}finally{
			try{
				if(out!=null){ 
					out.close(); 
				}
			}catch(IOException e){ 
				e.printStackTrace(); 
			} 
		}
		return null;
    }
}
