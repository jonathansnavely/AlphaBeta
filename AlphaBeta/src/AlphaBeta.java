
import java.util.ArrayList;


/**
 * This program emulates Alpha Beta game tree for a tic tac toe game
 * @author Jon
 */
public class AlphaBeta {
	
	String initialState;
	GameState gameState;
	int iterations = 0;
	SpotValue maximizer;
	int alphaCuts = 0;
	int betaCuts = 0;
	ArrayList<GameState> alphaCutsList;
	ArrayList<GameState> betaCutsList;
	
	public AlphaBeta(String commandLine) throws RuntimeException{
		
		//Failure of using command properly
		if(commandLine == null){
			throw(new RuntimeException("Improper use of commandline. Format: \"./your_program O_XOXX___\""));
		}	
		if(commandLine.length() != 9){
			throw(new RuntimeException("Improper use of commandline. Incorrect number of X's and O's."));
		}	
		
		initialState = commandLine;
		
		//Convert initial game state to lowercase character array
		char[] convert = initialState.toLowerCase().toCharArray();
		
		//Ensure argument only has x, o, and _ characters. Also, cannot have impossible count of either x or o.
		if(!isValid(convert)){
			throw(new RuntimeException("Invalid initial state format. Too many x's or o's"));		
		}	
		
		//Create the initial State of the game
		gameState = new GameState(convert);
		
		//Determine which player is going to be the maximizer
		maximizer = gameState.whoIsMaximizing();
		
		alphaCutsList = new ArrayList<GameState>();
		betaCutsList = new ArrayList<GameState>();
	}
	
	
	/**
	 * This will recursively find the solution to this problem. This will return
	 * +1 for a maximizer's win, 0 for draw, and -1 for maximizer's loss
	 * @param gameState
	 * @param whosTurn: Indicates spotValue (X or O) of who's turn it is 
	 * @param maximizing
	 */
	public int recur(GameState gameState, SpotValue whosTurn, boolean maximizingPlayer) {
		
		//Terminal condition. Return if a win, loss, or draw has happened
		if(gameState.whoWon() == 1)
			return 1;
		if(gameState.whoWon() == -1)
			return -1;
		if(gameState.isOver())
			return 0;
		
		if(maximizingPlayer){
			int max = -5;
			for(GameState children: gameState.getPossibleChildren(whosTurn)){
				iterations++;
				int val = recur(children, getWhosTurn(whosTurn), false);
				if(max < val)
					max = val;
			}	
			return max;
		}
		
		else{ 		//minimizer
			int min = 5;
			for(GameState children: gameState.getPossibleChildren(whosTurn)){
				iterations++;
				int val = recur(children, getWhosTurn(whosTurn), true);
				if(min > val)
					min = val;
			}
			return min;
		}
	}

	
	/**
	 * This will recursively find the solution to this problem utilizing alpha-beta pruning. This will return
	 * +1 for a maximizer's win, 0 for draw, and -1 for maximizer's loss
	 * @param gameState
	 * @param whosTurn: Indicates spotValue (X or O) of who's turn it is 
	 * @param maximizing
	 */
	public int recurWithPruning(GameState gameState, int alpha, int beta, SpotValue whosTurn, boolean maximizingPlayer) {
		
		//Terminal condition. Return if a win, loss, or draw has happened
		if(gameState.whoWon() == 1)
			return 1;
		if(gameState.whoWon() == -1)
			return -1;
		if(gameState.isOver())
			return 0;
		
		if(maximizingPlayer){
			int max = -5;
			for(GameState children: gameState.getPossibleChildren(whosTurn)){
				iterations++;
				int val = recurWithPruning(children, max(alpha, max), beta, getWhosTurn(whosTurn), false);
				if(max < val)
					max = val;
				if(max >= beta){
					betaCuts++;
					betaCutsList.add(gameState);
					break;	//Beta-Cutoff
				}		
			}	
			return max;
		}
		
		else{ 		//minimizer
			int min = 5;
			for(GameState children: gameState.getPossibleChildren(whosTurn)){
				iterations++;
				int val = recurWithPruning(children, alpha, min(beta, min), getWhosTurn(whosTurn), true);
				if(min > val)
					min = val;
				if(min <= alpha){
					alphaCuts++;
					alphaCutsList.add(gameState);
					break;	//Alpha-Cutoff
				}
			}
			return min;
		}
	}

	
	/**
	 * Helper methods for recurWithPruning
	 */
	private int max(int A, int B){
		if(A > B)
			return A;
		else
			return B;
	}
	private int min(int A, int B){
		if(A < B)
			return A;
		else
			return B;
	}
	
	
	
