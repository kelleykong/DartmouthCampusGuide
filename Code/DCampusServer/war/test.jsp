<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<title>Dartmouth Campus Guide</title>
  
	<!-- css -->
	<!-- metro grid-->
  	<link rel="stylesheet" type="text/css" href="http://fonts.googleapis.com/css?family=Anaheim|Droid+Sans">  
    <link rel="stylesheet" type="text/css" href="dist/jquery.gridster.css">
	<link rel="stylesheet" type="text/css" href="css/style.css">
	<!-- flip -->  
	<link rel="stylesheet" type="text/css" href="MetroJs.css">    

	<!-- js -->
	<!-- jquery -->
	<script type="text/javascript" src="http://code.jquery.com/jquery-1.7.1.js"></script>
	<!-- metro grid-->	
	<script src="dist/jquery.gridster.min.js" type="text/javascript" charset="utf-8"></script>    
  	<script src="src/jquery.gridster.js"></script>
	<!-- flip --> 
    <script type="text/javascript" src="MetroJs.js"></script>

	<!-- metro grid: define container-->	
	<script type="text/javascript">
    jQuery(function(){ //DOM Ready

		jQuery(".gridster ul").gridster({
			widget_margins: [10, 10],
			widget_base_dimensions: [140, 140]
		});
	});
	</script>  



</head>

<body>

	<h1 style="margin-left:100px">Dartmouth Campus Guide</h1>

<section class="container">

	  	<div class="wrapper gridster">
	    	<ul>
	        	<li data-row="1" data-col="1" data-sizex="2" data-sizey="2" class="bg-blueLight fg-darken-link">
	        		<span class="recent"></span>
	        	</li>
	        	<li data-row="1" data-col="3" data-sizex="2" data-sizey="3" class="bg-blue fg-white-link"><span class="twitter"></span></li>
	        	<li data-row="1" data-col="4" data-sizex="1" data-sizey="1" class="bg-blueDark fg-white-link"><span class="facebook"></span></li>
	        	<li data-row="1" data-col="6" data-sizex="1" data-sizey="1" class="bg-red fg-white-link"><span class="about"></span></li>
	        	<li data-row="1" data-col="7" data-sizex="1" data-sizey="1" class="bg-orangeDark fg-white-link"><span class="snippets"></span></li>
	        	<li data-row="1" data-col="8" data-sizex="1" data-sizey="2" class="bg-pinkDark fg-white-link"><span class="resources"></span></li>

	        	<li data-row="2" data-col="5" data-sizex="1" data-sizey="1" class="bg-orangeDark fg-white-link"><span class="googleplus"></span></li>
	        	<li data-row="2" data-col="6" data-sizex="2" data-sizey="1" class="bg-pink fg-white-link"><span class="contact"></span></li>

		        <li data-row="3" data-col="1" data-sizex="1" data-sizey="1" class="bg-greenDark fg-white-link"><span class="articles"></span></li>
		        <li data-row="3" data-col="2" data-sizex="1" data-sizey="1" class="bg-yellow fg-darken-link"><span class="tutorials"></span></li>
	    	    <li data-row="3" data-col="5" data-sizex="1" data-sizey="1" class="bg-red fg-white-link"><span class="pinterest"></span></li>
	        	<li data-row="3" data-col="6" data-sizex="3" data-sizey="1" class="bg-green fg-white-link"><span class="archives"></span></li>
	      	</ul>
	    </div>


</section>

<div class="tiles">
    <div class="live-tile red flip" id="tile1" data-mode="flip">        
        <div class="flip-front ha" style="-webkit-transform: rotateX(0deg); -webkit-transition: all 0s ease 0s; transition: all 0s ease 0s;">
            front
            <span class="tile-title">front title</span>
        </div>
        <div class="flip-back ha" style="-webkit-transform: rotateX(540deg); -webkit-transition: all 500ms ease 0s; transition: all 500ms ease 0s;">
            <p>this tile flips once vertically when clicked using a repeat count of 0</p>
            <span class="tile-title">back title</span>
        </div>
    </div>
    <div class="live-tile blue flip" id="tile2" data-direction="horizontal" data-mode="flip">     
        <div class="flip-front ha" style="-webkit-transform: rotateY(0deg); -webkit-transition: all 0s ease 0s; transition: all 0s ease 0s;">
        	front
            <span class="tile-title">front title</span>
        </div>
        <div class="flip-back ha" style="-webkit-transform: rotateY(540deg); -webkit-transition: all 500ms ease 0s; transition: all 500ms ease 0s;">
            <p>this tile flips horizontally on an interval or when when clicked</p>
            <span class="tile-title">back title</span>
        </div>
    </div>
</div>

	<script type="text/javascript">
	(function(){
		// Show window
		function showContent(elem){
			hideContent();
			elem.find('.content').addClass('expanded');
			elem.addClass('cover');	
		}
		// Reset all
		function hideContent(){
			$('.menu .content').removeClass('expanded');
			$('.menu li').removeClass('cover');		
		}
		
		//animate on click
		var $tile1 = $("#tile1").liveTile({ repeatCount: 0, delay:0 });
		$("#tile1").on("click", function(){
    		$(this).liveTile("play", 0);
		});

		var $tile2 = $("#tile2").liveTile();
		$("#tile2").on("click", function(){
    		$("#tile2").liveTile("play", 0);
		}); 
	})(); 
	</script>

</body>
</html>
