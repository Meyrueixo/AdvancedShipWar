package controleurJeu;

import java.io.IOException;

import org.json.JSONObject;

import jeu.Joueur;
import orientation.OrientationFactory;
import orientation.OrientationNORTH;

public class ControleurJoueur implements I_ControleurJoueur,I_ObservateurJoueur{


	private String TokenPlayer;
	public ControleurJeu monjeu;
	private ClientWebSocket socket;
	private Joueur joueur;
	private String nomJoueur;
	private boolean autoriseJeu = true;
	private boolean aJouer = false;
	private boolean posseautorize = true; 
	private boolean porteAvionPose = false;
	private boolean sousMarniPose = false;
	private boolean croiseurPose = false;
	private boolean torpilleurPose = false;
	private boolean fini = false;

	public ControleurJoueur(ControleurJeu jeu){
		monjeu = jeu;
	}
	
	public boolean toutBateauPosse(){ 
		return porteAvionPose && sousMarniPose && croiseurPose && torpilleurPose ;
	}
	
	public ControleurJoueur(ControleurJeu jeu,ClientWebSocket socket){
		monjeu = jeu;
		this.socket = socket;
		joueur = new Joueur(OrientationNORTH.getInstance(),
				OrientationNORTH.getInstance(),
				OrientationNORTH.getInstance(),
				OrientationNORTH.getInstance());
		joueur.setObservateurJoueur(this);
	}


