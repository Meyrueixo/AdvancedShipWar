package game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import bean.InfoJeuBean;

public class ControleurDeConnexion {
	
	
	private List<Jeu> listeJeu;
	
	
	private static ControleurDeConnexion instance;
	
	public static ControleurDeConnexion GETINSTANCE(){
		if(instance == null){
			instance = new ControleurDeConnexion();
		}
		return instance;
	}
	public String creationPartie(String Token , String nomParti, String idJoueur1){
		String idPartie = existeJoueur(idJoueur1);
		if(idPartie == null){
			Jeu monJeu =new Jeu(Token,nomParti, idJoueur1);
			listeJeu.add(monJeu);
			return monJeu.token;
		}else{
			return idPartie;
		}

	}
	
	public Jeu connexion(String idJeu,ClientWebSocket client){
		Jeu instanceJeu = recherche(idJeu);
		
		if(instanceJeu == null){
			try {
				String objectToReturn = "{ Erreur: 'La parti n'existe pas' }";
				client.getSession().getBasicRemote().sendText(objectToReturn);
				client.getSession().close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}else{
			return instanceJeu;
		}
			
	}
	public List<InfoJeuBean> listinfoJeu(){
		List<InfoJeuBean> infos = new ArrayList<InfoJeuBean>();
		for (Jeu jeu : listeJeu) {
			infos.add(new InfoJeuBean(jeu.token,jeu.nomDeLaPartie));
		}
		return infos;
	}
	
	public Jeu recherche(String Token){
		Jeu instanceJeu = null;
		boolean trouver = false;
		Iterator it =listeJeu.iterator();
		while (it.hasNext() && !trouver ) {
			
			instanceJeu = ((Jeu)it.next());
			if(Token.equals(instanceJeu.token)){
				trouver = true;
			}	
		}
		return instanceJeu;
	}
	public String existeJoueur(String idJoueur){
		Jeu instanceJeu = null;
		boolean trouver = false;
		String idPartie = null;
		Iterator it =listeJeu.iterator();
		while (it.hasNext() && !trouver ) {
			
			instanceJeu = ((Jeu)it.next());
			if(idJoueur.equals(instanceJeu.getIdJoueur1())){
				trouver = true;
				idPartie = instanceJeu.token;
			}	
		}
		return idPartie ;
	}

	private ControleurDeConnexion() {
		listeJeu = new ArrayList<Jeu>();
		// TODO Auto-generated constructor stub
	}

}
