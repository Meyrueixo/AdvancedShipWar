package controleurJeu;

import java.io.IOException;

import org.json.JSONObject;

import jeu.Joueur;
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
			
			try {	
				if( message.has("chat") ){
					traitementChat( message);
				}
				if(monjeu.adversaire(this) != null){
					
					Joueur advaisaire =((ControleurJoueur) monjeu.adversaire(this)).getJoueur();	
					if( message.has("mine") ){
						monjeu.broadcast(message.toString(),false);
						
						this.send(message.toString());
					}
					if( message.has("bateauPlacement") ){
						String bateau =  message.getJSONObject("bateauPlacement").getString("type");
						int pos = message.getJSONObject("bateauPlacement").getInt("pos");
						if(bateau.equals("PorteAvion")){
							
						}else if(bateau.equals("Croiseur")){
							
						
						}else if(bateau.equals("SousMarin")){
							
						}else if(bateau.equals("Torpiller")){
							
						}
						monjeu.broadcast(message.toString(),false);
						
						this.send(message.toString());
					}
					if( message.has("bateau") ){
						monjeu.broadcast(message.toString(),false);
						
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
