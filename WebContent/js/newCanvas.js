var validTypes = ["image/jpeg","image/png"];

$(document).ready(function(){
	buildControlPanel(true);
	
	$("input:submit").button();
	
	$("body").on("change", "input:file", function(){
		console.log("change");
		var output = $("#newCanvasOutput");
		
		if(this.files.length > 1){
			error(output, "Please select only a single file for upload");
		}
		else{
			var file = this.files[0];
			
			if(jQuery.inArray(file.type,validTypes) == -1){
				output.show();
				error(output, "The file must be either JPEG or PNG");
				output.delay(2000).fadeOut(750);
				
				var clone = $('<input type="file" name="initialImage"/>');
				clone.insertAfter($(this));
				$(this).remove();
			}
		}
	});
});