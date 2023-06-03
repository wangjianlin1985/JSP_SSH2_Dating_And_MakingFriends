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
import com.chengxusheji.dao.SignUpDAO;
import com.chengxusheji.dao.UserInfoDAO;
import com.chengxusheji.domain.ActivityInfo;
import com.chengxusheji.domain.SignUp;
import com.chengxusheji.domain.UserInfo;
import com.chengxusheji.utils.ExportExcelUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
@Controller @Scope("prototype")
public class SignUpAction extends ActionSupport {

    /*界面层需要查询的属性: 报名的活动*/
    private ActivityInfo activityObj;
    public void setActivityObj(ActivityInfo activityObj) {
        this.activityObj = activityObj;
    }
    public ActivityInfo getActivityObj() {
        return this.activityObj;
    }

    /*界面层需要查询的属性: 相亲用户*/
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

    private int signId;
    public void setSignId(int signId) {
        this.signId = signId;
    }
    public int getSignId() {
        return signId;
    }

    /*要删除的记录主键集合*/
    private String signIds;
    public String getSignIds() {
		return signIds;
	}
	public void setSignIds(String signIds) {
		this.signIds = signIds;
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
    @Resource SignUpDAO signUpDAO;

    @Resource ActivityInfoDAO activityInfoDAO;
    @Resource UserInfoDAO userInfoDAO;
    /*待操作的SignUp对象*/
    private SignUp signUp;
    public void setSignUp(SignUp signUp) {
        this.signUp = signUp;
    }
    public SignUp getSignUp() {
        return this.signUp;
    }

    /*ajax添加SignUp信息*/
    @SuppressWarnings("deprecation")
    public void ajaxAddSignUp() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try {
            ActivityInfo activityObj = activityInfoDAO.GetActivityInfoByActivityId(signUp.getActivityObj().getActivityId());
            signUp.setActivityObj(activityObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(signUp.getUserObj().getUser_name());
            signUp.setUserObj(userObj);
            signUpDAO.AddSignUp(signUp);
            success = true;
            writeJsonResponse(success, message); 
        } catch (Exception e) {
            e.printStackTrace();
            message = "SignUp添加失败!";
            writeJsonResponse(success, message);
        }
    }
    
    
    /*ajax用户报名添加SignUp信息*/
    @SuppressWarnings("deprecation")
    public void ajaxUserAddSignUp() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try {
            ActivityInfo activityObj = activityInfoDAO.GetActivityInfoByActivityId(signUp.getActivityObj().getActivityId());
            signUp.setActivityObj(activityObj); 
            String user_name = (String)ActionContext.getContext().getSession().get("user_name");
            if(user_name == null) {
            	message = "请先登录系统!";
                writeJsonResponse(success, message);
                return ;
            }
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(user_name);
            signUp.setUserObj(userObj);
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            signUp.setSignUpTime(sdf.format(new java.util.Date()));
            
            if(signUpDAO.QuerySignUpInfo(activityObj, userObj).size()>0) {
            	message = "你已经报过名了!";
                writeJsonResponse(success, message);
                return ;
            }
             
            signUpDAO.AddSignUp(signUp);
            success = true;
            writeJsonResponse(success, message); 
        } catch (Exception e) {
            e.printStackTrace();
            message = "报名失败!";
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

    /*查询SignUp信息*/
    public void ajaxQuerySignUp() throws IOException, JSONException {
        if(page == 0) page = 1;
        if(rows != 0) signUpDAO.setRows(rows);
        List<SignUp> signUpList = signUpDAO.QuerySignUpInfo(activityObj, userObj, page);
        /*计算总的页数和总的记录数*/
        signUpDAO.CalculateTotalPageAndRecordNumber(activityObj, userObj);
        /*获取到总的页码数目*/
        totalPage = signUpDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = signUpDAO.getRecordNumber();
        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(SignUp signUp:signUpList) {
			JSONObject jsonSignUp = signUp.getJsonObject();
			jsonArray.put(jsonSignUp);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
    }

    /*查询SignUp信息*/
    public void ajaxQueryAllSignUp() throws IOException, JSONException {
        List<SignUp> signUpList = signUpDAO.QueryAllSignUpInfo();        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONArray jsonArray = new JSONArray();
		for(SignUp signUp:signUpList) {
			JSONObject jsonSignUp = new JSONObject();
			jsonSignUp.accumulate("signId", signUp.getSignId());
			jsonSignUp.accumulate("signId", signUp.getSignId());
			jsonArray.put(jsonSignUp);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
    }

    /*查询要修改的SignUp信息*/
    public void ajaxModifySignUpQuery() throws IOException, JSONException {
        /*根据主键signId获取SignUp对象*/
        SignUp signUp = signUpDAO.GetSignUpBySignId(signId);

        HttpServletResponse response=ServletActionContext.getResponse(); 
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		//将要被返回到客户端的对象 
		JSONObject jsonSignUp = signUp.getJsonObject(); 
		out.println(jsonSignUp.toString()); 
		out.flush();
		out.close();
    };

    /*更新修改SignUp信息*/
    public void ajaxModifySignUp() throws IOException, JSONException{
    	String message = "";
    	boolean success = false;
        try {
            ActivityInfo activityObj = activityInfoDAO.GetActivityInfoByActivityId(signUp.getActivityObj().getActivityId());
            signUp.setActivityObj(activityObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(signUp.getUserObj().getUser_name());
            signUp.setUserObj(userObj);
            signUpDAO.UpdateSignUp(signUp);
            success = true;
            writeJsonResponse(success, message);
        } catch (Exception e) {
            message = "SignUp修改失败!"; 
            writeJsonResponse(success, message);
       }
   }

    /*删除SignUp信息*/
    public void ajaxDeleteSignUp() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try { 
        	String _signIds[] = signIds.split(",");
        	for(String _signId: _signIds) {
        		signUpDAO.DeleteSignUp(Integer.parseInt(_signId));
        	}
        	success = true;
        	message = _signIds.length + "条记录删除成功";
        	writeJsonResponse(success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(success, message);
        }
    }

    /*前台查询SignUp信息*/
    public String FrontQuerySignUp() {
        if(page == 0) page = 1;
        List<SignUp> signUpList = signUpDAO.QuerySignUpInfo(activityObj, userObj, page);
        /*计算总的页数和总的记录数*/
        signUpDAO.CalculateTotalPageAndRecordNumber(activityObj, userObj);
        /*获取到总的页码数目*/
        totalPage = signUpDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = signUpDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("signUpList",  signUpList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("page", page);
        ctx.put("activityObj", activityObj);
        List<ActivityInfo> activityInfoList = activityInfoDAO.QueryAllActivityInfoInfo();
        ctx.put("activityInfoList", activityInfoList);
        ctx.put("userObj", userObj);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        return "front_query_view";
    }

    
    /*前台用户查询SignUp信息*/
    public String FrontUserQuerySignUp() {
    	ActionContext ctx = ActionContext.getContext(); 
    	userObj = new UserInfo();
    	userObj.setUser_name(ctx.getSession().get("user_name").toString());
        if(page == 0) page = 1;
        List<SignUp> signUpList = signUpDAO.QuerySignUpInfo(activityObj, userObj, page);
        /*计算总的页数和总的记录数*/
        signUpDAO.CalculateTotalPageAndRecordNumber(activityObj, userObj);
        /*获取到总的页码数目*/
        totalPage = signUpDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = signUpDAO.getRecordNumber();
        
        ctx.put("signUpList",  signUpList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("page", page);
        ctx.put("activityObj", activityObj);
        List<ActivityInfo> activityInfoList = activityInfoDAO.QueryAllActivityInfoInfo();
        ctx.put("activityInfoList", activityInfoList);
         
        return "front_user_query_view";
    }
    
    /*查询要修改的SignUp信息*/
    public String FrontShowSignUpQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键signId获取SignUp对象*/
        SignUp signUp = signUpDAO.GetSignUpBySignId(signId);

        List<ActivityInfo> activityInfoList = activityInfoDAO.QueryAllActivityInfoInfo();
        ctx.put("activityInfoList", activityInfoList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("signUp",  signUp);
        return "front_show_view";
    }

    /*删除SignUp信息*/
    public String DeleteSignUp() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            signUpDAO.DeleteSignUp(signId);
            ctx.put("message", "SignUp删除成功!");
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error", "SignUp删除失败!");
            return "error";
        }
    }

    /*后台导出到excel*/
    public String querySignUpOutputToExcel() { 
        List<SignUp> signUpList = signUpDAO.QuerySignUpInfo(activityObj,userObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "SignUp信息记录"; 
        String[] headers = { "报名id","报名的活动","相亲用户","报名时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<signUpList.size();i++) {
        	SignUp signUp = signUpList.get(i); 
        	dataset.add(new String[]{signUp.getSignId() + "",signUp.getActivityObj().getTitle(),
signUp.getUserObj().getName(),
signUp.getSignUpTime()});
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
			response.setHeader("Content-disposition","attachment; filename="+"SignUp.xls");//filename是下载的xls的名，建议最好用英文 
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
