function login(username, password){
	var postObj = new Object();
	postObj.username = username;
	postObj.password = password;
	postObj.action = "login";
	
	$.post("LoginServlet",postObj,function(data){
		var output = $("#loginOutput");
		
		if(data.successful){			
			highlight(output, "Logged in. You will be forwarded in 2 seconds, or click ");
			output.append($('<span style="cursor:pointer; text-decoration: underline;" click="function(){history.back;};">here</span>'));
			forwardPage();
		}
		else{
			error(output, data.message);
		}
	});
}

function register(username, password){
	var postObj = new Object();
	postObj.username = username;
	postObj.password = password;
	postObj.action = "register";
	
	$.post("LoginServlet",postObj,function(data){
		var output = $("#registerOutput");
		
		if(data.successful){			
			highlight(output, "Registered. You will be forwarded in 2 seconds, or click ");
			output.append($('<span style="cursor:pointer; text-decoration: underline;" click="function(){history.back;};">here</span>'));
			forwardPage();
		}
		else{
			error(output, data.message);
		}
	});
}

function forwardPage(){
	console.log(document.referrer);
	$("input:submit").button("option","disabled",true);
	
	var refHost = document.referrer.split('/')[2];
	
	if(!document.referrer || refHost != document.location.host){
		setTimeout(function(){window.location.replace("userPanel.html");}, 2000);
	}
	
	setTimeout(function(){history.back();}, 2000);
}

$(document).ready(function(){
	$.getJSON("LoginServlet",function(data){
		if(data != null){
			$("#content-container").empty();
			window.location.replace("userPanel.html");
		}
	});
	
	$("#inputDiv").position({
		my: "center top",
		at: "center top",
		of: $("#content-container")
	});
	
	$("#loginOutput").addClass("ui-widget");
	$("#registerOutput").addClass("ui-widget");
	
	$("button").button();
	$("input:submit").button();
	
	$("#loginForm").submit(function(){
		var values = $(this).serializeArray();
		
		if(validateFields(values)){
			login(values[0].value, values[1].value);
		}
		else{
			error($("#loginOutput"), "Some field are not valid, please check your input and try again.");
		}
		
		return false;
	});
	
	$("#registerForm").submit(function(){
		var values = $(this).serializeArray();
		
		if(validateFields(values)){
			if(values[1].value == values[2].value){
				register(values[0].value, values[1].value);
			}
			else{
				error($("#registerOutput"), "The passwords entered do not match, please enter them again.");
			}
		}
		else{
			error($("#registerOutput"), "Some field are not valid, please check your input and try again.");
		}
		
		return false;
	});
});