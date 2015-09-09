package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import model.OthelloBoard;

/**
 * This is an indicator for a participant telling if it's the participant turn to move or not
 */
class TurnIndicator extends JPanel {

    private boolean selected;

    /* The color indicating the players turn to move */
    private Color selctColor = new Color(0, 205, 255);
    private Color c;

    /**
     * @param color The color of the participant for this indicator
     */
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

    /**
     * Toggles if this indicator should be selected or not, i.e. if it's the participants turn or not
     */
    public void toggleSelect() {
        selected = !selected;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int width = getWidth() - 10;
        int height = getHeight() - 10;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2));

        g2.setColor(c);
        g2.fillOval(5, 5, width, height);

        if (selected) {
            g2.setColor(selctColor);
            g2.drawOval(5, 5, width, height);
        }

    }
}
