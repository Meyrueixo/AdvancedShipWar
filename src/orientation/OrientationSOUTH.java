package orientation;

import java.util.List;

import bateau.Bateau;
import jeu.Mine;

public class OrientationSOUTH implements I_Orientation {
	
	private static OrientationSOUTH instance;
	
	protected OrientationSOUTH(){
		
	}
	
	public static OrientationSOUTH getInstance(){
		if(instance == null){
			instance = new OrientationSOUTH();
		}
		return instance;
	}
	
	@Override
	public boolean placerBateau(int pos, Bateau boat) {
		if(pos/10 > boat.getLongueur() ){
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
			res[i] = res[i+1]+10;
		}
		return res;
	}

	@Override
	public boolean  avancerBateau(int pos, Bateau boat, List<Mine> listeMine) {
		if(pos == boat.getPosition() + (10 * boat.getCapaciteDeDeplacement())){
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
			if((boat.getPosition()/10)< (m.getPosition()/10) && (m.getPosition()/10) <=(pos/10)+boat.getLongueur()){
				minePresente = true;
				boat.perdsUnPointDeVie();
				listeMine.remove(i);
			}
			
		}
		return minePresente;
	}
	public String toString(){
		return "S";
	}

}
