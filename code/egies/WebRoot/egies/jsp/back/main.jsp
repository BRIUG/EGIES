<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Back</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/egies/js/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/egies/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/egies/js/main.js"></script>
	
	<%
		String type = request.getParameter("type");
		if("addIndex".equals(type)){
	 %>
	 	<script type="text/javascript">
	 		$(document).ready(function(){
				$('#main').load("./egies/jsp/back/addIndex.jsp");
			});
	 	</script>
	<%
		}else if("addColumn".equals(type)){
	 %>
		 <script type="text/javascript">
		 		$(document).ready(function(){
					$('#main').load("ColumnAction_getAllColumn");
				});
		 </script>
	 <%
	 	}else if("addNews".equals(type)){
	  %>
	  	<script type="text/javascript">
		 		$(document).ready(function(){
					$('#main').load("NewsAction_getAllColumn");
				});
		 </script>
	  <%
	  	}else if("delNews".equals(type)){
	   %>
	   	<script type="text/javascript">
		 		$(document).ready(function(){
					$('#main').load("NewsAction_searchAllNewsPa");
				});
		 </script>
	   <%
	   	}else if("addLocal".equals(type)){
	    %>
	    <script type="text/javascript">
		 		$(document).ready(function(){
					$('#main').load("FileDocumentAction_searchAllFileDocumentPa.action");
				});
		 </script>
	    <%
	    }else if("delFileDocument".equals(type)){
	     %>
	     <script type="text/javascript">
		 		$(document).ready(function(){
					$('#main').load("FileDocumentAction_searchAllFileDocumentPa.action");
				});
		 </script>
	     <%
	     }
	      %>
	
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/egies/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/egies/css/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/egies/css/demo.css">
	
	<style type="text/css">
		a {
			text-decoration:none;
			border:none;
			color:#05497A
		}
		a:hover{
			text-decoration:none;
			color:#ccc
		}
	</style>

  </head>
  
  <body class="easyui-layout">
	<div data-options="region:'north',border:false" style="height:80px;background:#B3DFDA;padding:10px">
		Emergency Geographical Information Extraction System
	</div>
	
	<div data-options="region:'west',split:true,title:'Business management'" style="width:200px;padding:0px;">
		<div class="easyui-accordion" style="width:192px;height:485px;">
			<div id="selectone" title="Data source management" data-options="selected:true" style="padding:10px;">
				<p>
					<a href="javascript:$.treeLink('NewsAction_getAllColumn');" >Network data</a>
				</p>
				<p>
					<a href="javascript:$.treeLink('NewsAction_searchAllNewsPa');" >Network data management</a>
				</p>
				<p>
					<a href="javascript:$.treeLink('FileDocumentAction_searchAllFileDocumentPa');" >Local data</a>
				</p>
			</div>
			<div id="selecttwo" title="Index management" style="padding:10px">
				<p>
					<a href="javascript:$.treeLink('./egies/jsp/back/addIndex.jsp');" >Configuration index</a>
				</p>
				<p>
					<a href="javascript:$.treeLink('IndexAction_viewIndex');" >Create index</a>
				</p>
			</div>
			<div title="Column management" style="padding:10px">
				<p>
					<a href="javascript:$.treeLink('ColumnAction_getAllColumn');" >Add section</a>
				</p>
				<p>
					<a href="javascript:$.treeLink('ColumnAction_searchAllColumnPa');" >Column management</a>
				</p>
			</div>
			<div title="User Management" style="padding:10px">
			</div>
			<div title="Role management" style="padding:10px">
			</div>
			<div title="authority management" style="padding:10px">
			</div>
		</div>
	</div>
	
	<div data-options="region:'south',border:false" style="height:30px;background:#A9FACD;padding:5px;">
		<div>
			<a href="#">sign out</a>
		</div>
	</div>
	<div id="main" data-options="region:'center',title:' '">
	
	</div>
  </body>
</html>
