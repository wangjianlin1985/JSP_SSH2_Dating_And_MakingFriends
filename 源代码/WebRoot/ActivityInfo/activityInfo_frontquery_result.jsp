﻿<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.domain.ActivityInfo" %>
<%@ page import="com.chengxusheji.domain.ActivityType" %>
<%@ page import="com.chengxusheji.domain.UserInfo" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    List<ActivityInfo> activityInfoList = (List<ActivityInfo>)request.getAttribute("activityInfoList");
    //获取所有的typeObj信息
    List<ActivityType> activityTypeList = (List<ActivityType>)request.getAttribute("activityTypeList");
    //获取所有的userObj信息
    List<UserInfo> userInfoList = (List<UserInfo>)request.getAttribute("userInfoList");
    int currentPage =  (Integer)request.getAttribute("page"); //当前页
    int totalPage =   (Integer)request.getAttribute("totalPage");  //一共多少页
    int  recordNumber =   (Integer)request.getAttribute("recordNumber");  //一共多少记录
    ActivityType typeObj = (ActivityType)request.getAttribute("typeObj");
    String title = (String)request.getAttribute("title"); //活动主题查询关键字
    String activityTime = (String)request.getAttribute("activityTime"); //活动时间查询关键字
    UserInfo userObj = (UserInfo)request.getAttribute("userObj");
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>相亲活动查询</title>
<style type="text/css">
<!--
body {
    margin-left: 0px;
    margin-top: 0px;
    margin-right: 0px;
    margin-bottom: 0px;
}
.STYLE1 {font-size: 14px}
.STYLE3 {font-size: 14px; font-weight: bold; }
.STYLE4 {
    color: #03515d;
    font-size: 14px;
}
-->
</style>

<script type="text/javascript" src="<%=basePath %>js/jsdate.js"></script>
<script>
var  highlightcolor='#c1ebff';
//此处clickcolor只能用win系统颜色代码才能成功,如果用#xxxxxx的代码就不行,还没搞清楚为什么:(
var  clickcolor='#51b2f6';
function  changeto(){
source=event.srcElement;
if  (source.tagName=="TR"||source.tagName=="TABLE")
return;
while(source.tagName!="TD")
source=source.parentElement;
source=source.parentElement;
cs  =  source.children;
//alert(cs.length);
if  (cs[1].style.backgroundColor!=clickcolor&&source.id!="nc")
for(i=0;i<cs.length;i++){
    cs[i].style.backgroundColor=clickcolor;
}
else
for(i=0;i<cs.length;i++){
    cs[i].style.backgroundColor="";
}
}

function  changeback(){
if  (event.fromElement.contains(event.toElement)||source.contains(event.toElement)||source.id=="nc")
return
if  (event.toElement!=source&&cs[1].style.backgroundColor!=clickcolor)
//source.style.backgroundColor=originalcolor
for(i=0;i<cs.length;i++){
	cs[i].style.backgroundColor="";
}
}

/*跳转到查询结果的某页*/
function GoToPage(currentPage,totalPage) {
    if(currentPage==0) return;
    if(currentPage>totalPage) return;
    document.forms[0].page.value = currentPage;
    document.forms[0].submit();

}

function changepage(totalPage)
{
    var pageValue=document.activityInfoQueryForm.pageValue.value;
    if(pageValue>totalPage) {
        alert('你输入的页码超出了总页数!');
        return ;
    }
    document.activityInfoQueryForm.page.value = pageValue;
    document.activityInfoQueryForm.submit();
}

</script>
</head>

<body>
<form action="<%=basePath %>/ActivityInfo/ActivityInfo_FrontQueryActivityInfo.action" name="activityInfoQueryForm" method="post">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="30" background="<%=basePath %>images/tab_05.gif"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="12" height="30"><img src="<%=basePath %>images/tab_03.gif" width="12" height="30" /></td>
        <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="46%" valign="middle"><table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="5%"><div align="center"><img src="<%=basePath %>images/tb.gif" width="16" height="16" /></div></td>
                <td width="95%" class="STYLE1"><span class="STYLE3">你当前的位置</span>：[相亲活动管理]-[相亲活动查询]</td>
              </tr>
            </table></td>
            <td width="54%"><table border="0" align="right" cellpadding="0" cellspacing="0">

            </table></td>
          </tr>
        </table></td>
        <td width="16"><img src="<%=basePath %>images/tab_07.gif" width="16" height="30" /></td>
      </tr>
    </table></td>
  </tr>


  <tr>
  <td>
活动类别：<select name="typeObj.typeId">
 				<option value="0">不限制</option>
 				<%
 				for(ActivityType activityTypeTemp:activityTypeList) {
 					String selected = "";
 					if(typeObj!=null && typeObj.getTypeId()==activityTypeTemp.getTypeId())
 						selected = "selected";
 				%>
 				 <option value="<%=activityTypeTemp.getTypeId() %>" <%=selected %>><%=activityTypeTemp.getTypeName() %></option>
 				<%
 				}
 				%>
 			</select>
