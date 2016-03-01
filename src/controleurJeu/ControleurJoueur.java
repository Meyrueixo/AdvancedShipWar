package controleurJeu;

import java.io.IOException;

import org.json.JSONObject;

import jeu.Joueur;
import orientation.OrientationFactory;
import orientation.OrientationNORTH;

public class ControleurJoueur implements I_ControleurJoueur,I_ObservateurJoueur{


	private String TokenPlayer;
	public ControleurJeu monjeu;
	private ClientWebSocket socket;
	private Joueur joueur;

	public ControleurJoueur(ControleurJeu jeu){
		monjeu = jeu;
	}
	public ControleurJoueur(ControleurJeu jeu,ClientWebSocket socket){
		monjeu = jeu;
		this.socket = socket;
		joueur = new Joueur(OrientationNORTH.getInstance(),
				OrientationNORTH.getInstance(),
				OrientationNORTH.getInstance(),
				OrientationNORTH.getInstance());
	}


	@Override
	public void end() {
		monjeu.getListeConnections().remove(this);
		String message = String.format("* %s %s",
				getNickname(), "has disconnected.");
		monjeu.broadcast(message,true);

	}

	@Override
	public String getNickname() {
		// TODO Auto-generated method stub
		return "Joueur";
	}

	@Override
	public void setIWebSocket(ClientWebSocket sock) {
		this.socket = sock;
		// TODO Auto-generated method stub

	}

	@Override
	public void incoming(JSONObject message) {
		
		
		if(monjeu != null){
			boolean ok = true;
			try {	
				if( message.has("chat") ){
					traitementChat( message);
				}
				if(monjeu.adversaire(this) != null){
					
					Joueur advaisaire =((ControleurJoueur) monjeu.adversaire(this)).getJoueur();	
					if( message.has("mine") ){
						this.send(message.toString());
						joueur.placerMine(message.getJSONObject("mine").getInt("pos"));
						monjeu.broadcast(message.toString(),false);
						
					}
					if( message.has("bateauPlacement") ){
						String bateau =  message.getJSONObject("bateauPlacement").getString("type");
						int pos = message.getJSONObject("bateauPlacement").getInt("pos");
						String orientation =  message.getJSONObject("bateauPlacement").getString("orientation");
						if(bateau.equals("PorteAvion")){
							ok = joueur.placerPorteAvion(pos, OrientationFactory.create(orientation));
						}else if(bateau.equals("Croiseur")){
							ok = joueur.placerCroiseur(pos, OrientationFactory.create(orientation));
						
						}else if(bateau.equals("SousMarin")){
							ok = joueur.placerSousMarin(pos, OrientationFactory.create(orientation));
						}else if(bateau.equals("Torpilleur")){
							ok = joueur.placerTorpilleur(pos, OrientationFactory.create(orientation));
						}
						
						if(ok){
							JSONObject json = new JSONObject();
							json.accumulate("bateau",message.getJSONObject("bateauPlacement"));
							this.send(json.toString());
							monjeu.broadcast(json.toString(),false);
						}else{
							erreur("placement inposible");
						}
						
						
					}
					if( message.has("bateau") ){
						monjeu.broadcast(message.toString(),false);
						
						String bateau =  message.getJSONObject("bateau").getString("type");
						int pos = message.getJSONObject("bateau").getInt("pos");
						String orientation =  message.getJSONObject("bateau").getString("orientation");
						if(bateau.equals("PorteAvion")){
							ok = joueur.deplacerPorteAvion(pos, OrientationFactory.create(orientation),advaisaire.getListeDeMine());
						}else if(bateau.equals("Croiseur")){
							ok = joueur.deplacerCroiseur(pos, OrientationFactory.create(orientation),advaisaire.getListeDeMine());
						
						}else if(bateau.equals("SousMarin")){
							ok = joueur.deplacerSousMarin(pos, OrientationFactory.create(orientation),advaisaire.getListeDeMine());
						}else if(bateau.equals("Torpilleur")){
							ok = joueur.deplacerTorpilleur(pos, OrientationFactory.create(orientation),advaisaire.getListeDeMine());
						}
						if(ok){
							/*JSONObject json = new JSONObject();
							json.accumulate("bateau",message.getJSONObject("bateauPlacement"));*/
							this.send(message.toString());
							monjeu.broadcast(message.toString(),false);
						}else{
				
						}
						this.send(message.toString());
					}
					if( message.has("tir") ){
						monjeu.broadcast(message.toString(),false);
						joueur.tireUnMissile(message.getJSONObject("connect").getInt("pos"),advaisaire);
						this.send(message.toString());
	
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			monjeu.enVie = true;

		}


	}

	@Override
	public void send(String message) throws IOException{
		this.socket.getSession().getBasicRemote().sendText(message);

	}

	@Override
	public void setIdJoueur(String idJoueur) {
		this.TokenPlayer = idJoueur;

	}

	@Override
	public String getIdJoueur() {
		// TODO Auto-generated method stub
		return TokenPlayer;
	}

	@Override
	public ControleurJeu getjeu() {
		// TODO Auto-generated method stub
		return monjeu;
	}

	@Override
	public void close() throws IOException{
		socket.getSession().close();

	}

	@Override
	public void setJeu(ControleurJeu jeu) {
		this.monjeu = jeu;

	}

	private void traitementChat(JSONObject obj) {
		String text = obj.getJSONObject("chat").getString("message");
		String destination = obj.getJSONObject("chat").getString("destinataire");
		if(destination.equals("tous")){
			JSONObject json = new JSONObject();
			json.accumulate("chat",this.getNickname() +" : "+ text);
			monjeu.broadcast(json.toString(),true);
		}
	}
	public Joueur getJoueur() {
		return joueur;
	}
	public void setJoueur(Joueur joueur) {
		this.joueur = joueur;
	}
	@Override
	public void modifPointAction(int point) {
		JSONObject json = new JSONObject();
		json.accumulate("pointAction",point);
		try {
			this.send(json.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	public void touche(int pos, String Bateau) {
		JSONObject json = new JSONObject();
		json.accumulate("touche",pos);
		try {
			this.send(json.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	public void erreur(String message) {
		JSONObject json = new JSONObject();
		json.accumulate("erreur",message);
		try {
			this.send(json.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



}
