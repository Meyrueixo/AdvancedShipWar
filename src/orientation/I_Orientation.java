package orientation;

import java.util.List;

import bateau.Bateau;
import jeu.Mine;

public interface I_Orientation {
	
	public abstract boolean placerBateau(int pos,Bateau boat);
	public abstract boolean avancerBateau(int pos, Bateau boat, List<Mine> listeMine);
	public String toString();
	public abstract int[] caseOccupeParLeBateau(Bateau boat);
}
