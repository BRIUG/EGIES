<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Management news</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="${pageContext.request.contextPath}/egies/js/viewNews.js"></script>
	<style type="text/css">
		.viewList tr td {
			text-align:center;
			width:150px;
			font-size:12px;
			border:1px solid #BFDCE8;
		}
		
		.viewList .tablehead td {
			text-align:center;
			width:150px;
			font-size:12px;
			border:1px solid #BFDCE8;
			background-color:#95bce2;
		}
		.over{
			background:#95bce2;
		}
	</style>
  </head>
  
  <body>
    <div style="margin-top:30px;margin-left:30px">
  		<table class="viewList">
  			<tr class="tablehead">
  				<td>
  					newsTitle
  				</td>
  				<td>
  					location
  				</td>
  				<td>
  					longitude
  				</td>
  				<td>
  					latitude
  				</td>
  				<td>
  					newsTime
  				</td>
  				<td>
  					sourceNet
  				</td>
  				<td>
  					detail
  				</td>
  				<td>
  					sourceUrl
  				</td>
  				<td>
  					filePath
  				</td>
  				<td>
  					control
  				</td>
  			</tr>	
  			<c:forEach items="${newsAllListPa}" var="news">
  			<tr>
  				<td>
  					${news.newsTitle }
  				</td>
  				<td>
  					${news.location }
  				</td>
  				<td>
  					${news.longitude }
  				</td>
  				<td>
  					${news.latitude }
  				</td>
  				<td>
  					${news.newsTime }
  				</td>
  				<td>
  					${news.sourceNet }
  				</td>
<%--   				<td>
  					${news.detail }
  				</td>
  				<td>
  					${news.sourceUrl }
  				</td>
  				<td>
  					${news.filePath }
  				</td> --%>
  				<td style="width:150px;">
  					<a href="NewsAction_delNews.action?newsId=${news.newsId }">delete</a> &nbsp;&nbsp;
  					modify
  				</td>
  			</tr>
  			</c:forEach>	
  		</table>
	  	<div style="width:850px;margin-top:10px;font-size:10pt;text-align:center">
	   		count<font color="blue">${pageCount }</font>page&nbsp;&nbsp;
		            number<font color="blue">${pageNow }</font>page&nbsp;&nbsp;
	         <c:url var="url_pre" value="javascript:$.treeLink('NewsAction_searchAllNewsPa.action?');">   
	         	<c:param name="pageNow" value="${pageNow-1 }"></c:param>   
	         </c:url>   
	  
		     <c:url var="url_next" value="javascript:$.treeLink('NewsAction_searchAllNewsPa.action?');" >   
		         <c:param name="pageNow" value="${pageNow+1 }"></c:param> 
		     </c:url>   
		     
		     
		     <a href="${url_pre}">previous page</a>
		     <a href="${url_next}">next page</a>
	     </div>
  	</div>
  </body>
</html>
