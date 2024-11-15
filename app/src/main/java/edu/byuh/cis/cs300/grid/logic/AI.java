/* Inspiration by / References
 * https://github.com/dgpalmieri/TicTacToe
 * https://github.com/conorhennessy/Tic-Tac-Toe-AI
 * https://github.com/hammerniko/de.itg.hgs.Grundlagen/tree/2d6569e415c360db0ae90592155a27497ae0b98a/1_Grundlagen/src/K1_TICTACTOE_mit_minimax
 * https://github.com/jaeheonshim/Tic_Tac_Toe_AI/tree/master
 * https://archive.org/stream/run-magazine-08/Run_Issue_08_1984_Aug#page/n95/mode/2up
 */

package edu.byuh.cis.cs300.grid.logic;

import static edu.byuh.cis.cs300.grid.logic.AI.Check.copyGrid;

import java.util.ArrayList;
import java.util.List;

public class AI {
    private static final char NULL = '0';
    private char[] options = {'1', '2', '3', '4', '5', 'A', 'B', 'C', 'D', 'E'};

    /**
     * Returns a suggested move based on the current state of the game.
     * If a winning move is possible, it will be returned.
     * If no winning move is possible, it will check for a blocking move.
     * If no blocking move is possible, it will return a random move.
     * @param grid The current state of the game
     * @param currentPlayer The player to suggest a move for
     * @return A suggested move
     */
    public char getSuggestedMove(Player[][] grid, Player currentPlayer) {
        Player opponent = GameEngine.otherPlayer(currentPlayer);

        //check for blocking opponent's winning move
        for (char move : options) {
            Player[][] newGrid = copyGrid(grid);
            GameEngine.doOneMove(move, opponent, newGrid);
            if (GameEngine.checkForWin() == opponent) {
                return move;
            }
        }

        //check for winning move
        for (char move : options) {
            Player[][] newGrid = copyGrid(grid);
            GameEngine.doOneMove(move, currentPlayer, newGrid);
            if (GameEngine.checkForWin() == currentPlayer) {
                return move;
            }
        }

        //if no winning or blocking move is found, return a random move
        return options[(int) (Math.random() * options.length)];
    }

    static class Check {
        private static char[] moves = {'1', '2', '3', '4', '5', 'A', 'B', 'C', 'D', 'E'};
        private List<Check> children;
        private int cumulativeScore;
        private Player currentPlayer;
        private int currentScore;
        private char move;
        private String path;

        /**
         * Constructs a new Check object that represents a potential move in the game.
         *
         * @param grid The current state of the game grid.
         * @param path The path of moves taken to reach this state.
         * @param move The current move being evaluated.
         * @param cumulativeScore The cumulative score up to this point.
         * @param currentPlayer The player making the current move.
         * @param depth The depth of the move tree to explore.
         */
        public Check(Player[][] grid, String path, char move, int cumulativeScore, Player currentPlayer, int depth) {
            Player[][] newGrid = copyGrid(grid);
            this.currentPlayer = currentPlayer;
            this.move = move;
            if (move != NULL) {
                this.path = path + move;
                GameEngine.doOneMove(move, this.currentPlayer, newGrid);
                this.currentScore = evaluateGrid(newGrid, this.currentPlayer);
                this.cumulativeScore = cumulativeScore + this.currentScore;
            } else {
                this.path = "";
                this.cumulativeScore = 0;
            }
            if (depth > 0) {
                this.children = new ArrayList<>();
                for (char nextMove : moves) {
                    this.children.add(new Check(newGrid, this.path, nextMove, this.cumulativeScore, GameEngine.otherPlayer(this.currentPlayer), depth - 1));
                }
            }
        }

        /**
         * Retrieves the minimum score from the list of child Check instances.
         * 
         * @return the minimum score among the children or Integer.MAX_VALUE if there are no children.
         */
        public int getMinimumScoreOfChildren() {
            return children.stream().mapToInt(Check::getScore).min().orElse(Integer.MAX_VALUE);
        }


