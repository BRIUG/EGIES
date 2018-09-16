// Search map
var map;
	var zoom = 12;
	var localsearch;

	function onLoad() {
		//Initialize the map object
		map = new T.Map("mapDiv",{
			projection:'EPSG:4326'
		});
		//Set the center point and level of the display map
		map.centerAndZoom(new T.LngLat(116.40969, 39.89945), zoom);

		var config = {
			pageCapacity : 10, //Number displayed per page
			onSearchComplete : localSearchResult
		//Callback function for receiving data
		};
		//Create a search object
		localsearch = new T.LocalSearch(map, config);
		//      startTool = new T.MarkTool(map);
		//      startTool.open();

	}

	function localSearchResult(result) {
		//Clear maps and search lists
		clearAll();

		//Add a prompt
		prompt(result);

		//Parse search results based on return type
		switch (parseInt(result.getResultType())) {
		case 1:
			//Parsing point data results
			pois(result.getPois());
			break;
		case 2:
			//Analyze the recommended city
			statistics(result.getStatistics());
			break;
		case 3:
			//Analysis of administrative division boundaries
			area(result.getArea());
			break;
		case 4:
			//Analyze suggested word information
			suggests(result.getSuggests());
			break;
		case 5:
			//Analyze bus information
			lineData(result.getLineData());
			break;
		}
	}

	//Parsing prompt words
	function prompt(obj) {
		var prompts = obj.getPrompt();
		if (prompts) {
			var promptHtml = "";
			for (var i = 0; i < prompts.length; i++) {
				var prompt = prompts[i];
				var promptType = prompt.type;
				var promptAdmins = prompt.admins;
				var meanprompt = prompt.DidYouMean;
				if (promptType == 1) {
					promptHtml += "<p>Do you want to be at" + promptAdmins[0].name
							+ "</strong>Search for more included<strong>" + obj.getKeyword()
							+ "</strong>related content?<p>";
				} else if (promptType == 2) {
					promptHtml += "<p>at<strong>" + promptAdmins[0].name
							+ "</strong>Not searched with<strong>" + obj.getKeyword()
							+ "</strong>Related results.<p>";
					if (meanprompt) {
						promptHtml += "<p>Are you looking for:<font weight='bold' color='#035fbe'><strong>"
								+ meanprompt + "</strong></font><p>";
					}
				} else if (promptType == 3) {
					promptHtml += "<p style='margin-bottom:3px;'>Have the following relevant results, are you looking for:</p>"
					for (i = 0; i < promptAdmins.length; i++) {
						promptHtml += "<p>" + promptAdmins[i].name + "</p>";
					}
				}
			}
			if (promptHtml != "") {
				document.getElementById("promptDiv").style.display = "block";
				document.getElementById("promptDiv").innerHTML = promptHtml;
			}
		}
	}

	//Parsing point data results
	function pois(obj) {
		if (obj) {
			//Show search list
			var divMarker = document.createElement("div");
			//Coordinate array, will be used when setting the optimal scale
			var zoomArr = [];
			for (var i = 0; i < obj.length; i++) {
				//Closure
				(function(i) {
					//name
					var name = obj[i].name;
					//address
					var address = obj[i].address;
					//coordinate
					var lnglatArr = obj[i].lonlat.split(" ");
					var lnglat = new T.LngLat(lnglatArr[0], lnglatArr[1]);

					var winHtml = "name:" + name + "<br/>address:" + address;

					//Create an annotation object
					var marker = new T.Marker(lnglat);
					//Add label points to the map
					map.addOverLay(marker);
					//Register click events for tag points
					var markerInfoWin = new T.InfoWindow(winHtml, {
						autoPan : true
					});
					marker.addEventListener("click", function() {
						marker.openInfoWindow(markerInfoWin);
					});

					zoomArr.push(lnglat);

					//Display a list of searches on the page
					var a = document.createElement("a");
					a.href = "javascript://";
					a.innerHTML = name;
					a.onclick = function() {
						showPosition(marker, winHtml);
					}
					divMarker.appendChild(document
							.createTextNode((i + 1) + "."));
					divMarker.appendChild(a);
					divMarker.appendChild(document.createElement("br"));
				})(i);
			}
			//Show the best level of the map
			map.setViewport(zoomArr);
			//Show search results
			divMarker.appendChild(document.createTextNode('number'
					+ localsearch.getCountNumber() + 'records, part'
					+ localsearch.getCountPage() + 'page, current number'
					+ localsearch.getPageIndex() + 'page'));
			document.getElementById("searchDiv").appendChild(divMarker);
			document.getElementById("resultDiv").style.display = "block";
		}
	}

	//Display message box
	function showPosition(marker, winHtml) {
		var markerInfoWin = new T.InfoWindow(winHtml, {
			autoPan : true
		});
		marker.openInfoWindow(markerInfoWin);
	}

	//Analyze the recommended city
	function statistics(obj) {
		if (obj) {
			//Coordinate array, will be used when setting the optimal scale
			var pointsArr = [];
			var priorityCitysHtml = "";
			var allAdminsHtml = "";
			var priorityCitys = obj.priorityCitys;
			if (priorityCitys) {
				//Recommended city display
				priorityCitysHtml += "There are results in the following cities in China<ul>";
				for (var i = 0; i < priorityCitys.length; i++) {
					priorityCitysHtml += "<li>" + priorityCitys[i].name + "("
							+ priorityCitys[i].count + ")</li>";
				}
				priorityCitysHtml += "</ul>";
			}

			var allAdmins = obj.allAdmins;
			if (allAdmins) {
				allAdminsHtml += "More cities<ul>";
				for (var i = 0; i < allAdmins.length; i++) {
					allAdminsHtml += "<li>" + allAdmins[i].name + "("
							+ allAdmins[i].count + ")";
					var childAdmins = allAdmins[i].childAdmins;
					if (childAdmins) {
						for (var m = 0; m < childAdmins.length; m++) {
							allAdminsHtml += "<blockquote>"
									+ childAdmins[m].name + "("
									+ childAdmins[m].count + ")</blockquote>";
						}
					}
					allAdminsHtml += "</li>"
				}
				allAdminsHtml += "</ul>";
			}
			document.getElementById("statisticsDiv").style.display = "block";
			document.getElementById("statisticsDiv").innerHTML = priorityCitysHtml
					+ allAdminsHtml;
		}
	}

	//Analysis of administrative division boundaries
	function area(obj) {
		if (obj) {
			//Coordinate array, will be used when setting the optimal scale
			var pointsArr = [];
			var points = obj.points;
			for (var i = 0; i < points.length; i++) {
				var regionLngLats = [];
				var regionArr = points[i].region.split(",");
				for (var m = 0; m < regionArr.length; m++) {
					var lnglatArr = regionArr[m].split(" ");
					var lnglat = new T.LngLat(lnglatArr[0], lnglatArr[1]);
					regionLngLats.push(lnglat);
					pointsArr.push(lnglat);
				}
				//Create a line object
				var line = new T.Polyline(regionLngLats, {
					color : "blue",
					weight : 3,
					opacity : 1,
					lineStyle : "dashed"
				});
				//Add a line to the map
				map.addOverLay(line);
			}
			//Show the best scale
			map.setViewport(pointsArr);
		}
	}

	//Analyze suggested word information
	function suggests(obj) {
		if (obj) {
			//Suggested words suggest that if the search type is a bus planning suggestion or a bus stop search, the returned result is a suggested word for the bus information.
			var suggestsHtml = "Suggestion word prompt<ul>";
			for (var i = 0; i < obj.length; i++) {
				suggestsHtml += "<li>" + obj[i].name
						+ "&nbsp;&nbsp;<font style='color:#666666'>"
						+ obj[i].address + "</font></li>";
			}
			suggestsHtml += "</ul>";
			document.getElementById("suggestsDiv").style.display = "block";
			document.getElementById("suggestsDiv").innerHTML = suggestsHtml;
		}
	}

	//Analyze bus information
	function lineData(obj) {
		if (obj) {
			//Bus reminder
			var lineDataHtml = "Bus reminder<ul>";
			for (var i = 0; i < obj.length; i++) {
				lineDataHtml += "<li>" + obj[i].name
						+ "&nbsp;&nbsp;<font style='color:#666666'>number"
						+ obj[i].stationNum + "station</font></li>";
			}
			lineDataHtml += "</ul>";
			document.getElementById("lineDataDiv").style.display = "block";
			document.getElementById("lineDataDiv").innerHTML = lineDataHtml;
		}
	}

	//Clear maps and search lists
	function clearAll() {
		map.clearOverLays();
		document.getElementById("searchDiv").innerHTML = "";
		document.getElementById("resultDiv").style.display = "none";
		document.getElementById("statisticsDiv").innerHTML = "";
		document.getElementById("statisticsDiv").style.display = "none";
		document.getElementById("promptDiv").innerHTML = "";
		document.getElementById("promptDiv").style.display = "none";
		document.getElementById("suggestsDiv").innerHTML = "";
		document.getElementById("suggestsDiv").style.display = "none";
		document.getElementById("lineDataDiv").innerHTML = "";
		document.getElementById("lineDataDiv").style.display = "none";
	}