/**
 * Used to represent the value for each tic tac toe spot in GameState class.
 * @author Jon
 *
 */
public enum SpotValue {
	X 	("x"),
	O 	("o"),
	NONE 	("_");
	
	private final String value;
	
	SpotValue(String value){
		this.value = value;
	}
	
	public String getValue(){
		return this.value;
	}
}
