package game;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONObject;

@ServerEndpoint(value = "/AdvancedShipWarGame")
public class ClientWebSocket {
	 //private static final Log log = LogFactory.getLog(ChatAnnotation.class);

	private static final String PLAYER_PREFIX = "Joueur";
	private static final String GUEST_PREFIX = "Spectateur";


    private String nickname;
    private Session session;
    private String TokenPlayer;
    private boolean estJoueur;
    public Jeu monjeu;
    private ControleurDeConnexion controleCon =  ControleurDeConnexion.GETINSTANCE();
    
    public ClientWebSocket() {
        nickname = GUEST_PREFIX;
    }


    @OnOpen
    public void start(Session session) {
        this.setSession(session);
        
    }


    @OnClose
    public void end() {
    	monjeu.getListeConnections().remove(this);
        String message = String.format("* %s %s",
                getNickname(), "has disconnected.");
        monjeu.broadcast(message);
    }
    
    private void traitementJson(String Json){
    	
    	String filteredMessage = "";
    	try {
    		JSONObject obj = new JSONObject(Json);
    		if(obj.has("chat") && monjeu != null){
    			traitementChat(obj);
    		}
        	if(obj.has("connect")){
        		filteredMessage = TraitementConection(filteredMessage, obj);	
        	}
		} catch (Exception e) {
			filteredMessage ="Donnée invalide";
		}
    	if(monjeu !=null){
    		monjeu.enVie = true;
    		monjeu.broadcast(filteredMessage);
    	}
    	 
       
    	
    }


	private String TraitementConection(String filteredMessage, JSONObject obj) {
		String idgame = obj.getJSONObject("connect").getString("idgame");
		TokenPlayer = obj.getJSONObject("connect").getString("TokenPlayer");
		monjeu = controleCon.connexion(idgame, this);
		
		if(monjeu !=null){
			JSONObject json = new JSONObject();
			json.accumulate("connection",String.format("nom de la partie : %s: %s",monjeu.nomDeLaPartie,monjeu.getToken()));
			monjeu.getListeConnections().add(this);
			if(monjeu.getIdJoueur1().equals(TokenPlayer)){
				monjeu.setJoueur1(this);
				this.nickname = this.PLAYER_PREFIX;
			}else if(monjeu.getIdJoueur2().equals(TokenPlayer)){
				monjeu.setJoueur2(this);
				this.nickname = this.PLAYER_PREFIX;
			}
	        String message = String.format("* %s %s %s %s", getNickname(), "has joined.",monjeu.getIdJoueur1(),monjeu.getIdJoueur2());
	        monjeu.broadcast(message);
			filteredMessage = json.toString();
		}
		return filteredMessage;
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

    @OnMessage
    public void incoming(String message) {
        // Never trust the client
    	this.traitementJson(message);
  
    }

    @OnError
    public void onError(Throwable t) throws Throwable {
        System.err.println("Chat Error: " + t.toString() + " " + t);
    }

  

	public Session getSession() {
		return session;
	}


	private void setSession(Session session) {
		this.session = session;
	}


	public String getNickname() {
		return nickname;
	}

}
