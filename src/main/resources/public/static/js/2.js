 

function t117_appendMap() {

    if (typeof google === 'object' && typeof google.maps === 'object') {

        t117_handleApiReady();

    } else {

    	if(window.googleapiiscalled!==true){

	        var script = document.createElement("script");

	        script.type = "text/javascript";

	        script.src = "//maps.google.com/maps/api/js?callback=t117_handleApiReady";

	        document.body.appendChild(script);

	        window.googleapiiscalled=true;

	    }

    }

}



function t117_handleApiReady(){

    $('.t117_map').each(function(index,Element) {

		var el=$(Element);

		window.isDragMap = $isMobile ? false : true;

            

		if(el.attr('data-map-style')!=''){var mapstyle=eval(el.attr('data-map-style'));}else{var mapstyle='[]';}

	    var myLatlng = new google.maps.LatLng(parseFloat(el.attr('data-map-x')), parseFloat(el.attr('data-map-y')));

	    var myOptions = {

            zoom: parseInt(el.attr('data-map-zoom')),

			center:myLatlng,

			scrollwheel: false,

			draggable: window.isDragMap,          

			zoomControl: true,

            styles: mapstyle                                                     	

	    };

	    

	    var map = new google.maps.Map(Element, myOptions);

	

	    var marker = new google.maps.Marker({

	        position: myLatlng,

	        map: map,

	        title:el.attr('data-map-title')

	    });

	    

		// Resizing the map for responsive design

		google.maps.event.addDomListener(window, "resize", function() {

			var center = map.getCenter();

			google.maps.event.trigger(map, "resize");

			map.setCenter(center); 

		});

      

        // DBL Click - activate on mobile      

        if ($isMobile) {

          google.maps.event.addDomListener(window, "dblclick", function() {

            if (window.isDragMap) {

	            window.isDragMap = false;

            } else {

	            window.isDragMap = true;

            }

            map.setOptions({draggable: window.isDragMap});

          }); 

        }

      

    });	

} 

function t347_setHeight(recid){

  var el=$('#rec'+recid);

  var div = el.find(".t347__table");

  var height=div.width() * 0.5625;

  div.height(height);

}



window.t347showvideo = function(recid){

    $(document).ready(function(){

        var el = $('#rec'+recid);

        var videourl = '';



        var youtubeid=$("#rec"+recid+" .t347__video-container").attr('data-content-popup-video-url-youtube');

        if(youtubeid > '') {

            videourl = 'https://www.youtube.com/embed/' + youtubeid;

        }



        $("#rec"+recid+" .t347__video-container").removeClass( "t347__hidden");

        $("#rec"+recid+" .t347__video-carier").html("<iframe id=\"youtubeiframe"+recid+"\" class=\"t347__iframe\" width=\"100%\" height=\"100%\" src=\"" + videourl + "?autoplay=1\" frameborder=\"0\" allowfullscreen></iframe>");

    });

}



window.t347hidevideo = function(recid){

    $(document).ready(function(){

        $("#rec"+recid+" .t347__video-container").addClass( "t347__hidden");

        $("#rec"+recid+" .t347__video-carier").html("");

    });

}