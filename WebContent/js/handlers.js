var eventHandlers = {
	pointHandler:  new PointHandler(),
	lineHandler: new LineHandler(),
	brushHandler: new BrushHandler(),
	dropHandler: new DropHandler(),
	throwHandler: new ThrowHandler(),
	current: null
};

function PointHandler(){
	this.toolbarDiv = null;
	
	this.mousedown = function(event){
		var jsonObj = new Object();
		jsonObj.x = getCanvasRelativeX(event);
		jsonObj.y = getCanvasRelativeY(event);
		jsonObj.color = getCurrentColor();
		
		switch($("#shapeRadioContainer :radio:checked").attr('id')){
			case "circleRadio":
				jsonObj.radius = $("#pointSlider").slider("value");		
				postNotification(buildPostObject("circle",jsonObj));
				break;
			case "squareRadio":
				jsonObj.sideLength = $("#pointSlider").slider("value");		
				postNotification(buildPostObject("square", jsonObj));
				break;
			default:
			break;	
		}
	};
	
	this.mouseup = function(event){};
	
	this.mousemove = function(event){};
	
	this.showToolbar = function(){		
		detachToolbar();
		
		if(!this.toolbarDiv){
			this.toolbarDiv = $(document.createElement('div'));
			this.toolbarDiv.addClass("toolbar-div");
			
			var shapeDiv = $(document.createElement('div')).attr("id","shapeRadioContainer");
			shapeDiv.addClass("tool-container-div");
			shapeDiv.addClass("radio-set-container");
			var circleRadio = $('<input type="radio" id="circleRadio" name="pointShapeRadio" checked="checked" /><label for="circleRadio">Circle</label>'); 
			shapeDiv.append(circleRadio);
			var squareRadio = $('<input type="radio" id="squareRadio" name="pointShapeRadio" /><label for="squareRadio">Square</label>');
			shapeDiv.append(squareRadio);
			this.toolbarDiv.append(shapeDiv);
		
			shapeDiv.buttonset();
			
			var sizeDiv = $(document.createElement('div'));
			sizeDiv.addClass("tool-container-div");
			var sizeTextSpan = $(document.createElement('span'));
			sizeTextSpan.text("Size: ");
			var sizeNumberSpan = $(document.createElement('span'));
			sizeNumberSpan.text("8");
			sizeNumberSpan.attr("id", "pointSliderOut");
			sizeDiv.append(sizeTextSpan);
			sizeDiv.append(sizeNumberSpan);
			
			var sliderDiv = $(document.createElement('div'));
			sliderDiv.attr("id", "pointSlider");
			sliderDiv.addClass("slider");
			sizeDiv.append(sliderDiv);
			this.toolbarDiv.append(sizeDiv);
			
			sliderDiv.slider({
				range: "min",
				value: 32,
				min: 1,
				max: 256,
				step: 1, 
				slide: function(event, ui){
					$("#pointSliderOut").text(ui.value);
				}
			});
		}
		
		$("#studio-toolbar-container").append(this.toolbarDiv);
	};
}

function DropHandler(){
	this.toolbarDiv = null;
	
	this.mousedown = function(event){
		var jsonObj = new Object();
		jsonObj.x = getCanvasRelativeX(event);
		jsonObj.y = getCanvasRelativeY(event);
		jsonObj.color = getCurrentColor();
		jsonObj.dropHeight = $("#dropSlider").slider("value");
		jsonObj.seed = Math.round(Math.random() * new Date().getTime());
		postNotification(buildPostObject("drop", jsonObj));
	};
	
	this.mouseup = function(event){};
	
	this.mousemove = function(event){};
	
	this.showToolbar = function(){		
		detachToolbar();
		
		if(!this.toolbarDiv){
			this.toolbarDiv = $(document.createElement('div'));
			this.toolbarDiv.addClass("toolbar-div");
			
			var sizeDiv = $(document.createElement('div'));
			var sizeTextSpan = $(document.createElement('span'));
			sizeTextSpan.text("Drop Height: ");
			var sizeNumberSpan = $(document.createElement('span'));
			sizeNumberSpan.text("25");
			sizeNumberSpan.attr("id", "dropSliderOut");
			sizeDiv.append(sizeTextSpan);
			sizeDiv.append(sizeNumberSpan);
			this.toolbarDiv.append(sizeDiv);
			
			var sliderDiv = $(document.createElement('div'));
			sliderDiv.attr("id", "dropSlider");
			sliderDiv.addClass("slider");
			this.toolbarDiv.append(sliderDiv);
			
			sliderDiv.slider({
				range: "min",
				value: 25.0,
				min: 1,
				max: 50.0,
				step: 1, 
				slide: function(event, ui){
					$("#dropSliderOut").text(ui.value);
				}
			});
		}
		
		$("#studio-toolbar-container").append(this.toolbarDiv);
	};
}

