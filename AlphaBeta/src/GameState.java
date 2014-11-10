import java.util.ArrayList;


/**
 * This class contains the state of a tic tac toe game, including methods to act on it.
 * @author Jon
 *
 */
public class GameState {
	private SpotValue[][] gameBoard;

	public GameState(char[] gameString){

		gameBoard = new SpotValue[3][3];

		int index = 0;

		//Creating the game grid
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				if(gameString[index] == 'x')
					gameBoard[i][j] = SpotValue.X;
				else if(gameString[index] == 'o')
					gameBoard[i][j] = SpotValue.O;
				else if(gameString[index] == '_')
					gameBoard[i][j] = SpotValue.NONE;
				else
					System.out.println("This should never be printed!");

				index++;
			}
		}
	}
	
	/**
	 * This constructor will create a new GameState object using the gameBoard passed to it
	 */
	public GameState(SpotValue[][] newGameState){
		this.gameBoard = newGameState;
	}
	
	/**
	 * This method looks to see if the game board is filled with only x's and o's
	 * @return
	 */
	public boolean isOver(){
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				if(gameBoard[i][j] == SpotValue.NONE)
					return false;
			}
		}

		return true;
	}

	/**
	 * @return -1: O has won the match<br> 0: Draw<br> 1: X has won the match
	 */
	public int whoWon(){
		/*Evaluating if x has won*/

		//Rows
		if(gameBoard[0][0] == SpotValue.X && gameBoard[0][1] == SpotValue.X && gameBoard[0][2] == SpotValue.X)
			return 1;
		if(gameBoard[1][0] == SpotValue.X && gameBoard[1][1] == SpotValue.X && gameBoard[1][2] == SpotValue.X)
			return 1;
		if(gameBoard[2][0] == SpotValue.X && gameBoard[2][1] == SpotValue.X && gameBoard[2][2] == SpotValue.X)
			return 1;	
		//Cols
		if(gameBoard[0][0] == SpotValue.X && gameBoard[1][0] == SpotValue.X && gameBoard[2][0] == SpotValue.X)
			return 1;		
		if(gameBoard[0][1] == SpotValue.X && gameBoard[1][1] == SpotValue.X && gameBoard[2][1] == SpotValue.X)
			return 1;
		if(gameBoard[0][2] == SpotValue.X && gameBoard[1][2] == SpotValue.X && gameBoard[2][2] == SpotValue.X)
			return 1;
		//Diags
		if(gameBoard[0][0] == SpotValue.X && gameBoard[1][1] == SpotValue.X && gameBoard[2][2] == SpotValue.X)
			return 1;		
		if(gameBoard[0][2] == SpotValue.X && gameBoard[1][1] == SpotValue.X && gameBoard[2][0] == SpotValue.X)
			return 1;		

		/*Evaluating if O has won*/

		//Rows
		if(gameBoard[0][0] == SpotValue.O && gameBoard[0][1] == SpotValue.O && gameBoard[0][2] == SpotValue.O)
			return -1;
		if(gameBoard[1][0] == SpotValue.O && gameBoard[1][1] == SpotValue.O && gameBoard[1][2] == SpotValue.O)
			return -1;
		if(gameBoard[2][0] == SpotValue.O && gameBoard[2][1] == SpotValue.O && gameBoard[2][2] == SpotValue.O)
			return -1;	
		//Cols
		if(gameBoard[0][0] == SpotValue.O && gameBoard[1][0] == SpotValue.O && gameBoard[2][0] == SpotValue.O)
			return -1;		
		if(gameBoard[0][1] == SpotValue.O && gameBoard[1][1] == SpotValue.O && gameBoard[2][1] == SpotValue.O)
			return -1;
		if(gameBoard[0][2] == SpotValue.O && gameBoard[1][2] == SpotValue.O && gameBoard[2][2] == SpotValue.O)
			return -1;
		//Diags
		if(gameBoard[0][0] == SpotValue.O && gameBoard[1][1] == SpotValue.O && gameBoard[2][2] == SpotValue.O)
			return -1;		
		if(gameBoard[0][2] == SpotValue.O && gameBoard[1][1] == SpotValue.O && gameBoard[2][0] == SpotValue.O)
			return -1;	


		//Otherwise, count it as a draw
		return 0;	
	}

	/**
	 * This method will add to the tic tac toe game either an X or an O (whatever is passed in)
	 * to the first available open spot seen in the tic tac toe game.
	 */
	public void playMove(SpotValue player){
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				if(gameBoard[i][j] == SpotValue.NONE){
					gameBoard[i][j] = player;
					return;
				}		
			}
		}	
	}
	
	/**
	 * This method is used to create a copy of the gameboard, which will be altered to create a child node.
	 * @return
	 */
	public SpotValue[][] copy(){
		SpotValue[][] copy = new SpotValue[3][3];
		
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){	
				copy[i][j] = gameBoard[i][j];		
			}
		}	
		
		return copy;
	}
	
	/**
	 * This method will return an ArrayList containing all the children states for this current gameBoard
	 */
	public ArrayList<GameState> getPossibleChildren(SpotValue whosTurn){
		ArrayList<GameState> children = new ArrayList<GameState>();
		
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				if(gameBoard[i][j] == SpotValue.NONE){
					SpotValue[][] temp = copy();	//Make a copy of gameBoard
					temp[i][j] = whosTurn;			//Make gameboard[i][j] = whosTurn
					children.add(new GameState(temp));	//Add that gameboard to the array
				}		
			}
		}		
		
		return children;
	}
	
	
	/**
	 * Returns SpotValue indicating who will be the maximizer for the start of the game
	 */
	public SpotValue whoIsMaximizing(){
		int xCount = 0;
		int oCount = 0;
		
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				if(gameBoard[i][j] == SpotValue.X){
					xCount++;
				}	
				else if(gameBoard[i][j] == SpotValue.O){
					oCount++;
				}	
			}
		}		
		
		if(xCount >= oCount)
			return SpotValue.X;
		else
			return SpotValue.O;
	}
	


	/**
	 * This will be used strictly for ensuring board is being set-up correctly.
	 */
	public SpotValue[][] getBoard(){
		return gameBoard;
	}	

	/**
	 * Returns a 1-dimension String representation of the board. Used for testing. 
	 */
	public String toString(){

		String toReturn = "";
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				toReturn += gameBoard[i][j].getValue();	
			}
		}		
		
		return toReturn;
	}	
}
