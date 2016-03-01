package controleurJeu;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONObject;

public class ControleurJeu {
	
	public String token;
	public String nomDeLaPartie;
	private int nbJoueur;
	private int nbJoueurEnCour;
	private String idJoueur1;
	private String idJoueur2;
	private I_ControleurJoueur Joueur1;
	private I_ControleurJoueur Joueur2;
	public boolean enVie = true;// plusieur etat 1 = en vie , 0 en at
	private boolean spectateurAutorise;
    private  AtomicInteger connectionIds = new AtomicInteger(1);
    private  Set<I_ControleurParticipant> connections = new CopyOnWriteArraySet<>();
    public EtatJeu etat = EtatJeu.ATTENTEJOUEUR;
	
	public ControleurJeu() {
		// TODO Auto-generated constructor stub
	}

	public ControleurJeu(String token2, String nomParti) {
		this.token = token2;
		this.nomDeLaPartie = nomParti;
	}
	
	public ControleurJeu(String token, String nomParti, String idJoueur1){
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

	public Set<I_ControleurParticipant> getListeConnections() {
		return connections;
	}

	private void setListeConnections(Set<I_ControleurParticipant> connections) {
		this.connections = connections;
	}
	public void actionFaite(JSONObject json,I_ControleurJoueur joueurOrigine){
		
		if(joueurOrigine == Joueur1){
			json.accumulate("plateau", 1);
		}else{
			json.accumulate("plateau", 2);
		}
		
		this.broadcast(json.toString(), false);
	}
	
	public void finDePartie(){
		JSONObject json = new JSONObject();
		json.accumulate("chat","Partie Terminer");
		broadcast(json.toString(),true);
		 for (I_ControleurParticipant client : connections) {
			 try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		
		
	}
	
	public void broadcast(String msg ,  boolean tous) {
	        for (I_ControleurParticipant client : connections) {
	            try {
	                synchronized (client) {
	                	if(!client.getClass().equals(ControleurJoueur.class) || tous){
	                		client.send( msg);
	                	}
	                	
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
	                broadcast(message,true);
	            }
	        }
	    }

	public I_ControleurJoueur getJoueur1() {
		return Joueur1;
	}
	
	public I_ControleurJoueur adversaire(I_ControleurJoueur joueur){
		if(Joueur1 != joueur){
			return Joueur1;
		}else{
			return Joueur2;
		}
		
	}

	public void setJoueur1(I_ControleurJoueur joueur1) {
		Joueur1 = joueur1;
		this.Joueur1.setNickname("Joueur 1");
	}

	public I_ControleurJoueur getJoueur2() {
		return Joueur2;
	}
	
	

	public void setJoueur2(I_ControleurJoueur joueur2) {
		Joueur2 = joueur2;
		this.Joueur2.setNickname("Joueur 2");
		JSONObject json = new JSONObject();
		json.accumulate("chat","Un adversaire a rejoint la partie.");
		etat = EtatJeu.PREPARATION;
		broadcast(json.toString(),true);
	}
	
	public void ajoutParticipant(I_ControleurParticipant part){
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

       broadcast(message,true);
	}

}
