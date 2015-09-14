var selectedMenuItem;

function emptyToolbar(){
	$("#studio-toolbar-container").empty();
}

function setSelectedMenuItem(id){
	var newSelectedMenuItem = $(id);
	
	if(newSelectedMenuItem != selectedMenuItem){
		if(selectedMenuItem){
			selectedMenuItem.removeClass("studio-menu-item-selected");
		}
		
		selectedMenuItem = newSelectedMenuItem;
		selectedMenuItem.addClass("studio-menu-item-selected");
		return true;
	}
	else{
		return false;
	}
}

$(document).ready(function(){
	$("#studio-menu-container").tooltip({
		items: ".studio-menu-item",
		show: {
			delay: 750
		},
		content: function(){
			return $(this).data("tooltiptext");
		},
		tooltipClass: "menu-item-tooltip"
	});	
	
	$("#clearButton").click(function(){				
		var jsonObj = new Object();
		postNotification(buildPostObject("clear", jsonObj));
	});
	
	$("#bgButton").click(function(){
		var color = getCurrentColor(); 
		setBGColor(color[0],color[1],color[2]);
		
		var jsonObj = new Object();
		jsonObj.color = getToolColorNoA(color);
		postNotification(buildPostObject("setBG", jsonObj));
	});
	
	$("#pointButton").click(function(){
		if(setSelectedMenuItem("#pointButton")){
			eventHandlers.current = eventHandlers.pointHandler;
			eventHandlers.current.showToolbar();
		}
	});
	
	$("#lineButton").click(function(){
		if(setSelectedMenuItem("#lineButton")){
			eventHandlers.current = eventHandlers.lineHandler;
			eventHandlers.current.showToolbar();
		}
	});
	
	$("#brushButton").click(function(){
		if(setSelectedMenuItem("#brushButton")){
			eventHandlers.current = eventHandlers.brushHandler;
			eventHandlers.current.showToolbar();
		}
	});
	
	$("#dropButton").click(function(){
		if(setSelectedMenuItem("#dropButton")){
			eventHandlers.current = eventHandlers.dropHandler;
			eventHandlers.current.showToolbar();
		}
	});
	
	$("#throwButton").click(function(){
		if(setSelectedMenuItem("#throwButton")){
			eventHandlers.current = eventHandlers.throwHandler;
			eventHandlers.current.showToolbar();
		}
	});
	
	$("#pointButton").click();
});