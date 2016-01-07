package game;

public class Jeu {
	public String token;
	public String nomDeLaPartie;
	private int nbJoueur;
	private int nbJoueurEnCour;
	private String idJoueur1;
	private String idJoueur2;
	private boolean spectateurAutorise;
	
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
	
	public String getIdJoueur2(){
		return this.idJoueur2;
	}
	
	public void setIdJoueur2(String id){
		this.idJoueur2 = id;
	}

}
