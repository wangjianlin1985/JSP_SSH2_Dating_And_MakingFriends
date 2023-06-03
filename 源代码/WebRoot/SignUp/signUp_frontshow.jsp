<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.domain.SignUp" %>
<%@ page import="com.chengxusheji.domain.ActivityInfo" %>
<%@ page import="com.chengxusheji.domain.UserInfo" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //获取所有的activityObj信息
    List<ActivityInfo> activityInfoList = (List<ActivityInfo>)request.getAttribute("activityInfoList");
    //获取所有的userObj信息
    List<UserInfo> userInfoList = (List<UserInfo>)request.getAttribute("userInfoList");
    SignUp signUp = (SignUp)request.getAttribute("signUp");

%>
<!DOCTYPE html>
<html>
<head><TITLE>查看用户报名</TITLE>
<STYLE type=text/css>
body{margin:0px; font-size:14px; background-image:url(<%=basePath%>images/bg.jpg); background-position:bottom; background-repeat:repeat;}
.STYLE1 {color: #ECE9D8}
.label {font-style.:italic; }
.errorLabel {font-style.:italic;  color:red; }
.errorMessage {font-weight:bold; color:red; }
</STYLE>
 <script src="<%=basePath %>calendar.js"></script>
</head>
<body>
<s:fielderror cssStyle="color:red" />
<TABLE align="center" height="100%" cellSpacing=0 cellPadding="10px" width="80%" border=0>
  <TBODY>
  <TR>
    <TD align="left" vAlign=top ><s:form action="" method="post" onsubmit="return checkForm();" enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3'  class="tablewidth">
  <tr>
    <td width=30%>报名id:</td>
    <td width=70%><%=signUp.getSignId() %></td>
  </tr>

  <tr>
    <td width=30%>报名的活动:</td>
    <td width=70%>
      <%
        for(ActivityInfo activityInfo:activityInfoList) {
          String selected = "";
          if(activityInfo.getActivityId() == signUp.getActivityObj().getActivityId()) {
      %>
      		<%=activityInfo.getTitle() %>
      <%
      		break;
          }
        }
      %>
    </td>
  </tr>
  <tr>
    <td width=30%>相亲用户:</td>
    <td width=70%>
      <%
        for(UserInfo userInfo:userInfoList) {
          String selected = "";
          if(userInfo.getUser_name().equals(signUp.getUserObj().getUser_name())) {
      %>
      		<%=userInfo.getName() %>
      <%
      		break;
          }
        }
      %>
    </td>
  </tr>
  <tr>
    <td width=30%>报名时间:</td>
    <td width=70%><%=signUp.getSignUpTime() %></td>
  </tr>

  <tr>
      <td colspan="4" align="center">
        <input type="button" value="返回" onclick="history.back();"/>
      </td>
    </tr>

</table>
</s:form>
   </TD></TR>
  </TBODY>
</TABLE>
</body>
</html>
