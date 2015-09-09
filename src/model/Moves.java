package model;

import java.util.ArrayList;

/**
 * Hepler methods for calculating moves
 */
public class Moves {
    /* Outcomes */
    public static final int PLAYER_WON = -1;
    public static final int TIE = 0;
    public static final int AI_WON = 1;

    /**
     * Returns the legal moves available for color at the passed state of the board
     *
     * @param color HTe color to make a move
     * @param board The state of the board
     * @return The legal moves currently available
     */
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

    /**
     * Fills in moves with the possible moves for color at the state board starting at x,y and moving in
     * the direction dx,dy
     *
     * @param color The color making the move
     * @param board The state of the board
     * @param y     The y coordinate to start at
     * @param x     The x coordinate to start at
     * @param dy    The y direction to move at
     * @param dx    The x direction to move at
     * @param moves The possible moves
     */
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
                    moves.add(new Integer[]{x, y});
                }
                break;
            }
            y += dy;
            x += dx;
        }
    }

    /**
     * Fills in possible moves with all the horizontal moves at state board by color
     *
     * @param color The color making the moves
     * @param board The state of the board
     * @param y     The x coordinate to start at
     * @param x     The y coordinate to start at
     * @param moves The possible moves
     */
    private static void getHorizontalMoves(int color, int[][] board, int y,
                                           int x, ArrayList<Integer[]> moves) {
        getMoves(color, board, y, x, 0, 1, moves);
        getMoves(color, board, y, x, 0, -1, moves);
    }

    /**
     * Fills in possible moves with all the vertical moves at state board by color
     *
     * @param color The color making the moves
     * @param board The state of the board
     * @param y     The x coordinate to start at
     * @param x     The y coordinate to start at
     * @param moves The possible moves
     */
    private static void getVerticalMoves(int color, int[][] board, int y,
                                         int x, ArrayList<Integer[]> moves) {
        getMoves(color, board, y, x, 1, 0, moves);
        getMoves(color, board, y, x, -1, 0, moves);
    }

    /**
     * Fills in possible moves with all the diagonal moves at state board by color
     *
     * @param color The color making the moves
     * @param board The state of the board
     * @param y     The x coordinate to start at
     * @param x     The y coordinate to start at
     * @param moves The possible moves
     */
    private static void getDiagonalMoves(int color, int[][] board, int y,
                                         int x, ArrayList<Integer[]> moves) {
        getMoves(color, board, y, x, 1, 1, moves);
        getMoves(color, board, y, x, -1, 1, moves);
        getMoves(color, board, y, x, 1, -1, moves);
        getMoves(color, board, y, x, -1, -1, moves);
    }

    /**
     * Perform move by color at state board
     *
     * @param board The state of the board
     * @param move  The move to be performed
     * @param color The color making the move
     */
    public static void performMove(int[][] board, int[] move, int color) {
        board[move[0]][move[1]] = color;
        performHorizontalMoves(color, board, move);
        performVerticalMoves(color, board, move);
        performDiagonalMoves(color, board, move);
    }

    /**
     * Performs the horizontal moves by color at state board, turning the chips not in colorbetween the chip at x,y and
     * the next chip in the same color
     *
     * @param color The color making the move
     * @param board The state of the board
     * @param move  The move to be made
     */
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

    /**
     * Performs the vertical moves by color at state board, turning the chips not in color between the chip at x,y and
     * the next chip in the same color
     *
     * @param color The color making the move
     * @param board The state of the board
     * @param move  The move to be made
     */
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

    /**
     * Performs the diagonal moves by color at state board, turning the chips not in color between the chip at x,y and
     * the next chip in the same color
     *
     * @param color The color making the move
     * @param board The state of the board
     * @param move  The move to be made
     */
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

    /**
     * Checks whether move is a currently legal move
     *
     * @param currentLegalMoves The currently legal moves
     * @param move              The move
     * @return True if legal move, false if not
     */
    public static boolean isLegalMove(int[][] currentLegalMoves, int[] move) {
        for (int[] legalMove : currentLegalMoves) {
            if (legalMove[0] == move[0] && legalMove[1] == move[1]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks which participant has won and returns the outcome
     * @param state The state of the board
     * @param aiColor The color if the AI
     * @return Returns the outcome
     */
    public static int utility(int[][] state, int aiColor) {
        int aiMarkers = 0;
        int playerMarkers = 0;
        for (int[] row : state) {
            for (int cell : row) {
                if (cell == aiColor) {
                    aiMarkers++;
                } else if (cell + aiColor == 0) {
                    playerMarkers++;
                }
            }
        }
        if (aiMarkers > playerMarkers) {
            return AI_WON;
        } else if (aiMarkers < playerMarkers) {
            return PLAYER_WON;
        }
        return TIE;
    }
}
