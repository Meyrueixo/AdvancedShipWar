<%@page import="bean.InfoJeuBean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta charset="utf-8" />
        <title>Test</title>
    </head>
    <body>
        <p>Info du client :</p>
        <p>
            <% String IdSession = (String) request.getAttribute("IdSession");
            	out.print("Id Session :");
	            out.println( IdSession );
            %>
        </p>
        <p> Liste des Parties</p>
       	<form action="" method="post">
		  <% java.util.List<InfoJeuBean> listeInfoPartie = (java.util.ArrayList) request.getAttribute("ListPartie");
		  	for(int i =0;i < listeInfoPartie.size(); i++ ){
		  		out.print("<input type=\"radio\" name=\"Game\" value=\"");
	            out.print( listeInfoPartie.get(i).getToken() );
	            out.print("\">");
	            out.print( listeInfoPartie.get(i).getNomDeLaPartie() );
	            out.print("<br>");
		  	}
            	
            %>
			  <input type="submit" value="Submit">
		</form>
		<p> Crée une partie</p>
		<form action="" method="post">
			 Non de la partie<br>
			  <input type="text" name="nomPartie">
			  <br>
			  	<input type="hidden" name="Create" value="true" >
			   <input type="submit" value="Submit">
		</form>
    </body>
</html>