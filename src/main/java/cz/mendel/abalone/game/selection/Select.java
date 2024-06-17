package cz.mendel.abalone.game.selection;

import cz.mendel.abalone.game.Marble;
import cz.mendel.abalone.game.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class Select {

    protected List<Marble> selection = new ArrayList<>();
    protected Predicate<Marble> canAdd;

    protected abstract Select buildSelection(Marble m);

    public Select add(Marble m) {
        if (canAdd.test(m)) {
            m.setStyle("-fx-opacity: 0.5");
            return buildSelection(m);
        }
        return this;
    }

    public Select reset(Player p) {
        selection.forEach(m -> m.setStyle("-fx-opacity: 1.0"));
        return new EmptySelect(p);
    }

    public List<Marble> getSelection() {
        return new ArrayList<>(selection);
    }

    private boolean check(Predicate<Marble> p) {
        return p.test(selection.get(0)) || p.test(selection.get(selection.size() - 1));
    }
    public boolean isLeft(int x, int y) {
        return check(m -> m.isLeft(x, y));
    }

    public boolean isRight(int x, int y) {
        return check(m -> m.isRight(x ,y));
    }

    public boolean isUpperLeft(int x, int y) {
        return check(m -> m.isUpperLeft(x ,y));
    }

    public boolean isUpperRight(int x, int y) {
        return check(m -> m.isUpperRight(x ,y));
    }

    public boolean isLowerLeft(int x, int y) {
        return check(m -> m.isLowerLeft(x ,y));
    }

    public boolean isLowerRight(int x, int y) {
        return check(m -> m.isLowerRight(x ,y));
    }
}
