package com.chengxusheji.action;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.chengxusheji.dao.ActivityInfoDAO;
import com.chengxusheji.dao.ActivityTypeDAO;
import com.chengxusheji.dao.UserInfoDAO;
import com.chengxusheji.domain.ActivityInfo;
import com.chengxusheji.domain.ActivityType;
import com.chengxusheji.domain.UserInfo;
import com.chengxusheji.utils.ExportExcelUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
@Controller @Scope("prototype")
public class ActivityInfoAction extends ActionSupport {

    /*界面层需要查询的属性: 活动类别*/
    private ActivityType typeObj;
    public void setTypeObj(ActivityType typeObj) {
        this.typeObj = typeObj;
    }
    public ActivityType getTypeObj() {
        return this.typeObj;
    }

    /*界面层需要查询的属性: 活动主题*/
    private String title;
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return this.title;
    }

    /*界面层需要查询的属性: 活动时间*/
    private String activityTime;
    public void setActivityTime(String activityTime) {
        this.activityTime = activityTime;
    }
    public String getActivityTime() {
        return this.activityTime;
    }

    /*界面层需要查询的属性: 组织者*/
    private UserInfo userObj;
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }
    public UserInfo getUserObj() {
        return this.userObj;
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

    private int activityId;
    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }
    public int getActivityId() {
        return activityId;
    }

    /*要删除的记录主键集合*/
    private String activityIds;
    public String getActivityIds() {
		return activityIds;
	}
	public void setActivityIds(String activityIds) {
		this.activityIds = activityIds;
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
    @Resource ActivityInfoDAO activityInfoDAO;

    @Resource ActivityTypeDAO activityTypeDAO;
    @Resource UserInfoDAO userInfoDAO;
    /*待操作的ActivityInfo对象*/
    private ActivityInfo activityInfo;
    public void setActivityInfo(ActivityInfo activityInfo) {
        this.activityInfo = activityInfo;
    }
    public ActivityInfo getActivityInfo() {
        return this.activityInfo;
    }

    /*ajax添加ActivityInfo信息*/
    @SuppressWarnings("deprecation")
    public void ajaxAddActivityInfo() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try {
            ActivityType typeObj = activityTypeDAO.GetActivityTypeByTypeId(activityInfo.getTypeObj().getTypeId());
            activityInfo.setTypeObj(typeObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(activityInfo.getUserObj().getUser_name());
            activityInfo.setUserObj(userObj);
            activityInfoDAO.AddActivityInfo(activityInfo);
            success = true;
            writeJsonResponse(success, message); 
        } catch (Exception e) {
            e.printStackTrace();
            message = "ActivityInfo添加失败!";
            writeJsonResponse(success, message);
        }
    }
    
    
    /*ajax添加组织者发布ActivityInfo信息*/
    @SuppressWarnings("deprecation")
    public void ajaxUserAddActivityInfo() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try {
            ActivityType typeObj = activityTypeDAO.GetActivityTypeByTypeId(activityInfo.getTypeObj().getTypeId());
            activityInfo.setTypeObj(typeObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(activityInfo.getUserObj().getUser_name());
            activityInfo.setUserObj(userObj);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            activityInfo.setAddTime(sdf.format(new java.util.Date()));
            
            activityInfoDAO.AddActivityInfo(activityInfo);
            success = true;
            writeJsonResponse(success, message); 
        } catch (Exception e) {
            e.printStackTrace();
            message = "ActivityInfo添加失败!";
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

    /*查询ActivityInfo信息*/
    public void ajaxQueryActivityInfo() throws IOException, JSONException {
        if(page == 0) page = 1;
        if(title == null) title = "";
        if(activityTime == null) activityTime = "";
        if(rows != 0) activityInfoDAO.setRows(rows);
        List<ActivityInfo> activityInfoList = activityInfoDAO.QueryActivityInfoInfo(typeObj, title, activityTime, userObj, page);
        /*计算总的页数和总的记录数*/
        activityInfoDAO.CalculateTotalPageAndRecordNumber(typeObj, title, activityTime, userObj);
        /*获取到总的页码数目*/
        totalPage = activityInfoDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = activityInfoDAO.getRecordNumber();
        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(ActivityInfo activityInfo:activityInfoList) {
			JSONObject jsonActivityInfo = activityInfo.getJsonObject();
			jsonArray.put(jsonActivityInfo);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
    }

    /*查询ActivityInfo信息*/
    public void ajaxQueryAllActivityInfo() throws IOException, JSONException {
        List<ActivityInfo> activityInfoList = activityInfoDAO.QueryAllActivityInfoInfo();        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONArray jsonArray = new JSONArray();
		for(ActivityInfo activityInfo:activityInfoList) {
			JSONObject jsonActivityInfo = new JSONObject();
			jsonActivityInfo.accumulate("activityId", activityInfo.getActivityId());
			jsonActivityInfo.accumulate("title", activityInfo.getTitle());
			jsonArray.put(jsonActivityInfo);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
    }
 
    
    /*组织者查询自己发布的ActivityInfo信息*/
    public void ajaxUserQueryAllActivityInfo() throws IOException, JSONException {
    	String user_name = ActionContext.getContext().getSession().get("zuzhizhe").toString();
        List<ActivityInfo> activityInfoList = activityInfoDAO.userQueryAllActivityInfoInfo(user_name);       
        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONArray jsonArray = new JSONArray();
		for(ActivityInfo activityInfo:activityInfoList) {
			JSONObject jsonActivityInfo = new JSONObject();
			jsonActivityInfo.accumulate("activityId", activityInfo.getActivityId());
			jsonActivityInfo.accumulate("title", activityInfo.getTitle());
			jsonArray.put(jsonActivityInfo);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
    }
    
    
    /*查询要修改的ActivityInfo信息*/
    public void ajaxModifyActivityInfoQuery() throws IOException, JSONException {
        /*根据主键activityId获取ActivityInfo对象*/
        ActivityInfo activityInfo = activityInfoDAO.GetActivityInfoByActivityId(activityId);

        HttpServletResponse response=ServletActionContext.getResponse(); 
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		//将要被返回到客户端的对象 
		JSONObject jsonActivityInfo = activityInfo.getJsonObject(); 
		out.println(jsonActivityInfo.toString()); 
		out.flush();
		out.close();
    };

    /*更新修改ActivityInfo信息*/
    public void ajaxModifyActivityInfo() throws IOException, JSONException{
    	String message = "";
    	boolean success = false;
        try {
            ActivityType typeObj = activityTypeDAO.GetActivityTypeByTypeId(activityInfo.getTypeObj().getTypeId());
            activityInfo.setTypeObj(typeObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(activityInfo.getUserObj().getUser_name());
            activityInfo.setUserObj(userObj);
            activityInfoDAO.UpdateActivityInfo(activityInfo);
            success = true;
            writeJsonResponse(success, message);
        } catch (Exception e) {
            message = "ActivityInfo修改失败!"; 
            writeJsonResponse(success, message);
       }
   }

    /*删除ActivityInfo信息*/
    public void ajaxDeleteActivityInfo() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try { 
        	String _activityIds[] = activityIds.split(",");
        	for(String _activityId: _activityIds) {
        		activityInfoDAO.DeleteActivityInfo(Integer.parseInt(_activityId));
        	}
        	success = true;
        	message = _activityIds.length + "条记录删除成功";
        	writeJsonResponse(success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(success, message);
        }
    }

    /*前台查询ActivityInfo信息*/
    public String FrontQueryActivityInfo() {
        if(page == 0) page = 1;
        if(title == null) title = "";
        if(activityTime == null) activityTime = "";
        List<ActivityInfo> activityInfoList = activityInfoDAO.QueryActivityInfoInfo(typeObj, title, activityTime, userObj, page);
        /*计算总的页数和总的记录数*/
        activityInfoDAO.CalculateTotalPageAndRecordNumber(typeObj, title, activityTime, userObj);
        /*获取到总的页码数目*/
        totalPage = activityInfoDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = activityInfoDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("activityInfoList",  activityInfoList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("page", page);
        ctx.put("typeObj", typeObj);
        List<ActivityType> activityTypeList = activityTypeDAO.QueryAllActivityTypeInfo();
        ctx.put("activityTypeList", activityTypeList);
        ctx.put("title", title);
        ctx.put("activityTime", activityTime);
        ctx.put("userObj", userObj);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        return "front_query_view";
    }

    /*查询要修改的ActivityInfo信息*/
    public String FrontShowActivityInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键activityId获取ActivityInfo对象*/
        ActivityInfo activityInfo = activityInfoDAO.GetActivityInfoByActivityId(activityId);

        List<ActivityType> activityTypeList = activityTypeDAO.QueryAllActivityTypeInfo();
        ctx.put("activityTypeList", activityTypeList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("activityInfo",  activityInfo);
        return "front_show_view";
    }

    /*删除ActivityInfo信息*/
    public String DeleteActivityInfo() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            activityInfoDAO.DeleteActivityInfo(activityId);
            ctx.put("message", "ActivityInfo删除成功!");
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error", "ActivityInfo删除失败!");
            return "error";
        }
    }

    /*后台导出到excel*/
    public String queryActivityInfoOutputToExcel() { 
        if(title == null) title = "";
        if(activityTime == null) activityTime = "";
        List<ActivityInfo> activityInfoList = activityInfoDAO.QueryActivityInfoInfo(typeObj,title,activityTime,userObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "ActivityInfo信息记录"; 
        String[] headers = { "记录id","活动类别","活动主题","活动时间","已参与人数","组织者","发起时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<activityInfoList.size();i++) {
        	ActivityInfo activityInfo = activityInfoList.get(i); 
        	dataset.add(new String[]{activityInfo.getActivityId() + "",activityInfo.getTypeObj().getTypeName(),
activityInfo.getTitle(),activityInfo.getActivityTime(),activityInfo.getPersonNum() + "",activityInfo.getUserObj().getName(),
activityInfo.getAddTime()});
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
			response.setHeader("Content-disposition","attachment; filename="+"ActivityInfo.xls");//filename是下载的xls的名，建议最好用英文 
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
