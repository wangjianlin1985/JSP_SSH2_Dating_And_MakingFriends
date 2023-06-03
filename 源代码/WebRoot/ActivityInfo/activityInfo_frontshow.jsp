<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.domain.ActivityInfo" %>
<%@ page import="com.chengxusheji.domain.ActivityType" %>
<%@ page import="com.chengxusheji.domain.UserInfo" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //获取所有的typeObj信息
    List<ActivityType> activityTypeList = (List<ActivityType>)request.getAttribute("activityTypeList");
    //获取所有的userObj信息
    List<UserInfo> userInfoList = (List<UserInfo>)request.getAttribute("userInfoList");
    ActivityInfo activityInfo = (ActivityInfo)request.getAttribute("activityInfo");

%>
<!DOCTYPE html>
<html>
<head><TITLE>查看相亲活动</TITLE>
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
    <td width=30%>记录id:</td>
    <td width=70%><%=activityInfo.getActivityId() %></td>
  </tr>

  <tr>
    <td width=30%>活动类别:</td>
    <td width=70%>
      <%
        for(ActivityType activityType:activityTypeList) {
          String selected = "";
          if(activityType.getTypeId() == activityInfo.getTypeObj().getTypeId()) {
      %>
      		<%=activityType.getTypeName() %>
      <%
      		break;
          }
        }
      %>
    </td>
  </tr>
  <tr>
    <td width=30%>活动主题:</td>
    <td width=70%><%=activityInfo.getTitle() %></td>
  </tr>

  <tr>
    <td width=30%>活动内容:</td>
    <td width=70%><%=activityInfo.getContent() %></td>
  </tr>

  <tr>
    <td width=30%>活动时间:</td>
    <td width=70%><%=activityInfo.getActivityTime() %></td>
  </tr>

  <tr>
    <td width=30%>已参与人数:</td>
    <td width=70%><%=activityInfo.getPersonNum() %></td>
  </tr>

  <tr>
    <td width=30%>组织者:</td>
    <td width=70%>
      <%
        for(UserInfo userInfo:userInfoList) {
          String selected = "";
          if(userInfo.getUser_name().equals(activityInfo.getUserObj().getUser_name())) {
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
    <td width=30%>发起时间:</td>
    <td width=70%><%=activityInfo.getAddTime() %></td>
  </tr>

  <tr>
      <td colspan="4" align="center">
        <input type="button" value="我要报名" onclick="javascript:userSignUp();"/>&nbsp;&nbsp;&nbsp;
        <input type="button" value="返回" onclick="history.back();"/> 
      </td>
    </tr>

</table>
</s:form>
   </TD></TR>
  </TBODY>
</TABLE>

<script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script> 
<script>
function userSignUp(){  
	$.ajax({
		url: "../SignUp/SignUp_ajaxUserAddSignUp.action",
		type: "post",
		data: {
			"signUp.activityObj.activityId": "<%=activityInfo.getActivityId() %>",
		},
		success: function(data,response,status) {
			if(data.success){ 
				alert("报名成功~");  
				//location.href = "<%=basePath%>SignUp/SignUp_FrontUserQuerySignUp.action";
			}else{
				alert(data.message);
			}   
		} 
	});  
}
</script>

</body>
</html>
