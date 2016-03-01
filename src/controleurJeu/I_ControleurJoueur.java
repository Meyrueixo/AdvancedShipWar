package controleurJeu;

public interface I_ControleurJoueur extends I_ControleurParticipant {

	
	public void setIdJoueur(String idJoueur);
	public String getIdJoueur();
	public void setNickname(String nomJoueur);
	public boolean getAutorisationJeu();
	public void setAutorisationJeu(boolean autorise);
	public void nouveautour();
	public boolean aFini();
}
