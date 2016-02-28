package bateau;

import java.util.List;

import jeu.Mine;
import orientation.I_Orientation;

public abstract class Bateau {
	
	protected int position;
	protected int pointDeVie;
	protected int longueur;
	protected int capaciteDeDeplacement;
	protected I_Orientation orientation;
	
	public int getPosition() {
		return position;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}
	
	public int getPointDeVie() {
		return pointDeVie;
	}
	
	public void setPointDeVie(int pointDeVie) {
		this.pointDeVie = pointDeVie;
	}
	
	public int getCapaciteDeDeplacement() {
		return capaciteDeDeplacement;
	}
	
	public void setCapaciteDeDeplacement(int capaciteDeDeplacement) {
		this.capaciteDeDeplacement = capaciteDeDeplacement;
	}

	public I_Orientation getOrientation() {
		return orientation;
	}

	public void setOrientation(I_Orientation orientation) {
		this.orientation = orientation;
	}

	public int getLongueur() {
		return longueur;
	}

	public void setLongueur(int longueur) {
		this.longueur = longueur;
	}
	
	public abstract boolean placerBateau(int pos);
	public abstract int[] caseOccupeParLeBateau();
	public abstract boolean deplacerBateau(int pos,I_Orientation rotation,List<Mine> listeMine);
	
	public void perdsUnPointDeVie(){
		this.pointDeVie --;
	}
	
	
	
	
}
