package game;

import java.io.IOException;

import org.json.JSONObject;

public interface I_Participant {
	
	public void end();
	public String getNickname();
	public void incoming(JSONObject message);
	public void send(String message) throws IOException;
	public Jeu getjeu();
	public void setJeu(Jeu jeu);
	public void close() throws IOException;
	void setIWebSocket(ClientWebSocket sock);
	

}
