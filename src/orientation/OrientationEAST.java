package orientation;

import java.util.List;

import bateau.Bateau;
import jeu.Mine;

public class OrientationEAST implements I_Orientation{
	
	private static OrientationEAST instance;
	
	protected OrientationEAST(){
		
	}
	
	public static OrientationEAST getInstance(){
		if(instance == null){
			instance = new OrientationEAST();
		}
		return instance;
	}
	
	@Override
	public boolean placerBateau(int pos, Bateau boat) {
		if(pos%10 > boat.getLongueur() ){
			return false;
		} else{
			return true;
		}
	}
	
	@Override
	public int[] caseOccupeParLeBateau(Bateau boat){
		int[] res = new int[boat.getLongueur()];
		res[boat.getLongueur()-1] = boat.getPosition();
		for (int i = boat.getLongueur()-2;i>=0;i--){
			res[i] = res[i+1]+1;
		}
		return res;
	}

	@Override
	public boolean avancerBateau(int pos, Bateau boat, List<Mine> listeMine) {
		if(pos == boat.getPosition() + boat.getCapaciteDeDeplacement()){
			if(mineSurLeChemin(pos, boat, listeMine)){
				System.out.println("Votre bateau a rencontré une mine !");
			}
			boat.setPosition(pos);
			return true;
		} else {
			return false;
		}
	}
	
	private boolean mineSurLeChemin(int pos,Bateau boat,List<Mine> listeMine){
		boolean minePresente = false;
		Mine m;
		for(int i = 0;i < listeMine.size();i++){
			m = listeMine.get(i);
			if(boat.getPosition()< m.getPosition() && m.getPosition() <= pos+boat.getLongueur()){
				minePresente = true;
				boat.perdsUnPointDeVie();
				listeMine.remove(m);
			}
		}
		return minePresente;
	}
	public String toString(){
		return "E";
	}

}
