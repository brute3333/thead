package za.co.wethinkcode.robot.AcceptanceTest;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robotworlds.Client.Client;

import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

public class LookCommandTest {
    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "localhost";
    private  Client serverClient;
    private Socket socket;

    @BeforeEach
    void connectToServer() throws IOException {
        socket = new Socket(DEFAULT_IP, DEFAULT_PORT);
        serverClient = new Client(socket, "");
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
    }

    @AfterEach
    void disconnectFromServer() {
        serverClient.closeEverything();
    }

    /**
     * Story: Look Command
     *
     * Scenario: The world is empty
     *   Given a connected server client
     *   When a look command is sent in an empty world
     *   Then the response should indicate an error
     *   And the response data should contain an error message indicating that the robot does not exist
     */
    @Test
    void lookCommandInEmptyWorldShouldReturnEmptyObjectsList() throws IOException {
        assertTrue(serverClient.isConnected());

        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"look\"" +
                "}";
        serverClient.sendRequest(request);

        JSONObject response = serverClient.getResponse();
        assertEquals("Robot does not exist", response.getJSONObject("data").getString("message"));
        assertNotNull(response.getString("result"));
        assertEquals("ERROR", response.getString("result"));
        assertNotNull(response.getJSONObject("data"));
    }
}
