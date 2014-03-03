package ai;

import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;

import model.OthelloBoard;

public class AI implements Observer {
	private int color;
	private OthelloBoard board;

	public AI(OthelloBoard board, int color) {
		board.addObserver(this);
		this.board = board;
		this.color = color;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof Integer && ((Integer) arg) == color) {
			new runThread().start();
		}
	}

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
