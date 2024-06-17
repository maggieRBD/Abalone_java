package cz.mendel.abalone.game.selection;

import cz.mendel.abalone.game.Marble;

public class SingleSelect extends Select {
    protected SingleSelect(Marble m) {
        canAdd = x -> m.isLeft(x)
                || m.isRight(x)
                || m.isUpperLeft(x)
                || m.isUpperRight(x)
                || m.isLowerLeft(x)
                || m.isLowerRight(x);
        selection.add(m);
    }

    @Override
    protected Select buildSelection(Marble m) {
        selection.add(m);
        Marble seed = selection.get(0);
        if (seed.isLeft(m) || seed.isRight(m)) {
            return new HorizontalLineSelect(selection);
        } else if (seed.isUpperLeft(m) || seed.isLowerRight(m)) {
            return new LeftDiagonalSelect(selection);
        } else if (seed.isLowerLeft(m) || seed.isUpperRight(m)) {
            return new RightDiagonalSelect(selection);
        }
        throw new RuntimeException("wat?");
    }
}
