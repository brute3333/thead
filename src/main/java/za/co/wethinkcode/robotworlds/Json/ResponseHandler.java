package za.co.wethinkcode.robotworlds.Json;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ResponseHandler {
    private JSONObject jsonObject;
    private JSONObject data;
    private JSONObject state;
    private String direction;
    private String message;
    private String result;

    public ResponseHandler() {
        jsonObject = new JSONObject();
        data = new JSONObject();
        state = new JSONObject();
    }

    public String serverResponse(String position) throws JSONException {
        data.put("position", "0");
        data.put("visibility", "0");
        data.put("reload", "5");
        data.put("repair", "5");
        data.put("shields", "5");

        state.put("position", position);
        state.put("direction", "NORTH");
        state.put("shields", "5");
        state.put("shots", "5");
        state.put("status", "NORMAL");

        jsonObject.put("result", "OK");
        jsonObject.put("data", data);
        jsonObject.put("state", state);

        return jsonObject.toString();
    }

    public String movementResponse(String position, String message) throws JSONException {
        data.put("message", message);

        state.put("position", position);
        state.put("direction", direction);
        state.put("shields", "5");
        state.put("shots", "5");
        state.put("status", "NORMAL");

        jsonObject.put("result", result);
        jsonObject.put("data", data);
        jsonObject.put("state", state);

        return jsonObject.toString();
    }

    public String setState(String position) throws JSONException {
        state.put("position", position);

        jsonObject.put("state", state);

        return jsonObject.toString();
    }

    public String invalidCommand() throws JSONException {
        jsonObject.put("result", "ERROR");
        jsonObject.put("message", "INVALID COMMAND");

        return jsonObject.toString();
    }

    public String dumpCommand(String position) throws JSONException {
        data.put("position", position);
        data.put("visibility", "0");
        data.put("reload", "5");
        data.put("repair", "5");
        data.put("shields", "5");

        state.put("position", position);
        state.put("direction", "NORTH");
        state.put("shields", "5");
        state.put("shots", "5");
        state.put("status", "NORMAL");

        jsonObject.put("name", "Hal");
        jsonObject.put("data", data);
        jsonObject.put("state", state);

        return jsonObject.toString();
    }

    public JSONObject createErrorResponse(String message) throws JSONException {
        JSONObject errorResponse = new JSONObject();
        errorResponse.put("result", "ERROR");
        errorResponse.put("message", message);
        return errorResponse;
    }

    public JSONObject createSuccessResponse(JSONObject state) throws JSONException {
        JSONObject successResponse = new JSONObject();
        successResponse.put("result", "OK");
        successResponse.put("data", new JSONObject());
        successResponse.put("state", state);
        return successResponse;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
