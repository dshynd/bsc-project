function changePassword(oldPassword, newPassword){
	var postObj = new Object();
	postObj.oldPassword = oldPassword;
	postObj.newPassword = newPassword;
	postObj.action = "changePW";
	
	$.post("LoginServlet",postObj,function(data){
		var output = $("#changeOutput");
		
		if(data.successful){			
			highlight(output, "Password successfully changed.");
		}
		else{
			error(output, data.message);
		}
	});
}

$(document).ready(function(){
	$("#changeOutput").addClass("ui-widget");
	
	$("input:submit").button();

	$("#changePWForm").submit(function(){
		var values = $(this).serializeArray();
		
		if(validateFields(values)){
			if(values[1].value == values[2].value){
				changePassword(values[0].value, values[1].value);
			}
			else{
				error($("#changeOutput"), "The new passwords entered do not match, please enter them again.");
			}
		}
		else{
			error($("#changeOutput"), "Some field are not valid, please check your input and try again.");
		}
		
		return false;
	});
	
	$("#canvasesDiv a").button();
	
	$.getJSON("LoginServlet",function(data){
		if(data != null){
			$("#usernameInfoDiv span").text(data.username);
			/*var button = $("#usernameInfoDiv button");
			button.button();
			button.click(function() {
				$.post("LoginServlet", {action:"logout"}, function(){
					window.location.replace("login.html");
				});
			});*/
			
			$("#canvasNumSpan").text(data.canvasIds.length);
			
			if(data.canvasIds.length > 0){
				var gallery = new Gallery();
				gallery.setItemIds(data.canvasIds);
				gallery.buildGallery($("#canvasGallery"), 0, 12);	
			}
		}
		else{
			$("#content-container").empty();
			window.location.replace("login.html");
		}
	});
	
	buildControlPanel(true);
});