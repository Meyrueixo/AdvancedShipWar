package bean;

public class InfoJeuBean {

	
	public InfoJeuBean(String token, String nomDeLaPartie) {
		super();
		Token = token;
		this.nomDeLaPartie = nomDeLaPartie;
	}

	private String Token;
	private String nomDeLaPartie;
	private int nbJoueur;
	private int nbJoueurEnCour;
	private String idJoueur1;
	private String idJoueur2;
	private boolean spectateurAutorise;
	
	
	public String getToken() {
		return Token;
	}

	public void setToken(String token) {
		Token = token;
	}

	public String getNomDeLaPartie() {
		return nomDeLaPartie;
	}

	public void setNomDeLaPartie(String nomDeLaPartie) {
		this.nomDeLaPartie = nomDeLaPartie;
	}

	public int getNbJoueur() {
		return nbJoueur;
	}

	public void setNbJoueur(int nbJoueur) {
		this.nbJoueur = nbJoueur;
	}

	public int getNbJoueurEnCour() {
		return nbJoueurEnCour;
	}

	public void setNbJoueurEnCour(int nbJoueurEnCour) {
		this.nbJoueurEnCour = nbJoueurEnCour;
	}

	public String getIdJoueur1() {
		return idJoueur1;
	}

	public void setIdJoueur1(String idJoueur1) {
		this.idJoueur1 = idJoueur1;
	}

	public String getIdJoueur2() {
		return idJoueur2;
	}

	public void setIdJoueur2(String idJoueur2) {
		this.idJoueur2 = idJoueur2;
	}

	public boolean isSpectateurAutorise() {
		return spectateurAutorise;
	}

	public void setSpectateurAutorise(boolean spectateurAutorise) {
		this.spectateurAutorise = spectateurAutorise;
	}

	public InfoJeuBean() {
		// TODO Auto-generated constructor stub
	}

}
