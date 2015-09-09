package ai;

import java.util.ArrayList;
import java.util.List;

import model.MoveUtility;
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
        private class Node {
            private int[][] state;
            int[] move;
            private Node parent;
            private List<Node> children;
            /* Whether to maximize or minimize the outcome of a move. See https://en.wikipedia.org/wiki/Minimax */
            private boolean max;
            private int aiColor;
            private double evaluation;
            private boolean pass;

            private String name;
            private int level;

            public Node(int[][] state, int[] move, int aiColor, boolean max, int level, Node parent, String name) {
                this.state = state;
                this.move = move;
                this.aiColor = aiColor;
                this.max = max;
                children = new ArrayList<>();
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
            public void createNewStates(int levelLimit, long timeLimit, long startTime) {
                int[][] moves = MoveUtility.getLeagalMoves(aiColor, state);
                int childNbr = 0;
                if (level == levelLimit) {
                    evaluation = eVal(state, aiColor);
                } else if (moves.length == 0) {
                    pass = true;
                    if (parent.pass) {
                        evaluation = MoveUtility.utility(state, aiColor);
                    } else {
                        if (isOutOfTime(timeLimit, startTime))
                            return;

                        int[][] newState = copy(state);
                        Node child = createNewSubTree(newState, new int[]{move[0], move[1]}, levelLimit, timeLimit,
                                startTime, childNbr);
                        evaluation = child.evaluation;
                    }
                } else {
                    for (int[] move : moves) {
                        if (isOutOfTime(timeLimit, startTime))
                            return;

                        int[][] newState = copy(state);
                        MoveUtility.performMove(newState, move, aiColor);
                        Node child = createNewSubTree(newState, new int[]{move[0], move[1]}, levelLimit, timeLimit,
                                startTime, childNbr);
                        updateEvaluation(child);
                        childNbr++;
                    }
                }

            }

            /**
             * Creates a new sub tree based on move on the current state
             *
             * @param newState   The new state of the board to use
             * @param move       The move to be made
             * @param levelLimit The level limit of the search
             * @param timeLimit  The time limit of the search
             * @param startTime  The start time of the search
             * @param childNbr   The number of this new child node
             */
            private Node createNewSubTree(int[][] newState, int[] move, int levelLimit, long timeLimit, long startTime,
                                          int childNbr) {

                Node child = createNewChild(newState, move, childNbr);

                child.createNewStates(levelLimit, timeLimit, startTime);

                return child;
            }

            /**
             * Creates a new child and adds it to the current node
             *
             * @param newState The new state of the board to use
             * @param move     The move to be performed
             * @param childNbr The number of this new child node
             * @return The new child node
             */
            private Node createNewChild(int[][] newState, int[] move, int childNbr) {
                int childColor = switchColor(aiColor);
                boolean childMax = !max;
                int childLevel = level + 1;
                String childName = name + ":" + childNbr;
                Node child = new Node(newState, move, childColor, childMax, childLevel, this, childName);
                children.add(child);
                return child;
            }

            /**
             * Returns whether we are out of time for the search
             *
             * @param timeLimit The time limit of the search
             * @param startTime The start time of the search
             * @return If we are out of time
             */
            private boolean isOutOfTime(long timeLimit, long startTime) {
                return System.currentTimeMillis() - startTime >= timeLimit;
            }

            /**
             * Switches the color between black and white
             *
             * @param color The color
             * @return The new color
             */
            private int switchColor(int color) {
                return color * -1;
            }

            /**
             * Updates the evaluation based on whether to maximize or minimize the outcome
             *
             * @param child
             */
            private void updateEvaluation(Node child) {
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
