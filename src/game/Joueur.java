package game;

import java.io.IOException;

import org.json.JSONObject;

public class Joueur implements I_Joueur{


	private String TokenPlayer;

	public Jeu monjeu;
	private ClientWebSocket socket;

	public Joueur(Jeu jeu){
		monjeu = jeu;
	}
	public Joueur(Jeu jeu,ClientWebSocket socket){
		monjeu = jeu;
		this.socket = socket;
	}


	@Override
	public void end() {
		monjeu.getListeConnections().remove(this);
		String message = String.format("* %s %s",
				getNickname(), "has disconnected.");
		monjeu.broadcast(message);

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
		if( message.has("chat") && monjeu != null){
			traitementChat( message);
		}
		if(monjeu !=null){
			monjeu.enVie = true;
			//monjeu.broadcast(filteredMessage);
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
	public Jeu getjeu() {
		// TODO Auto-generated method stub
		return monjeu;
	}

	@Override
	public void close() throws IOException{
		socket.getSession().close();

	}

	@Override
	public void setJeu(Jeu jeu) {
		this.monjeu = jeu;

	}

	private void traitementChat(JSONObject obj) {
		String text = obj.getJSONObject("chat").getString("message");
		String destination = obj.getJSONObject("chat").getString("destinataire");
		if(destination.equals("tous")){
			JSONObject json = new JSONObject();
			json.accumulate("chat",this.getNickname() +" : "+ text);
			monjeu.broadcast(json.toString());
		}
	}



}
