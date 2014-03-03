package gui;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;

import model.OthelloBoard;

@SuppressWarnings("serial")
public class TurnPanel extends JPanel implements Observer {

	TurnIndicator aiIndicator;
	TurnIndicator playerIndicator;

	public TurnPanel(OthelloBoard board) {
		board.addObserver(this);
		aiIndicator = new TurnIndicator(OthelloBoard.WHITE);
		playerIndicator = new TurnIndicator(OthelloBoard.BLACK);
		add(new JLabel("Turn:"));
		add(aiIndicator);
		add(playerIndicator);
		aiIndicator.repaint();
	}

	@Override
	public void update(Observable o, Object arg) {
		aiIndicator.toggleSelect();
		playerIndicator.toggleSelect();
	}

}