	/**
	 * Methods evaluate the recur calls with additional logic, makes it simple from main call.
	 * The outcome of the recur call will be respective of who was the maximizer (i.e. +1 for maximizer win).	
	 * Thus, this will convert call answer to +1 X win, -1 O win
	 */
	public int solveNoPruning(){
		int outcome = recur(getGameState(), maximizer, true);
		
		//Convert maximizer win to +1 for X, -1 for O
		if(maximizer == SpotValue.O){
			if(outcome == 1)
				return -1;
			if(outcome == -1)
				return 1;
		}

		return outcome;
	}
	public int solveWithPruning(){
		int outcome = recurWithPruning(getGameState(), -5, 5, maximizer, true);

		//Convert maximizer win to +1 for X, -1 for O
		if(maximizer == SpotValue.O){
			if(outcome == 1)
				return -1;
			if(outcome == -1)
				return 1;
		}

		return outcome;
	}
	
	
	
	/**
	 * Return the opposite value of the passed in SpotValue
	 */
	public SpotValue getWhosTurn(SpotValue whosTurnPrevious){
		if(whosTurnPrevious == SpotValue.O)
			return SpotValue.X;
		else
			return SpotValue.O;
	}
	

	/**
	 * Return the gameState (for testing purposes);
	 */
	public GameState getGameState(){
		return gameState;
	}
	
	
	/**
	 * Check to see if every character is x,o, or _, and check for valid count of x's and o's.
	 * @param initialState
	 * @return false if 
	 */
	public static boolean isValid(char[] initialState){

		if(initialState == null)
			return false;

		//Illegal character detection
		for(char eachChar: initialState){
			if(eachChar != '_' && eachChar != 'o' && eachChar != 'x')
				return false;
		}
		
		//Compare x and o count
		int xCount = 0;
		int yCount = 0;
		for(char eachChar: initialState){
			if(eachChar == 'x')
				xCount++;
			else if(eachChar == 'o')
				yCount++;
		}
		
		if(Math.abs(xCount - yCount) >= 2)
			return false;
		else
			return true;
	}
	
	
	/**
	 * @param args[0] is a string of 'X,' 'O,' and '_' representing a partially finished tic tac toe game. The first three characters are the top row,
	 * the second three characters are the middle row, etc. 
	 */
	public static void main(String[] args){	

		//No pruning used
		AlphaBeta testCase = new AlphaBeta(args[0]);
		int outcome1 = testCase.solveNoPruning();
		int iterations1 = testCase.iterations;
		
		//Resolve with pruning
		testCase = new AlphaBeta(args[0]);
		int outcome2 = testCase.solveWithPruning();
		int iterations2 = testCase.iterations;
				
		System.out.println("-----------------------------------------");
		System.out.print("RESULTS FROM NO PRUNING\n\n");
		System.out.println("Game Result: " + outcome1);
		System.out.println("Moves Considered: " + iterations1);
		
		System.out.println("-----------------------------------------");
		System.out.print("RESULTS USING PRUNING\n\n");
		System.out.println("Game Result: " + outcome2);
		System.out.println("Moves Considered: " + iterations2);
		System.out.println("Alpha Cuts: " + testCase.alphaCuts);
		System.out.println("Beta Cuts: " + testCase.betaCuts);	
		System.out.print("\nAlpha Cutoffs\n");
		
		for(GameState gameState: testCase.alphaCutsList){
			System.out.println(gameState);
		}
		
		System.out.print("\nBeta Cutoffs\n");
		for(GameState gameState: testCase.betaCutsList){
			System.out.println(gameState);
		}
	}
}
