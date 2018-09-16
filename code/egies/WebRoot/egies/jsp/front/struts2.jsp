<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Test Struts 2,3.16</title>
  </head>
  
  <body>
  	<center>
  	<h1>Test Spring4.0.1 with Hibernate4.3.1</h1>
  	<hr>
	  	<div>
		  	<s:form action="UserAction_execute.action" method="post">
			    <s:textfield name="user.userName" label="username"></s:textfield><br/>
			    <s:textfield name="user.password" label="password"></s:textfield><br/>
			    <s:submit value="submit"></s:submit>
		  	</s:form>
	  	</div>
  	</center>
  </body>
</html>
