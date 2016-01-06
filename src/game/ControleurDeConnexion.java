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
	public void creationPartie(String Token , String nomParti){
		listeJeu.add(new Jeu(Token,nomParti));
	}
	public void connexion(String idJeu,ClientWebSocket client){
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
		}else{
			//TODO g�re ajout joueur ou spectateur
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

	private ControleurDeConnexion() {
		listeJeu = new ArrayList<Jeu>();
		// TODO Auto-generated constructor stub
	}

}