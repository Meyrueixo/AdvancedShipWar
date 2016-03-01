package controleurJeu;

public interface I_ObservateurJoueur {

	public void modifPointAction(int point);
	
	public void erreur(String message);
	void touche(int pos, String Bateau, int pointVie,String orientation);
	
}
