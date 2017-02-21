function getTitle(query){
	var url = "https://en.wikipedia.org/w/api.php?action=opensearch&format=json&limit=1&callback=?&search=" + query;
	return $.ajax({
    	url: url,
		type: 'GET',
		contentType: "application/json; charset=utf-8",
		async: false,
		dataType: "json"
	});
}

function getImage(query){
	var url = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=pageimages&piprop=original&callback=?&titles=" + query;
	return $.ajax({
    	url: url,
		type: 'GET',
		contentType: "application/json; charset=utf-8",
		async: false,
		dataType: "json"
	});
}

function direct(name){
	window.open("http://en.wikipedia.org/w/index.php?search=" + name);
}

function createEntityBox(entity, size){
	var width = 0;
	if(size == 1)
		width = 80;
	else if(size == 2)
		width = 110;
	else if(size == 3)
		width = 240;
	
	var entityBox = document.createElement("div");
	entityBox.setAttribute("style", "margin: 2px;float: left;width:" + width + "px");
	var thumbnail = document.createElement("div");
	thumbnail.setAttribute("class", "thumbnail");
	var img = document.createElement("img");
	img.setAttribute("style", "display: block; margin: 1px;width:" + width + "px;height:" + width * 1.1 +"px");
	img.setAttribute("onclick", "direct('" + entity.name + "')");
	img.setAttribute("class", "img-thumbnail");	
	
	var extract = document.createElement("p");
	extract.setAttribute("style", "margin-top: 2px");
	getTitle(entity.name).success(function(data){
		extract.innerHTML = data[2][0];
		getImage(data[1][0]).success(function(data){
	    	var pages = data.query.pages;
			for (var page in pages) {
				if (pages.hasOwnProperty(page)) {
					var pdata = pages[page];
					if(pdata.thumbnail)
						img.setAttribute("src", pdata.thumbnail.original);
					else
						img.setAttribute("src","http://www.ehypermart.in/0/images/frontend/image-not-found.png");
				}
			}
	    });
	})

	var caption = document.createElement("div");
	caption.setAttribute("class", "caption");
	caption.setAttribute("style", "margin-top: 2px");
	
	var title = document.createElement("div");
	title.setAttribute("class", "overflow");
	title.setAttribute("style", "margin-top: 2px");
	title.appendChild(createSpan_entity(entity));
	
	caption.appendChild(title);
	if(size == 3)
		caption.appendChild(extract);
	thumbnail.appendChild(img);
	thumbnail.appendChild(caption);	
	
	entityBox.appendChild(thumbnail);
	
	return entityBox;
}