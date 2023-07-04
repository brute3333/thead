package za.co.wethinkcode.robotworlds.Server;

import org.json.JSONException;
import org.json.JSONObject;

// import com.fasterxml.jackson.databind.JsonNode;
// import com.fasterxml.jackson.databind.ObjectMapper;
// 

import za.co.wethinkcode.robotworlds.Command;
import za.co.wethinkcode.robotworlds.Commands.CommandHandler;
import za.co.wethinkcode.robotworlds.Direction;

import za.co.wethinkcode.robotworlds.Position;
import za.co.wethinkcode.robotworlds.Robot;
import za.co.wethinkcode.robotworlds.world.AbstractWorld;
// import za.co.wethinkcode.robotworlds.world.SquareObstacle;

// import java.awt.*;
// import java.util.Scanner;
import java.io.*;
import java.net.*;
import java.util.Random;


public class SimpleServer implements Runnable {

    // public static final int PORT = 5000;
    // private final BufferedReader in;
    // private final PrintStream out2;
    // private final String clientMachine;
    // private Robot robot;
    // String name;
    // private ResponseHandler response;
    // String command;
    // Command commands;
    // Position position;
    // CommandHandler getCommand = new CommandHandler();
    // AbstractWorld world;
    // Direction direction;

    public static final int PORT = 5000;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final String clientMachine;
    private Robot robot;
    private String name;
    private CommandHandler commandHandler;
    private AbstractWorld world;
    private Position position;
    private Direction direction;

    public SimpleServer(Socket socket, AbstractWorld world, Robot robot) throws IOException {
        System.out.println("Waiting for client...");
        clientMachine = socket.getInetAddress().getHostName();
        System.out.println("Connection from " + clientMachine);
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // this.out2 = new PrintStream(socket.getOutputStream());
        this.world = world;
        this.robot = robot;
        this.commandHandler = new CommandHandler();
        // serverCommands();
    }

    public void run() {
        try {
            String messageFromClient;
            Random rand = new Random();
            int x = rand.nextInt(100) - 100;
            int y = rand.nextInt(200) - 200;
            position = new Position(x, y);
            direction = Direction.NORTH;

            while ((messageFromClient = in.readLine()) != null) {
                System.out.println("Message from client: " + messageFromClient);
                JSONObject jsonObject = new JSONObject(messageFromClient);
                String robotName = jsonObject.getString("robot");
                String commandString = jsonObject.getString("command");
                JSONObject arguments = jsonObject.getJSONObject("arguments");

                // Update the robot's position and direction
                robot.getWorld().setPosition(position);
                robot.getWorld().setDirection(direction);

                String command;
                if (commandString.equals("state")) {
                    command = "state";
                } else {
                    command = commandString + " " + arguments.toString();
                }

                if (commandHandler.commandHandle(commandString, robot)) {
                    command = "invalid";
                }

                Command commandObjects = Command.create(command);
                robot.name = commandObjects.getName();
                robot.handleCommand(commandObjects);
                JSONObject response = new JSONObject();
                response.put("result", "OK");
                JSONObject data = new JSONObject();
                data.put("position", new int[]{position.getX(), position.getY()});
                data.put("direction", direction.toString());
                response.put("data", data);
                JSONObject state = new JSONObject();
                state.put("position", new int[]{position.getX(), position.getY()});
                state.put("direction", direction.toString());
                state.put("shields", robot.getShield());
                state.put("shots", robot.getShots());
                state.put("status", robot.getStatus());
                response.put("state", state);
                out.write(response.toString());
                out.newLine();
                out.flush();           

                // Update the robot's name, position, and direction for next iteration
                this.name = robotName;
                position = robot.getWorld().getPosition();
                direction = robot.getWorld().getCurrentDirection();
            }
        } catch (IOException | JSONException e) {
            System.out.println("Shutting down single client server");
        } finally {
            closeQuietly();
        }
    }

    private void closeQuietly() {
        try {
            in.close();
            out.close();
        } catch (IOException ignored) {
        }
    }

    public static void main(String[] args) throws IOException{
        AbstractWorld world = new AbstractWorld(0, 0);
        Robot robot = new Robot(world);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server running & waiting for client connections.");

            while (true){
                Socket socket = serverSocket.accept();
                new Thread(new SimpleServer(socket, world, robot)).start();
            }
        }
    }

    // private void serverCommands() {
    //     new Thread(() -> {
    //         while (true) {
    //             String msgFromServer;
    //             try {
    //                 msgFromServer = in.readLine();
    //             } catch (IOException e) {
    //                 e.printStackTrace();
    //                 break;
    //             }

    //             if (msgFromServer == null) {
    //                 // The client has closed the connection
    //                 break;
    //             }

    //             if (msgFromServer.equals("quit") || msgFromServer.equals("off")) {
    //                 System.out.println("PROGRAM ENDED!");
    //                 System.exit(0);
    //             } else if (msgFromServer.equals("dump")) {
    //                 System.out.println("There are some obstacles:");
    //                 robot.getWorld().showObstacles();
    //             }
    //         }
    //     }).start();
    // }
}
