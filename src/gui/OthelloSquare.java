package gui;

import static model.OthelloBoard.BLACK;
import static model.OthelloBoard.EMPTY;
import static model.OthelloBoard.WHITE;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import model.OthelloBoard;

/**
 * This class represents a square on the Othello board
 */
class OthelloSquare extends JPanel implements Observer, MouseListener {

    private OthelloBoard board;

    /* Square location */
    private int x, y;

    /* The two colors creating the background pattern */
    private Color[] bgs = new Color[]{new Color(150, 150, 150),
            new Color(200, 200, 200)};

    public OthelloSquare(OthelloBoard board, int x, int y) {
        board.addObserver(this);
        this.board = board;
        this.x = x;
        this.y = y;
        addMouseListener(this);
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int width = getWidth();
        int height = getHeight();

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(bgs[x & 1 ^ y & 1]); /* This will alternate the color creating a checker pattern */
        g2.fillRect(0, 0, width, height);

        switch (board.getSquare(x, y)) {
            case BLACK:
                g2.setColor(Color.BLACK);
                break;
            case WHITE:
                g2.setColor(Color.WHITE);
                break;
            case EMPTY:
                break;
        }
        g2.fillOval(10, 10, width - 20, height - 20);

    }

    /**
     * The method called by the observable of this observer.
     * This class is observing the Othello model.
     * When called the square should be repainted.
     *
     * @param o   The observable making the call
     * @param arg The argument the observable sends
     */
    @Override
    public void update(Observable o, Object arg) {
        repaint();

    }

    /**
     * Sets the color of the square to the color of the player when clicked
     *
     * @param e The mouse event triggering the call
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        board.setSquare(x, y, board.getPlayerColor());

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }
}
