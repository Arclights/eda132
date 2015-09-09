package gui;

import model.OthelloBoard;

/**
 * This is the main method for the whole program.
 * This launches the program.
 */
public class Game {
    public static void main(String[] args) {

        OthelloBoard model = new OthelloBoard(8, 8);

        new Othello(model);


    }
}
