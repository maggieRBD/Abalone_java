package cz.mendel.abalone.game;

public class Player {

    private final Class<? extends Marble> marbleClass;
    private short marbles = 14;

    public Player(Class<? extends Marble> marbleClass) {
        this.marbleClass = marbleClass;
    }

    public boolean ownsMarble(Marble m) {
        return m.getClass().equals(marbleClass);
    }

    public void removeMarble() {
        marbles--;
    }

    public boolean lost() {
        return marbles <= 8;
    }

    @Override
    public String toString() {
        return "Player " + marbleClass.getSimpleName();
    }
}
