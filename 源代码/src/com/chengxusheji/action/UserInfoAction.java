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
import com.chengxusheji.dao.UserInfoDAO;
import com.chengxusheji.domain.UserInfo;
@Controller @Scope("prototype")
public class UserInfoAction extends ActionSupport {

/*UserInfo类字段userPhoto参数接收*/
	 private File userPhotoFile;
	 private String userPhotoFileFileName;
	 private String userPhotoFileContentType;
	 public File getUserPhotoFile() {
		return userPhotoFile;
	}
	public void setUserPhotoFile(File userPhotoFile) {
		this.userPhotoFile = userPhotoFile;
	}
	public String getUserPhotoFileFileName() {
		return userPhotoFileFileName;
	}
	public void setUserPhotoFileFileName(String userPhotoFileFileName) {
		this.userPhotoFileFileName = userPhotoFileFileName;
	}
	public String getUserPhotoFileContentType() {
		return userPhotoFileContentType;
	}
	public void setUserPhotoFileContentType(String userPhotoFileContentType) {
		this.userPhotoFileContentType = userPhotoFileContentType;
	}
    /*界面层需要查询的属性: 用户名*/
    private String user_name;
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public String getUser_name() {
        return this.user_name;
    }

    /*界面层需要查询的属性: 姓名*/
    private String name;
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    /*界面层需要查询的属性: 出生日期*/
    private String birthday;
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public String getBirthday() {
        return this.birthday;
    }

    /*界面层需要查询的属性: 联系电话*/
    private String telephone;
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getTelephone() {
        return this.telephone;
    }

