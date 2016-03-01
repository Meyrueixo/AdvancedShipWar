package jeu;

import java.util.ArrayList;
import java.util.List;

import bateau.Croiseur;
import bateau.PorteAvion;
import bateau.SousMarin;
import bateau.Torpilleur;
import controleurJeu.I_ObservateurJoueur;
import orientation.I_Orientation;
import orientation.OrientationNORTH;

public class Joueur  {
	
	/****************** Attributs ********************************/
	private String iD;
	private PorteAvion porteAvion;
	private Croiseur croiseur;
	private SousMarin sousMarin;
	private Torpilleur torpilleur;
	private int pointDAction = 3;
	private List<Mine> listeDeMine;
	private I_ObservateurJoueur ObservateurJoueur;
	private int[] plateau;
	
	/******************* Methods ***********************************/
	
	/************ Constructor *************/
	public Joueur(I_Orientation orientationPorteAvion,
			I_Orientation orientationCroiseur,
			I_Orientation orientationSousMarin,
			I_Orientation orientationTorpilleur){
		this.setPlateau(new int[100]);
		this.setPorteAvion(new PorteAvion(orientationPorteAvion));
		this.setCroiseur(new Croiseur(orientationCroiseur));
		this.setSousMarin(new SousMarin(orientationSousMarin));
		this.setTorpilleur(new Torpilleur(orientationTorpilleur));
		this.listeDeMine = new ArrayList<Mine>();
	}
	
	
	public void placerBateaux(int posPA,int posC,int posSM,int posT){
		this.porteAvion.placerBateau(posPA);
		this.croiseur.placerBateau(posC);
		this.sousMarin.placerBateau(posSM);
		this.torpilleur.placerBateau(posT);
	}
	
	public boolean placerPorteAvion(int pos,I_Orientation orientation){
		this.porteAvion.setOrientation(orientation);
		return this.porteAvion.placerBateau(pos);
		
	}
	
	public boolean placerCroiseur(int pos , I_Orientation orientation){
	
		this.croiseur.setOrientation(orientation);
		return this.croiseur.placerBateau(pos);
	}
	
	public boolean placerSousMarin(int pos, I_Orientation orientation){
		
		this.sousMarin.setOrientation(orientation);
		return this.sousMarin.placerBateau(pos);
	}
	
	public boolean placerTorpilleur(int pos , I_Orientation orientation){
		this.torpilleur.setOrientation(orientation);
		return this.torpilleur.placerBateau(pos);
	}
	
	public boolean tireUnMissile(int tir,Joueur adversaire){
		perdsUnPointDAction();
		if(adversaire.bateauTouche(tir)){
			System.out.println("bateau touché !");
			return true;
		} else {
			System.out.println("plouf !");
			return false;
		}
	}
//appel par l'adv
	public boolean bateauTouche(int pos) {
		boolean pATouche = estPresent(pos,this.porteAvion.caseOccupeParLeBateau());
		if(pATouche){
			this.porteAvion.perdsUnPointDeVie();
			ObservateurJoueur.touche(pos, PorteAvion.class.getName());
		}
		boolean cTouche = estPresent(pos,this.croiseur.caseOccupeParLeBateau());
		if(cTouche){
			this.croiseur.perdsUnPointDeVie();
			ObservateurJoueur.touche(pos, Croiseur.class.getName());
		}
		boolean sMTouche = estPresent(pos,this.sousMarin.caseOccupeParLeBateau());
		if(sMTouche){
			this.sousMarin.perdsUnPointDeVie();
			ObservateurJoueur.touche(pos, SousMarin.class.getName());
		}
		boolean tTouche = estPresent(pos,this.torpilleur.caseOccupeParLeBateau());
		if(tTouche){
			this.torpilleur.perdsUnPointDeVie();
			ObservateurJoueur.touche(pos, Torpilleur.class.getName());
		}
		return (pATouche || cTouche || sMTouche || tTouche);
	}
	
	private boolean estPresent(int a,int[] tab){
		int i =0;
		boolean estTrouve = false;
		while(!estTrouve && i<tab.length){
			if(a == tab[i]){
				estTrouve = true;
			} else {
				i++;
			}
		}
		return estTrouve;
	}
	
	public void placerMine(int pos){
		if(this.getPointDAction() > 0){
			perdsUnPointDAction();
			perdsUnPointDAction();
			this.listeDeMine.add(new Mine(pos));
		}
		
	}
	
	public void perdsUnPointDAction(){
		this.pointDAction --;
		ObservateurJoueur.modifPointAction(this.pointDAction);
	}
	
	public void resetPointsDAction(){
		this.pointDAction = 3;
		ObservateurJoueur.modifPointAction(this.pointDAction);
	}
	//liste mine adversair
	public boolean deplacerPorteAvion(int pos, I_Orientation rotation,List<Mine> listeMine){
		if (this.porteAvion.deplacerBateau(pos, rotation, listeMine)){
			perdsUnPointDAction();
			return true;
		} else {
			//ObservateurJoueur.erreur("Deplacement impossible");
			return false;
		}
	}
	
	public boolean deplacerCroiseur(int pos, I_Orientation rotation,List<Mine> listeMine){
		if (this.croiseur.deplacerBateau(pos, rotation, listeMine)){
			perdsUnPointDAction();
			return true;
		} else {
			//ObservateurJoueur.erreur("Deplacement impossible");
			return false;
		}
	}
	
	public boolean deplacerSousMarin(int pos, I_Orientation rotation,List<Mine> listeMine){
		if (this.sousMarin.deplacerBateau(pos, rotation, listeMine)){
			perdsUnPointDAction();
			return true;
		} else {
			//ObservateurJoueur.erreur("Deplacement impossible");
			return false;
		}
	}
	
	public boolean deplacerTorpilleur(int pos, I_Orientation rotation,List<Mine> listeMine){
		if (this.torpilleur.deplacerBateau(pos, rotation, listeMine)){
			perdsUnPointDAction();
			return true;
		} else {
			//ObservateurJoueur.erreur("Deplacement impossible");
			return false;
		}
	}
	
	/*********** Getter/Setter ************/
	public String getID() {
		return iD;
	}

	public void setID(String id) {
		iD = id;
	}

	public PorteAvion getPorteAvion() {
		return porteAvion;
	}

	public void setPorteAvion(PorteAvion porteAvion) {
		this.porteAvion = porteAvion;
	}

	public Croiseur getCroiseur() {
		return croiseur;
	}

	public void setCroiseur(Croiseur croiseur) {
		this.croiseur = croiseur;
	}

	public SousMarin getSousMarin() {
		return sousMarin;
	}

	public void setSousMarin(SousMarin sousMarin) {
		this.sousMarin = sousMarin;
	}

	public Torpilleur getTorpilleur() {
		return torpilleur;
	}

	public void setTorpilleur(Torpilleur torpilleur) {
		this.torpilleur = torpilleur;
	}
	
	public int getPointDAction() {
		return pointDAction;
	}

	public void setPointDAction(int pointDAction) {
		this.pointDAction = pointDAction;
	}

	public int[] getPlateau() {
		return plateau;
	}

	public void setPlateau(int[] plateau) {
		this.plateau = plateau;
	}
	
	public List<Mine> getListeDeMine() {
		return listeDeMine;
	}

	public void setListeDeMine(List<Mine> listeDeMine) {
		this.listeDeMine = listeDeMine;
	}
	
}