        /**
         * Retrieves the score for the current state of the game.
         * @return The score for the current state of the game.
         */
        public int getScore() {
            return this.currentScore;
        }

        /**
         * Retrieves the first move in the path of moves taken.
         * 
         * @return the first character representing the initial move in the path.
         */
        public char getFirstStepInPath() {
            return this.path.charAt(0);
        }

        /**
         * Retrieves the list of child Check instances.
         * 
         * @return The list of child Check instances, or an empty list if there are no children.
         */
        public List<Check> getChildren() {
            return this.children;
        }

        /**
         * Creates a deep copy of the given 5x5 grid.
         * @param grid The grid to be copied.
         * @return A new 5x5 grid with the same values as the given grid.
         */
        static Player[][] copyGrid(Player[][] grid) {
            Player[][] newGrid = new Player[5][5];
            for (int i = 0; i < 5; i++) {
                System.arraycopy(grid[i], 0, newGrid[i], 0, 5);
            }
            return newGrid;
        }

        /**
         * Evaluates the given grid and returns a score representing the number of open lines the given player has minus the number of open lines the opponent has.
         * A higher score indicates more open lines for the given player and fewer open lines for the opponent.
         * @param grid The 5x5 grid to be evaluated.
         * @param player The player for whom the score is being calculated.
         * @return The score for the given player.
         */
        private static int evaluateGrid(Player[][] grid, Player player) {
            int score = 0;

            //evaluate rows, columns, and diagonals for potential wins
            //count the number of open lines in each direction
            int openLines = 0;
            int opponentOpenLines = 0;
            Player opponent = GameEngine.otherPlayer(player);

            //rows
            for (int i = 0; i < 5; i++) {
                boolean isBlank = false;
                boolean isPlayer = false;
                boolean isOpponent = false;
                for (int j = 0; j < 5; j++) {
                    if (grid[i][j] == Player.BLANK) {
                        isBlank = true;
                    } else if (grid[i][j] == player) {
                        isPlayer = true;
                    } else if (grid[i][j] == opponent) {
                        isOpponent = true;
                    }
                }
                if (isBlank && isPlayer && !isOpponent) {
                    openLines++;
                }
                if (isBlank && isOpponent && !isPlayer) {
                    opponentOpenLines++;
                }
            }

            //columns
            for (int i = 0; i < 5; i++) {
                boolean isBlank = false;
                boolean isPlayer = false;
                boolean isOpponent = false;
                for (int j = 0; j < 5; j++) {
                    if (grid[j][i] == Player.BLANK) {
                        isBlank = true;
                    } else if (grid[j][i] == player) {
                        isPlayer = true;
                    } else if (grid[j][i] == opponent) {
                        isOpponent = true;
                    }
                }
                if (isBlank && isPlayer && !isOpponent) {
                    openLines++;
                }
                if (isBlank && isOpponent && !isPlayer) {
                    opponentOpenLines++;
                }
            }

            //diagonals
            boolean isBlank = false;
            boolean isPlayer = false;
            boolean isOpponent = false;
            for (int i = 0; i < 5; i++) {
                if (grid[i][i] == Player.BLANK) {
                    isBlank = true;
                } else if (grid[i][i] == player) {
                    isPlayer = true;
                } else if (grid[i][i] == opponent) {
                    isOpponent = true;
                }
            }
            if (isBlank && isPlayer && !isOpponent) {
                openLines++;
            }
            if (isBlank && isOpponent && !isPlayer) {
                opponentOpenLines++;
            }

            isBlank = false;
            isPlayer = false;
            isOpponent = false;
            for (int i = 0; i < 5; i++) {
                if (grid[i][4 - i] == Player.BLANK) {
                    isBlank = true;
                } else if (grid[i][4 - i] == player) {
                    isPlayer = true;
                } else if (grid[i][4 - i] == opponent) {
                    isOpponent = true;
                }
            }
            if (isBlank && isPlayer && !isOpponent) {
                openLines++;
            }
            if (isBlank && isOpponent && !isPlayer) {
                opponentOpenLines++;
            }

            score = openLines - opponentOpenLines;
            return score;
        }
    }
}