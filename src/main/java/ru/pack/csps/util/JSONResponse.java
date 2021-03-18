package ru.pack.csps.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;

import java.io.IOException;

public class JSONResponse {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static JSONObject createOKResponse(String message) {
        JSONObject object = new JSONObject();
        object.put("state", "OK");
        object.put("message", message);
        return object;
    }

    public static JSONObject createOKResponse(String message, Object returnObject) {
        JSONObject object = new JSONObject();
        object.put("state", "OK");
        object.put("message", message);
        try {
            object.put("object", objectMapper.readValue(objectMapper.writeValueAsString(returnObject), JSONObject.class));
        } catch (IOException e) {
            object.put("object", "{}");
        }
        return object;
    }

    public static JSONObject createERRResponse(String message) {
        JSONObject object = new JSONObject();
        object.put("state", "ERR");
        object.put("message", message);
        return object;
    }

    public static JSONObject createNotFoundResponse(String message) {
        JSONObject object = new JSONObject();
        object.put("state", "NOT_FOUND");
        object.put("message", message);
        return object;
    }

    public static JSONObject createNotUniqueResponse(String message) {
        JSONObject object = new JSONObject();
        object.put("state", "NOT_UNIQUE_DATA");
        object.put("message", message);
        return object;
    }

    public static JSONObject createAccessDeniedResponse(String message) {
        JSONObject object = new JSONObject();
        object.put("state", "ACCESS_DENIED");
        object.put("message", message);
        return object;
    }
}