	@Override
	public void end() {
		monjeu.getListeConnections().remove(this);
		String message = String.format("* %s %s",
				getNickname(), "has disconnected.");
		monjeu.broadcast(message,true);

	}
	public void changeEtat(EtatJeu etat){
	
			JSONObject json = new JSONObject();
			JSONObject etatjson = new JSONObject();
			etatjson.accumulate("description",etat.getDescription());
			etatjson.accumulate("id",etat.ordinal());
			if(etat.ordinal() != 1){
				posseautorize = false; 
			}else{
				posseautorize = true; 
			}
			json.accumulate("etat",etatjson);
			try {
				this.send(json.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public void setNickname(String nomJoueur) {
		this.nomJoueur = nomJoueur;
	}
	@Override
	public String getNickname() {
		// TODO Auto-generated method stub
		return this.nomJoueur;
	}

	@Override
	public void setIWebSocket(ClientWebSocket sock) {
		this.socket = sock;
		// TODO Auto-generated method stub

	}

	@Override
	public void incoming(JSONObject message) {
		
		
		if(monjeu != null){
			boolean ok = false;
			try {	
				if( message.has("chat") ){
					traitementChat( message);
				}
				if(monjeu.adversaire(this) != null){
					
					if( message.has("fin") ){
						if(autoriseJeu && (aJouer==false) || (toutBateauPosse() == false)){
							monjeu.finDePartie("\n" +this.getNickname() + " n'a pas jouer dans les temps \n"+monjeu.adversaire(this).getNickname() + " gagne !!" );
						}else{
							fini = true;
							monjeu.finDeTour(this);
							if(((ControleurJoueur) monjeu.adversaire(this)).getJoueur().plusdevie()){
								monjeu.finDePartie("\n" +this.getNickname() + " a perdu \n"+monjeu.adversaire(this).getNickname() + " GAGNE !!" );
							}
							
						}
					}
					if(autoriseJeu){
						
						aJouer = true;
						Joueur advaisaire =((ControleurJoueur) monjeu.adversaire(this)).getJoueur();	
						if( message.has("mine") ){
							
							if(joueur.placerMine(message.getJSONObject("mine").getInt("pos"))){
								monjeu.actionFaite(message, monjeu.adversaire(this));
								message.accumulate("plateau", 1);
								this.send(message.toString());
							}
							
						}else{
							if( message.has("bateauPlacement") && (posseautorize)){
								String bateau =  message.getJSONObject("bateauPlacement").getString("type");
								int pos = message.getJSONObject("bateauPlacement").getInt("pos");
								String orientation =  message.getJSONObject("bateauPlacement").getString("orientation");
								int pointDeVie = 0;
								if(bateau.equals("PorteAvion")){
									ok = joueur.placerPorteAvion(pos, OrientationFactory.create(orientation));
									pointDeVie = joueur.getPointViePorteAvion();
									porteAvionPose = true;
								}else if(bateau.equals("Croiseur")){
									ok = joueur.placerCroiseur(pos, OrientationFactory.create(orientation));
									pointDeVie = joueur.getPointVieCroiseur();
									croiseurPose = true;
								}else if(bateau.equals("SousMarin")){
									ok = joueur.placerSousMarin(pos, OrientationFactory.create(orientation));
									 sousMarniPose = true;
									 pointDeVie = joueur.getPointVieSousMarin();
								}else if(bateau.equals("Torpilleur")){
									ok = joueur.placerTorpilleur(pos, OrientationFactory.create(orientation));
									torpilleurPose = true;
									pointDeVie = joueur.getPointVieTorpilleur();
								}
								
								if(ok){
									JSONObject json = new JSONObject();
									json.accumulate("bateau",message.getJSONObject("bateauPlacement").accumulate("vie", pointDeVie));
									monjeu.actionFaite(json,this);
									json.remove("plateau");
									json.accumulate("plateau", 1);
									this.send(json.toString());
									
								}else{
									erreur("placement inposible");
								}
								
								
							}
						}
						
						if( message.has("bateau") ){
							
							
							String bateau =  message.getJSONObject("bateau").getString("type");
							int pos = message.getJSONObject("bateau").getInt("pos");
							String orientation =  message.getJSONObject("bateau").getString("orientation");
							int pointDeVie = 0;
							if(bateau.equals("PorteAvion")){
								ok = joueur.deplacerPorteAvion(pos, OrientationFactory.create(orientation),advaisaire.getListeDeMine());
								pointDeVie = joueur.getPointViePorteAvion();
							}else if(bateau.equals("Croiseur")){
								ok = joueur.deplacerCroiseur(pos, OrientationFactory.create(orientation),advaisaire.getListeDeMine());
								pointDeVie = joueur.getPointVieCroiseur();
							}else if(bateau.equals("SousMarin")){
								ok = joueur.deplacerSousMarin(pos, OrientationFactory.create(orientation),advaisaire.getListeDeMine());
								pointDeVie = joueur.getPointVieSousMarin();
							}else if(bateau.equals("Torpilleur")){
								ok = joueur.deplacerTorpilleur(pos, OrientationFactory.create(orientation),advaisaire.getListeDeMine());
								pointDeVie = joueur.getPointVieTorpilleur();
							}
							if(ok){
								JSONObject json = new JSONObject();
								json.accumulate("bateau",message.getJSONObject("bateau").accumulate("vie", pointDeVie));
								monjeu.actionFaite(json,this);
								json.remove("plateau");
								json.accumulate("plateau", 1);
								this.send(json.toString());
							}else{
								erreur("placement inposible");
							}
							//this.send(message.toString());
						}
						if( message.has("tir") ){
							
							
							if(joueur.tireUnMissile(message.getJSONObject("tir").getInt("pos"),advaisaire)){
								
								message.getJSONObject("tir").accumulate("resultat", 2);
							}else{
								message.getJSONObject("tir").accumulate("resultat", 1);
							}
							message.accumulate("plateau", 2);
							this.send(message.toString());
							message.remove("plateau");
							monjeu.actionFaite(message,monjeu.adversaire(this));
						
		
						}
					}
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//monjeu.broadcast(e.toString(), true);
			}
			monjeu.enVie = true;

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
	public ControleurJeu getjeu() {
		// TODO Auto-generated method stub
		return monjeu;
	}

	@Override
	public void close() throws IOException{
		socket.getSession().close();

	}

	@Override
	public void setJeu(ControleurJeu jeu) {
		this.monjeu = jeu;

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
	public Joueur getJoueur() {
		return joueur;
	}
	public void setJoueur(Joueur joueur) {
		this.joueur = joueur;
	}
	@Override
	public void modifPointAction(int point) {
		JSONObject json = new JSONObject();
		json.accumulate("pointAction",point);
		if(point <= 0){
			this.setAutorisationJeu(false);
		}
		try {
			this.send(json.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	@Override
	public void touche(int pos, String Bateau, int pointVie,String orientation) {
		
		JSONObject jsonBateau = new JSONObject();
		JSONObject jsonData = new JSONObject();
		jsonData.accumulate("type",Bateau);
		jsonData.accumulate("vie",pointVie);
		jsonData.accumulate("pos", pos);
		jsonData.accumulate("orientation",orientation);
		jsonBateau.accumulate("bateau",jsonData);
		jsonBateau.accumulate("plateau", 1);
		try {
			this.send(jsonBateau.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jsonBateau.remove("plateau");
		monjeu.actionFaite(jsonBateau,this);
		
	}
	@Override
	public void erreur(String message) {
		JSONObject json = new JSONObject();
		json.accumulate("erreur",message);
		try {
			this.send(json.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean getAutorisationJeu() {
		
		return this.autoriseJeu;
	}

	@Override
	public void setAutorisationJeu(boolean autorise) {
		this.autoriseJeu = autorise;
		
	}

	@Override
	public void nouveautour() {
		this.joueur.resetPointsDAction();
		this.autoriseJeu = true;
		this.aJouer = false;
		this.fini = false;
		JSONObject json = new JSONObject();
		json.accumulate("go",1);
		try {
			this.send(json.toString());
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	@Override
	public boolean aFini() {
		
		return this.fini;
	}



}
