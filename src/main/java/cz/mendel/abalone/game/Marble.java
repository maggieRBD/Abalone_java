package cz.mendel.abalone.game;

import javafx.scene.image.ImageView;

public abstract class Marble extends ImageView {

    /**
     * X suradnica najlavejieho stlpca.
     */
    public final static double X_ORIGIN = 110.0;
    /**
     * Y suradnica najvyssieho riadku
     */
    public final static double Y_ORIGIN = 100.0;
    /**
     * Krok medzi polickami v riadku.
     */
    public final static double X_STEP = 108.5;
    /**
     * Posun do prava v riadku oproti prosterdnemu riadku.
     */
    public final static double X_SHIFT = 55.0;
    /**
     * Krok medzi riadkami.
     */
    public final static double Y_STEP = 93.0;

    private int xPos;

    private int yPos;

    public Marble(String imgUrl) {
        super(imgUrl);
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int newX) {
        double xStep = X_STEP * newX;
        double xShift = X_SHIFT * Math.abs(4 - yPos);
        double x = X_ORIGIN + xStep + xShift; // vychodzi bod + pozicia v riadku + shift do prava v zavislosti na vzdialenosti od stredneho riadku
        setX(x);
        xPos = newX;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int newY) {
        double yStep = Y_STEP * newY;
        double y = Y_ORIGIN + yStep;
        setY(y);
        yPos = newY;
    }

    public Marble setPosition(int newX, int newY) {
        setyPos(newY);
        setxPos(newX);
        return this;
    }

    public boolean isLeft(Marble m) {
        return isLeft(m.getxPos(), m.getyPos());
    }

    public boolean isRight(Marble m) {
        return isRight(m.getxPos(), m.getyPos());
    }

    public boolean isUpperLeft(Marble m) {
        return isUpperLeft(m.getxPos(), m.getyPos());
    }

    public boolean isUpperRight(Marble m) {
        return isUpperRight(m.getxPos(), m.getyPos());
    }

    public boolean isLowerLeft(Marble m) {
        return isLowerLeft(m.getxPos(), m.getyPos());
    }

    public boolean isLowerRight(Marble m) {
        return isLowerRight(m.getxPos(), m.getyPos());
    }

    public boolean isLeft(int x, int y) {
        return yPos == y && xPos - 1 == x;
    }

    public boolean isRight(int x, int y) {
        return yPos == y && xPos + 1 == x;
    }

    public boolean isUpperLeft(int x, int y) {
        int xDelta = yPos > 4 ? 0 : 1;
        return yPos == y + 1 && xPos == x + xDelta;
    }

    public boolean isUpperRight(int x, int y) {
        int xDelta = yPos > 4 ? 1 : 0;
        return yPos == y + 1 && xPos == x - xDelta;
    }

    public boolean isLowerLeft(int x, int y) {
        int xDelta = yPos >= 4 ? 1 : 0;
        return yPos == y - 1 && xPos == x + xDelta;
    }

    public boolean isLowerRight(int x, int y) {
        int xDelta = yPos >= 4 ? 0 : 1;
        return yPos == y - 1 && xPos == x - xDelta;
    }

    public void shiftRight() {
        horizontalShift(1);
    }

    public void shiftLeft() {
        horizontalShift(-1);
    }

    public void shiftUpperLeft() {
        int xDir = yPos > 4 ? 0 : -1;
        shift(xDir, -1);
    }

    public void shiftUpperRight() {
        int xDir = yPos > 4 ? 1 : 0;
        shift(xDir, -1);
    }

    public void shiftLowerLeft() {
        int xDir = yPos >= 4 ? -1 : 0;
        shift(xDir, 1);
    }

    public void shiftLowerRight() {
        int xDir = yPos >= 4 ? 0 : 1;
        shift(xDir, 1);
    }

    public void shift(int xDir, int yDir) {
        setPosition(xPos + xDir, yPos + yDir);
    }

    public void horizontalShift(int dir) {
        shift(dir, 0);
    }
}
