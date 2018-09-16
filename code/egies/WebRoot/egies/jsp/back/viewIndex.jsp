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
    
    <title>Create index</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/manager/js/viewIndex.js"></script>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
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
      <table width="1000" class="viewList">
		  <tr class="tablehead">
		    <td>Index name</td>
		    <td style="width:150px">Index path</td>
		    <td style="width:150px">file path</td>
		    <td>Indexing tool</td>
		    <td>Index action</td>
		    <td>Index status</td>
		    <td style="width:150px">Index time</td>
		    <td style="width:150px">operating</td>
		  </tr>
		  <s:iterator value="indexList">
		  <tr>
		    <td>
		    	<s:property value="indexName"/>
			</td>
		    <td>
		    	<s:property value="indexPath"/>
			</td>
		    <td>
		    	<s:property value="sourcePath"/>
			</td>
		    <td>
		    	<s:property value="type"/>
			</td>
		    <td>
		    	<s:property value="action"/>
			</td>
		    <td>
		    	<s:if test="status > 0">Indexed</s:if>
		    	<s:elseif test="status < 1">Not indexed</s:elseif>
			</td>
		    <td>
		    	<s:property value="indexTime"/>
			</td>
		    <td>
		    	<input type="hidden" name="indexId" value="<s:property value='indexId'/>" />
		    	<a href="javascript:$.treeLink('IndexAction_createIndex?indexIdStr=<s:property value='indexId'/>');" >Create index</a>
			</td>
		  </tr>
		  </s:iterator>
		</table>
		<div style="width:1000px;margin-left:400px;margin-top:10px">
			counts<font color="blue"><s:property value="#request.pageCount"/></font>page&nbsp;&nbsp;
			number<font color="blue"><s:property value="#request.pageNow"/></font>page&nbsp;&nbsp;
			
			<s:url id="url_pre" value="javascript:$.treeLink('IndexAction_viewIndex?pageType=pre');">
			</s:url>
			
			<s:url id="url_next" value="javascript:$.treeLink('IndexAction_viewIndex?pageType=next');">
			</s:url>
			
			<s:a href="%{url_pre}">previous page</s:a>&nbsp;&nbsp;
			<s:a href="%{url_next}">next page</s:a>
		</div>
  	</div>
  </body>
</html>
