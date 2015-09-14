function Gallery(){
	this.items = [];
	
	this.setItemIds =  function(itemIds){
		this.items = itemIds;
	};
	
	this.buildGallery = function(galleryDiv, startIndex, perPage) {
		var limit = Math.min(startIndex + perPage, this.items.length);
		for ( var i = startIndex; i < limit; i++) {
			var id = this.items[i];

			var item = $('<div class="gallery-item"></div>');
			
			var container = $('<div class="gallery-image-container"></div>');
			var link = $('<a></a>');
			link.attr("href", "canvas.html?canvasId=" + id);
			var placeholder = $('<div class="gallery-placeholder"></div>');
			//placeholder.text(id);
			placeholder.attr("id", "placeHolder" + id);
			link.append(placeholder);
			container.append(link);

			item.append(container);
			galleryDiv.append(item);

			$.ajax({
				type : "GET",
				url : "DataServlet",
				data : {
					dataType : "url",
					canvasId : id
				},
				context : {
					id : id
				},
				success : replacePlaceholder
			});
		}
		
		var bottomBarContainer = $('<div></div>');
		bottomBarContainer.addClass("gallery-bottom-bar-container");
		galleryDiv.append(bottomBarContainer);
		
		var bottomBar = $('<div></div>');
		bottomBar.addClass("gallery-bottom-bar");
		bottomBarContainer.append(bottomBar);
		
		var currentItems = $('<span></span>');
		currentItems.html((startIndex + 1)  + " &dash; " + limit);
		bottomBar.append(currentItems);
		currentItems.position({
			my: "center",
			at: "center",
			of: bottomBarContainer
		});

		if(startIndex > 0){
			var backArrow = $('<span>&lt;&lt;&lt;</span>');
			backArrow.addClass("gallery-nav-arrow");
			backArrow.click({"object":this}, function(event){
				galleryDiv.empty();
				event.data.object.buildGallery(galleryDiv, startIndex - perPage, perPage);
			});
			
			bottomBar.append(backArrow);
			backArrow.position({
				my: "right center",
				at: "left center",
				of: currentItems,
				offset: "-20 0"
			});
		}
		
		if(limit < this.items.length){
			var forwardArrow = $('<span>&gt;&gt;&gt;</span>');
			forwardArrow.addClass("gallery-nav-arrow");
			forwardArrow.click({"object": this},function(event){
				galleryDiv.empty();
				event.data.object.buildGallery(galleryDiv, startIndex + perPage, perPage);
			});
			
			bottomBar.append(forwardArrow);
			forwardArrow.position({
				my: "left center",
				at: "right center",
				of: currentItems,
				offset: "20 0"
			});
		}
	};
}

function replacePlaceholder(url) {
	var img = $('<img src="' + url + '">');
	img.addClass("gallery-image");
	
	var selector = "#placeHolder" + this.id;
	img.load(function(){
		$(selector).replaceWith(img);
	});
}