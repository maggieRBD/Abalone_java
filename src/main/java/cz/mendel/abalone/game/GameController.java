package cz.mendel.abalone.game;

import cz.mendel.abalone.game.selection.EmptySelect;
import cz.mendel.abalone.game.selection.HorizontalLineSelect;
import cz.mendel.abalone.game.selection.LeftDiagonalSelect;
import cz.mendel.abalone.game.selection.RightDiagonalSelect;
import cz.mendel.abalone.game.selection.Select;
import javafx.application.Platform;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameController {

    private Board gameBoard;
    private Player currentPlayer;
    private final Deque<Player> players = new LinkedList<>();
    private Select selection;

    @FXML
    private Pane pane;

    @FXML
    void initialize() {
        gameBoard = new Board(pane.getChildren());
        players.offer(new Player(WhiteMarble.class));
        players.offer(new Player(BlackMarble.class));
        currentPlayer = players.pop();
        selection = new EmptySelect(currentPlayer);
    }

    public void onClick(MouseEvent mouseEvent) {
        switch (mouseEvent.getButton()) {
            case PRIMARY:
                EventTarget source = mouseEvent.getTarget();
                if (source instanceof Marble && currentPlayer.ownsMarble((Marble) source)) {
                    selection = selection.add((Marble) source);
                } else if (!(selection instanceof EmptySelect)) {
                    int y = (int) ((mouseEvent.getY() - Marble.Y_ORIGIN) / Marble.Y_STEP); // y suradnica kliknutia - y najvyssieho riadku / vyska riadku
                    int x = (int) ((mouseEvent.getX() - Marble.X_ORIGIN - Marble.X_SHIFT * Math.abs(4 - y)) / Marble.X_STEP); // x suradnica kliknutia - x suradnica najlavejsieho policka - posun podla riadku (absolutna hodnota rozdielu stredneho riadku a akutalneho riadku) / sirka policka
                    move(x, y);
                }
                break;
            case SECONDARY:
                selection = selection.reset(currentPlayer);
        }
    }

    private void move(int x, int y) {
        if (selection.isLeft(x, y)) {
            moveLeft();
        } else if (selection.isRight(x, y)) {
            moveRight();
        } else if (selection.isUpperLeft(x, y)) {
            moveUpperLeft();
        } else if (selection.isUpperRight(x, y)) {
            moveUpperRight();
        } else if (selection.isLowerLeft(x, y)) {
            moveLowerLeft();
        } else if (selection.isLowerRight(x, y)) {
            moveLowerRight();
        }
    }

    private void moveUpperLeft() {
        if (selection instanceof LeftDiagonalSelect) {
            pushableMoveUpperLeft();
        } else {
            List<Position> neighbors = new ArrayList<>();
            for (Marble m : selection.getSelection()) {
                int shiftX = m.getyPos() <= 4 ? 1 : 0;
                neighbors.add(gameBoard.get(m.getxPos() - shiftX, m.getyPos() - 1));
            }
            simpleMove(gameBoard::shiftUpperLeft, neighbors);
        }
    }

    private void pushableMoveUpperLeft() {
        int selectedCount = selection.getSelection().size();
        Marble upperMarble = selection.getSelection().get(0);
        int x = upperMarble.getxPos();
        int y = upperMarble.getyPos();
        Deque<Marble> neighbors = new LinkedList<>();
        int xShift = 0;
        for (int i = 1; i <= selectedCount && y - i >= 0; i++) {
            if (y - i < 4) {
                xShift++;
            }
            if (x - xShift < 0) {
                break;
            }
            Position position = gameBoard.get(x - xShift, y - i);
            if (position.isEmpty()) {
                break;
            } else {
                neighbors.offerFirst(position.getMarble());
            }
        }
        if (neighbors.size() < selectedCount) {
            if (!neighbors.isEmpty()) {
                Marble first = neighbors.peekFirst();
                if (first.getyPos() == 0 || (first.getxPos() == 0 && first.getyPos() <= 4)) {
                    removeMarble(neighbors.removeFirst());
                }
            }
            ArrayList<Marble> marbles = new ArrayList<Marble>(neighbors.size() + selectedCount);
            marbles.addAll(neighbors);
            marbles.addAll(selection.getSelection());
            makeMove(marbles, gameBoard::shiftUpperLeft);
        }
    }

    private void moveUpperRight() {
        if (selection instanceof RightDiagonalSelect) {
            pushableMoveUpperRight();
        } else {
            List<Position> neighbors = new ArrayList<>();
            for (Marble m : selection.getSelection()) {
                int shiftX = m.getyPos() > 4 ? 1 : 0;
                neighbors.add(gameBoard.get(m.getxPos() + shiftX, m.getyPos() - 1));
            }
            simpleMove(gameBoard::shiftUpperRight, neighbors);
        }
    }

    private void pushableMoveUpperRight() {
        int selectedCount = selection.getSelection().size();
        Marble upperMarble = selection.getSelection().get(0);
        int x = upperMarble.getxPos();
        int y = upperMarble.getyPos();
        Deque<Marble> neighbors = new LinkedList<>();
        int xShift = 0;
        for (int i = 1; i <= selectedCount && y - i >= 0; i++) {
            if (y - i >= 4) {
                xShift++;
            }
            if (x + xShift > 4 + Math.min(y - i, 8 - (y - i))) {
                break;
            }
            Position position = gameBoard.get(x + xShift, y - i);
            if (position.isEmpty()) {
                break;
            } else {
                neighbors.offerFirst(position.getMarble());
            }
        }
        if (neighbors.size() < selectedCount) {
            if (!neighbors.isEmpty()) {
                Marble first = neighbors.peekFirst();
                int firstX = first.getxPos();
                int firstY = first.getyPos();
                if (firstY == 0 || (firstX == 4 + Math.min(firstY, 8 - firstY) && firstY <= 4)) {
                    removeMarble(neighbors.removeFirst());
                }
            }
            ArrayList<Marble> marbles = new ArrayList<Marble>(neighbors.size() + selectedCount);
            marbles.addAll(neighbors);
            marbles.addAll(selection.getSelection());
            makeMove(marbles, gameBoard::shiftUpperRight);
        }
    }

    private void moveLeft() {
        if (selection instanceof HorizontalLineSelect) {
            pushableMoveLeft();
        } else {
            List<Position> neighbors = new ArrayList<>();
            for (Marble m : selection.getSelection()) {
                neighbors.add(gameBoard.get(m.getxPos() - 1, m.getyPos()));
            }
            simpleMove(gameBoard::shiftLeft, neighbors);
        }
    }

    private void pushableMoveLeft() {
        int selectedCount = selection.getSelection().size();
        Marble leftMarble = selection.getSelection().get(0);
        int x = leftMarble.getxPos();
        int y = leftMarble.getyPos();
        Deque<Marble> neighbors = new LinkedList<>();
        for (int i = 1; i <= selectedCount && x - i >= 0; i++) {
            Position position = gameBoard.get(x - i, y);
            if (position.isEmpty()) {
                break;
            } else {
                neighbors.offerFirst(position.getMarble());
            }
        }
        if (neighbors.size() < selectedCount) {
            if (!neighbors.isEmpty() && neighbors.peekFirst().getxPos() == 0) {
                removeMarble(neighbors.removeFirst());
            }
            ArrayList<Marble> marbles = new ArrayList<Marble>(neighbors.size() + selectedCount);
            marbles.addAll(neighbors);
            marbles.addAll(selection.getSelection());
            makeMove(marbles, gameBoard::shiftLeft);
        }
    }

    private void moveRight() {
        if (selection instanceof HorizontalLineSelect) {
            pushableMoveRight();
        } else {
            List<Position> neighbors = new ArrayList<>();
            for (Marble m : selection.getSelection()) {
                neighbors.add(gameBoard.get(m.getxPos() + 1, m.getyPos()));
            }
            simpleMove(gameBoard::shiftRight, neighbors);
        }
    }

    private void pushableMoveRight() {
        int selectedCount = selection.getSelection().size();
        Marble rightMarble = selection.getSelection().get(selectedCount - 1);
        int x = rightMarble.getxPos();
        int y = rightMarble.getyPos();
        Deque<Marble> neighbors = new LinkedList<>();
        for (int i = 1; i <= selectedCount && x + i <= 4 + Math.min(y, 8 - y); i++) {
            Position position = gameBoard.get(x + i, y);
            if (position.isEmpty()) {
                break;
            } else {
                neighbors.offerFirst(position.getMarble());
            }
        }
        if (neighbors.size() < selectedCount) {
            if (!neighbors.isEmpty() && neighbors.peekFirst().getxPos() == 4 + Math.min(y, 8 - y)) {
                removeMarble(neighbors.removeFirst());
            }
            ArrayList<Marble> marbles = new ArrayList<Marble>(neighbors.size() + selectedCount);
            marbles.addAll(neighbors);
            marbles.addAll(selection.getSelection());
            makeMove(marbles,  gameBoard::shiftRight);
        }
    }

    private void moveLowerLeft() {
        if (selection instanceof RightDiagonalSelect) {
            pushableMoveLowerLeft();
        } else {
            List<Position> neighbors = new ArrayList<>();
            for (Marble m : selection.getSelection()) {
                int shiftX = m.getyPos() > 4 ? 1 : 0;
                neighbors.add(gameBoard.get(m.getxPos() - shiftX, m.getyPos() + 1));
            }
            simpleMove(gameBoard::shiftLowerLeft, neighbors);
        }
    }

    private void pushableMoveLowerLeft() {
        int selectedCount = selection.getSelection().size();
        Marble lowerMarble = selection.getSelection().get(selectedCount - 1);
        int x = lowerMarble.getxPos();
        int y = lowerMarble.getyPos();
        Deque<Marble> neighbors = new LinkedList<>();
        int xShift = 0;
        for (int i = 1; i <= selectedCount && y + i <= 8; i++) {
            if (y + i > 4) {
                xShift++;
            }
            if (x - xShift < 0) {
                break;
            }
            Position position = gameBoard.get(x - xShift, y + i);
            if (position.isEmpty()) {
                break;
            } else {
                neighbors.offerFirst(position.getMarble());
            }
        }
        if (neighbors.size() < selectedCount) {
            if (!neighbors.isEmpty()) {
                Marble first = neighbors.peekFirst();
                if (first.getyPos() == 8 || (first.getxPos() == 0 && first.getyPos() >= 4)) {
                    removeMarble(neighbors.removeFirst());
                }
            }
            ArrayList<Marble> marbles = new ArrayList<Marble>(neighbors.size() + selectedCount);
            marbles.addAll(neighbors);
            marbles.addAll(selection.getSelection());
            makeMove(marbles, gameBoard::shiftLowerLeft);
        }
    }

    private void moveLowerRight() {
        if (selection instanceof LeftDiagonalSelect) {
            pushableMoveLowerRight();
        } else {
            List<Position> neighbors = new ArrayList<>();
            for (Marble m : selection.getSelection()) {
                int shiftX = m.getyPos() < 4 ? 1 : 0;
                neighbors.add(gameBoard.get(m.getxPos() + shiftX, m.getyPos() + 1));
            }
            simpleMove(gameBoard::shiftLowerRight, neighbors);
        }
    }

    private void pushableMoveLowerRight() {
        int selectedCount = selection.getSelection().size();
        Marble lowerMarble = selection.getSelection().get(selectedCount - 1);
        int x = lowerMarble.getxPos();
        int y = lowerMarble.getyPos();
        Deque<Marble> neighbors = new LinkedList<>();
        int xShift = 0;
        for (int i = 1; i <= selectedCount && y + i <= 8; i++) {
            if (y + i <= 4) {
                xShift++;
            }
            if (x + xShift > 4 + Math.min(y + i, 8 - (y + i))) { // ukonci cyklus ak sa dostane za najpravejsie policko
                break;
            }
            Position position = gameBoard.get(x + xShift, y + i);
            if (position.isEmpty()) {
                break;
            } else {
                neighbors.offerFirst(position.getMarble());
            }
        }
        if (neighbors.size() < selectedCount) {
            if (!neighbors.isEmpty()) {
                Marble first = neighbors.peekFirst();
                int firstX = first.getxPos();
                int firstY = first.getyPos();
                if (firstY == 8 || (firstX == 4 + Math.min(firstY, 8 - firstY) && firstY >= 4)) {
                    removeMarble(neighbors.removeFirst());
                }
            }
            ArrayList<Marble> marbles = new ArrayList<Marble>(neighbors.size() + selectedCount);
            marbles.addAll(neighbors);
            marbles.addAll(selection.getSelection());
            makeMove(marbles, gameBoard::shiftLowerRight);
        }
    }

    private void simpleMove(Consumer<Collection<Marble>> move, Collection<Position> neighbors) {
        if (neighbors.stream().allMatch(Position::isEmpty)) {
            makeMove(selection.getSelection(), move);
        }
    }

    private void makeMove(Collection<Marble> marbles, Consumer<Collection<Marble>> move) {
        move.accept(marbles);
        switchPlayers();
        if (currentPlayer.lost()) {
            gameOverDialog(currentPlayer);
        }
    }

    private void switchPlayers() {
        players.offer(currentPlayer);
        currentPlayer = players.pop();
        selection = selection.reset(currentPlayer);
    }

    private void removeMarble(Marble m) {
        gameBoard.removeMarble(m);
        players.peekFirst().removeMarble();
    }

    private void gameOverDialog(Player p) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Game over");
        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.setContentText(p + " has lost");
        dialog.getDialogPane().getButtonTypes().add(type);
        ((Button) dialog.getDialogPane().lookupButton(type)).setOnAction(e -> Platform.exit());
        dialog.showAndWait();
    }

}