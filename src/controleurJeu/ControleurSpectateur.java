package controleurJeu;

import java.io.IOException;

import org.json.JSONObject;

public class ControleurSpectateur implements I_ControleurParticipant{

    private String TokenPlayer;
    private boolean estJoueur;
    public ControleurJeu monjeu;
    private ClientWebSocket socket;
	
	
	public ControleurSpectateur(ControleurJeu instanceJeu) {
		this.monjeu = instanceJeu;
	}

	public ControleurSpectateur(ControleurJeu instanceJeu, ClientWebSocket client) {
		this.monjeu = instanceJeu;
		this.socket = client;
		
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
		return "Spectateur";
	}


	@Override
	public void incoming(JSONObject message) {
		
		if( message.has("chat") && monjeu != null){
			traitementChat( message);
		}
		
	}

	@Override
	public void send(String message) throws IOException {
		this.socket.getSession().getBasicRemote().sendText(message);
		
	}

	@Override
	public ControleurJeu getjeu() {
		// TODO Auto-generated method stub
		return monjeu;
	}

	@Override
	public void close() throws IOException {
		socket.getSession().close();
		
	}

	@Override
	public void setJeu(ControleurJeu jeu) {
		this.monjeu = jeu;
		
	}

	@Override
	public void setIWebSocket(ClientWebSocket sock) {
		this.socket = sock;
		
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



}
