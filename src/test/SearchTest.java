package test;

import model.OthelloBoard;
import ai.Search;

public class SearchTest {

	public static void main(String[] args){
		
		int[] optMove = Search.findMove(new OthelloBoard(8, 8), OthelloBoard.WHITE, 5L);
		System.out.println("Optimal move: " + optMove[0] + ", " + optMove[1]);
	}
}
