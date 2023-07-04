package za.co.wethinkcode.robotworlds;

import org.json.JSONException;
import org.json.JSONObject;

import za.co.wethinkcode.robotworlds.world.AbstractWorld;
import za.co.wethinkcode.robotworlds.world.IWorld;
import za.co.wethinkcode.robotworlds.Position;

import java.util.HashMap;
import java.util.List;

public class Robot {
    public String name;
    private Position position;
    private int currentShield;
    private int currentShots;
    private IWorld.Direction currentDirection = IWorld.Direction.NORTH;
    public AbstractWorld world;
    private String status;

    public Robot(String name, int maximumShieldStrength, int maximumShots) {
        this.name = name;
        this.currentShield = maximumShieldStrength;
        this.currentShots = maximumShots;
        this.position = new Position(0, 0);
        this.world = new AbstractWorld(maximumShots, maximumShots); 
        this.status = "NORMAL";
    }

    public Robot(AbstractWorld world) {
        this.name = null; // Set the name to null or provide a default name
        this.currentShield = 5; 
        this.currentShots = 10; 
        this.position = new Position(0, 0);
        this.world = world;
        this.status = "NORMAL";
    }

    public Robot(String name, Position position) {
        this.name = name;
        this.currentShield = 5;
        this.currentShots = 10;
        this.position = position;
        this.world = null; // Set the world to null or provide a default world
        this.status = "NORMAL";
    }


    public void handleCommand(Command command) throws JSONException {
        command.execute(this);
    }

    public AbstractWorld getWorld() {
        return world;
    }

    public void setWorld(AbstractWorld world) {
        this.world = world;
    }

    public String getName() {
        return name;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public void setDirection(IWorld.Direction direction) {
        this.currentDirection = direction;
    }

    public IWorld.Direction getDirection() {
        return currentDirection;
    }

    public IWorld.Direction getCurrentDirection() {
        return currentDirection;
    }

    public int getShield() {
        return currentShield;
    }

    public int getShots() {
        return currentShots;
    }

    public void setShield(int shield) {
        this.currentShield = shield;
    }

    public void setShots(int shots) {
        this.currentShots = shots;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HashMap<String, Object> getState() {
        HashMap<String, Object> state = new HashMap<>();
        state.put("position", new int[]{position.getX(), position.getY()});
        state.put("direction", currentDirection.toString());
        state.put("shields", currentShield);
        state.put("shots", currentShots);
        state.put("status", status);
        return state;
    }

    public String toString() {
        try {
            JSONObject stateJson = new JSONObject(getState());
            return stateJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


}
