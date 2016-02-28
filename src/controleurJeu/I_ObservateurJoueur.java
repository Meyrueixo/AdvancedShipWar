package controleurJeu;

public interface I_ObservateurJoueur {

	public void modifPointAction(int point);
	public void touche(int pos,String Bateau);
	public void erreur(String message);
	
}
