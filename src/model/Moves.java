package model;

import java.util.ArrayList;

public class Moves {
	public static int[][] getLeagalMoves(int color, int[][] board) {
		ArrayList<Integer[]> moves = new ArrayList<Integer[]>();
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[x].length; y++) {
				if (board[x][y] == color) {
					getHorizontalMoves(color, board, y, x, moves);
					getVerticalMoves(color, board, y, x, moves);
					getDiagonalMoves(color, board, y, x, moves);
				}
			}
		}

		int[][] out = new int[moves.size()][2];
		int i = 0;
		for (Integer[] move : moves) {
			out[i][0] = move[0].intValue();
			out[i][1] = move[1].intValue();
			i++;
		}
		return out;
	}

	private static void getMoves(int color, int[][] board, int y, int x,
			int dy, int dx, ArrayList<Integer[]> moves) {
		y += dy;
		x += dx;
		while (x < board.length && x >= 0 && y < board[0].length && y >= 0) {
			if (board[x][y] == color) {
				break;
			}
			if (board[x][y] == OthelloBoard.EMPTY) {
				if (board[x - dx][y - dy] + color == 0) {
					moves.add(new Integer[] { x, y });
				}
				break;
			}
			y += dy;
			x += dx;
		}
	}

	private static void getHorizontalMoves(int color, int[][] board, int y,
			int x, ArrayList<Integer[]> moves) {
		getMoves(color, board, y, x, 0, 1, moves);
		getMoves(color, board, y, x, 0, -1, moves);
	}

	private static void getVerticalMoves(int color, int[][] board, int y,
			int x, ArrayList<Integer[]> moves) {
		getMoves(color, board, y, x, 1, 0, moves);
		getMoves(color, board, y, x, -1, 0, moves);
	}

	private static void getDiagonalMoves(int color, int[][] board, int y,
			int x, ArrayList<Integer[]> moves) {
		getMoves(color, board, y, x, 1, 1, moves);
		getMoves(color, board, y, x, -1, 1, moves);
		getMoves(color, board, y, x, 1, -1, moves);
		getMoves(color, board, y, x, -1, -1, moves);
	}

	public static void performMove(int[][] board, int[] move, int color) {
		board[move[0]][move[1]] = color;
		performHorizontalMoves(color, board, move);
		performVerticalMoves(color, board, move);
		performDiagonalMoves(color, board, move);
	}

	private static void performHorizontalMoves(int color, int[][] board,
			int[] move) {
		int y = move[1];
		int x = move[0];
		for (int i = x - 1; i >= 0; i--) {
			if (board[i][y] == color) {
				for (int j = i + 1; j < x; j++) {
					board[j][y] = color;
				}
				break;
			}
			if (board[i][y] == OthelloBoard.EMPTY) {
				break;
			}
		}
		for (int i = x + 1; i < board.length; i++) {
			if (board[i][y] == color) {
				for (int j = i - 1; j > x; j--) {
					board[j][y] = color;
				}
				break;
			}
			if (board[i][y] == OthelloBoard.EMPTY) {
				break;
			}
		}
	}

	private static void performVerticalMoves(int color, int[][] board,
			int[] move) {
		int y = move[1];
		int x = move[0];

		for (int i = y - 1; i >= 0; i--) {
			if (board[x][i] == color) {
				for (int j = i + 1; j < y; j++) {
					board[x][j] = color;
				}
				break;
			}
			if (board[x][i] == OthelloBoard.EMPTY) {
				break;
			}
		}
		for (int i = y + 1; i < board.length; i++) {
			if (board[x][i] == color) {
				for (int j = i - 1; j > y; j--) {
					board[x][j] = color;
				}
				break;
			}
			if (board[x][i] == OthelloBoard.EMPTY) {
				break;
			}
		}
	}

	private static void performDiagonalMoves(int color, int[][] board,
			int[] move) {
		int y = move[1];
		int x = move[0];

		// \
		for (int i = y - 1, j = x - 1; i >= 0 && j >= 0; i--, j--) {
			if (board[j][i] == color) {
				for (int k = i + 1, l = j + 1; k < y && l < x; k++, l++) {
					board[l][k] = color;
				}
				break;
			}
			if (board[j][i] == OthelloBoard.EMPTY) {
				break;
			}
		}
		for (int i = y + 1, j = x + 1; i < board.length && j < board.length; i++, j++) {
			if (board[j][i] == color) {
				for (int k = i - 1, l = j - 1; k > y && l > x; k--, l--) {
					board[l][k] = color;
				}
				break;
			}
			if (board[j][i] == OthelloBoard.EMPTY) {
				break;
			}
		}

		// /
		for (int i = y - 1, j = x + 1; i >= 0 && j < board.length; i--, j++) {
			if (board[j][i] == color) {
				for (int k = i + 1, l = j - 1; k < y && l > x; k++, l--) {
					board[l][k] = color;
				}
				break;
			}
			if (board[j][i] == OthelloBoard.EMPTY) {
				break;
			}
		}
		for (int i = y + 1, j = x - 1; i < board.length && j >= 0; i++, j--) {
			if (board[j][i] == color) {
				for (int k = i - 1, l = j + 1; k > y && l < x; k--, l++) {
					board[l][k] = color;
				}
				break;
			}
			if (board[j][i] == OthelloBoard.EMPTY) {
				break;
			}
		}
	}

	public static boolean isLegalMove(int[][] currentLegalMoves, int[] move) {
		for (int[] legalMove : currentLegalMoves) {
			if (legalMove[0] == move[0] && legalMove[1] == move[1]) {
				return true;
			}
		}
		return false;
	}
}
