function getPath(query, type){
	var url = "Explore?" + query + "&type=" + type;
	return $.ajax({
    	url: url,
		type: 'GET',
		contentType: "application/json; charset=utf-8",
		async: false,
		dataType: "json"
	});
}

function createPath(query) {
	removePathBox();
    var chart = document.createElement("div");
    chart.setAttribute("id", "pathBox");
    chart.setAttribute("class", "col-md-12");
    
    var div = document.createElement("div");
    div.setAttribute("class", "table-responsive");
    var table = document.createElement("table");
    table.setAttribute("class", "table table--striped");
    var thead = document.createElement("thead");
    var tr = document.createElement("tr");
    var td1 = document.createElement("td");
    td1.setAttribute("style", "width: 40px");
    td1.innerHTML = "#";
    var td2 = document.createElement("td");
    td2.innerHTML = "Explanation";
    tr.appendChild(td1);
    tr.appendChild(td2);
    thead.appendChild(tr);
    
    var count = 0;
    var tbody = document.createElement("tbody");
    var path_json;
    getPath(query, 1).success(function(data){
    	path_json = data;
    });
    if(path_json != null){
	    for (var i = 0; i < path_json.length; i ++){
	        var tr = document.createElement("tr");
	        var td1 = document.createElement("td");
	        td1.setAttribute("style", "width: 40px");
	        td1.innerHTML = ++ count;
	        
	        var td2 = document.createElement("td");
            td2.appendChild(createSpan_entity(path_json[i].head));
	        for(var j = 0; j < path_json[i].relationList.length; j ++) { 
	        	td2.appendChild(createSpan_relation(path_json[i].relationList[j]));     	        	
	        	td2.appendChild(createSpan_entity(path_json[i].entityList[j]));  ;
	        }
	        tr.appendChild(td1);
	        tr.appendChild(td2);
	        tbody.appendChild(tr);
	    }
    }
    table.appendChild(thead);
    table.appendChild(tbody);
    div.appendChild(table);
    chart.appendChild(div);
    
    document.getElementById("resultGroup").appendChild(chart);
}

function removePathBox(){
	if(document.getElementById("pathBox") != null)
		document.getElementById("resultGroup").removeChild(document.getElementById("pathBox"));
}