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
    
    <title>Add news</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
  	<div style="margin-left:30px;margin-top:30px">
  	 	<form action="NewsAction_addNews" method="post">
		    <table style="margin-top:20px;margin-left:20px;font-size:12px">
			  <tr>
			    <td>program name<:/td>
			    <td>
			    	<select name="column.columnId" style="width:150px">
					  	<c:forEach items="${columnList}" var="column" >
					  		<option selected="selected" value="-1">--please choose-- </option>
							<option value="${column.columnId }">${column.columnName }</option>
				 		</c:forEach> 
					</select>
			    </td>
			    <td>Headlines:</td>
			    <td><input name="news.newsTitle" class="easyui-validatebox" required="true" /></td>
			  </tr>
			  <tr>
			    <td>Source URL:</td>
			    <td>
			    	<input name="news.sourceUrl" class="easyui-validatebox" required="true" />
			    </td>
			    <td>file path:</td>
			    <td>
			    	<input name="news.filePath" class="easyui-validatebox" required="true" />
			    </td>
			  </tr>
			  <tr>
			    <td>News time:</td>
			    <td>
			    	<input name="news.newsTime" class="easyui-validatebox" required="true" />
			    </td>
			    <td>Source website:</td>
			    <td>
			    	<input name="news.sourceNet" class="easyui-validatebox" required="true" />
				</td>
			  </tr>
			  <tr>
			    <td>remarks:</td>
			    <td>
			    	<textarea rows="6" cols="40" name="news.mark" value=""></textarea>
			    </td>
			  </tr>
			</table>
			<div style="margin-top:10px;margin-left:180px;">
				<input type="submit" value="Add to" ><input type="reset" value="Empty" >
			</div>
  	 	</form>
	</div>
  </body>
</html>
