package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import ai.AI;
import model.Moves;

public class OthelloBoard extends Observable implements ActionListener {

	public static final int EMPTY = 0;
	public static final int BLACK = 1;
	public static final int WHITE = -1;

	private int[][] board;
	private int[][] currentLegalMoves;
	private int currentColor;

	private int playerColor;
	private int AIColor;

	private int[] passes;

	public OthelloBoard(int width, int height) {
		board = new int[width][height];
		board[width / 2][height / 2] = WHITE;
		board[width / 2 - 1][height / 2] = BLACK;
		board[width / 2 - 1][height / 2 - 1] = WHITE;
		board[width / 2][height / 2 - 1] = BLACK;

		passes = new int[2];

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

	public void setSquare(int x, int y, int color) {
		if (color == currentColor
				&& Moves.isLegalMove(currentLegalMoves, new int[] { x, y })) {
			Moves.performMove(board, new int[] { x, y }, color);
			handleMoves();
		}
	}

	public void pass(int color) {
		if (color == currentColor) {
			if (passes[0] + passes[1] == 2) {
				int result = Moves.utility(board, AIColor);
				switch (result) {
				case Moves.PLAYER_WON:
					setChanged();
					notifyObservers("Congratulations!\nYou won!");
					break;
				case Moves.AI_WON:
					setChanged();
					notifyObservers("GAME OVER\nThe computer beat you");
					break;
				case Moves.TIE:
					setChanged();
					notifyObservers("GAME OVER\nYou tied with the computer");
					break;
				}

			} else {
				handleMoves();
			}
		}
	}

	private void handleMoves() {
		currentColor *= -1;
		currentLegalMoves = Moves.getLeagalMoves(currentColor, board);
		if (currentLegalMoves.length == 0) {
			switch (currentColor) {
			case BLACK:
				passes[0] = 1;
				break;
			case WHITE:
				passes[1] = 1;
				break;
			}
			pass(currentColor);
		} else {
			setChanged();
			notifyObservers(currentColor);
		}
	}

	public int getPlayerColor() {
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

	@Override
	public void actionPerformed(ActionEvent e) {
		pass(playerColor);
	}
}
