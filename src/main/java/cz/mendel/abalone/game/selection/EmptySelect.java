package cz.mendel.abalone.game.selection;

import cz.mendel.abalone.game.Marble;
import cz.mendel.abalone.game.Player;

public class EmptySelect extends Select {
    public EmptySelect(Player p) {
        canAdd = p::ownsMarble;
    }

    @Override
    protected Select buildSelection(Marble m) {
        return new SingleSelect(m);
    }
}
