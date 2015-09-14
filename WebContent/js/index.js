function test(){
	this.val = 1;
}

$(document).ready(function(){
	$.getJSON("LoginServlet",function(data){
		if(data != null){
			var paras = $("#welcomeDiv p");
			$(paras[0]).text("Welcome back to (name) " + data.username + ".");
			var para2 = $(paras[2]);
			para2.text("Or work on one of your canvases:");
			
			var container = $('<div class="gallery-container"></div>');
			var gallery = $('<div class="canvas-gallery"></div>');
			container.append(gallery);
			para2.append(container);
			
			var galObj = new Gallery();
			galObj.setItemIds(data.canvasIds);
			galObj.buildGallery(gallery, 0, 5);
		}
	});
	
	$.get("CreationServlet",function(data){
		if(data != null){
			if(data.length > 0){
				var galObj = new Gallery();
				galObj.setItemIds(data);
				galObj.buildGallery($("#recentCanvasGallery"), 0, 4);	
			}
			else{
				var noRecentDiv = $('<div id="noRecentDiv">There don\'t appear to be any recently created canvases, why not </div>');
				
				var link = $('<a id="noRecentLink" href="newCanvas.html">create one?</a>');
				noRecentDiv.append(link);
				
				$("#recentCanvasGallery").append(noRecentDiv);
			}
		}
	});
	
	buildControlPanel(false);
});