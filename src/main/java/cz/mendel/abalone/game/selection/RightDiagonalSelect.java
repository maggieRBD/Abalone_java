package cz.mendel.abalone.game.selection;

import cz.mendel.abalone.game.Marble;

import java.util.Comparator;
import java.util.List;

public class RightDiagonalSelect extends Select {
    protected RightDiagonalSelect(List<Marble> marbles) {
        selection.addAll(marbles);
        selection.sort(Comparator.comparingInt(Marble::getyPos));
        canAdd = x -> selection.size() < 3
                && (selection.get(0).isUpperRight(x)
                || selection.get(selection.size() - 1).isLowerLeft(x));
    }

    @Override
    protected Select buildSelection(Marble m) {
        selection.add(m);
        return new RightDiagonalSelect(selection);
    }
}
