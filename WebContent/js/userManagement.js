function buildControlPanel(goToLoginPageOnLogout){
	var panel = $("#controlPanelContainer");
	
	$.getJSON("LoginServlet",function(data){
		if(data != null){
			var userLink = $('<a href="userPanel.html">'+data.username+'</a>').addClass("header-link bold italic");
			panel.append(userLink);			
			
			var newCanvasLink = $('<span>New Canvas</span>').addClass("header-link");
			newCanvasLink.click(function(){
				window.location.assign("newCanvas.html");
			});
			panel.append(newCanvasLink);			
			
			var logoutLink;
			
			if(goToLoginPageOnLogout){
				logoutLink = $('<span>Log Out</span>').addClass("header-link");
				logoutLink.click(function(){
					$.post("LoginServlet", {action:"logout"}, function(){
						$.post("LoginServlet", {action:"logout"}, function(){
							window.location.assign("login.html");
						});
					});
				});
			}
			else{				
				logoutLink = $('<span>Log Out</span>').addClass("header-link");
				logoutLink.click(function(){
					$.post("LoginServlet", {action:"logout"}, function(){
						panel.html(loginHTML());
					});
				});
			}
			
			panel.append(logoutLink);
		}
		else{
			panel.html(loginHTML());	
		}
	});
}

function loginHTML(){
	return '<a href="login.html" class="header-link">Log In / Register</a>';
}

function validateFields(values){	
	var valid = true;		
	jQuery.each(values,function(i, field){
		if(!field.value){
			valid = false;
		}
	});
	
	return valid;
}