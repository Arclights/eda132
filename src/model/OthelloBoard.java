package model;

import java.util.Observable;

import ai.AI;

public class OthelloBoard extends Observable {

	public static final int EMPTY = 0;
	public static final int BLACK = 1;
	public static final int WHITE = -1;

	private int[][] board;
	private int[][] currentLegalMoves;
	private int currentColor;

	private int playerColor;
	private int AIColor;

	public OthelloBoard(int width, int height) {
		board = new int[width][height];
		board[width / 2][height / 2] = WHITE;
		board[width / 2 - 1][height / 2] = BLACK;
		board[width / 2 - 1][height / 2 - 1] = WHITE;
		board[width / 2][height / 2 - 1] = BLACK;

		playerColor = BLACK;
		AIColor = WHITE;
		currentColor = playerColor;
		currentLegalMoves = Moves.getLeagalMoves(currentColor, board);
		new AI(this, AIColor);
	}

	private OthelloBoard(int[][] board) {
		this.board = board;
	}

	public int getSquare(int x, int y) {
		return board[x][y];
	}

	public void setSquare(int x, int y, int n) {
		if (n == currentColor
				&& Moves.isLegalMove(currentLegalMoves, new int[] { x, y })) {
			Moves.performMove(board, new int[] { x, y }, n);
			currentColor *= -1;
			currentLegalMoves = Moves.getLeagalMoves(currentColor, board);
			setChanged();
			notifyObservers(currentColor);
		}
	}

	public void pass(int color) {
		if (color == currentColor) {
			currentColor *= -1;
			currentLegalMoves = Moves.getLeagalMoves(currentColor, board);
			setChanged();
			notifyObservers(currentColor);
		}
	}
	
	public int getPlayerColor(){
		return playerColor;
	}

	public int getWidth() {
		return board.length;
	}

	public int getHeight() {
		return board[0].length;
	}

	public int[][] getState() {
		return board;
	}

	@Override
	protected OthelloBoard clone() {
		int[][] cloneBoard = new int[board.length][board[0].length];
		return new OthelloBoard(cloneBoard);
	}
}
