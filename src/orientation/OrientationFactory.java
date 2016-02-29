package orientation;

public class OrientationFactory {
	
	public OrientationFactory (){
		
	}
	
	public static I_Orientation create(String orientation){
		if(orientation.contains("N")){
			return OrientationNORTH.getInstance();
		} else if(orientation.contains("E")){
			return OrientationEAST.getInstance();
		} else if(orientation.contains("S")){
			return OrientationSOUTH.getInstance();
		} else {
			return OrientationWEST.getInstance();
		}
	}

}
