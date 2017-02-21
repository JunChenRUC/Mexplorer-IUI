function createSpan_input(name, type){
	var span = document.createElement("span");
	if(type == 0)
		span.setAttribute("class","tag error");
	else if(type == concept_type)
		span.setAttribute("class","tag concept");
	else if(type == relation_type)
		span.setAttribute("class","tag relation");
	else if(type == entity_type)
		span.setAttribute("class","tag entity");
	else 
		span.setAttribute("class", type);
	span.innerHTML = name;
	
	var input = document.createElement("input");
	input.setAttribute("type","text");
	input.setAttribute("name","entity");
	input.setAttribute("value",name);
	input.style = "display:none;";
	
	var span2 = document.createElement("span");
	span2.setAttribute("data-role","remove");
	span2.setAttribute("onclick","deleteSpan(this)");
	
	span.appendChild(input);
	span.appendChild(span2);
	return span;
}

function createSpan_entity(entity){
	var span = document.createElement("span");
  
    span.setAttribute("draggable", "true");
	span.setAttribute("ondragstart", "drag(event)");
	span.setAttribute("ondragover", "allowDrop(event)");
	span.setAttribute("ondrop", "findPath(event)");
    
	if(entity.type == concept_type)
    	span.setAttribute("class", "tag concept");
    else if(entity.type == entity_type)
    	span.setAttribute("class", "tag entity");

    span.setAttribute("title", entity.name);
    span.innerHTML = entity.name;
    
    return span
}
    
function createSpan_relation(relation){
	var span = document.createElement("span");
  
    span.setAttribute("class", "tag relation");
    span.setAttribute("title", name);
    if(relation.direction == 0)
    	span.innerHTML = relation.name;
    else
    	span.innerHTML =  "<u>" + relation.name + "</u>";
    
    return span
}

function deleteSpan(object){	 
	var inputBox = document.getElementById("inputBox");
	inputBox.removeChild(object.parentNode);
}