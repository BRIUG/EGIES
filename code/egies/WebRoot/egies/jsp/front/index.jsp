<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
<meta http-equiv="content-Type" content="text/html;charset=utf-8">
<title>Front</title>
<!-- reset.css is used to clear the browser's default style -->
<link rel="stylesheet" type="text/css" href="egies/css/reset-min.css" />
<!-- page-index -->
<link rel="stylesheet" type="text/css" href="egies/css/page-index.css" />
<!-- search bar -->
<link rel="stylesheet" href="egies/css/style.css" type="text/css"
	media="screen" />
<link rel="stylesheet" href="egies/css/menu.css">
<!-- login -->
<link rel='stylesheet prefetch' href='egies/css/icono.min.css'>
<link rel="stylesheet" href="egies/css/login.css" media="screen"
	type="text/css" />

<!--web-fonts-->
<base href="<%=basePath%>">

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!-- loading map -->
<script type="text/javascript" src="http://api.tianditu.com/api?v=4.0"></script>
<script type="text/javascript" src="egies/js/map.js"></script>
</head>

<body onload="onLoad()">

	<div class="header w">

		<!-- Create a navigation bar -->
		<ul class="nav">
			<li><a href="#">应 急 地 理 信 息 提 取 系 统</a>
				<p>Emergency Geographic Information Extraction System</p></li>
		</ul>
		<!-- Set the logo in the div -->
		<div class="logo">
			<a href="#" title="Home"> <img src="egies/img/logo1.png"
				alt="Website logo" />
			</a>
		</div>
	</div>
	<!--End of header-->

	<!--Map search starts-->
	<div id="move-to-top" class="animate hiding"></div>
	<div id="page">
		<div class="ninenineo_wrap">
			<div id="topheader">

				<div class="logo">DesignBlog</div>
			</div>
			<!-- Mega Menu Start -->
			<div class="container">
				<div class="menu style-1">
					<ul class="menu">
						<li>
							<!-- <div class="sidesearch"> -->
							<div>
								<form id="sitesearchform" style="display:inline" method="post"
									action="IndexAction_doQuery.action">
									<fieldset>
										<input class="sidesearch_input" type="text" name="queryString"
											value="${queryString }"
											onfocus="if (this.value == 'e.g: Peking') {this.value = '';}"
											x-webkit-speech=""
											onwebkitspeechchange="transcribe(this.value)"
											onblur="if (this.value == '') {this.value = 'e.g: Peking';}"
											id="s" /> <input type="image" name="submit"
											class="sidesearch_submit"
											src="egies/img/sidesearchsubmit.png" />
									</fieldset>
								</form>
							</div>
						</li>
						<li class="right"><a href="./egies/jsp/back/main.jsp">
								<div class="stage">
									<span> <i class="icono-user"></i>
									</span>
								</div>
						</a></li>
					</ul>
				</div>
			</div>

			<!-- Search results panel -->
 			<div class="searchFrame">
				Search panel
				<div class="search">
					Search content：<input type="text" id="keyWord" value="Tiananmen" /> <input
						type="button"
						onClick="localsearch.search(document.getElementById('keyWord').value)"
						value="search" />
				</div>
				<br />
				Prompt word panel
				<div id="promptDiv" class="prompt"></div>
				Statistics panel
				<div id="statisticsDiv" class="statistics"></div>
				Suggestion panel
				<div id="suggestsDiv" class="suggests"></div>
				Bus reminder panel
				<div id="lineDataDiv" class="lineData"></div>
				Search results panel
				<div id="resultDiv" class="result">
					<div id="searchDiv"></div>
					<div id="pageDiv">
						<input type="button" value="first page" onClick="localsearch.firstPage()" />
						<input type="button" value="previous page"
							onClick="localsearch.previousPage()" /> <input type="button"
							value="next page" onClick="localsearch.nextPage()" /> <input
							type="button" value="last page" onClick="localsearch.lastPage()" /> <br />
						turn to number<input type="text" value="1" id="pageId" size="3" />page <input
							type="button"
							onClick="localsearch.gotoPage(parseInt(document.getElementById('pageId').value));"
							value="turn to" />
					</div>
				</div>
			</div>
			
			<div id="mapDiv">
				<!-- Display of search results -->
				<div id="searchResultFrame">
					<div class="searchResult">
						<c:forEach items="${searchList}" var="map">
							<div class="searchResultSingle">
								<div class="searchResultTitle">${map.newsTitle }</div>
								<div class="searchResultContent">${map.content }</div>
								<a href="${map.sourceUrl }" target="blank">${map.sourceUrl }</a>
							</div>
						</c:forEach>
					</div>

					<!-- Pagination -->
					<div class="paging">
						count<font>${pageCount }</font>page&nbsp;&nbsp; number<font>${pageNow }</font>page&nbsp;&nbsp;
						<c:url var="url_pre" value="IndexAction_doQuery.action">
							<c:param name="pageNow" value="${pageNow-1 }"></c:param>
							<c:param name="queryString" value="${queryString}"></c:param>
						</c:url>

						<c:url var="url_next" value="IndexAction_doQuery.action">
							<c:param name="pageNow" value="${pageNow+1 }"></c:param>
							<c:param name="queryString" value="${queryString}"></c:param>
						</c:url>

						<a href="${url_pre}">previous page</a> <a href="${url_next}">next page</a>
					</div>
				</div>
			</div>
		</div>
		<!--End of map-->

		<!--footer start-->
		<div class="footer">
			<div class="w">
				<p class="copy">Copytright 2018-2021   Filing number:Peking xxx1000</p>
                <p>charge: Beijing Research Institute of Uranium Geology</p>
				<p>
					<a href="#">Home</a> | <a href="#">About</a> | <a href="#">Products</a>
					| <a href="#">Services</a> | <a href="#">Contact</a>
				</p>
				<p>
					<a href="#">Privacy Policy</a> | <a href="#">Terms of use</a>
				</p>
			</div>
		</div>
		<!--footer end-->
</body>
</html>
