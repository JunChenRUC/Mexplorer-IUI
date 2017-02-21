var pop_len = 10;
var pop_cn = "autoDis";
var hover_cn = "cur";
var target;

function getEntityList(query){
	var url = "Search?keywords=" + query;
	return $.ajax({
    	url: url,
		type: "GET",
		contentType: "application/json; charset=utf-8",
		async: false,
		dataType: "jsonp",
		jsonpCallback: "callback"
	});
}

function trim(str){
	return str.replace(/(^\s*)|(\s*$)/g, "");
}

function closeAutoDis(){
	var div = document.getElementById("autoDis");
	if(div != null)	
		document.body.removeChild(div);
}

function getDropdown(x){
	setDom();
	bind(x);
}

function bind(x){
    var self = this;
    target = x;
    x.onkeyup = function(e){
    	target = x;
        e = e || window.event;
        var lis = self.pop.getElementsByTagName("li"),lens = self.pop.getElementsByTagName("li").length,n=lens,temp=0;
        if(e.keyCode == 38){  //keyup  
            if(self.pop.style.display != "none"){
                for(var i=0;i<lens;i++){                          
                    if(lis[i].className)
                        temp = i;
                    else
                        n--;
                }
                if(n==0){                                             
                    lis[lens-1].className = self.hover_cn;
                    this.value = lis[lens-1].innerHTML;
                }else{                                                  
                    if(lis[temp] == lis[0]){                    
                        lis[lens-1].className = self.hover_cn;
                        this.value = lis[lens-1].innerHTML;
                        lis[temp].className = "";
                    }else{                                                
                        lis[temp-1].className = self.hover_cn;
                        this.value = lis[temp-1].innerHTML;
                        lis[temp].className = "";
                    }
                }
            }
            else{
            	insert(this.value, target);
                setTimeout(function(){this.pop.style.display="none";},5000);
            }
        }else if(e.keyCode == 40){  //keydown
            if(self.pop.style.display != "none"){
                for(var i=0;i<lens;i++){
                    if(lis[i].className)
                        temp = i;
                    else
                        n--;
                }
                if(n==0){
                    lis[0].className = self.hover_cn;
                    this.value = lis[0].innerHTML;
                }else{
                    if(lis[temp] == lis[lens-1]){
                        lis[0].className = self.hover_cn;
                        this.value = lis[0].innerHTML;
                        lis[temp].className = "";
                    }else{
                        lis[temp+1].className = self.hover_cn;
                        this.value = lis[temp+1].innerHTML;
                        lis[temp].className = "";
                    }
                }
            }else {
            	insert(this.value, target);
                setTimeout(function(){this.pop.style.display="none";},5000);
            }
        }else if(e.keyCode == 13 || e.keyCode == 9){  //enter&&tab
        	self.pop.style.display="none";
        }else{
        	insert(this.value, target);
            setTimeout(function(){this.pop.style.display="none";},5000);
        }
    };
    x.onblur = function(){      
        setTimeout(function(){self.pop.style.display="none";},0);
    	if(target.value == "")
    		target.setAttribute("style","border-color:#a94442");
    	else
    		target.setAttribute("style","none");		
    };
    return this;
}

function setDom(){
    var self = this;
    var dom = document.createElement("div"),frame=document.createElement("iframe"),ul=document.createElement("ul");
    document.body.appendChild(dom);
    with(frame){           
        setAttribute("frameborder","0");
        setAttribute("scrolling","no");
        style.cssText="z-index:-1;position:absolute;left:0;top:0";
   }
    with(dom){                   
    	id = this.pop_cn;
        className = this.pop_cn;
        style = "display: none";
        appendChild(frame);
        appendChild(ul);
        onmouseover  = function(e){  
            e = e || window.event;
            var target = e.srcElement || e.target;
            if(target.tagName == "li"){          
                for(var i=0,lis=self.pop.getElementsByTagName("li");i<lis.length;i++)
                    lis[i].className = "";
                target.className=self.hover_cn;        
            }
        };
        onmouseout = function(e){
            e = e || window.event;
            var target = e.srcElement || e.target;
            if(target.tagName == "li")
                target.className="";
        };
    }
    this.pop = dom;
}

function insert(query, self){
    var bak = [], s, li = [], left = 0, top = 0;
   
    var result
    if(query != ""){
	    getEntityList(query).success(function callback(data){   	
	    	result = eval('(' + data + ')');     	
	    });
    }
    if(result != null){
		//console.log(result);
	    for(var i = 0; i < result.length; i++)
	        bak.push(result[i]);
	    
	    if(bak.length == 0){
	        this.pop.style.display="none";
	        return false;
	    }
	    left = self.getBoundingClientRect().left + document.body.scrollLeft -5;
	    top = self.getBoundingClientRect().top + document.body.scrollTop + self.offsetHeight + 10;
	    with(this.pop){
	        style.cssText = "width:"+self.offsetWidth+"px;"+"position:absolute;left:"+left+"px;top:"+top+"px;display:none;";
	        getElementsByTagName("iframe")[0].setAttribute("width",self.offsetWidth);
	        getElementsByTagName("iframe")[0].setAttribute("height",self.offsetHeight);
	        onclick = function(e){
	            e = e || window.event;
	            var target = e.srcElement || e.target;
	            if(target.tagName == "li")
	                self.value = target.innerHTML;
	            this.style.display="none";
	        };
	    }
	    s = bak.length > this.pop_len ? this.pop_len : bak.length;
	    for(var j = 0; j < s; j++)
	        li.push( "<li>" + bak[j] +"</li>");
	    this.pop.getElementsByTagName("ul")[0].innerHTML = li.join("");
	    this.pop.style.display="block";
    }
}

