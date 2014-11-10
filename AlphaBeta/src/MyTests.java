

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class MyTests {

	//TODO test false inputs
	
	
	//////////////////////Testing GameState//////////////////////
	@Test
	public void testDraw() {
		char[] initialState = {'x', 'o', 'x', 'x', 'x', 'o', 'o', 'x', 'o'};
		GameState newGame = new GameState(initialState);
		assertEquals(true, newGame.isOver());
		assertEquals(0, newGame.whoWon());
	}	
	
	@Test
	public void testGameNotFinished() {
		char[] initialState = {'x', 'o', 'x', 'x', 'x', 'o', 'o', '_', 'o'};
		GameState newGame = new GameState(initialState);
		assertEquals(false, newGame.isOver());
		assertEquals(0, newGame.whoWon());
	}		
	
	@Test
	public void testXWinDiagnal() {
		char[] initialState = {'x', 'o', 'o', 'o', 'x', 'x', 'o', '_', 'x'};
		GameState newGame = new GameState(initialState);
		assertEquals(false, newGame.isOver());
		assertEquals(1, newGame.whoWon());
	}		
	
	@Test
	public void testXWinHorizontal() {
		char[] initialState = {'o', 'x', 'o', 'x', 'x', 'x', 'o', 'o', 'x'};
		GameState newGame = new GameState(initialState);
		assertEquals(true, newGame.isOver());
		assertEquals(1, newGame.whoWon());
	}		
	
	@Test
	public void testXWinVertical() {
		char[] initialState = {'x', 'x', 'o', 'x', 'o', '_', 'x', 'o', 'o'};
		GameState newGame = new GameState(initialState);
		assertEquals(false, newGame.isOver());
		assertEquals(1, newGame.whoWon());
	}	
	
	@Test
	public void testOWinDiagnal() {
		char[] initialState = {'o', 'x', 'o', 'x', 'o', 'x', 'o', 'x', 'x'};
		GameState newGame = new GameState(initialState);
		assertEquals(true, newGame.isOver());
		assertEquals(-1, newGame.whoWon());
	}		
	
	@Test
	public void testOWinHorizontal() {
		char[] initialState = {'x', 'o', 'x', 'x', 'x', 'o', 'o', 'o', 'o'};
		GameState newGame = new GameState(initialState);
		assertEquals(true, newGame.isOver());
		assertEquals(-1, newGame.whoWon());
	}		
	
	@Test
	public void testOWinVertical() {
		char[] initialState = {'x', 'o', 'x', '_', 'o', 'x', 'x', 'o', 'o'};
		GameState newGame = new GameState(initialState);
		assertEquals(false, newGame.isOver());
		assertEquals(-1, newGame.whoWon());
	}	
	
	@Test
	public void testGameStateInitialization(){
		char[] initialState = {'x', 'o', 'x', '_', 'o', 'x', 'x', 'o', 'o'};
		GameState newGame = new GameState(initialState);
		assertEquals("xox_oxxoo", newGame.toString());
	}

	@Test
	public void testGetPossibleChildren(){
		AlphaBeta newGame = new AlphaBeta("xo_oxx_xo");
		ArrayList<GameState> list = newGame.getGameState().getPossibleChildren(SpotValue.O);
		assertEquals("xoooxx_xo", list.get(0).toString());
		assertEquals("xo_oxxoxo", list.get(1).toString());
		assertEquals(2, list.size());
		
		newGame = new AlphaBeta("xo_ox__xo");
		list = newGame.getGameState().getPossibleChildren(SpotValue.X);
		assertEquals("xoxox__xo", list.get(0).toString());
		assertEquals("xo_oxx_xo", list.get(1).toString());
		assertEquals("xo_ox_xxo", list.get(2).toString());
		assertEquals(3, list.size());
	}

	
//////////////////////Testing AlphaBeta//////////////////////
	@Test(expected=RuntimeException.class)
	public void testWrongInputCount(){
		String commandLine = "xxooxxo";
		new AlphaBeta(commandLine);
	}
	
	@Test(expected=RuntimeException.class)
	public void testNullInput(){
		new AlphaBeta(null);
	}
	
	@Test(expected=RuntimeException.class)
	public void wrongCharacterFound(){
		new AlphaBeta("asdfasdff");
	}
	
	@Test
	public void testToLowerCase(){
		AlphaBeta testCase = new AlphaBeta("XX__OO__x");
		assertEquals("xx__oo__x", testCase.getGameState().toString());
	}
	
	//TODO playMove method is obsolete, will not allow all children to be investigated
	@Test	//Test that addition of X and O is working properly (i.e. filling the first open spot it comes across)
	public void addingWorksProperly(){
		AlphaBeta testCase = new AlphaBeta("XX__OO__x");
		testCase.getGameState().playMove(SpotValue.O);
		assertEquals("xxo_oo__x", testCase.getGameState().toString());
		testCase.getGameState().playMove(SpotValue.X);
		assertEquals("xxoxoo__x", testCase.getGameState().toString());
		testCase.getGameState().playMove(SpotValue.X);
		assertEquals("xxoxoox_x", testCase.getGameState().toString());
		testCase.getGameState().playMove(SpotValue.O);
		assertEquals("xxoxooxox", testCase.getGameState().toString());	
	}	
	
	@Test
	public void testingDrawScenario(){
		//AlphaBeta testCase = new AlphaBeta("XX__OO__x");
		AlphaBeta testCase = new AlphaBeta("xo_ox__xo");
		int outcome = testCase.recur(testCase.getGameState(), testCase.maximizer, true);
		assertEquals(0, outcome);
	}	
	
	@Test	
	public void testingXWinScenario(){
		AlphaBeta testCase = new AlphaBeta("XO_______");
		int outcome = testCase.recur(testCase.getGameState(), testCase.maximizer, true);
		assertEquals(1, outcome);
		
		testCase = new AlphaBeta("OXX___O__");
		outcome = testCase.recur(testCase.getGameState(), testCase.maximizer, true);
		assertEquals(SpotValue.X, testCase.maximizer);
		assertEquals(-1, outcome);
		
		testCase = new AlphaBeta("O___X____");
		outcome = testCase.recur(testCase.getGameState(), testCase.maximizer, true);
		assertEquals(SpotValue.X, testCase.maximizer);
		assertEquals(0, outcome);	
	}	
	
	@Test
	public void testingPruningIsBetter(){
		AlphaBeta testCase = new AlphaBeta("XO_______");
		int outcome1 = testCase.recur(testCase.getGameState(), testCase.maximizer, true);
		int iterations1 = testCase.iterations;	
		testCase = new AlphaBeta("XO_______");
		int outcome2 = testCase.recurWithPruning(testCase.getGameState(), -5, 5, testCase.maximizer, true);
		int iterations2 = testCase.iterations;
		assertEquals(outcome1, outcome2);
		assertTrue(iterations2 < iterations1);	
	}
	
	@Test
	public void testingNewEvalMethod(){
		//Testcase 1
		AlphaBeta testCase = new AlphaBeta("XO_______");
		int outcome1 = testCase.solveNoPruning();
		int iterations1 = testCase.iterations;
		testCase = new AlphaBeta("XO_______");
		int outcome2 = testCase.solveWithPruning();
		int iterations2 = testCase.iterations;
		assertEquals(outcome1, outcome2);
		assertTrue(iterations2 < iterations1);
		
		//TestCase 2
		testCase = new AlphaBeta("OXX___O__");
		outcome1 = testCase.solveNoPruning();
		iterations1 = testCase.iterations;
		testCase = new AlphaBeta("OXX___O__");
		outcome2 = testCase.solveWithPruning();
		iterations2 = testCase.iterations;
		assertEquals(outcome1, outcome2);
		assertTrue(iterations2 < iterations1);
		
		//TestCase 3
		testCase = new AlphaBeta("O___X____");
		outcome1 = testCase.solveNoPruning();
		iterations1 = testCase.iterations;
		testCase = new AlphaBeta("O___X____");
		outcome2 = testCase.solveWithPruning();
		iterations2 = testCase.iterations;
		assertEquals(outcome1, outcome2);
		assertTrue(iterations2 < iterations1);
	}
	
	@Test
	public void testingListOfCuts(){
		AlphaBeta testCase = new AlphaBeta("XO_______");
		testCase.solveWithPruning();
		int size = testCase.alphaCutsList.size() + testCase.betaCutsList.size();
		int cuts = testCase.alphaCuts + testCase.betaCuts;
		assertEquals(size, cuts);
	}
	
	@Test
	public void testingMainPrintout(){
		//No pruning used
		AlphaBeta testCase = new AlphaBeta("XO_______");
		int outcome1 = testCase.solveNoPruning();
		int iterations1 = testCase.iterations;
		
		//Resolve with pruning
		testCase = new AlphaBeta("XO_______");
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
