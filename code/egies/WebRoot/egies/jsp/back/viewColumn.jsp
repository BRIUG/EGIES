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
    
    <title>Column management</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<style type="text/css">
		.viewList tr td {
			text-align:center;
			width:100px;
			font-size:12px;
			border:1px solid #BFDCE8;
		}
		
		.viewList .tablehead td {
			text-align:center;
			width:100px;
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
  					program name
  				</td>
  				<td style="width:150px;">
  					Parent column
  				</td>
  				<td>
  					Index name
  				</td>
  				<td style="width:150px;">
  					operating
  				</td>
  			</tr>	
  			<c:forEach items="${columnAllListPa}" var="column">
  			<tr>
  				<td>
  					${column.columnName }
  				</td>
  				<td>
  					${column.pcolumnName }
  				</td>
  				<td>
  					${column.indexName }
  				</td>
  				<td style="width:150px;">
  					<input type="hidden" id="columnId" value="${column.columnId }" />
  					 delete&nbsp;&nbsp; modify
  				</td>
  			</tr>
  			</c:forEach>	
  		</table>
	  	<div style="width:550px;margin-top:10px;font-size:10pt;text-align:center">
	   		counts<font color="blue">${pageCount }</font>page&nbsp;&nbsp;
		            number<font color="blue">${pageNow }</font>page&nbsp;&nbsp;
	         <c:url var="url_pre" value="javascript:$.treeLink('ColumnAction_searchAllColumnPa.action?');">   
	         	<c:param name="pageNow" value="${pageNow-1 }"></c:param>   
	         </c:url>   
	  
		     <c:url var="url_next" value="javascript:$.treeLink('ColumnAction_searchAllColumnPa.action?');" >   
		         <c:param name="pageNow" value="${pageNow+1 }"></c:param> 
		     </c:url>   
		     
		     
		     <a href="${url_pre}">previous page</a>
		     <a href="${url_next}">next page</a>
	     </div>
  	</div>
  </body>
</html>
