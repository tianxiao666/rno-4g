function IFrameResize( iframe , flag ) { 
	    //var iframe = document.getElementById("myframe");  
	    var iframeDocument = null; 
	    //safari和chrome都是webkit内核的浏览器，但是webkit可以,chrome不可以 
	    if (iframe.contentDocument) {  
	        //ie 8,ff,opera,safari 
	        iframeDocument = iframe.contentDocument; 
	    }  
	    else if (iframe.contentWindow) {  
	        // for IE, 6 and 7: 
	        iframeDocument = iframe.contentWindow.document; 
	    }  
	    if (!!iframeDocument) {
	    	var width = iframeDocument.documentElement.scrollWidth+"px";
	    	var height = iframeDocument.documentElement.scrollHeight+"px";
	    	if ( flag == "all" ) {
	    		iframe.width = width ; 
	       		iframe.height = height ;      
	    	} else if ( flag == "width" ) {
	    		iframe.width = width ; 
	    	} else if ( flag == "height" ) {
	    	 	iframe.height = height ;      
	    	} else if ( flag == "parent" ) {
	    		
	    	}
	    } else { 
	        alert("this browser doesn't seem to support the iframe document object"); 
	    }
    }