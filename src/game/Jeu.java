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

}
