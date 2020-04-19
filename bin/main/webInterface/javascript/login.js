    
  
	
$(document).ready(function() {
		// Check if logged
		var actualLocation=document.location.pathname;
		checkLogged();
		
		setInterval(function() {
				log("checkLogged");
				checkLogged()
					}, 10000);
	    
		function checkLogged()
	    {
	    	getValueFromUrl("admin?action=isLogged",true,function(isLogged){
				log(isLogged);
	    		if ( isLogged == "false" || isLogged == "" || isLogged == "ERROR" )
    			{
					
	    			if ( actualLocation != "/index.html" )
    					{
						console.log("Redirect to " + actualLocation);
	    				document.location="index.html?redirect=" + actualLocation ;
    					}
					
    			}
	    	});
	    } 			    
	});

	