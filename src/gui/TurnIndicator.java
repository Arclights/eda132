package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import model.OthelloBoard;

@SuppressWarnings("serial")
class TurnIndicator extends JPanel {

	private boolean selected;
	private Color selctColor = new Color(0, 205, 255);
	private Color c;

	public TurnIndicator(int color) {
		switch (color) {
		case OthelloBoard.BLACK:
			c = Color.BLACK;
			selected = true;
			break;
		case OthelloBoard.WHITE:
			c = Color.WHITE;
			selected = false;
			break;
		default:
			break;
		}
		setPreferredSize(new Dimension(50, 50));
		repaint();
	}

	public void toggleSelect() {
		selected = !selected;
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		int width = getWidth()-10;
		int height = getHeight()-10;
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2));
        
        g2.setColor(c);
		g2.fillOval(5, 5, width, height);
		
		if (selected) {
			g2.setColor(selctColor);
			g2.drawOval(5, 5, width, height);
		}
		
	}
}
