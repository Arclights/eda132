package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import ai.AI;

/**
 * This is the Othello model.
 * This keeps track of state of the board.
 */
public class OthelloBoard extends Observable implements ActionListener {

    /* The possible colors of the chips in the game */
    public static final int EMPTY = 0;
    public static final int BLACK = 1;
    public static final int WHITE = -1;

    /* The state of the squares of the board */
    private int[][] board;

    /* The legal moves currently available */
    /* The x coordinate have index 0, the y coordinates have index 1 */
    private int[][] currentLegalMoves;

    private int currentColor;
    /* The participants colors */
    private int playerColor;
    private int AIColor;

    /* The number of passes made by each color */
    /* Black has index 0 and white has index 1 */
    private int[] passes;

    /**
     * Creates a board with width squares wide and height square high
     *
     * @param width  The number of squares wide the board should be
     * @param height The number of squares high the board should be
     */
    public OthelloBoard(int width, int height) {
        board = new int[width][height];
        initMarkers(width, height);

        passes = new int[2];

        playerColor = BLACK;
        AIColor = WHITE;
        currentColor = playerColor;

        currentLegalMoves = MoveUtility.getLeagalMoves(currentColor, board);
        new AI(this, AIColor);
    }

    /**
     * Initiates the center squares for a new game
     *
     * @param width  The number of squares wide the board is
     * @param height The number of squares high the board is
     */
    private void initMarkers(int width, int height) {
        board[width / 2][height / 2] = WHITE;
        board[width / 2 - 1][height / 2] = BLACK;
        board[width / 2 - 1][height / 2 - 1] = WHITE;
        board[width / 2][height / 2 - 1] = BLACK;
    }

    /**
     * Creates a new Othello model from the state of the board
     *
     * @param board The state of the board
     */
    private OthelloBoard(int[][] board) {
        this.board = board;
    }

    /**
     * Returns the state of the square at x,y
     *
     * @param x The x coordinate of the square
     * @param y The y coordinate of the square
     * @return The state of the square
     */
    public int getSquare(int x, int y) {
        return board[x][y];
    }

    /**
     * Sets the square at x,y to color if color is the current color playing and x,y is a legal move to make
     *
     * @param x     The x coordinate of the square to set to color
     * @param y     The y coordinate of the square to set to color
     * @param color The color
     */
    public void setSquare(int x, int y, int color) {
        if (color == currentColor
                && MoveUtility.isLegalMove(currentLegalMoves, new int[]{x, y})) {
            MoveUtility.performMove(board, new int[]{x, y}, color);
            handleMoves();
        }
    }

    /**
     * Handles the logic when a pass is made
     * If two passes has been made by the two participants combined, the game is ended
     *
     * @param color The color making the pass
     */
    public void pass(int color) {
        if (color == currentColor) {
            if (passes[0] + passes[1] == 2) {
                int result = MoveUtility.utility(board, AIColor);
                switch (result) {
                    case MoveUtility.PLAYER_WON:
                        setChanged();
                        notifyObservers("Congratulations!\nYou won!");
                        break;
                    case MoveUtility.AI_WON:
                        setChanged();
                        notifyObservers("GAME OVER\nThe computer beat you");
                        break;
                    case MoveUtility.TIE:
                        setChanged();
                        notifyObservers("GAME OVER\nYou tied with the computer");
                        break;
                }

            } else {
                handleMoves();
            }
        }
    }

    /**
     * When a move has been made, this function takes care of the logic to prepare for the next turn.
     * If the moves currently legal after the last move are none for the current color, the program will
     * automatically make a pass
     */
    private void handleMoves() {
        switchCurrentColor();
        currentLegalMoves = MoveUtility.getLeagalMoves(currentColor, board);
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

    /**
     * Switches the current color from one to the other
     */
    private void switchCurrentColor() {
        currentColor *= -1;
    }

    /**
     * Returns the players color
     *
     * @return The color
     */
    public int getPlayerColor() {
        return playerColor;
    }

    /**
     * Returns the number of squares wide the board is
     *
     * @return The number squares wide
     */
    public int getWidth() {
        return board.length;
    }

    /**
     * Returns the number squares high the board is
     *
     * @return The number squares high
     */
    public int getHeight() {
        return board[0].length;
    }

    /**
     * Returns the state of the board
     *
     * @return The state of the board
     */
    public int[][] getState() {
        return board;
    }

    /**
     * Creates a clone of the Othello model based on an empty state
     *
     * @return The cloned Othello model
     */
    @Override
    protected OthelloBoard clone() {
        int[][] cloneBoard = new int[board.length][board[0].length];
        return new OthelloBoard(cloneBoard);
    }

    /**
     * Coupled with the pass button and will pass for the player
     *
     * @param e The action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        pass(playerColor);
    }
}
