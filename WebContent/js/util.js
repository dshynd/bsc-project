function toRadians(degrees){
	return Math.PI * degrees / 180;
}

function getUrlVars() {
    var vars = {};
    window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
}

function error(output, message){
	output.removeClass("ui-state-highlight");
	output.addClass("ui-state-error ui-corner-all");
	output.text(message);
	output.prepend($('<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;">'));
}

function highlight(output, message){
	output.removeClass("ui-state-error");
	output.addClass("ui-state-highlight ui-corner-all");
	output.text(message);
	output.prepend($('<span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;">'));
}

function compareIds(a,b){
	return (a - b);
}