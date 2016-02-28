package bateau;

import java.util.List;

import jeu.Mine;
import orientation.I_Orientation;

public class Croiseur extends Bateau{
	
	public Croiseur(I_Orientation orientation){
		this.orientation = orientation;
		this.pointDeVie = 4;
		this.capaciteDeDeplacement = 2;
		this.longueur = 4;
	}
	
	@Override
	public boolean placerBateau(int pos){
		if (this.orientation.placerBateau(pos, this)){
			this.position = pos;
			return true;
		} else {
			return false;
		}
	}
	
	public int[] caseOccupeParLeBateau(){
		return this.orientation.caseOccupeParLeBateau(this);
	}

	@Override
	public boolean deplacerBateau(int pos,I_Orientation rotation,List<Mine> listeMine) {
		this.orientation = rotation;
		return this.orientation.avancerBateau(pos,this,listeMine);
	}
}
