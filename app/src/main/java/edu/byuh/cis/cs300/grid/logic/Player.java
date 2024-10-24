package edu.byuh.cis.cs300.grid.logic;

/**
 * This enum includes the 3 possible states for each
 * cell on the game board. Either there's an X, or an O,
 * or nothing. For convenience, I also include a 4th
 * value that we can use to represent when the game
 * ends in a tie.
 */
public enum Player {
    X,
    O,
    BLANK,
    TIE,
}

