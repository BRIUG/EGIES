<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Configuration index</title>
    
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
  
  	<div style="margin-top:30px;margin-left:30px">
      <s:form action="IndexAction_addIndex.action" method="post">
      <table width="600" border="0">
		  <tr>
		    <td>Index name:</td>
		    <td><input name="index.indexName" class="easyui-validatebox" required="true" /></td>
		    <td>Index path:</td>
		    <td><input name="index.indexPath" class="easyui-validatebox" required="true" /></td>
		  </tr>
		  <tr>
		    <td>file path:</td>
		    <td><input name="index.sourcePath" class="easyui-validatebox" required="true" /></td>
		    <td>Indexing tool:</td>
		    <td><input name="index.type" class="easyui-validatebox" required="true" /></td>
		  </tr>
		  <tr>
		    <td>Check for phrases:</td>
		    <td><input name="index.sqlSentence" class="easyui-validatebox" /></td>
		    <td>Remarks:</td>
		    <td><input name="index.mark" class="easyui-validatebox" /></td>
		  </tr>
		</table>
		<div style="margin-top:10px;margin-left:200px">
      		<s:submit value="Add to" theme="simple"></s:submit>
      		<s:reset value="Empty" theme="simple"></s:reset>
      	</div>
  	  </s:form>
  	</div>
  </body>
</html>
