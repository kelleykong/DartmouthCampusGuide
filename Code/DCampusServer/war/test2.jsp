<%@page import="edu.dartmouth.cs.dcampus.server.data.PostEntity"%>
<%@page import="java.util.*"%>
<%@page import="java.lang.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<link rel="shortcut icon" href="/icon.png" type="image/x-icon">
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
	
	
	<script>
	document.write("<style type='text/css'>");	
	document.write("		<%
					ArrayList<PostEntity> postList = (ArrayList<PostEntity>) request.getAttribute("postList"); 
					for (PostEntity entry: postList) {
					String[] str = entry.mName.split(" ");
					String cls = "";
					for (int i = 0; i < str.length; i++)
						cls += "_"+str[i];
		%>");
	document.write(".<%=cls%>:before{ content:''; background:url('<%=entry.mPic%>') no-repeat center; background-size:250px} ");
	document.write(".<%=cls%>:after{content:'<%=entry.mName%>';} ");
	document.write("<% } %>");
	document.write("</style>");
	</script>
	
	<style type="text/css">
	.close {
		display: block;
		  color: #fff;
		  cursor: pointer;
		  height: 40px; width: 40px;
		  font: bold 20px/40px arial, helvetica;
		  position: absolute;
		  text-align: right;
		  top: -10px; right: 10px;
		  -moz-border-radius: 40px;
		  -webkit-border-radius: 40px;
		  border-radius: 40px;
		  z-index:102;      
}
	</style>

</head>

<body>

	<h1 style="margin-left:100px">Dartmouth Campus Guide</h1>

	<%		String colors[] = {"amber", "blue", "cyan", "brown", "cobalt", "crimson", "emerald", "green", 
	"indigo", "lime", "magenta", "mango", "mauve", "olive", "orange", "pink", "purple", "violet", "red", 
	"sienna", "steel", "teal", "yellow"};
			int is_admin = 0;
	%>
<section class="container">

	  	<div class="wrapper gridster">
	    	<ul>
	        	<li id = "bakerLib" data-row="1" data-col="1" data-sizex="2" data-sizey="2" class="steel fg-darken-link live-tile" data-mode="flip">
	        		<div class="flip-front ha" style="-webkit-transform: rotateX(0deg); -webkit-transition: all 0s ease 0s; transition: all 0s ease 0s;">
	        	        <span class="bakerLib"></span>
	        	    </div>
	        	    <div class="flip-back ha" style="-webkit-transform: rotateX(540deg); -webkit-transition: all 500ms ease 0s; transition: all 500ms ease 0s;">
            			<p>Baker-Berry Library sits at the heart of the Dartmouth College campus and is the most recognized icon of the College. Baker-Berry Library houses the collections for the Humanities and the Social Sciences, including the Cook Mathematics Collection, and more! </p>
            			<audio src="http://www.cs.dartmouth.edu/~tyun/cs65/uploads/20140528003551.mp3" controls="controls">Your browser does not support the audio element.</audio>
            			<span class="tile-title">Baker-Berry Library</span>
        			</div>
	        	</li>
	        	
	        	<li data-row="1" data-col="3" data-sizex="2" data-sizey="3" class="blue fg-white-link live-tile" data-direction="horizontal" data-mode="flip">
	        		<div class="flip-front ha" style="-webkit-transform: rotateY(0deg); -webkit-transition: all 0s ease 0s; transition: all 0s ease 0s;">	        	
	        			<span class="green"></span>
	        		</div>
	        	    <div class="flip-back ha" style="-webkit-transform: rotateY(540deg); -webkit-transition: all 500ms ease 0s; transition: all 500ms ease 0s;">
            			<p>The Green (formally the College Green) is a grass-covered field and common space at the center of Dartmouth College, an Ivy League university located in Hanover, New Hampshire, United States. It was among the first parcels of land obtained by the College upon its founding in 1769, and is the only creation of the 18th century remaining at the center of the campus.</p>
            			<span class="tile-title">The green</span>
        			</div>
        		</li>
        		
	        	<li data-row="1" data-col="4" data-sizex="1" data-sizey="1" class="cobalt fg-white-link live-tile" data-mode="flip">
	        		<div class="flip-front ha" style="-webkit-transform: rotateX(0deg); -webkit-transition: all 0s ease 0s; transition: all 0s ease 0s;">	        	
	        			<span class="dhall"></span>
	        	    </div>
	        	    <div class="flip-back ha" style="-webkit-transform: rotateX(540deg); -webkit-transition: all 500ms ease 0s; transition: all 500ms ease 0s;">
            			<p>this tile flips once vertically when clicked using a repeat count of 0</p>
            			<span class="tile-title">Dartmouth Hall</span>
        			</div>	        	
	        	</li>
	        	<li data-row="1" data-col="6" data-sizex="1" data-sizey="1" class="red fg-white-link live-tile" data-direction="horizontal" data-mode="flip">
	        		<div class="flip-front ha" style="-webkit-transform: rotateY(0deg); -webkit-transition: all 0s ease 0s; transition: all 0s ease 0s;">	        	
	        			<span class="rockcenter"></span>
	        	    </div>
	        	    <div class="flip-back ha" style="-webkit-transform: rotateY(540deg); -webkit-transition: all 500ms ease 0s; transition: all 500ms ease 0s;">
            			<p>this tile flips once vertically when clicked using a repeat count of 0</p>
            			<span class="tile-title">Rockefeller Center</span>
        			</div>
	        	</li>
	        	<li data-row="1" data-col="7" data-sizex="1" data-sizey="1" class="orange fg-white-link live-tile" data-mode="flip">
	        		<div class="flip-front ha" style="-webkit-transform: rotateX(0deg); -webkit-transition: all 0s ease 0s; transition: all 0s ease 0s;">	        	
	        			<span class="gym"></span>
	        	    </div>
	        	    <div class="flip-back ha" style="-webkit-transform: rotateX(540deg); -webkit-transition: all 500ms ease 0s; transition: all 500ms ease 0s;">
            			<p>this tile flips once vertically when clicked using a repeat count of 0</p>
            			<span class="tile-title">Alumni Gym</span>
        			</div>
	        	</li>
	        	<li data-row="1" data-col="8" data-sizex="1" data-sizey="2" class="purple fg-white-link live-tile" data-mode="flip">
	        		<div class="flip-front ha" style="-webkit-transform: rotateX(0deg); -webkit-transition: all 0s ease 0s; transition: all 0s ease 0s;">	        	
	        			<span class="sudi"></span>
	        	    </div>
	        	    <div class="flip-back ha" style="-webkit-transform: rotateX(540deg); -webkit-transition: all 500ms ease 0s; transition: all 500ms ease 0s;">
            			<p>this tile flips once vertically when clicked using a repeat count of 0</p>
            			<span class="tile-title">Sudikoff</span>
        			</div>
	        	</li>

	        	<li data-row="2" data-col="5" data-sizex="1" data-sizey="1" class="orange fg-white-link live-tile" data-direction="horizontal" data-mode="flip">
	        		<div class="flip-front ha" style="-webkit-transform: rotateY(0deg); -webkit-transition: all 0s ease 0s; transition: all 0s ease 0s;">	        	
	        			<span class="moore"></span>
	        	    </div>
	        	    <div class="flip-back ha" style="-webkit-transform: rotateY(540deg); -webkit-transition: all 500ms ease 0s; transition: all 500ms ease 0s;">
            			<p>this tile flips once vertically when clicked using a repeat count of 0</p>
            			<span class="tile-title">Moore</span>
        			</div>
	        	</li>
	        	<li data-row="2" data-col="6" data-sizex="2" data-sizey="1" class="pink fg-white-link live-tile" data-mode="flip">
	        		<div class="flip-front ha" style="-webkit-transform: rotateX(0deg); -webkit-transition: all 0s ease 0s; transition: all 0s ease 0s;">	        	
	        			<span class="collis"></span>
	        	    </div>
	        	    <div class="flip-back ha" style="-webkit-transform: rotateX(540deg); -webkit-transition: all 500ms ease 0s; transition: all 500ms ease 0s;">
            			<p>this tile flips once vertically when clicked using a repeat count of 0</p>
            			<span class="tile-title">Collis</span>
        			</div>
	        	</li>

		        <li id="add" data-row="3" data-col="1" data-sizex="1" data-sizey="1" class="bg-greenDark fg-white-link">
	        			<span class="add"></span>
		        </li>
		        <li id="admin" data-isadmin="0" data-row="3" data-col="2" data-sizex="1" data-sizey="1" class="bg-yellow fg-darken-link">
	        			<span class="admin"></span>
		        </li>
		        
	    	    <li data-row="3" data-col="5" data-sizex="1" data-sizey="1" class="red fg-white-link live-tile" data-direction="horizontal" data-mode="flip">
	        		<div class="flip-front ha" style="-webkit-transform: rotateY(0deg); -webkit-transition: all 0s ease 0s; transition: all 0s ease 0s;">	        	
	        			<span class="hopkin"></span>
	        	    </div>
	        	    <div class="flip-back ha" style="-webkit-transform: rotateY(540deg); -webkit-transition: all 500ms ease 0s; transition: all 500ms ease 0s;">
            			<p>this tile flips once vertically when clicked using a repeat count of 0</p>
            			<span class="tile-title">Hopkins Center</span>
        			</div>
	    	    </li>
	        	<li data-row="3" data-col="6" data-sizex="3" data-sizey="1" class="green fg-white-link live-tile" data-mode="flip">
	        		<div class="flip-front ha" style="-webkit-transform: rotateX(0deg); -webkit-transition: all 0s ease 0s; transition: all 0s ease 0s;">	        	
	        			<span class="commons"></span>
	        	    </div>
	        	    <div class="flip-back ha" style="-webkit-transform: rotateX(540deg); -webkit-transition: all 500ms ease 0s; transition: all 500ms ease 0s;">
            			<p>this tile flips once vertically when clicked using a repeat count of 0</p>
            			<span class="tile-title">Class of 1953 Commons</span>
        			</div>
	        	</li>	        	
		<%
//					ArrayList<PostEntity> postList = (ArrayList<PostEntity>) request.getAttribute("postList"); 
					for (PostEntity entry: postList) {
						int data_sizex=1, data_sizey=1;
						Random random = new Random();
						int rand_23 = random.nextInt(23);
						int rand = rand_23 %2;
						int data_1x1 = 20;
						if (entry.mDesc != null) {
						int len = entry.mDesc.length();
						if (len > data_1x1*2 && len <= data_1x1 *6) {
							if (rand == 0)
								data_sizey = 2;
							else
								data_sizex = 2;
						}
						if (len > data_1x1 * 6 && len <= data_1x1 *15) {
							if (rand == 0)
								data_sizey = 3;
							else
								data_sizex = 3;
						}	
						if (len > data_1x1 * 15 && len <= data_1x1 *20) {
								data_sizey = 2;
								data_sizex = 2;
						}	
						if (len > data_1x1 * 20 && len <= data_1x1 *50) {
							if (rand == 0) {
								data_sizex = 2;
								data_sizey = 3;
							}
							else {
								data_sizex = 3;
								data_sizey = 2;
							}
						}
						if (len > data_1x1 * 50) {
								data_sizey = 3;
								data_sizex = 3;
						}
						}																
		%>
	        	<li data-row="1" data-col="7" data-sizex=<%=data_sizex%> data-sizey=<%=data_sizey%> class="<%=colors[rand_23]%> fg-white-link live-tile" data-mode="flip">

	        		<div class="flip-front ha" style="-webkit-transform: rotateX(0deg); -webkit-transition: all 0s ease 0s; transition: all 0s ease 0s;">	        	
	        			<% String[] str = entry.mName.split(" ");
	        				String cls = "";
							for (int i = 0; i < str.length; i++)
								cls += "_"+str[i];%>
	        			<span class=<%=cls%>></span>
	        			       	
	        	    </div>
	        	    <div class="flip-back ha" style="-webkit-transform: rotateX(540deg); -webkit-transition: all 500ms ease 0s; transition: all 500ms ease 0s;">
            			<p><%=entry.mDesc%></p>
            			<p> 
            				<% for (int i = 0; i < 5; i++) {
            					if (i < entry.mRank) {%>
            						<img src = "images/star_full.png" width="20px" />
            					<%}else{ %>
            						<img src = "images/star_empty.png" width="20px" />
            				<%}}%>
            			</p>
            			<audio src=<%=entry.mVoice%> controls="controls">Your browser does not support the audio element.</audio>
            			<span class="tile-title"><%=entry.mName%></span>
            			<% String form = "delete"+entry.mId; %>
						<form id=<%=form%> name=<%=form%> action="/delete.do" method="post">
							<input type="hidden" name="_id" value=<%=entry.mId%> >
							<input type="hidden" name="type" value="delete" >
						</form>
            			<span id=<%=entry.mId%> data-form=<%=entry.mId%> style="width:40px" class="close">x</span>
        			</div>
        			
	        	</li>
		<%
				}
		%>	   
	        	<li data-row="2" data-col="6" data-sizex="2" data-sizey="1" class="pink fg-white-link live-tile" data-mode="flip">
	        		<div class="flip-front ha" style="-webkit-transform: rotateX(0deg); -webkit-transition: all 0s ease 0s; transition: all 0s ease 0s;">	        	
	        			<span class="AboutUs"></span>
	        	    </div>
	        	    <div class="flip-back ha" style="-webkit-transform: rotateX(540deg); -webkit-transition: all 500ms ease 0s; transition: all 500ms ease 0s;">
            			<p>We are Mengjia Kong, Xiaohong Qiu, Tianlong Yun, graduate students in Dartmouth College. This is CS65 Smartphone Programming Final Project!</p>
            			<span class="tile-title">About us</span>
        			</div>
	        	</li>		     	        	
	      	</ul>
	    </div>


</section>



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
			$('.container').animate({opacity:'0.5'});	
		}
		
		//animate on click
		$('.container .live-tile').liveTile();	
//		$('.container .live-tile').liveTile({ repeatCount: 0, delay:0 });
		$('.container .live-tile').dblclick(function() {
			$(this).liveTile("play", 0);
		});	
		$('.container .live-tile').mouseenter(function() {
//			$(this).animate({width:'150%', height:'150%', zIndex:'100'});
			$(this).css('zIndex', '100');
			var sizex= $(this).attr('data-sizex');
			var sizey= $(this).attr('data-sizey');			
			$(this).data('sizex', sizex);
			$(this).data('sizey', sizey);			
			$(this).attr('data-sizex', "3");
			$(this).attr('data-sizey', "3");
			
			$(this).liveTile("pause");	 
		});
		$('.container .live-tile').mouseleave(function() {
			var sizex= $(this).data('sizex');
			var sizey= $(this).data('sizey');
			$(this).attr('data-sizey', sizey);
			$(this).attr('data-sizex', sizex);
			$(this).css('zIndex', '1');			
//			$(this).animate({width:'50%', height:'150%', zIndex:'1'});
    		$(this).liveTile("play");  
		});
		
		$('#admin').click(function() {
			var isadmin = $(this).attr('data-isadmin');
			if (isadmin == '0') {
			var psw = prompt("admin password!");
			if (psw != 'dartmouthcs65')
				alert("admin password is wrong!");
			else
				$(this).attr('data-isadmin', '1');
			}

		});
		
		$('.close').click(function() {
			var isadmin = $('#admin').attr('data-isadmin');
			if (isadmin == 0)
				alert("You are not the admin! Please login!");
			else {
				alert("You are admin!");
				var formid = $(this).attr('data-form');
//				document.write(formid);
				var form = document.getElementById("delete"+formid);  
				form.submit(); 
			}
		});
				
	})(); 
	</script>

</body>
</html>