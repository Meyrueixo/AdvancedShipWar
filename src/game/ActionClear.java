package game;


import java.util.List;
import java.util.TimerTask;

public class ActionClear extends TimerTask {
	List<Jeu> ListJeu;
	
	public ActionClear(List<Jeu> ListJeu)
	{
		this.ListJeu = ListJeu;
	}
	@Override
	public void run() {
		
		this.clearListeJeu();

	}
	public synchronized void clearListeJeu(){
		Jeu instanceJeu = null;
		for (int i = 0; i < ListJeu.size(); i++) {
			instanceJeu = ListJeu.get(i);
			if(instanceJeu.enVie == false){
				ListJeu.remove(i);
			}else{
				instanceJeu.enVie = false;
			}
		}
	}

}
