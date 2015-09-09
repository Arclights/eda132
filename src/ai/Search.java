package ai;

import java.util.ArrayList;
import java.util.List;

import model.Moves;
import model.OthelloBoard;

/**
 * A class for performing a search over the state of the board and find the best move for the AI
 */
public class Search {


    /**
     * Returns the best move to make by aiColor on the state of board with the time limit timeLimit
     *
     * @param board     The state of the board
     * @param aiColor   The color of the AI
     * @param timeLimit The time limit in which to perform the search
     * @return The best move
     */
    public static int[] findMove(OthelloBoard board, int aiColor, long timeLimit) {
        StateTree states = new StateTree(board.getState(), aiColor);
        states.buildTree(timeLimit);

        return states.getOptMove();
    }


    /**
     * Evaluates the state for the AI by returning the quota of the number of AI markers over the number of player
     * markers
     *
     * @param state   The state of the baord
     * @param aiColor The color of the AI
     * @return The quota
     */
    private static double eVal(int[][] state, int aiColor) {
        double aiMarkers = 0;
        double playerMarkers = 0;
        for (int[] row : state) {
            for (int cell : row) {
                if (cell == aiColor) {
                    aiMarkers++;
                } else if (cell + aiColor == 0) {
                    playerMarkers++;
                }
            }
        }
        return aiMarkers / playerMarkers;
    }

    /**
     * This class represents a state tree of the board
     */
    private static class StateTree {
        private Node root;

        public StateTree(int[][] rootData, int playerColor) {
            root = new Node(rootData, null, playerColor, false, 0, null, "0");
        }

        /**
         * Build the state tree with the time limit timeLimit
         *
         * @param timeLimit The time limit in which to build the state tree
         */
        public void buildTree(Long timeLimit) {
            root.createNewStates(10, timeLimit, System.currentTimeMillis());
        }

        /**
         * Return the optimum move from the search tree
         *
         * @return The optimum move
         */
        public int[] getOptMove() {
            return root.getOptMove();
        }

        /**
         * Prints the state tree
         */
        public void printTree() {
            System.out.println("The tree");
            root.printTree();
            System.out.println();
        }

        /**
         * Thi class represents a node in the state tree
         */
        public class Node {
            private int[][] state;
            int[] move;
            private Node parent;
            private List<Node> children;
            private boolean max;
            private int aiColor;
            private double evaluation;
            private boolean pass;

            private String name;
            private int level;

            public Node(int[][] state, int[] move, int aiColor, boolean max,
                        int level, Node parent, String name) {
                this.state = state;
                this.move = move;
                this.aiColor = aiColor;
                this.max = max;
                children = new ArrayList<Node>();
                this.parent = parent;
                this.level = level;
                evaluation = Double.MIN_VALUE;

                this.name = name;
            }

            /**
             * Will generate a sub tree and creates new children to this node. It will recursively iterate those
             * children for a maximum depth of levelLimit and a time limit of timeLimit
             *
             * @param levelLimit The limit of the depth of the tree to generate
             * @param timeLimit  The time limit in which to complete the search
             * @param startTime  The time when the search started
             */
            public void createNewStates(int levelLimit, long timeLimit,
                                        long startTime) {
                int[][] moves = Moves.getLeagalMoves(aiColor, state);
                int childNbr = 0;
                if (level == levelLimit) {
                    evaluation = eVal(state, aiColor);
                } else if (moves.length == 0) {
                    pass = true;
                    if (parent.pass) {
                        evaluation = Moves.utility(state, aiColor);
                    } else {
                        int[][] newState = copy(state);
                        Node child = new Node(newState, new int[]{move[0],
                                move[1]}, aiColor * -1, !max, level + 1, this,
                                name + ":" + childNbr);
                        children.add(child);
                        evaluation = child.evaluation;
                    }
                } else {

                    for (int[] move : moves) {
                        if (System.currentTimeMillis() - startTime >= timeLimit) {
                            return;
                        }
                        int[][] newState = copy(state);
                        Moves.performMove(newState, move, aiColor);
                        Node child = new Node(newState, new int[]{move[0],
                                move[1]}, aiColor * -1, !max, level + 1, this,
                                name + ":" + childNbr);
                        children.add(child);
                        child.createNewStates(levelLimit, timeLimit, startTime);

                        if (max) {
                            // Max
                            if (child.evaluation > evaluation) {
                                evaluation = child.evaluation;
                            }
                        } else {
                            // Min
                            if (evaluation == Double.MIN_VALUE) {
                                evaluation = child.evaluation;
                            } else if (child.evaluation < evaluation) {
                                evaluation = child.evaluation;
                            }
                        }

                        childNbr++;
                    }
                }

            }

            /**
             * Returns a copy of the passed matrix
             *
             * @param in The passed matrix
             * @return The copy of in
             */
            private int[][] copy(int[][] in) {
                int[][] res = new int[in.length][];
                for (int i = 0; i < in.length; i++)
                    res[i] = in[i].clone();
                return res;
            }

            /**
             * Returns the most optimum move
             *
             * @return The most optimum move
             */
            public int[] getOptMove() {
                for (Node child : children) {
                    if (evaluation == child.evaluation) {
                        return child.move;
                    }
                }
                return null;
            }

            /**
             * Prints the tree
             */
            public void printTree() {
                String tabs = new String(new char[level]).replace("\0", "\t");
                System.out.println(tabs + "Children: " + children.size());
                System.out.println(tabs + "Players turn: " + max);
                System.out.println(tabs + "Evaluation: " + evaluation);
                if (move != null) {
                    System.out.println(tabs + "Move: " + move[0] + ", "
                            + move[1]);
                }
                printBoard(state, tabs);
                System.out.println();
                for (Node child : children) {
                    child.printTree();
                }
            }

            /**
             * Prints the state of the board
             *
             * @param board The state of the board
             * @param tabs  The number of tabs to use to shift the table in
             */
            public void printBoard(int[][] board, String tabs) {
                for (int y = 0; y < board.length; y++) {
                    System.out.print(tabs + "|");
                    for (int x = 0; x < board[y].length; x++) {
                        if (board[x][y] < 0) {
                            System.out.print(board[x][y] + "|");
                        } else {
                            System.out.print(" " + board[x][y] + "|");
                        }
                    }
                    System.out.println();
                }
            }
        }
    }
}
