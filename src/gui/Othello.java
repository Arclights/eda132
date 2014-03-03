package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import model.OthelloBoard;

@SuppressWarnings("serial")
public class Othello extends JFrame {

	private final int SQUARE_SIZE = 60;

	public Othello(OthelloBoard board) {
		super("Othello");
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		add(new TurnPanel(board));

		gc.gridx = 1;
		JButton passButton = new JButton("Pass");
		add(passButton, gc);

		JPanel squares = new JPanel();
		squares.setLayout(new GridLayout(board.getWidth(), board.getHeight()));
		// squares.setSize(board.getWidth() * SQUARE_SIZE, board.getHeight() *
		// SQUARE_SIZE);
		squares.setPreferredSize(new Dimension(board.getWidth() * SQUARE_SIZE,
				board.getHeight() * SQUARE_SIZE));
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		for (int y = 0; y < board.getHeight(); y++) {
			for (int x = 0; x < board.getWidth(); x++) {
				squares.add(new OthelloSquare(board, x, y));
			}
		}
		gc.gridx = 0;
		gc.gridy = 1;
		gc.gridwidth = 2;
		add(squares, gc);
		pack();
		setVisible(true);
	}

}
