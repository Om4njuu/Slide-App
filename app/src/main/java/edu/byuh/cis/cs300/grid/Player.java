package edu.byuh.cis.cs300.grid;

/**
 * Represents a player in the game, either X, O, BLANK, or TIE.
 *
 * @param symbol The string representation of the player.
 */
public enum Player {
    X("X"), O("O"), BLANK("BLANK"), TIE("TIE");

    private final String symbol;

    Player(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
