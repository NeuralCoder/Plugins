// Function that constructs dinamically an HML table
function myCreateFunction(data,identifier) 
{
	// Parse the K1 Objects and split the object field one by one based on comma (" | ") 
	var str = data.split ("|");
	var j = 0;
	
	if (identifier == "K1")
	{
		var table = document.getElementById("tableK1").getElementsByTagName('tbody')[0];
	}
	else
	{
		var table = document.getElementById("tableK1lite").getElementsByTagName('tbody')[0];
	}
	
	for (var i = 0; i < str.length; i+=4)
	{
			var row = table.insertRow(table.rows.length);
		
			var cell0 = row.insertCell(0);
			var cell1 = row.insertCell(1);
			var cell2 = row.insertCell(2);
			var cell3 = row.insertCell(3);
		
			cell0.innerHTML = str[i];
			cell1.innerHTML = str[i+1];
			
			if (str[i+3] == "null")
			{
				cell3.innerHTML = " - ";
			}
			else if (str[i+3] == 100.0)
			{
				cell3.innerHTML = 100;
			}
			
			else
			{
				cell3.innerHTML = str[i+3];
			}
			
			if (j % 2 == 1)
			{
				cell0.className = "tg-vn4c";
				cell1.className = "tg-vn4c-center";
				cell3.className = "tg-vn4c-center";
			
				if (str[i+2] == "null")
				{
					cell2.innerHTML = " - ";
					cell2.className = "tg-vn4c-center";
					row.setAttribute("class","row-hover-no-swatt");
				}
				else
				{
					cell2.innerHTML = str[i+2];
					cell2.className = "tg-vn4c";
					row.setAttribute("class","row-hover-swatt");
					
				}
				
				if (str[i+3] == 0.0)
				{
					row.setAttribute("class","row-hover-swatt-zero");
				}
				
				else if ((str[i+3] < 50) && (str[i+3] > 0))
				{
					row.setAttribute("class","row-hover-swatt-25");
				}
				
				else if ((str[i+3] < 100) && (str[i+3] > 50))
				{
					row.setAttribute("class","row-hover-swatt-50");
				}
				
				else if (str[i+3] == 100)
				{
					row.setAttribute("class","row-hover-swatt-100");
				}
			
			}
			else
			{
				cell0.className = "tg-yw4l";
				cell1.className = "tg-yw4l-center";
				cell3.className = "tg-yw4l-center";
			
				if (str[i+2] == "null")
				{
					cell2.innerHTML = " - ";
					cell2.className = "tg-yw4l-center";
					row.setAttribute("class","row-hover-no-swatt");
				}
				else
				{
					cell2.innerHTML = str[i+2];
					cell2.className = "tg-yw4l";
					row.setAttribute("class","row-hover-swatt");
				}
				
				if (str[i+3] == 0.0)
				{
					row.setAttribute("class","row-hover-swatt-zero");
				}
				
				else if ((str[i+3] < 50) && (str[i+3] > 0))
				{
					row.setAttribute("class","row-hover-swatt-25");
				}
				
				else if ((str[i+3] < 100) && (str[i+3] > 50))
				{
					row.setAttribute("class","row-hover-swatt-50");
				}
				
				else if (str[i+3] == 100)
				{
					row.setAttribute("class","row-hover-swatt-100");
				}
				
				
			}
			
			j++;
	}
}



