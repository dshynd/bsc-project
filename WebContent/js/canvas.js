var canvas;
var canvasContext;
var paletteContext;

var toolColor = [,,];
var bgColor = [,,];
var savedColors = [,,,,,,,];

function getCanvasRelativeX(event){
	return Math.round(event.pageX - $("#canvas").offset().left);
}

function getCanvasRelativeY(event){
	return Math.round(event.pageY - $("#canvas").offset().top);
}

function setToolColor(r,g,b){	
	toolColor[0] = r;
	toolColor[1] = g;
	toolColor[2] = b;
	
	paletteContext.fillStyle = getToolColorRGB(toolColor);
	paletteContext.fillRect(25,251,142,23);
	
	canvasContext.strokeStyle = getToolColorRGB(toolColor);
}

function setToolAlpha(alpha){
	toolColor[3] = alpha;
}

function getCurrentColor(){
	return toolColor;
}

function getToolColorNoA(colorArray){
	return new Array(colorArray[0],colorArray[1],colorArray[2]);
}

function getToolColorRGB(colorArray){
	return "rgb("+colorArray[0]+","+colorArray[1]+","+colorArray[2]+")";
}

function getToolColorRGBA(colorArray){
	return "rgba("+colorArray[0]+","+colorArray[1]+","+colorArray[2]+","+colorArray[3]+")";
}

function setBGColor(r,g,b){
	bgColor[0] = r;
	bgColor[1] = g;
	bgColor[2] = b;
	
	paletteContext.fillStyle = getToolColorRGB(bgColor);
	paletteContext.fillRect(169,251,23,23);
}

function getBGColor(){
	return bgColor;
}

function saveColor(r,g,b){
	for(var i = savedColors.length - 1; i > 0; i--){
		var copyFrom = savedColors[i-1];
		var copyTo = savedColors[i];
		
		for(var j = 0; j < 3; j++){
			copyTo[j] = copyFrom[j];
		}
		
		paletteContext.fillStyle = getToolColorRGB(copyTo);
		paletteContext.fillRect(1 + (24 * (i)),276,23,25);
	}
	
	savedColors[0] = [r,g,b];
	paletteContext.fillStyle = getToolColorRGB(savedColors[0]);
	paletteContext.fillRect(1,276,23,25);
}

var animationController = {
    timeout: null,
    delay: 200,
    actualStart: function(){
        $("#loading-overlay").fadeIn(150);
    },
    actualStop: function(){
        $("#loading-overlay").fadeOut(150);
    },
    start: function(){ 
        this.timeout = setTimeout(this.actualStart, this.delay);
    },
    stop: function(){
    	if(this.timeout){
        	clearTimeout(this.timeout);
    	}
    	
       this.actualStop();
    }
};

function startGet(code, canvasId){
	$.getJSON("NotifyServlet", {code: code, canvasId : canvasId}, function(data){
		if(data.canvasIDInvalid){
			var container = $("#content-container");
			container.empty();
			 
			var output = $('<div class="output-box"></div>');
			error(output, "ERROR: Invalid canvasId");
			container.append(output);
			
			var newButton = $('<a href="newCanvas.html">Create New Canvas</a>');
			newButton.button();
			container.append(newButton);
	
			newButton.position({
				my: "top",
				at: "bottom",
				of: output,
				offset: "0 10"
			});
		}
		else{
			getURLData(data.canvasId);
			startGet(data.code, data.canvasId);
		}
	});
};

function getURLData(canvasId){
	$.get("DataServlet", {canvasId : canvasId, dataType: "url"}, function(data){
		$("#output").attr("src", data + "?" + new Date().getTime());
		animationController.stop();
	},"text");
};

function getBGColorData(canvasId){
	$.get("DataServlet", {canvasId : canvasId, dataType: "bgColor"}, function(data){
		var bgColor = jQuery.parseJSON(data);
		if(bgColor != null){
			setBGColor(bgColor[0],bgColor[1],bgColor[2]);
		}
		else{
			setBGColor(255,255,255);
		}
	},"text");
};

function buildPostObject(tool, jsonObj){
	var obj = new Object();
	obj.tool = tool;
	obj.json = JSON.stringify(jsonObj);
	obj.canvasId = canvasId;
	return obj;
}

function postNotification(dataObj){
	$.post("NotifyServlet", dataObj);
	animationController.start();
}

function init(){
	canvasId = parseInt(getUrlVars()["canvasId"], 10);
	
	$.get("DataServlet",{dataType:"exists", canvasId: canvasId},function(data){
		if(data == "true"){
			getBGColorData(canvasId);
			startGet(-1, canvasId);
		}
		else{
			$("#content-container").empty();
			window.location.replace("newCanvas.html");
		}
	});
	
	buildControlPanel(false);
}

var canvasId;

$(document).ready(function(){
	canvas = document.getElementById("canvas");
	canvasContext = canvas.getContext("2d");
	canvasContext.lineWidth = 3;
	canvasContext.lineCap = "round";
	
	paletteContext = document.getElementById("palette").getContext("2d");
	var pImg = new Image();
	pImg.src = "Palette2.png";
	pImg.onload = function(){
		paletteContext.drawImage(pImg,0,0);
	};
	
	paletteContext.fillStyle = getToolColorRGB([24,101,136]);
	paletteContext.fillRect(0,250,192,50);
	
	setToolColor(0,0,0);
	
	for(var i = 0; i<8; i++){
		savedColors[i] = [255,255,255];
	}
	saveColor(255,255,255);
	
	$("#palette").click(function(event){
		var x = Math.round(event.pageX - $("#palette").offset().left);
		var y = Math.round(event.pageY - $("#palette").offset().top);
		
		if(y < 250 || y > 275){
			var data = paletteContext.getImageData(x,y,1,1).data;
			setToolColor(data[0],data[1],data[2]);
		}
	});
	
	$("#opacitySlider").slider({
		range: "min",
		value: 100,
		min: 0,
		max: 100,
		step: 1, 
		slide: function(event, ui){
			$("#opacitySliderText").text(ui.value + "%");
			
			var alpha = Math.round((ui.value / 100) * 255);
			setToolAlpha(alpha);
		}
	});
	
	$("#saveColorButton").click(function(event){
		saveColor(toolColor[0],toolColor[1],toolColor[2]);
	});
	
	$("#bgColorHelper").tooltip({
		items: "div",
		show: {
			delay: 750
		},
		content: function(){
			return $(this).data("tooltiptext");
		},
		tooltipClass: "menu-item-tooltip"
	});	
	
	$("#canvas").mousedown(function(event){
		eventHandlers.current.mousedown(event);
	});
	
	$("#canvas").mousemove(function(event){
		eventHandlers.current.mousemove(event);
	});
	
	$("#canvas").mouseup(function(event){
		eventHandlers.current.mouseup(event);
	});
	
	$("#saveImageButton").button()
	.click(function(){
		var path = $("#output").get(0).src;
		path = "DownloadServlet?location=" + path.substring(0,path.indexOf('?'));
		$.fileDownload(path, {
			failMessageHtml: "There was a problem dowloading the image, please try again."
		});
		
        return false;
	});
	
	init();
});
