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
