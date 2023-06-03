package com.chengxusheji.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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
import com.chengxusheji.dao.LeaveWordDAO;
import com.chengxusheji.domain.LeaveWord;
import com.chengxusheji.dao.UserInfoDAO;
import com.chengxusheji.domain.UserInfo;
@Controller @Scope("prototype")
public class LeaveWordAction extends ActionSupport {

    /*界面层需要查询的属性: 留言标题*/
    private String title;
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return this.title;
    }

    /*界面层需要查询的属性: 留言时间*/
    private String addTime;
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
    public String getAddTime() {
        return this.addTime;
    }

    /*界面层需要查询的属性: 留言人*/
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

    private int leaveWordId;
    public void setLeaveWordId(int leaveWordId) {
        this.leaveWordId = leaveWordId;
    }
    public int getLeaveWordId() {
        return leaveWordId;
    }

    /*要删除的记录主键集合*/
    private String leaveWordIds;
    public String getLeaveWordIds() {
		return leaveWordIds;
	}
	public void setLeaveWordIds(String leaveWordIds) {
		this.leaveWordIds = leaveWordIds;
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
    @Resource LeaveWordDAO leaveWordDAO;

    @Resource UserInfoDAO userInfoDAO;
    /*待操作的LeaveWord对象*/
    private LeaveWord leaveWord;
    public void setLeaveWord(LeaveWord leaveWord) {
        this.leaveWord = leaveWord;
    }
    public LeaveWord getLeaveWord() {
        return this.leaveWord;
    }

    /*ajax添加LeaveWord信息*/
    @SuppressWarnings("deprecation")
    public void ajaxAddLeaveWord() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try {
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(leaveWord.getUserObj().getUser_name());
            leaveWord.setUserObj(userObj);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            leaveWord.setAddTime(sdf.format(new java.util.Date()));
            leaveWordDAO.AddLeaveWord(leaveWord);
            success = true;
            writeJsonResponse(success, message); 
        } catch (Exception e) {
            e.printStackTrace();
            message = "LeaveWord添加失败!";
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

    /*查询LeaveWord信息*/
    public void ajaxQueryLeaveWord() throws IOException, JSONException {
        if(page == 0) page = 1;
        if(title == null) title = "";
        if(addTime == null) addTime = "";
        if(rows != 0) leaveWordDAO.setRows(rows);
        List<LeaveWord> leaveWordList = leaveWordDAO.QueryLeaveWordInfo(title, addTime, userObj, page);
        /*计算总的页数和总的记录数*/
        leaveWordDAO.CalculateTotalPageAndRecordNumber(title, addTime, userObj);
        /*获取到总的页码数目*/
        totalPage = leaveWordDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = leaveWordDAO.getRecordNumber();
        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(LeaveWord leaveWord:leaveWordList) {
			JSONObject jsonLeaveWord = leaveWord.getJsonObject();
			jsonArray.put(jsonLeaveWord);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
    }

    /*查询LeaveWord信息*/
    public void ajaxQueryAllLeaveWord() throws IOException, JSONException {
        List<LeaveWord> leaveWordList = leaveWordDAO.QueryAllLeaveWordInfo();        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONArray jsonArray = new JSONArray();
		for(LeaveWord leaveWord:leaveWordList) {
			JSONObject jsonLeaveWord = new JSONObject();
			jsonLeaveWord.accumulate("leaveWordId", leaveWord.getLeaveWordId());
			jsonLeaveWord.accumulate("title", leaveWord.getTitle());
			jsonArray.put(jsonLeaveWord);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
    }

    /*查询要修改的LeaveWord信息*/
    public void ajaxModifyLeaveWordQuery() throws IOException, JSONException {
        /*根据主键leaveWordId获取LeaveWord对象*/
        LeaveWord leaveWord = leaveWordDAO.GetLeaveWordByLeaveWordId(leaveWordId);

        HttpServletResponse response=ServletActionContext.getResponse(); 
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		//将要被返回到客户端的对象 
		JSONObject jsonLeaveWord = leaveWord.getJsonObject(); 
		out.println(jsonLeaveWord.toString()); 
		out.flush();
		out.close();
    };

    /*更新修改LeaveWord信息*/
    public void ajaxModifyLeaveWord() throws IOException, JSONException{
    	String message = "";
    	boolean success = false;
        try {
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(leaveWord.getUserObj().getUser_name());
            leaveWord.setUserObj(userObj);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            leaveWord.setReplyTime(sdf.format(new java.util.Date()));
            leaveWordDAO.UpdateLeaveWord(leaveWord);
            success = true;
            writeJsonResponse(success, message);
        } catch (Exception e) {
            message = "LeaveWord修改失败!"; 
            writeJsonResponse(success, message);
       }
   }

    /*删除LeaveWord信息*/
    public void ajaxDeleteLeaveWord() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try { 
        	String _leaveWordIds[] = leaveWordIds.split(",");
        	for(String _leaveWordId: _leaveWordIds) {
        		leaveWordDAO.DeleteLeaveWord(Integer.parseInt(_leaveWordId));
        	}
        	success = true;
        	message = _leaveWordIds.length + "条记录删除成功";
        	writeJsonResponse(success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(success, message);
        }
    }

    /*前台查询LeaveWord信息*/
    public String FrontQueryLeaveWord() {
        if(page == 0) page = 1;
        if(title == null) title = "";
        if(addTime == null) addTime = "";
        List<LeaveWord> leaveWordList = leaveWordDAO.QueryLeaveWordInfo(title, addTime, userObj, page);
        /*计算总的页数和总的记录数*/
        leaveWordDAO.CalculateTotalPageAndRecordNumber(title, addTime, userObj);
        /*获取到总的页码数目*/
        totalPage = leaveWordDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = leaveWordDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("leaveWordList",  leaveWordList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("page", page);
        ctx.put("title", title);
        ctx.put("addTime", addTime);
        ctx.put("userObj", userObj);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        return "front_query_view";
    }

    
    
    /*前台学生查询LeaveWord信息*/
    public String FrontUserQueryLeaveWord() {
    	ActionContext ctx = ActionContext.getContext();
        if(page == 0) page = 1;
        if(title == null) title = "";
        if(addTime == null) addTime = "";
        userObj = new UserInfo();
        userObj.setUser_name(ctx.getSession().get("user_name").toString());
        List<LeaveWord> leaveWordList = leaveWordDAO.QueryLeaveWordInfo(title, addTime, userObj, page);
        /*计算总的页数和总的记录数*/
        leaveWordDAO.CalculateTotalPageAndRecordNumber(title, addTime, userObj);
        /*获取到总的页码数目*/
        totalPage = leaveWordDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = leaveWordDAO.getRecordNumber();
       
        ctx.put("leaveWordList",  leaveWordList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("page", page);
        ctx.put("title", title);
        ctx.put("addTime", addTime); 
        
        return "front_user_query_view";
    }
    
    
    /*查询要修改的LeaveWord信息*/
    public String FrontShowLeaveWordQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键leaveWordId获取LeaveWord对象*/
        LeaveWord leaveWord = leaveWordDAO.GetLeaveWordByLeaveWordId(leaveWordId);

        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("leaveWord",  leaveWord);
        return "front_show_view";
    }

    /*删除LeaveWord信息*/
    public String DeleteLeaveWord() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            leaveWordDAO.DeleteLeaveWord(leaveWordId);
            ctx.put("message", "LeaveWord删除成功!");
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error", "LeaveWord删除失败!");
            return "error";
        }
    }

    /*后台导出到excel*/
    public String queryLeaveWordOutputToExcel() { 
        if(title == null) title = "";
        if(addTime == null) addTime = "";
        List<LeaveWord> leaveWordList = leaveWordDAO.QueryLeaveWordInfo(title,addTime,userObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "LeaveWord信息记录"; 
        String[] headers = { "记录id","留言标题","留言时间","留言人","回复时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<leaveWordList.size();i++) {
        	LeaveWord leaveWord = leaveWordList.get(i); 
        	dataset.add(new String[]{leaveWord.getLeaveWordId() + "",leaveWord.getTitle(),leaveWord.getAddTime(),leaveWord.getUserObj().getName(),
leaveWord.getReplyTime()});
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
			response.setHeader("Content-disposition","attachment; filename="+"LeaveWord.xls");//filename是下载的xls的名，建议最好用英文 
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
