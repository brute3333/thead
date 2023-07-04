package za.co.wethinkcode.robotworlds.Client;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.username = username;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            closeEverything();
        }
    }

    public void sendMessage() {
        try {
            System.out.println("Enter 'launch' followed by your robot type and name to start the program:");
            Scanner scanner = new Scanner(System.in);

            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();

                if (messageToSend.equalsIgnoreCase("quit")) {
                    System.out.println("PROGRAM ENDED!");
                    closeEverything();
                    System.exit(0);
                }

                if (messageToSend.startsWith("launch")) {
                    String[] launchCommand = messageToSend.split(" ");
                    if (launchCommand.length != 3) {
                        System.err.println("Invalid launch command. Please provide robot type and name.");
                        continue;
                    }
                    String robotType = launchCommand[1];
                    String robotName = launchCommand[2];

                    String requestJson = createLaunchRequest(robotType, robotName);
                    System.out.println(requestJson);
                    sendRequest(requestJson);
                } else {
                    System.err.println("Invalid command. Please enter a valid command.");
                }

                JSONObject responseJson = readResponse();
                if (responseJson != null) {
                    handleLaunchResponse(responseJson);
                }
            }
        } catch (IOException | JSONException e) {
            closeEverything();
        }
    }

    private String createLaunchRequest(String robotType, String robotName) throws JSONException {

        JSONObject requestJson = new JSONObject();
        requestJson.put("robot", robotType);
        requestJson.put("command", "launch");

        JSONObject argumentsJson = new JSONObject();
        argumentsJson.put("name", robotName);
        argumentsJson.put("shields", 5);
        argumentsJson.put("shots", 10);

        requestJson.put("arguments", argumentsJson);
        return requestJson.toString();
    }

    private void sendRequest(String requestJson) throws IOException {
        bufferedWriter.write(requestJson);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    private void handleLaunchResponse(JSONObject responseJson) throws JSONException {
        System.out.println("Server Response: " + responseJson.toString()); //server response.
        String result = responseJson.getString("result");
        if (result.equalsIgnoreCase("OK")) {
            JSONObject stateJson = responseJson.getJSONObject("state");
            int shieldStrength = stateJson.getInt("shields");
            int shots = stateJson.getInt("shots");
            String status = stateJson.getString("status");

            System.out.println("Launch successful!");
            System.out.println("shields " + shieldStrength);
            System.out.println("shots " + shots);
            System.out.println("status " + status);
        } else {
            String errorMessage = responseJson.getString("message");
            System.out.println("Launch failed. Reason: " + errorMessage);
        }
    }

    private JSONObject readResponse() throws IOException, JSONException {
        String responseStr = bufferedReader.readLine();
        if (responseStr != null && !responseStr.isEmpty()) {
            return new JSONObject(responseStr);
        } else {
            return null;
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    try {
                        String responseStr = bufferedReader.readLine();
                        JSONObject responseJson = new JSONObject(responseStr);
                        handleLaunchResponse(responseJson);
                    } catch (IOException | JSONException e) {
                        closeEverything();
                    }
                }
            }
        }).start();
    }

    private void closeEverything() {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, JSONException {
        Socket socket = new Socket("localhost", 5000);
        Client client = new Client(socket, "");
        client.listenForMessage();
        client.sendMessage();
    }

    public void connect(String defaultIp, int defaultPort) {
    }

    public boolean isConnected() {
        return socket.isConnected();
    }

    public JSONObject getResponse() {
        try {
            return new JSONObject(bufferedReader.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
