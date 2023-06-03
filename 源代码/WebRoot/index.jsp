<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
<title>微相亲平台的设计与实现-首页</title>
<link href="<%=basePath %>css/index.css" rel="stylesheet" type="text/css" />
 </head>
<body>
<div id="container">
	<div id="banner"><img src="<%=basePath %>images/logo.gif" /></div>
	<div id="globallink">
		<ul>
			
			 
			<li><a href="<%=basePath %>UserInfo/UserInfo_FrontQueryUserInfo.action" target="OfficeMain">用户搜索</a></li>
			<li><a href="<%=basePath %>ActivityInfo/ActivityInfo_FrontQueryActivityInfo.action" target="OfficeMain">相亲活动</a></li><!--  
			<li><a href="<%=basePath %>ActivityType/ActivityType_FrontQueryActivityType.action" target="OfficeMain">相亲类型</a></li> 
			<li><a href="<%=basePath %>index.jsp">首页</a></li>
			<li><a href="<%=basePath %>SignUp/SignUp_FrontQuerySignUp.action" target="OfficeMain">用户报名</a></li>  -->
			<li><a href="<%=basePath %>News/News_FrontQueryNews.action" target="OfficeMain">相亲咨询</a></li>
			<li><a href="<%=basePath %>UserInfo/userInfo_register.jsp" target="OfficeMain">用户注册</a></li> 
		</ul>
		<br />
	</div> 

	<div id="loginBar">
	  <%
	  	String user_name = (String)session.getAttribute("user_name");
	    if(user_name==null){
	  %>
	  <form action="<%=basePath %>login/login_CheckFrontLogin.action" style="height:25px;margin:0px 0px 2px 0px;" method="post">
		用户名：<input type=text name="userName" size="12"/>&nbsp;&nbsp;
		密码：<input type=password name="password" size="12"/>&nbsp;&nbsp;
		<input type=submit value="登录" />
	  </form>
	  <%} else { %>
	    <ul>
	    	<li><a href="<%=basePath %>SignUp/SignUp_FrontUserQuerySignUp.action" target="OfficeMain">【我的报名查询】</a></li>
	    	<li><a href="<%=basePath %>LeaveWord/leaveWord_userAdd.jsp" target="OfficeMain">【我要留言】</a></li> 
	    	<li><a href="<%=basePath %>LeaveWord/LeaveWord_FrontUserQueryLeaveWord.action" target="OfficeMain">【我的留言】</a></li> 
	    	<li><a href="<%=basePath %>UserInfo/userInfo_selfModify2.jsp" target="OfficeMain">【修改个人信息】</a></li>
	    	<li><a href="<%=basePath %>login/login_LoginOut.action">【退出登陆】</a></li>
	    </ul>
	 <% } %>
	</div> 

	<div id="main">
	 <iframe id="frame1" src="<%=basePath %>desk.jsp" name="OfficeMain" width="100%" height="100%" scrolling="yes" marginwidth=0 marginheight=0 frameborder=0 vspace=0 hspace=0 >
	 </iframe>
	</div>
	<div id="footer">
		<p>&nbsp;&nbsp;<a href="<%=basePath%>login.jsp"><font color=red>后台登陆</font></a></p>
	</div>
</div>
</body>
</html>
