package game;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONObject;

public class Jeu {
	public String token;
	public String nomDeLaPartie;
	private int nbJoueur;
	private int nbJoueurEnCour;
	private String idJoueur1;
	private String idJoueur2;
	private I_Joueur Joueur1;
	private I_Joueur Joueur2;
	private boolean spectateurAutorise;
	public boolean enVie = true;
    private  AtomicInteger connectionIds = new AtomicInteger(1);
    private  Set<I_Participant> connections = new CopyOnWriteArraySet<>();
	
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

	public Set<I_Participant> getListeConnections() {
		return connections;
	}

	private void setListeConnections(Set<I_Participant> connections) {
		this.connections = connections;
	}
	
	public void broadcast(String msg) {
	        for (I_Participant client : connections) {
	            try {
	                synchronized (client) {
	                	client.send(msg);
	                    //client.getSession().getBasicRemote().sendText(msg);
	                }
	            } catch (IOException e) {
	                //log.debug("Chat Error: Failed to send message to client", e);
	               
	                try {
	                    client.close();
	                } catch (IOException e1) {
	                     connections.remove(client);
	                }
	                String message = String.format("* %s %s",
	                        client.getNickname(), "has been disconnected.");
	                broadcast(message);
	            }
	        }
	    }

	public I_Joueur getJoueur1() {
		return Joueur1;
	}

	public void setJoueur1(I_Joueur joueur1) {
		Joueur1 = joueur1;
	}

	public I_Joueur getJoueur2() {
		return Joueur2;
	}

	public void setJoueur2(I_Joueur joueur2) {
		Joueur2 = joueur2;
	}
	
	public void ajoutParticipant(I_Participant part){
		this.connections.add(part);
		
		JSONObject json = new JSONObject();
		json.accumulate("connection",String.format("nom de la partie : %s: %s",nomDeLaPartie,getToken()));
		try {
			part.send(json.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
       String message = String.format("* %s %s %s %s", part.getNickname(), "has joined.",getIdJoueur1(),getIdJoueur2());

       broadcast(message);
	}

}
