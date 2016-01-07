<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <body>
	    <p> Identifient du jeu   
	    	<div id="idJeu"><% String IdJeu = (String) request.getAttribute("IdJeu");
		            out.println( IdJeu );
	        %></div>
	        
	        Identifient du Client :  
	        <div id="idClient">
	            <% String IdSession = (String) request.getAttribute("IdSession");
	            	out.print("Id Session :");
		            out.println( IdSession );
	            %>
            </div>
               <textarea id="chatlog" readonly></textarea><br/>
		        <input id="msg" type="text"  width="500" height="50"/>
		        <button type="submit" id="sendButton" onClick="postToServer()">Send lolilol!</button>
		        <button type="submit" id="sendButton" onClick="closeConnect()">End</button>
      
	        
	        </p>
	<div>
		<canvas id="myCanvas" width="500" height="500" style=" border: 3px solid #ccc;"/>
	</div>
  <button onclick="poserMine();">Poser une mine</button>
  <button onclick="marquerCase();">Marquer case</button>
  <div>
		<canvas id="myCanvas" width="500" height="500" style=" border: 3px solid #ccc;"/>
	</div>
	<div>
		<p id="info" >h</p>
		<p id="infoserv" >h</p>
	</div>

    </body>
	<script src="js/jquery.min.js"></script>
	<script src="js/main.js" type="text/javascript"></script>

</html>
