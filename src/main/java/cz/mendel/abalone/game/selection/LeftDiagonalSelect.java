package cz.mendel.abalone.game.selection;

import cz.mendel.abalone.game.Marble;

import java.util.Comparator;
import java.util.List;

public class LeftDiagonalSelect extends Select {
    protected LeftDiagonalSelect(List<Marble> marbles) {
        selection.addAll(marbles);
        selection.sort(Comparator.comparingInt(Marble::getyPos));
        canAdd = x -> selection.size() < 3
                && (selection.get(0).isUpperLeft(x)
                || selection.get(selection.size() - 1).isLowerRight(x));
    }

    @Override
    protected Select buildSelection(Marble m) {
        selection.add(m);
        return new LeftDiagonalSelect(selection);
    }
}
