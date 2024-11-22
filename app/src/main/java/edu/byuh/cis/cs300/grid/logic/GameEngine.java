package edu.byuh.cis.cs300.grid.logic;

import java.util.Arrays;
import java.util.stream.IntStream;

public class GameEngine {

    private static Player[][] grid;
    private static final int DIM = 5;
    private Player currentPlayer;
    private AI ai;

    public GameEngine() {
        grid = new Player[DIM][DIM];
        clear();
        currentPlayer = Player.X;
        ai = new AI();
    }

    /**
     * Clears the game board and resets the current player to X.
     */
    public void clear() {
        IntStream.range(0, DIM).forEach(i -> 
            IntStream.range(0, DIM).forEach(j -> 
                grid[i][j] = Player.BLANK
            )
        );
    }

    /**
     * Submit a move to the game engine. The move should be a letter from A to E
     * or a number from 1 to 5. The letter will place the move in the corresponding
     * row, and the number will place the move in the corresponding column.
     * If the location is already occupied, the move will be placed in the first
     * available position in that row or column.
     * @param move The move to submit. Should be a letter from A to E or a number
     *             from 1 to 5.
     */
    public void submitMove(char move) {
        if (move >= '1' && move <= '5') {
            int col = Integer.parseInt("" + move) - 1;
            Player newVal = currentPlayer;
            for (int i = 0; i < DIM; i++) {
                if (grid[i][col] == Player.BLANK) {
                    grid[i][col] = newVal;
                    break;
                } else {
                    Player tmp = grid[i][col];
                    grid[i][col] = newVal;
                    newVal = tmp;
                }
            }
        } else {
            int row = (int) (move - 'A');
            Player newVal = currentPlayer;
            for (int i = 0; i < DIM; i++) {
                if (grid[row][i] == Player.BLANK) {
                    grid[row][i] = newVal;
                    break;
                } else {
                    Player tmp = grid[row][i];
                    grid[row][i] = newVal;
                    newVal = tmp;
                }
            }
        }
        currentPlayer = (currentPlayer == Player.X) ? Player.O : Player.X;
    }

    /**
     * Check if there is a winner. A winner is a player that has all of their
     * marks in a row, column, or diagonal. If there is no winner, return
     * Player.BLANK.
     * @return The player that has won, or Player.BLANK if there is no winner.
     */
    public static Player checkForWin() {
        Player winner = Player.BLANK;

        //check all rows
        winner = IntStream.range(0, DIM)
                .mapToObj(i -> grid[i][0] != Player.BLANK && Arrays.stream(grid[i]).allMatch(p -> p == grid[i][0]) ? grid[i][0] : Player.BLANK)
                .filter(p -> p != Player.BLANK)
                .reduce(winner, (w, p) -> w == Player.BLANK ? p : Player.TIE);

        //check all columns
        winner = IntStream.range(0, DIM)
                .mapToObj(i -> grid[0][i] != Player.BLANK && IntStream.range(0, DIM).allMatch(j -> grid[j][i] == grid[0][i]) ? grid[0][i] : Player.BLANK)
                .filter(p -> p != Player.BLANK)
                .reduce(winner, (w, p) -> w == Player.BLANK ? p : Player.TIE);

        //check top-left to bottom-right diagonal
        if (grid[0][0] != Player.BLANK && IntStream.range(0, 5).allMatch(i -> grid[i][i] == grid[0][0])) {
            return grid[0][0];
        }

        //check bottom-left to top-right diagonal
        if (grid[4][0] != Player.BLANK && IntStream.range(0, 5).allMatch(i -> grid[4 - i][i] == grid[4][0])) {
            return grid[4][0];
        }

        return winner;
    }

    /**
     * Retrieves the current player.
     * 
     * @return The player whose turn it currently is.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the current player in the game engine.
     * 
     * @param player The player to set as the current player.
     */
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    /**
     * Executes a move on the specified grid for the given player.
     * The move can be a character from '1' to '5' for column placement or 
     * from 'A' to 'E' for row placement. The player's symbol is placed in 
     * the first available blank position in the specified row or column.
     *
     * @param move The move to execute, either a character '1'-'5' for column
     *             or 'A'-'E' for row.
     * @param player The player making the move.
     * @param grid The grid where the move is to be executed.
     */
    public static void doOneMove(char move, Player player, Player[][] grid) {
        if (move >= '1' && move <= '5') {
            int col = move - '1';
            for (int i = 0; i < 5; i++) {
                if (grid[i][col] == Player.BLANK) {
                    grid[i][col] = player;
                    break;
                }
            }
        } else {
            int row = move - 'A';
            for (int i = 0; i < 5; i++) {
                if (grid[row][i] == Player.BLANK) {
                    grid[row][i] = player;
                    break;
                }
            }
        }
    }

    /**
     * Suggests the next move for the current player based on the current state of the game.
     * Delegates the decision-making to the AI component, which analyzes the grid and the current player.
     * 
     * @return A character representing the suggested move, either a row ('A'-'E') or column ('1'-'5').
     */
    public char suggestNextMove() {
        return ai.getSuggestedMove(grid, currentPlayer);
    }

    /**
     * Determines the opponent player.
     *
     * @param player The current player.
     * @return The opponent player, either Player.O if the current player is Player.X, or Player.X if the current player is Player.O.
     */
    public static Player otherPlayer(Player player) {
        return (player == Player.X) ? Player.O : Player.X;
    }
}