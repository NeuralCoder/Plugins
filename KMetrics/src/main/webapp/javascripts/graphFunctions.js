// Callback that creates and populates a data table,
// instantiates the pie chart, passes in the data and
// draws it.

function drawCharts(data,options,chartContainer,chartType,groupsArray,labelName,colorPattern) {

			if(colorPattern != "") {
			
				var chart = c3.generate({
					
					bindto: "#"+chartContainer,
					
				    data: {
				    
				    	x: 'BUILD',
				        columns: data,
				        groups:groupsArray,
				        type: chartType
				        
				    },
				    
				    axis: {
				    
				    	x: {
				    	
				    		type: 'category',
				    		label: 'BUILDS',
				    		
				    		tick: {
				    		
				    				culling: {max:50},
				    				rotate: 90,
				    				multiline: false,
				    				fit: true
				    				
				    			},
				    		
				    		height:100
				    		
				    		
				    	},
				    	
				    	y: {
				    	
				    		label: {
				    		
				    			text: labelName,
				    			position: 'outer-center'
				    			
				    		},
				    		
				    		padding: {
				    		
				    					bottom:0
				    					
				    				}
				    				
				    	}
				    	
				    },
				    
				    zoom: {
				    
				    	enabled: true
				    	
				    },
				    
				    color: {
				    
				    	pattern: colorPattern
				    	
				    },
				    
				    legend: {
				    
        				position: 'right'
        				
    				},
    				
    				padding: {
				    		
				    	bottom:40
				    					
				    }
    				
    				
				    
				});
				
			} else {
			
				var chart = c3.generate({
					
					bindto: "#"+chartContainer,
					
				    data: {
				    
				    	x: 'BUILD',
				        columns: data,
				        groups:groupsArray,
				        type: chartType
				        
				    },
				    
				    axis: {
				    
				    	x: {
				    	
				    		type: 'category',
				    		label: 'BUILDS',
				    		
				    		tick: {
				    		
				    				culling: {max:50},
				    				rotate: 90,
				    				multiline: false,
				    				fit: true
				    				
				    			   },
				    			   
		    			   height:100
				    		
				    	},
				    	
				    	y: {
				    	
				    		label: labelName,
				    		position: 'outer-center'
				    		
				    	}
				    	
				    },
				    
				    legend: {
				    
        				position: 'right'
        				
    				},
				    
				    padding: {
				    		
	    					bottom:0
				    					
		    				 },
				    
				    zoom: {
				    
				    	enabled: true
				    	
				    }
				    
				});
			}
	
}