活动主题:<input type=text name="title" size="10" value="<%=title %>" />&nbsp;
活动时间:<input type=text name="activityTime" size="10" value="<%=activityTime %>" />&nbsp;
<select style="display:none;" name="userObj.user_name">
 				<option value="">不限制</option>
 				<%
 				for(UserInfo userInfoTemp:userInfoList) {
 					String selected = "";
 					if(userObj!=null && userObj.getUser_name().equals(userInfoTemp.getUser_name()))
 						selected = "selected";
 				%>
 				 <option value="<%=userInfoTemp.getUser_name() %>" <%=selected %>><%=userInfoTemp.getName() %></option>
 				<%
 				}
 				%>
 			</select>
    <input type=hidden name=page value="1" />
    <input type=submit value="查询" />
  </td>
</tr>
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="8" background="<%=basePath %>images/tab_12.gif">&nbsp;</td>
        <td><table width="100%" border="0" cellpadding="3px" cellspacing="1" bgcolor="b5d6e6" onmouseover="changeto()"  onmouseout="changeback()">
          <tr>
          <!-- 
            <td width="3%" height="22" background="<%=basePath %>images/bg.gif" bgcolor="#FFFFFF"><div align="center">
              <input type="checkbox" name="checkall" onclick="checkAll();" />
            </div></td> -->
            <td height="22" background="<%=basePath %>images/bg.gif" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">序号</span></div></td>
            <td  height="22" background="<%=basePath %>images/bg.gif" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">记录id</span></div></td>
            <td  height="22" background="<%=basePath %>images/bg.gif" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">活动类别</span></div></td>
            <td  height="22" background="<%=basePath %>images/bg.gif" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">活动主题</span></div></td>
            <td  height="22" background="<%=basePath %>images/bg.gif" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">活动时间</span></div></td>
            <td  height="22" background="<%=basePath %>images/bg.gif" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">已参与人数</span></div></td>
            <td  height="22" background="<%=basePath %>images/bg.gif" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">组织者</span></div></td>
            <td  height="22" background="<%=basePath %>images/bg.gif" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">发起时间</span></div></td>
            <td  height="22" background="<%=basePath %>images/bg.gif" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">详情</span></div></td> 
          </tr>
           <%
           		/*计算起始序号*/
            	int startIndex = (currentPage -1) * 5;
            	/*遍历记录*/
            	for(int i=0;i<activityInfoList.size();i++) {
            		int currentIndex = startIndex + i + 1; //当前记录的序号
            		ActivityInfo activityInfo = activityInfoList.get(i); //获取到ActivityInfo对象
             %>
          <tr>
            <td height="20" bgcolor="#FFFFFF"><div align="center" class="STYLE1">
              <div align="center"><%=currentIndex %></div>
            </div></td>
            <td height="20" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1"><%=activityInfo.getActivityId() %></span></div></td>
            <td bgcolor="#FFFFFF"><div align="center"><span class="STYLE1"><%=activityInfo.getTypeObj().getTypeName() %></span></div></td>
            <td height="20" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1"><%=activityInfo.getTitle() %></span></div></td>
            <td height="20" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1"><%=activityInfo.getActivityTime() %></span></div></td>
            <td height="20" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1"><%=activityInfo.getPersonNum() %></span></div></td>
            <td bgcolor="#FFFFFF"><div align="center"><span class="STYLE1"><%=activityInfo.getUserObj().getName() %></span></div></td>
            <td height="20" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1"><%=activityInfo.getAddTime() %></span></div></td>
            <td height="20" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1"><a href="<%=basePath  %>ActivityInfo/ActivityInfo_FrontShowActivityInfoQuery.action?activityId=<%=activityInfo.getActivityId() %>">查看</a></span></div></td> 
          </tr>
          <%	} %>
        </table></td>
        <td width="8" background="<%=basePath %>images/tab_15.gif">&nbsp;</td>
      </tr>
    </table></td>
  </tr>

  <tr>
    <td height="35" background="<%=basePath %>images/tab_19.gif"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="12" height="35"><img src="<%=basePath %>images/tab_18.gif" width="12" height="35" /></td>
        <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td class="STYLE4">&nbsp;&nbsp;共有<%=recordNumber %>条记录，当前第 <%=currentPage %>/<%=totalPage %> 页</td>
            <td><table border="0" align="right" cellpadding="0" cellspacing="0">
                <tr>
                  <td width="40"><img src="<%=basePath %>images/first.gif" width="37" height="15" style="cursor:hand;" onclick="GoToPage(1,<%=totalPage %>);" /></td>
                  <td width="45"><img src="<%=basePath %>images/back.gif" width="43" height="15" style="cursor:hand;" onclick="GoToPage(<%=currentPage-1 %>,<%=totalPage %>);"/></td>
                  <td width="45"><img src="<%=basePath %>images/next.gif" width="43" height="15" style="cursor:hand;" onclick="GoToPage(<%=currentPage+1 %>,<%=totalPage %>);" /></td>
                  <td width="40"><img src="<%=basePath %>images/last.gif" width="37" height="15" style="cursor:hand;" onclick="GoToPage(<%=totalPage %>,<%=totalPage %>);"/></td>
                  <td width="100"><div align="center"><span class="STYLE1">转到第
                    <input name="pageValue" type="text" size="4" style="height:12px; width:20px; border:1px solid #999999;" />
                    页 </span></div></td>
                  <td width="40"><img src="<%=basePath %>images/go.gif" onclick="changepage(<%=totalPage %>);" width="37" height="15" /></td>
                </tr>
            </table></td>
          </tr>
        </table></td>
        <td width="16"><img src="<%=basePath %>images/tab_20.gif" width="16" height="35" /></td>
      </tr>
    </table></td>
  </tr>
</table>
  </form>
</body>
</html>
