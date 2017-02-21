function allowDrop(ev){
    ev.preventDefault();
}

function drag(ev){
    ev.dataTransfer.setData("text", ev.target.innerHTML);
    ev.dataTransfer.setData("class", ev.target.className);
}

function addSpan(ev){
    ev.preventDefault();
    
	inputBox.insertBefore(createSpan_input(ev.dataTransfer.getData("text"), ev.dataTransfer.getData("class")),attach);
}

function findPath(ev){
	if(ev.currentTarget.innerHTML != ev.dataTransfer.getData("text")){
		var queryString = "head=" + ev.currentTarget.innerHTML + "&tail=" + ev.dataTransfer.getData("text");
		createPath(queryString);
	}
}
