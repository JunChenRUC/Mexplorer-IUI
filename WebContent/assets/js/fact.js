function getFact(queryString) {
	var xmlHttpRequest = null;
	var triple_json = null;
	
	if (window.XMLHttpRequest){// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlHttpRequest = new XMLHttpRequest();
	}
	else {// code for IE6, IE5
		xmlHttpRequest = new ActiveXObject("Microsoft.XMLHTTP");
	}
	
	xmlHttpRequest.onreadystatechange = function() {  
	    if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
	    	triple_json =  eval("(" + xmlHttpRequest.responseText + ")");
	    }  
	};  
	xmlHttpRequest.open("GET", "Explore?" + "head=" + queryString + "&type=4", false); 
	xmlHttpRequest.send(null); 
	
    return triple_json;
}

function getWikiImage(queryString) {
	var xmlHttpRequest = null;
	var result_json = null;
	
	if (window.XMLHttpRequest){// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlHttpRequest = new XMLHttpRequest();
	}
	else {// code for IE6, IE5
		xmlHttpRequest = new ActiveXObject("Microsoft.XMLHTTP");
	}
	
	xmlHttpRequest.onreadystatechange = function() {  
	    if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
	    	result_json =  eval("(" + xmlHttpRequest.responseText + ")");
	    }  
	};  
	xmlHttpRequest.open("GET", "https://en.wikipedia.org/w/api.php?action=query&titles=" + queryString + "&prop=images&imlimit=1&format=json", false); 
	xmlHttpRequest.send(null); 
	
    return result_json;
}

function getWikiInfo(queryString) {
	var xmlHttpRequest = null;
	var result_json = null;
	
	if (window.XMLHttpRequest){// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlHttpRequest = new XMLHttpRequest();
	}
	else {// code for IE6, IE5
		xmlHttpRequest = new ActiveXObject("Microsoft.XMLHTTP");
	}
	
	xmlHttpRequest.onreadystatechange = function() {  
	    if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
	    	result_json =  eval("(" + xmlHttpRequest.responseText + ")");
	    }  
	};  
	xmlHttpRequest.open("GET", "https://en.wikipedia.org/w/api.php?action=opensearch&search="+ queryString +"&format=json&callback=callback&limit=1", false); 
	xmlHttpRequest.send(null); 
	
    return result_json;
}

function createFact(queryString) {
	createFactBox();
	
    var chart = document.getElementById("chart");
    /*var wikiInfo_json = getWikiInfo(queryString);
    var p = document.createElement("p");
    p.innerHTML = wikiInfo_json[2][0];
    chart.appendChild(p);*/
    
    var div = document.createElement("div");
    div.setAttribute("class", "table-responsive");
    var table = document.createElement("table");
    table.setAttribute("class", "table table--striped");
    var thead = document.createElement("thead");
    var tr = document.createElement("tr");
    var th1 = document.createElement("th");
    th1.innerHTML = "#";
    var th2 = document.createElement("th");
    th2.innerHTML = "Factual triples";
    tr.appendChild(th1);
    tr.appendChild(th2);
    thead.appendChild(tr);
    
    var count = 0;
    var tbody = document.createElement("tbody");
    var fact_json = getFact(queryString);
    if(fact_json != null){
	    for (var i = 0; i < fact_json.length; i ++){
	        /*var tr = document.createElement("tr");
	        var td1 = document.createElement("th");
	        td1.innerHTML = ++ count;
	        
	        var td2 = document.createElement("th");*/	        	        
	        for (var j = 0; j < fact_json[i].resultList.length; j ++) {
	        	var tr = document.createElement("tr");
		        var td1 = document.createElement("th");
		        if(j == 0)
		        	td1.innerHTML = ++ count;
		        var td2 = document.createElement("th");
	            
		        var span = document.createElement("span");
	            if(fact_json[i].resultList[j].type == concept_type)
		        	span.setAttribute("class", "tag concept");
	            else if(fact_json[i].resultList[j].type == relation_type)
		        	span.setAttribute("class", "tag relation");
		        else if(fact_json[i].resultList[j].type == entity_type){
		        	span.setAttribute("class", "tag entity");
		        	span.setAttribute("onclick","direct('" + fact_json[i].resultList[j].name + "')");
		        }
	            
	            span.innerHTML = fact_json[i].resultList[j].name;
	            td2.appendChild(span);

		        tr.appendChild(td1);
		        tr.appendChild(td2);
		        tbody.appendChild(tr);
	        }
	    }
    }
    table.appendChild(thead);
    table.appendChild(tbody);
    div.appendChild(table);
    chart.appendChild(div);
}

function createFactBox(){
	var box = document.createElement("div");
	var boxOffsetWidthRate = 0.5;
	var boxOffsetHeightRate = 0.8;
	var left = document.body.getBoundingClientRect().left + document.body.scrollLeft + document.body.offsetWidth * (1 - boxOffsetWidthRate)/2;
    var top = document.body.getBoundingClientRect().top + document.body.scrollTop + document.body.offsetHeight * (1 - boxOffsetHeightRate)/2;
	var style = "width: " + document.body.offsetWidth * boxOffsetWidthRate + "px; height: " + document.body.offsetHeight * boxOffsetHeightRate + "px; position:absolute; left: " + left + "px; top: " + top + "px; ";
	box.setAttribute("id", "chart");
	box.setAttribute("class", "chart");
	box.setAttribute("style", style);
	
	var img = document.createElement("img");
	img.setAttribute("src", "assets/img/delete.png");
	img.setAttribute("height", "15px");
	img.setAttribute("width", "15px");
	img.setAttribute("style", "margin: 10px; float: right");
	img.setAttribute("onclick", "removeFactBox()");
	box.appendChild(img);
	
	document.body.appendChild(box);
}

function removeFactBox(){
	document.getElementById("content").setAttribute("style", "");
	if(document.getElementById("chart") != null)
		document.body.removeChild(document.getElementById("chart"));
}