package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import model.OthelloBoard;

@SuppressWarnings("serial")
public class Othello extends JFrame implements ActionListener {

	private final int SQUARE_SIZE = 60;
	JButton passButton;
	OthelloBoard board;

	public Othello(OthelloBoard board) {
		super("Othello");
		this.board = board;
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		add(new TurnPanel(board));

		gc.gridx = 1;
		add(Box.createHorizontalGlue(), gc);

		gc.gridx = 2;
		passButton = new JButton("Pass");
		passButton.addActionListener(this);
		add(passButton, gc);

		JPanel squares = new JPanel();
		squares.setLayout(new GridLayout(board.getWidth(), board.getHeight()));
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
		gc.gridwidth = 3;
		add(squares, gc);
		pack();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == passButton) {
			board.pass(board.getPlayerColor());
		}

	}

}
