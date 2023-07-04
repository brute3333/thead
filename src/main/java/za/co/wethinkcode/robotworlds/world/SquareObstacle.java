package za.co.wethinkcode.robotworlds.world;

import za.co.wethinkcode.robotworlds.Position;

public class SquareObstacle {
    private int x;
    private int y;
    private int size;
    private String robotName;
    private String name;

    public SquareObstacle(){
        this.size = 0;
    }
    public SquareObstacle(int size) {
        this.size = size;
    }

    public void setSize(int size){
        this.size = size;
    }

    public int getSize(){
        return size;
    }

    public int getBottomLeftX() {
        return this.x;
    }

    public int getBottomLeftY() {
        return this.y;
    }

    public boolean blocksPosition(Position position) {
        return position.isIn(new Position(this.x, this.y + this.size - 1), new Position(this.x + this.size - 1, this.y));
    }

    public boolean blocksPath(Position a, Position b) {
        if (a.getX() == b.getX()) {
            for (int i = Math.min(a.getY(), b.getY()); i <= Math.max(a.getY(), b.getY()); i++) {
                Position position = new Position(b.getX(), i);
                if (blocksPosition(position)) {
                    return true;
                }
            }
        }

        if (a.getY() == b.getY()) {
            for (int i = Math.min(a.getX(), b.getX()); i <= Math.max(a.getX(), b.getX()); i++) {
                Position position = new Position(i, b.getY());
                if (blocksPosition(position)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public boolean intersects(Position position) {
        return false;
    }

    public void setRobot(String robotName) {
        this.robotName = robotName;
    }

    public boolean hasRobot() {
        return robotName != null;
    }
}
