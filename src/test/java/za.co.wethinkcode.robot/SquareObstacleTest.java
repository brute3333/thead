package za.co.wethinkcode.robot;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robotworlds.Position;
import za.co.wethinkcode.robotworlds.world.SquareObstacle;

import static org.junit.jupiter.api.Assertions.*;

public class SquareObstacleTest {

    @Test
    void testObstacleDimensions() {
        SquareObstacle obstacle = new SquareObstacle(5,5);
        assertEquals(5, obstacle.getBottomLeftX());
        assertEquals(5, obstacle.getBottomLeftY());
        assertEquals(5, obstacle.getSize());
    }

    @Test
    void testSize() {
        SquareObstacle obstacles = new SquareObstacle(1,1);
        assertEquals(1, obstacles.getBottomLeftX());
        assertEquals(1, obstacles.getBottomLeftY());
        assertEquals(5, obstacles.getSize());
    }

    @Test
    void testBlockPosition(){
        SquareObstacle obstacles = new SquareObstacle(7,7);
        assertFalse(obstacles.blocksPosition(new Position(5,1)));
        assertFalse(obstacles.blocksPosition(new Position(5,1)));
        assertFalse(obstacles.blocksPosition(new Position(2,5)));
        assertFalse(obstacles.blocksPosition(new Position(0,9)));
        assertFalse(obstacles.blocksPosition(new Position(9,1)));
    }


    @Test
    void testBlockedPath(){
        SquareObstacle obstacles = new SquareObstacle(1,1);
        assertTrue(obstacles.blocksPath(new Position(1,5), new Position(1,50)));
        assertFalse(obstacles.blocksPath(new Position(8,-10), new Position(2, 100)));
        assertTrue(obstacles.blocksPath(new Position(-15,5), new Position(20, 5)));
        assertFalse(obstacles.blocksPath(new Position(7,2), new Position(0, 100)));
        assertTrue(obstacles.blocksPath(new Position(1,3), new Position(1, 100)));

    }
}
