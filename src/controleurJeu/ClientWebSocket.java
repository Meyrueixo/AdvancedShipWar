package controleurJeu;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


import org.json.JSONObject;

@ServerEndpoint(value = "/game")
public class ClientWebSocket  {
	 //private static final Log log = LogFactory.getLog(ChatAnnotation.class);

	private static final String PLAYER_PREFIX = "Joueur";
	private static final String GUEST_PREFIX = "Spectateur";


    private String nickname;
    private Session session;

    private ControleurDeConnexion controleCon =  ControleurDeConnexion.GETINSTANCE();
    private I_ControleurParticipant participant;
    
    public ClientWebSocket() {
        nickname = GUEST_PREFIX;
    }


    @OnOpen
    public void start(Session session) {
        this.setSession(session);
        
    }


    @OnClose
    public void end() {
    	if(participant != null)
    	{
    		participant.end();
    	}
    	
    }
    
    private void traitementJson(String Json){
    	
    	String filteredMessage = Json;
    	try {
    		JSONObject obj = new JSONObject(Json);
        	
        	if(obj.has("connect")){
        		filteredMessage = TraitementConection(filteredMessage, obj);	
        	}else if(participant != null){
        		participant.incoming(obj);
        	}
        	
		} catch (Exception e) {
			filteredMessage ="Donn�e invalide";
		}
    	
    	 
       
    	
    }


	private String TraitementConection(String filteredMessage, JSONObject obj) {
		String idgame = obj.getJSONObject("connect").getString("idgame");
		String idJoueur = obj.getJSONObject("connect").getString("TokenPlayer");
		participant = controleCon.connexion(idgame,idJoueur, this);
		
		if(participant !=null){
			participant.setIWebSocket(this);
			
			filteredMessage = "truc";
		}
		return filteredMessage;
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
