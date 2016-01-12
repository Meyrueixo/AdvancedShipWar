package game;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

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
    private static final AtomicInteger connectionIds = new AtomicInteger(0);
    private static final Set<ClientWebSocket> connections =
            new CopyOnWriteArraySet<>();

    private final String nickname;
    private Session session;
    private String TokenPlayer;
    private boolean estJoueur;
    public Jeu monjeu;
    private ControleurDeConnexion controleCon =  ControleurDeConnexion.GETINSTANCE();
    
    public ClientWebSocket() {
        nickname = GUEST_PREFIX + connectionIds.getAndIncrement();
    }


    @OnOpen
    public void start(Session session) {
        this.setSession(session);
        connections.add(this);
        String message = String.format("* %s %s", nickname, "has joined.");
        broadcast(message);
    }


    @OnClose
    public void end() {
        connections.remove(this);
        String message = String.format("* %s %s",
                nickname, "has disconnected.");
        broadcast(message);
    }
    private void traitementJson(String Json){
    	
    	String filteredMessage = "";
    	try {
    		JSONObject obj = new JSONObject(Json);
    		if(obj.has("chat")){
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
    	}
    	 broadcast(filteredMessage);
       
    	
    }


	private String TraitementConection(String filteredMessage, JSONObject obj) {
		String idgame = obj.getJSONObject("connect").getString("idgame");
		TokenPlayer = obj.getJSONObject("connect").getString("TokenPlayer");
		monjeu = controleCon.connexion(idgame, this);
		
		if(monjeu !=null){
			JSONObject json = new JSONObject();
			json.accumulate("connection",String.format("nom de la partie : %s: %s",monjeu.nomDeLaPartie,monjeu.getToken()));
			 
			filteredMessage = json.toString();
		}
		return filteredMessage;
	}


	private void traitementChat(JSONObject obj) {
		String text = obj.getJSONObject("chat").getString("message");
		String destination = obj.getJSONObject("chat").getString("destinataire");
		if(destination.equals("tous")){
			JSONObject json = new JSONObject();
			json.accumulate("chat", PLAYER_PREFIX +" : "+ text);
			 broadcast(json.toString());
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

//pour test
    private static void broadcast(String msg) {
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
                        client.nickname, "has been disconnected.");
                broadcast(message);
            }
        }
    }


	public Session getSession() {
		return session;
	}


	private void setSession(Session session) {
		this.session = session;
	}

}