function ThrowHandler(){
	this.toolbarDiv = null;
	
	this.mousedown = function(event){
		var jsonObj = new Object();
		jsonObj.x = getCanvasRelativeX(event);
		jsonObj.y = getCanvasRelativeY(event);
		jsonObj.color = getCurrentColor();
		jsonObj.throwHeight = $("#throwHeightSlider").slider("value");
		jsonObj.throwHeight = 1;
		jsonObj.throwYaw = toRadians($("#throwYawSlider").slider("value"));
		jsonObj.throwPitch = toRadians($("#throwPitchSlider").slider("value"));
		jsonObj.velocity = $("#throwVelocitySlider").slider("value");
		jsonObj.seed = Math.round(Math.random() * new Date().getTime());
		
		postNotification(buildPostObject("throw", jsonObj));
	};
	
	this.mouseup = function(event){};
	
	this.mousemove = function(event){};
	
	this.showToolbar = function(){		
		detachToolbar();
		
		if(!this.toolbarDiv){
			this.toolbarDiv = $(document.createElement('div'));
			this.toolbarDiv.addClass("toolbar-div");
			
			//Height
			/*var heightDiv = $(document.createElement('div'));
			heightDiv.attr("id", "throwHeightDiv");
			heightDiv.addClass("tool-container-div");
			
			var heightLabelDiv = $(document.createElement('div'));
			heightLabelDiv.addClass("throw-label");
			var heightTextSpan = $(document.createElement('span'));
			heightTextSpan.text("Throw Height: ");
			var heightNumberSpan = $(document.createElement('span'));
			heightNumberSpan.text("1");
			heightNumberSpan.attr("id", "throwHeightSliderOut");
			heightLabelDiv.append(heightTextSpan);
			heightLabelDiv.append(heightNumberSpan);
			heightDiv.append(heightLabelDiv);
			
			var heightSliderDiv = $(document.createElement('div'));
			heightSliderDiv.attr("id", "throwHeightSlider");
			heightSliderDiv.addClass("throw-slider");
			heightDiv.append(heightSliderDiv);
			
			heightSliderDiv.slider({
				range: "min",
				value: 1.0,
				min: 0.1,
				max: 250.0,
				step: 0.1, 
				slide: function(event, ui){
					$("#throwHeightSliderOut").text(ui.value);
				}
			});
			
			this.toolbarDiv.append(heightDiv);*/
			
			//Yaw
			var yawDiv = $(document.createElement('div'));
			yawDiv.attr("id", "throwYawDiv");
			yawDiv.addClass("tool-container-div");
			
			var yawLabelDiv = $(document.createElement('div'));
			var yawTextSpan = $(document.createElement('span'));
			yawTextSpan.text("Throw Angle: ");
			var yawNumberSpan = $(document.createElement('span'));
			yawNumberSpan.html("0&deg;");
			yawNumberSpan.attr("id", "throwYawSliderOut");
			yawLabelDiv.append(yawTextSpan);
			yawLabelDiv.append(yawNumberSpan);
			yawDiv.append(yawLabelDiv);
			
			var yawSliderDiv = $(document.createElement('div'));
			yawSliderDiv.attr("id", "throwYawSlider");
			yawSliderDiv.addClass("slider");
			yawDiv.append(yawSliderDiv);
			
			yawSliderDiv.slider({
				value: 0,
				min: 0,
				max: 359,
				step: 1, 
				slide: function(event, ui){
					$("#throwYawSliderOut").html(ui.value + "&deg;");
				}
			});
			
			this.toolbarDiv.append(yawDiv);
			
			//Pitch
			var pitchDiv = $(document.createElement('div'));
			pitchDiv.attr("id", "throwPitchDiv");
			pitchDiv.addClass("tool-container-div");
			
			var pitchLabelDiv = $(document.createElement('div'));
			var pitchTextSpan = $(document.createElement('span'));
			pitchTextSpan.text("Throw Pitch: ");
			var pitchNumberSpan = $(document.createElement('span'));
			pitchNumberSpan.html("-20&deg;");
			pitchNumberSpan.attr("id", "throwPitchSliderOut");
			pitchLabelDiv.append(pitchTextSpan);
			pitchLabelDiv.append(pitchNumberSpan);
			pitchDiv.append(pitchLabelDiv);
			
			var pitchSliderDiv = $(document.createElement('div'));
			pitchSliderDiv.attr("id", "throwPitchSlider");
			pitchSliderDiv.addClass("slider");
			pitchDiv.append(pitchSliderDiv);
			
			pitchSliderDiv.slider({
				value: 20,
				min: 5,
				max: 90,
				step: 1, 
				slide: function(event, ui){
					$("#throwPitchSliderOut").html("-" + ui.value + "&deg;");
				}
			});
			
			this.toolbarDiv.append(pitchDiv);
			
			//Velocity
			var velocityDiv = $(document.createElement('div'));
			velocityDiv.attr("id", "throwVelocityDiv");
			velocityDiv.addClass("tool-container-div");
			
			var velocityLabelDiv = $(document.createElement('div'));
			var velocityTextSpan = $(document.createElement('span'));
			velocityTextSpan.text("Throw Force: ");
			var velocityNumberSpan = $(document.createElement('span'));
			velocityNumberSpan.text("50");
			velocityNumberSpan.attr("id", "throwVelocitySliderOut");
			velocityLabelDiv.append(velocityTextSpan);
			velocityLabelDiv.append(velocityNumberSpan);
			velocityDiv.append(velocityLabelDiv);
			
			var velocitySliderDiv = $(document.createElement('div'));
			velocitySliderDiv.attr("id", "throwVelocitySlider");
			velocitySliderDiv.addClass("slider");
			velocityDiv.append(velocitySliderDiv);
			
			velocitySliderDiv.slider({
				range: "min",
				value: 50,
				min: 0,
				max: 500.0,
				step: 1, 
				slide: function(event, ui){
					$("#throwVelocitySliderOut").text(ui.value);
				}
			});
			
			this.toolbarDiv.append(velocityDiv);
		}
		
		$("#studio-toolbar-container").append(this.toolbarDiv);
	};
}

