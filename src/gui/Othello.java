package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import model.OthelloBoard;

@SuppressWarnings("serial")
public class Othello extends JFrame implements Observer {

	private final int SQUARE_SIZE = 60;
	JButton passButton;

	public Othello(OthelloBoard board) {
		super("Othello");
		board.addObserver(this);
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		add(new TurnPanel(board));

		gc.gridx = 1;
		add(Box.createHorizontalGlue(), gc);

		gc.gridx = 2;
		passButton = new JButton("Pass");
		passButton.addActionListener(board);
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
		try {
			UIManager.setLookAndFeel(
			        UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setVisible(true);
		
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof String) {
			JOptionPane.showMessageDialog(this, (String) arg);
		}
	}

}
