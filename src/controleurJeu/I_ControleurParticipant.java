package controleurJeu;

import java.io.IOException;

import org.json.JSONObject;

public interface I_ControleurParticipant {
	
	public void end();
	public String getNickname();
	public void incoming(JSONObject message);
	public void send(String message) throws IOException;
	public ControleurJeu getjeu();
	public void setJeu(ControleurJeu jeu);
	public void close() throws IOException;
	void setIWebSocket(ClientWebSocket sock);
	

}
