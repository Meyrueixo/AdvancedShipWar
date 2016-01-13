package game;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

public class Jeu {
	public String token;
	public String nomDeLaPartie;
	private int nbJoueur;
	private int nbJoueurEnCour;
	private String idJoueur1;
	private String idJoueur2;
	private ClientWebSocket Joueur1;
	private ClientWebSocket Joueur2;
	private boolean spectateurAutorise;
	public boolean enVie = true;
    private  AtomicInteger connectionIds = new AtomicInteger(1);
    private  Set<ClientWebSocket> connections = new CopyOnWriteArraySet<>();
	
	public Jeu() {
		// TODO Auto-generated constructor stub
	}

	public Jeu(String token2, String nomParti) {
		this.token = token2;
		this.nomDeLaPartie = nomParti;
	}
	
	public Jeu(String token, String nomParti, String idJoueur1){
		this.token = token;
		this.nomDeLaPartie = nomParti;
		this.idJoueur1 = idJoueur1;
	}
	
	public String getToken(){
		return this.token;
	}
	
	public String getIdJoueur1(){
		return this.idJoueur1;
	}
	
	public void setIdJoueur1(String id){
		this.idJoueur1 = id;
	}
	
	public String getIdJoueur2(){
		return this.idJoueur2;
	}
	
	public void setIdJoueur2(String id){
		this.idJoueur2 = id;
	}

	public Set<ClientWebSocket> getListeConnections() {
		return connections;
	}

	private void setListeConnections(Set<ClientWebSocket> connections) {
		this.connections = connections;
	}
	
	public void broadcast(String msg) {
	        for (ClientWebSocket client : connections) {
	            try {
	                synchronized (client) {
	                    client.getSession().getBasicRemote().sendText(msg);
	                }
	            } catch (IOException e) {
	                //log.debug("Chat Error: Failed to send message to client", e);
	                connections.remove(client);
	                try {
	                    client.getSession().close();
	                } catch (IOException e1) {
	                    // Ignore
	                }
	                String message = String.format("* %s %s",
	                        client.getNickname(), "has been disconnected.");
	                broadcast(message);
	            }
	        }
	    }

	public ClientWebSocket getJoueur1() {
		return Joueur1;
	}

	public void setJoueur1(ClientWebSocket joueur1) {
		Joueur1 = joueur1;
	}

	public ClientWebSocket getJoueur2() {
		return Joueur2;
	}

	public void setJoueur2(ClientWebSocket joueur2) {
		Joueur2 = joueur2;
	}

}
