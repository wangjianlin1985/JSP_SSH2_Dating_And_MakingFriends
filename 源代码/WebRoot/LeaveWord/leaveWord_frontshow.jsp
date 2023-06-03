<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.domain.LeaveWord" %>
<%@ page import="com.chengxusheji.domain.UserInfo" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //获取所有的studentObj信息
    List<UserInfo> userInfoList = (List<UserInfo>)request.getAttribute("userInfoList");
    LeaveWord leaveWord = (LeaveWord)request.getAttribute("leaveWord");

%>
<!DOCTYPE html>
<html>
<head><TITLE>查看留言</TITLE>
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
    <td width=70%><%=leaveWord.getLeaveWordId() %></td>
  </tr>

  <tr>
    <td width=30%>留言标题:</td>
    <td width=70%><%=leaveWord.getTitle() %></td>
  </tr>

  <tr>
    <td width=30%>留言内容:</td>
    <td width=70%><%=leaveWord.getContent() %></td>
  </tr>

  <tr>
    <td width=30%>留言时间:</td>
    <td width=70%><%=leaveWord.getAddTime() %></td>
  </tr>

  <tr>
    <td width=30%>留言人:</td>
    <td width=70%>
      <%
        for(UserInfo userInfo:userInfoList) {
          String selected = "";
          if(userInfo.getUser_name().equals(leaveWord.getUserObj().getUser_name())) {
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
    <td width=30%>回复内容:</td>
    <td width=70%><%=leaveWord.getReplyContent() %></td>
  </tr>

  <tr>
    <td width=30%>回复时间:</td>
    <td width=70%><%=leaveWord.getReplyTime() %></td>
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