function LineHandler(){
	this.x1;
	this.x2;
	
	this.drawing = false;
	
	this.mousedown = function(event){
		this.x1 = getCanvasRelativeX(event);
		this.y1 = getCanvasRelativeY(event);
		this.drawing = true;
	};
	
	this.mouseup = function(event){
		var jsonObj = new Object();
		jsonObj.x1 = this.x1;
		jsonObj.y1 = this.y1;
		jsonObj.x2 = getCanvasRelativeX(event);
		jsonObj.y2 = getCanvasRelativeY(event);
		jsonObj.color = getCurrentColor();
		
		postNotification(buildPostObject("line", jsonObj));
		
		this.drawing = false;
		
		canvasContext.clearRect(0, 0, canvas.width, canvas.height);
	};
	
	this.mousemove = function(event){
		if(this.drawing){
			canvasContext.clearRect(0, 0, canvas.width, canvas.height);
			
			canvasContext.beginPath();
			canvasContext.moveTo(this.x1,this.y1);
			canvasContext.lineTo(getCanvasRelativeX(event), getCanvasRelativeY(event));
			canvasContext.stroke();
		}
	};
	
	this.showToolbar = function(){
		detachToolbar();
	};
}

function BrushHandler(){
	this.x1 = null;
	this.x2 = null;
	
	this.drawing = false;
	
	this.toolbarDiv = null;
	
	this.mousedown = function(event){
		this.x1 = getCanvasRelativeX(event);
		this.y1 = getCanvasRelativeY(event);
		this.drawing = true;
	};
	
	this.mouseup = function(event){
		var jsonObj = new Object();
		jsonObj.x1 = this.x1;
		jsonObj.y1 = this.y1;
		jsonObj.x2 = getCanvasRelativeX(event);
		jsonObj.y2 = getCanvasRelativeY(event);
		jsonObj.color = getCurrentColor();
		jsonObj.width = $("#brushWidthSlider").slider("value");
		jsonObj.skipChance = $("#brushSkipSlider").slider("value");
		
		postNotification(buildPostObject("brush", jsonObj));
		
		this.drawing = false;
		
		canvasContext.clearRect(0, 0, canvas.width, canvas.height);
	};
	
	this.mousemove = function(event){
		if(this.drawing){
			canvasContext.clearRect(0, 0, canvas.width, canvas.height);
			
			canvasContext.beginPath();
			canvasContext.moveTo(this.x1,this.y1);
			canvasContext.lineTo(getCanvasRelativeX(event), getCanvasRelativeY(event));
			canvasContext.stroke();
		}
	};
	
	this.showToolbar = function(){		
		detachToolbar();
		
		if(!this.toolbarDiv){
			this.toolbarDiv = $(document.createElement('div'));
			this.toolbarDiv.addClass("toolbar-div");
			
			//Width
			var widthDiv = $(document.createElement('div'));
			widthDiv.attr("id", "brushWidthDiv");
			widthDiv.addClass("tool-container-div");
			
			var widthLabelDiv = $(document.createElement('div'));
			var widthTextSpan = $(document.createElement('span'));
			widthTextSpan.text("Width: ");
			var widthNumberSpan = $(document.createElement('span'));
			widthNumberSpan.text("32");
			widthNumberSpan.attr("id", "brushWidthSliderOut");
			widthLabelDiv.append(widthTextSpan);
			widthLabelDiv.append(widthNumberSpan);
			widthDiv.append(widthLabelDiv);
			
			var widthSliderDiv = $(document.createElement('div'));
			widthSliderDiv.attr("id", "brushWidthSlider");
			widthSliderDiv.addClass("slider");
			widthDiv.append(widthSliderDiv);
			
			widthSliderDiv.slider({
				range: "min",
				value: 32,
				min: 1,
				max: 1024,
				step: 1, 
				slide: function(event, ui){
					$("#brushWidthSliderOut").text(ui.value);
				}
			});
			
			this.toolbarDiv.append(widthDiv);
			
			//Skip chance
			var skipDiv = $(document.createElement('div'));
			skipDiv.attr("id", "brushSkipDiv");
			skipDiv.addClass("tool-container-div");
			
			var skipLabelDiv = $(document.createElement('div'));
			var skipTextSpan = $(document.createElement('span'));
			skipTextSpan.text("Pressure: ");
			var skipNumberSpan = $(document.createElement('span'));
			skipNumberSpan.text("95%");
			skipNumberSpan.attr("id", "brushSkipSliderOut");
			skipLabelDiv.append(skipTextSpan);
			skipLabelDiv.append(skipNumberSpan);
			skipDiv.append(skipLabelDiv);
			
			var skipSliderDiv = $(document.createElement('div'));
			skipSliderDiv.attr("id", "brushSkipSlider");
			skipSliderDiv.addClass("slider");
			skipDiv.append(skipSliderDiv);
			
			skipSliderDiv.slider({
				range: "min",
				value: 0.015,
				min: 0,
				max: 1,
				step: 0.005, 
				slide: function(event, ui){
					var max = $("#brushSkipSlider").slider("option","max");
					var percent = 100 - ((ui.value / max) * 100);
					$("#brushSkipSliderOut").text(percent + "%");
				}
			});
			
			this.toolbarDiv.append(skipDiv);
		}
		
		$("#studio-toolbar-container").append(this.toolbarDiv);
	};
}

function detachToolbar(){
	$("#studio-toolbar-container").children().detach();
}