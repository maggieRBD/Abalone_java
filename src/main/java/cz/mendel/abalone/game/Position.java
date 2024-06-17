package cz.mendel.abalone.game;

public class Position {

    private Marble marble;

    public Marble getMarble() {
        return marble;
    }

    public Position setMarble(Marble marble) {
        this.marble = marble;
        return this;
    }

    public boolean isEmpty() {
        return marble == null;
    }
}
