package gui;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;

import model.OthelloBoard;

/**
 * A panel containing two TurnIndicators, one for each participant
 */
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

    /**
     * The function called by the observable being observed.
     * This class observes the Othello model.
     * When called the selection is switched from one of the participants to the other
     *
     * @param o   The observable making the call
     * @param arg The argument passed by the observable
     */
    @Override
    public void update(Observable o, Object arg) {
        aiIndicator.toggleSelect();
        playerIndicator.toggleSelect();
    }

}
