package ai;

import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;

import model.OthelloBoard;

/**
 * Class making the moves for the AI
 */
public class AI implements Observer {
    private int color;
    private OthelloBoard board;

    public AI(OthelloBoard board, int color) {
        board.addObserver(this);
        this.board = board;
        this.color = color;
    }

    /**
     * Invoked when a move is to be made by the AI
     *
     * @param o   The observable making the call
     * @param arg The arguments passed by the observable
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Integer && ((Integer) arg) == color) {
            new runThread().start();
        }
    }

    /**
     * A private class for executing the search algorithm to find the next move and update the board asynchronously
     * The search has a time limit of 5 seconds
     */
    private class runThread extends Thread {

        @Override
        public void run() {
            final int[] move = Search.findMove(board, color, 5000L);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    board.setSquare(move[0], move[1], color);
                }
            });

        }
    }

}
