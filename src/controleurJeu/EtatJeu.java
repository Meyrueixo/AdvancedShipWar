package controleurJeu;

public enum EtatJeu {
	ATTENTEJOUEUR("En Attente de joueur"),
	PREPARATION("Pr�paration des bateaux"),
	ENJEU("En jeu"),
	FIN("Fin");

	private String name = "";

	EtatJeu (String name){
		this.name = name;

	}
	public String getDescription(){
		return this.name;
	}
}
