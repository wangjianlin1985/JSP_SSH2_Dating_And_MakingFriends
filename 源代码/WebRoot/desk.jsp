<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>信息管理系统 - 桌面</title>
<link href="<%=basePath %>css/desk.css" rel="stylesheet" type="text/css"> 
</head>

<body>
<table align=center width="90%" border="0" cellspacing="0" cellpadding="0" height="100%">
      <tr>
        <td valign="top"><font color="#336666" size="3">
         <font color='green'><br/><br/>
        
          欢迎使用本系统

			系统开发环境: MyEclipse8.5 + mysql5 <br/><br/>
			后台采用技术: struts 2 + Spring + Hibernate (SSH2)   <br/><br/> 
			前台技术: jquery + easyui框架 <br/><br/>
		 
					</font><font color=blue>本系统开发时间: 2018年</font>
				</td>
      </tr>
    </table>
</body>
</html>

