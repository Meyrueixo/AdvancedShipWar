package frontEnd;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import game.ControleurDeConnexion;

public class ListePartieTest extends HttpServlet {
	
	private ControleurDeConnexion controleCon =  ControleurDeConnexion.GETINSTANCE();
	
	public void PageListDejeu(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
	 	request.setAttribute( "IdSession", session.getId() );
	 	request.setAttribute( "ListPartie", controleCon.listinfoJeu() );
		try {
			this.getServletContext().getRequestDispatcher( "/WEB-INF/ListePartie.jsp" ).forward( request, response );
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setContentType("text/html") ;
	}
	
	 public  void doGet(HttpServletRequest request, HttpServletResponse response)
			 throws ServletException, IOException  {
		 	
		 		
		 			PageListDejeu (request,  response);
		 	
			}

			 public  void doPost(HttpServletRequest request, HttpServletResponse response)
			 throws ServletException, IOException  {
				 if(((String)request.getParameter("Game")) != null){
					 
					 PageJeu(request,  response,request.getParameter("Game"));
			 		
				 }else if (((String)request.getParameter("Create")) != null){
					 //TODO bug nouvelle création de partie a chaque F5 
			 			UUID idJeu = UUID.randomUUID();	
			 			HttpSession session = request.getSession();
			 			String idjeuCrée = controleCon.creationPartie(idJeu.toString(), request.getParameter("nomPartie"),session.getId());		 			
			 			PageJeu(request,  response,idjeuCrée);
			 		
			 		}
			
			}
			 public void PageJeu(HttpServletRequest request, HttpServletResponse response,String idJeu ){
				 	HttpSession session = request.getSession();
					request.setAttribute( "IdSession", session.getId() );
		 			request.setAttribute( "IdJeu", idJeu );
		 			try {
						this.getServletContext().getRequestDispatcher( "/WEB-INF/PlateauDeJeu.jsp" ).forward( request, response );
					} catch (ServletException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			 }
}
