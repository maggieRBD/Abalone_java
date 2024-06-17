package cz.mendel.abalone.game;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class Board {

    private final List<List<Position>> data = new ArrayList<>() {{
        add(new ArrayList<>() {{
            add(new Position().setMarble(new WhiteMarble()));
            add(new Position().setMarble(new WhiteMarble()));
            add(new Position().setMarble(new WhiteMarble()));
            add(new Position().setMarble(new WhiteMarble()));
            add(new Position().setMarble(new WhiteMarble()));
        }});
        add(new ArrayList<>() {{
            add(new Position().setMarble(new WhiteMarble()));
            add(new Position().setMarble(new WhiteMarble()));
            add(new Position().setMarble(new WhiteMarble()));
            add(new Position().setMarble(new WhiteMarble()));
            add(new Position().setMarble(new WhiteMarble()));
            add(new Position().setMarble(new WhiteMarble()));
        }});
        add(new ArrayList<>() {{
            add(new Position());
            add(new Position());
            add(new Position().setMarble(new WhiteMarble()));
            add(new Position().setMarble(new WhiteMarble()));
            add(new Position().setMarble(new WhiteMarble()));
            add(new Position());
            add(new Position());
        }});
        add(new ArrayList<>() {{
            add(new Position());
            add(new Position());
            add(new Position());
            add(new Position());
            add(new Position());
            add(new Position());
            add(new Position());
            add(new Position());
        }});
        add(new ArrayList<>() {{
            add(new Position());
            add(new Position());
            add(new Position());
            add(new Position());
            add(new Position());
            add(new Position());
            add(new Position());
            add(new Position());
            add(new Position());
        }});
        add(new ArrayList<>() {{
            add(new Position());
            add(new Position());
            add(new Position());
            add(new Position());
            add(new Position());
            add(new Position());
            add(new Position());
            add(new Position());
        }});
        add(new ArrayList<>() {{
            add(new Position());
            add(new Position());
            add(new Position().setMarble(new BlackMarble()));
            add(new Position().setMarble(new BlackMarble()));
            add(new Position().setMarble(new BlackMarble()));
            add(new Position());
            add(new Position());
        }});
        add(new ArrayList<>() {{
            add(new Position().setMarble(new BlackMarble()));
            add(new Position().setMarble(new BlackMarble()));
            add(new Position().setMarble(new BlackMarble()));
            add(new Position().setMarble(new BlackMarble()));
            add(new Position().setMarble(new BlackMarble()));
            add(new Position().setMarble(new BlackMarble()));
        }});
        add(new ArrayList<>() {{
            add(new Position().setMarble(new BlackMarble()));
            add(new Position().setMarble(new BlackMarble()));
            add(new Position().setMarble(new BlackMarble()));
            add(new Position().setMarble(new BlackMarble()));
            add(new Position().setMarble(new BlackMarble()));
        }});
    }};
    private final ObservableList<Node> scene;

    /**
     * Konstruktor.
     *
     * @param scene scena na ktorej ma byt stav plochy vyrkesleny
     */
    public Board(ObservableList<Node> scene) {
        this.scene = scene;
        initBoard();
    }

    public void shiftLeft(Collection<Marble> marbles) {
        for (Marble m : marbles) {
            shift(m, Marble::shiftLeft);
        }
    }

    public void shiftRight(Collection<Marble> marbles) {
        for (Marble m : marbles) {
            shift(m, Marble::shiftRight);
        }
    }

    public void shiftUpperLeft(Collection<Marble> marbles) {
        for (Marble m : marbles) {
            shift(m, Marble::shiftUpperLeft);
        }
    }

    public void shiftUpperRight(Collection<Marble> marbles) {
        for (Marble m : marbles) {
            shift(m, Marble::shiftUpperRight);
        }
    }

    public void shiftLowerLeft(Collection<Marble> marbles) {
        for (Marble m : marbles) {
            shift(m, Marble::shiftLowerLeft);
        }
    }

    public void shiftLowerRight(Collection<Marble> marbles) {
        for (Marble m : marbles) {
            shift(m, Marble::shiftLowerRight);
        }
    }

    public void shift(Marble marble, Consumer<Marble> move) {
        // odstrani gulicku zo starej pozicie
        int xPos = marble.getxPos();
        int yPos = marble.getyPos();
        Position position = data.get(yPos).get(xPos);
        if (position.getMarble().equals(marble)) {
            position.setMarble(null);
        }
        // posun gulicky
        move.accept(marble);
        // nastavenie gulicky na novu poziciu
        xPos = marble.getxPos();
        yPos = marble.getyPos();
        position = data.get(yPos).get(xPos);
        position.setMarble(marble);

    }

    public Position get(int x, int y) {
        return data.get(y).get(x);
    }

    public void removeMarble(Marble m) {
        scene.remove(m);
    }

    private void initBoard() {
        for (int i = 0; i < data.size(); i++) {
            List<Position> line = data.get(i);
            for (int j = 0; j < line.size(); j++) {
                int xPos = j;
                int yPos = i;
                Position position = line.get(xPos);
                Optional.ofNullable(position.getMarble())
                        .map(m -> m.setPosition(xPos, yPos))
                        .ifPresent(scene::add);
            }
        }
    }
}