    /*界面层需要查询的属性: 注册时间*/
    private String registerTime;
    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }
    public String getRegisterTime() {
        return this.registerTime;
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

    /*要删除的记录主键集合*/
    private String user_names;
    public String getUser_names() {
		return user_names;
	}
	public void setUser_names(String user_names) {
		this.user_names = user_names;
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
    @Resource UserInfoDAO userInfoDAO;

    /*待操作的UserInfo对象*/
    private UserInfo userInfo;
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
    public UserInfo getUserInfo() {
        return this.userInfo;
    }

    /*ajax添加UserInfo信息*/
    @SuppressWarnings("deprecation")
    public void ajaxAddUserInfo() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        /*验证用户名是否已经存在*/
        String user_name = userInfo.getUser_name();
        UserInfo db_userInfo = userInfoDAO.GetUserInfoByUser_name(user_name);
        if(null != db_userInfo) {
        	message = "该用户名已经存在!";
        	writeJsonResponse(success, message);
        	return ;
        }
        try {
            String path = ServletActionContext.getServletContext().getRealPath("/upload"); 
            /*处理图片上传*/
            String userPhotoFileName = ""; 
       	 	if(userPhotoFile != null) {
       	 		InputStream is = new FileInputStream(userPhotoFile);
       			String fileContentType = this.getUserPhotoFileContentType();
       			if(fileContentType.equals("image/jpeg")  || fileContentType.equals("image/pjpeg"))
       				userPhotoFileName = UUID.randomUUID().toString() +  ".jpg";
       			else if(fileContentType.equals("image/gif"))
       				userPhotoFileName = UUID.randomUUID().toString() +  ".gif";
       			else {
       				message = "上传图片格式不正确!";
       				writeJsonResponse(success, message);
       				return ;
       			}
       			File file = new File(path, userPhotoFileName);
       			OutputStream os = new FileOutputStream(file);
       			byte[] b = new byte[1024];
       			int bs = 0;
       			while ((bs = is.read(b)) > 0) {
       				os.write(b, 0, bs);
       			}
       			is.close();
       			os.close();
       	 	}
            if(userPhotoFile != null)
            	userInfo.setUserPhoto("upload/" + userPhotoFileName);
            else
            	userInfo.setUserPhoto("upload/NoImage.jpg");
            userInfoDAO.AddUserInfo(userInfo);
            success = true;
            writeJsonResponse(success, message); 
        } catch (Exception e) {
            e.printStackTrace();
            message = "UserInfo添加失败!";
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

    /*查询UserInfo信息*/
    public void ajaxQueryUserInfo() throws IOException, JSONException {
        if(page == 0) page = 1;
        if(user_name == null) user_name = "";
        if(name == null) name = "";
        if(birthday == null) birthday = "";
        if(telephone == null) telephone = "";
        if(registerTime == null) registerTime = "";
        if(rows != 0) userInfoDAO.setRows(rows);
        List<UserInfo> userInfoList = userInfoDAO.QueryUserInfoInfo(user_name, name, birthday, telephone, registerTime, page);
        /*计算总的页数和总的记录数*/
        userInfoDAO.CalculateTotalPageAndRecordNumber(user_name, name, birthday, telephone, registerTime);
        /*获取到总的页码数目*/
        totalPage = userInfoDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = userInfoDAO.getRecordNumber();
        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(UserInfo userInfo:userInfoList) {
			JSONObject jsonUserInfo = userInfo.getJsonObject();
			jsonArray.put(jsonUserInfo);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
    }

    /*查询UserInfo信息*/
    public void ajaxQueryAllUserInfo() throws IOException, JSONException {
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONArray jsonArray = new JSONArray();
		for(UserInfo userInfo:userInfoList) {
			JSONObject jsonUserInfo = new JSONObject();
			jsonUserInfo.accumulate("user_name", userInfo.getUser_name());
			jsonUserInfo.accumulate("name", userInfo.getName());
			jsonArray.put(jsonUserInfo);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
    }

    /*查询要修改的UserInfo信息*/
    public void ajaxModifyUserInfoQuery() throws IOException, JSONException {
        /*根据主键user_name获取UserInfo对象*/
        UserInfo userInfo = userInfoDAO.GetUserInfoByUser_name(user_name);

        HttpServletResponse response=ServletActionContext.getResponse(); 
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		//将要被返回到客户端的对象 
		JSONObject jsonUserInfo = userInfo.getJsonObject(); 
		out.println(jsonUserInfo.toString()); 
		out.flush();
		out.close();
    };

    /*更新修改UserInfo信息*/
    public void ajaxModifyUserInfo() throws IOException, JSONException{
    	String message = "";
    	boolean success = false;
        try {
            String path = ServletActionContext.getServletContext().getRealPath("/upload"); 
            /*处理图片上传*/
            String userPhotoFileName = ""; 
       	 	if(userPhotoFile != null) {
       	 		InputStream is = new FileInputStream(userPhotoFile);
       			String fileContentType = this.getUserPhotoFileContentType();
       			if(fileContentType.equals("image/jpeg") || fileContentType.equals("image/pjpeg"))
       				userPhotoFileName = UUID.randomUUID().toString() +  ".jpg";
       			else if(fileContentType.equals("image/gif"))
       				userPhotoFileName = UUID.randomUUID().toString() +  ".gif";
       			else {
       				message = "上传图片格式不正确!";
       				writeJsonResponse(success, message);
       				return ;
       			}
       			File file = new File(path, userPhotoFileName);
       			OutputStream os = new FileOutputStream(file);
       			byte[] b = new byte[1024];
       			int bs = 0;
       			while ((bs = is.read(b)) > 0) {
       				os.write(b, 0, bs);
       			}
       			is.close();
       			os.close();
       			userInfo.setUserPhoto("upload/" + userPhotoFileName);
       	 	}
            userInfoDAO.UpdateUserInfo(userInfo);
            success = true;
            writeJsonResponse(success, message);
        } catch (Exception e) {
            message = "UserInfo修改失败!"; 
            writeJsonResponse(success, message);
       }
   }

    /*删除UserInfo信息*/
    public void ajaxDeleteUserInfo() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try { 
        	String _user_names[] = user_names.split(",");
        	for(String _user_name: _user_names) {
        		userInfoDAO.DeleteUserInfo(_user_name);
        	}
        	success = true;
        	message = _user_names.length + "条记录删除成功";
        	writeJsonResponse(success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(success, message);
        }
    }

    /*前台查询UserInfo信息*/
    public String FrontQueryUserInfo() {
        if(page == 0) page = 1;
        if(user_name == null) user_name = "";
        if(name == null) name = "";
        if(birthday == null) birthday = "";
        if(telephone == null) telephone = "";
        if(registerTime == null) registerTime = "";
        List<UserInfo> userInfoList = userInfoDAO.QueryUserInfoInfo(user_name, name, birthday, telephone, registerTime, page);
        /*计算总的页数和总的记录数*/
        userInfoDAO.CalculateTotalPageAndRecordNumber(user_name, name, birthday, telephone, registerTime);
        /*获取到总的页码数目*/
        totalPage = userInfoDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = userInfoDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("userInfoList",  userInfoList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("page", page);
        ctx.put("user_name", user_name);
        ctx.put("name", name);
        ctx.put("birthday", birthday);
        ctx.put("telephone", telephone);
        ctx.put("registerTime", registerTime);
        return "front_query_view";
    }

    /*查询要修改的UserInfo信息*/
    public String FrontShowUserInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键user_name获取UserInfo对象*/
        UserInfo userInfo = userInfoDAO.GetUserInfoByUser_name(user_name);

        ctx.put("userInfo",  userInfo);
        return "front_show_view";
    }

    /*删除UserInfo信息*/
    public String DeleteUserInfo() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            userInfoDAO.DeleteUserInfo(user_name);
            ctx.put("message", "UserInfo删除成功!");
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error", "UserInfo删除失败!");
            return "error";
        }
    }

    /*后台导出到excel*/
    public String queryUserInfoOutputToExcel() { 
        if(user_name == null) user_name = "";
        if(name == null) name = "";
        if(birthday == null) birthday = "";
        if(telephone == null) telephone = "";
        if(registerTime == null) registerTime = "";
        List<UserInfo> userInfoList = userInfoDAO.QueryUserInfoInfo(user_name,name,birthday,telephone,registerTime);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "UserInfo信息记录"; 
        String[] headers = { "用户名","用户类型","姓名","性别","用户照片","出生日期","联系电话","注册时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<userInfoList.size();i++) {
        	UserInfo userInfo = userInfoList.get(i); 
        	dataset.add(new String[]{userInfo.getUser_name(),userInfo.getUserType(),userInfo.getName(),userInfo.getSex(),userInfo.getUserPhoto(),userInfo.getBirthday(),userInfo.getTelephone(),userInfo.getRegisterTime()});
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
			response.setHeader("Content-disposition","attachment; filename="+"UserInfo.xls");//filename是下载的xls的名，建议最好用英文 
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
