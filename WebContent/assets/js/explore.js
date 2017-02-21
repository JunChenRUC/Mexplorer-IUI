var concept_type = 1;
var relation_type = 2;
var entity_type = 3;
var count = 0;

function getResult(query, type){
	var url = "Explore?" + query + "type=" + type;
	console.log(url);
	return $.ajax({
    	url: url,
		type: 'GET',
		contentType: "application/json; charset=utf-8",
		async: false,
		dataType: "json"
	});
}

function getQuery(){
	var entitys = document.getElementsByName("entity");
	var query = "";
	for(var i = 0; i < entitys.length; i ++)
		if(entitys[i].value.length != 0)
			query += "entity=" + entitys[i].value + "&";
	
	return query;
}

function checkKey(event){
	if(event.keyCode == 13)
		explore();
}

function explore(){
	var query = getQuery();
	if(query != "")
		createResultBox(query);
	else
		console.log("no input");
}

function flashResultWithConcept(concept){
	document.getElementById("inputBox").insertBefore(createSpan_input(concept, concept_type), document.getElementById("attach"));
}

function flashInput(result){
	var inputBox = document.getElementById("inputBox");
	var attach = document.getElementById("attach");
	while(inputBox.hasChildNodes() && inputBox.firstChild.id != "attach")
		inputBox.removeChild(inputBox.firstChild);
	
	
	attach.getElementsByTagName("input")[0].value = "";
}

function removeResultBox(id){
	var object = document.getElementById(id);
	if(object != null)
		object.parentNode.removeChild(object);
}

function createResultBox(query) {
	getResult(query, 0).success(function(result){	
		if(result != null){		
			console.log(result);
			var resultGroup = document.getElementById("resultGroup");
			while(resultGroup.firstChild)
				resultGroup.removeChild(resultGroup.firstChild);
			
			var resultBox = document.createElement("div");
			var id = "result" + (count ++);
			resultBox.setAttribute("id", id);
			resultBox.setAttribute("class", "col-md-12");
			resultBox.setAttribute("style", "margin: 0px; height: 100%;");
					
			resultGroup.appendChild(resultBox);
			createResult(id, result);	
		}
	})
}

function createResult(id, result){
	var resultBox = document.getElementById(id);
	
	//create and flash query
	flashInput(result[0]);
	
	//create profile box
	resultBox.appendChild(createProfileBox(result[0]));
	
	//create recommender box
	resultBox.appendChild(createRecommenderBox(result[0]));
}

function createProfileBox(result){
	var profileBox = document.createElement("div");
	profileBox.setAttribute("class", "col-md-6");
	
    profileBox.appendChild(createConceptBox(result.queryFeatureList, 0));
    
    var hr = document.createElement("hr");
    profileBox.appendChild(hr);
    
    var entityBox = document.createElement("div");
    entityBox.setAttribute("class", "row");
    entityBox.appendChild(createHeadBox(result.queryEntity));
    entityBox.appendChild(createFeatureBox(result.queryFeatureList));
    
    profileBox.appendChild(entityBox);
	
    return profileBox;
}

function createRecommenderBox(result){
	var recommenderBox = document.createElement("div");
	recommenderBox.setAttribute("class", "col-md-6");
	
	recommenderBox.appendChild(createConceptBox(result.resultFeatureList, 1));
	    
    var hr = document.createElement("hr");
    recommenderBox.appendChild(hr);
    
    var entityBox = document.createElement("div");
    entityBox.setAttribute("class", "row");
    entityBox.appendChild(createHeadBox(result.resultEntityList));
    entityBox.appendChild(createFeatureBox(result.resultFeatureList));
    
    recommenderBox.appendChild(entityBox);
	
    return recommenderBox;
}

function createConceptBox(result, type){
	var conceptBox = document.createElement("div");
	conceptBox.setAttribute("style", "margin-top: 10px");
	
	var conceptGroup = document.createElement("form");
	conceptGroup.setAttribute("class", "form-group");
	var label = document.createElement("label");
	label.setAttribute("style", "margin: 5px");
	if(type == 0)
		label.innerHTML = "Presentation: ";
	else if(type == 1)
		label.innerHTML = "Recommendation: ";
	conceptGroup.appendChild(label);
    
    var btnGroup = document.createElement("div");
    btnGroup.setAttribute("class", "btn-group");
    btnGroup.setAttribute("style", "float: right");
    btnGroup.setAttribute("role", "group");
	var btn = document.createElement("button");
    btn.setAttribute("class", "btn btn-success btn-sm dropdown-toggle");
    btn.setAttribute("data-toggle", "dropdown");
    btn.setAttribute("aria-haspopup", "true");
    btn.setAttribute("aria-expanded", "false");
    btn.innerHTML = "Category";
    var span = document.createElement("span");
    span.setAttribute("class", "caret");
    btn.appendChild(span);
    btnGroup.appendChild(btn);
    
    var ul = document.createElement("ul");
    ul.setAttribute("class", "dropdown-menu");
    ul.setAttribute("style", "max-width: 400px")
    
    for (var i = 0; i < result.length; i ++){
    	if(result[i].relation.name != "subject")
    		continue;
    	var li = document.createElement("li");
    	var div = document.createElement("div");
    	var label = document.createElement("label");
    	label.setAttribute("class", "overflow");
    	label.setAttribute("title", result[i].target.name);
    	label.setAttribute("style", "width: 100%; ");
    	label.innerHTML = result[i].target.name;
    	div.appendChild(label);
    	li.appendChild(div);
    	ul.appendChild(li);
    }
    btnGroup.appendChild(ul);
    conceptGroup.appendChild(btnGroup);
	conceptBox.appendChild(conceptGroup);
    
    return conceptBox;
}

function createHeadBox(result){
	var headBox = document.createElement("div");
	headBox.setAttribute("class", "col-md-6");
	if(result instanceof Array){
		for(var i = 0; i < result.length; i ++)
			headBox.appendChild(createEntityBox(result[i], 2))
	}
	else
		headBox.appendChild(createEntityBox(result, 3));
	
		 	
    return headBox;	
}

function createFeatureBox(result){
	var featureBox = document.createElement("div");
	featureBox.setAttribute("class", "col-md-6");

	var relationSet = new Set();
	var entityGroup = null;
	for(var i = 0; i < result.length; i ++){
		if(result[i].relation.name == "subject")
    		continue;
		
		if(!relationSet.has(result[i].relation.id )){
			relationSet.add(result[i].relation.id);
			
		    var relationGroup = document.createElement("div");
		    relationGroup.setAttribute("class", "row");
		    relationGroup.appendChild(createSpan_relation(result[i].relation));
			featureBox.appendChild(relationGroup);
			
			entityGroup = document.createElement("div");
			entityGroup.setAttribute("class", "row");
		    entityGroup.setAttribute("style", "margin-bottom: 10px");	
		    featureBox.appendChild(entityGroup);
		}
	    
		entityGroup.appendChild(createEntityBox(result[i].target, 1));
	    
	}
            	
    return featureBox;
}