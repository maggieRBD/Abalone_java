package cz.mendel.abalone.game.selection;

import cz.mendel.abalone.game.Marble;

import java.util.Comparator;
import java.util.List;

public class HorizontalLineSelect extends Select {
    protected HorizontalLineSelect(List<Marble> marbles) {
        selection.addAll(marbles);
        selection.sort(Comparator.comparingInt(Marble::getxPos));
        canAdd = x -> selection.size() < 3
                && (selection.get(0).isLeft(x)
                || selection.get(selection.size() - 1).isRight(x));
    }

    @Override
    protected Select buildSelection(Marble m) {
        selection.add(m);
        return new HorizontalLineSelect(selection);
    }
}
