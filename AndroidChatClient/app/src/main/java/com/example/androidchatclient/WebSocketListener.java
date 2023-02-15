package com.example.androidchatclient;

import android.app.Activity;
import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;


public class WebSocketListener extends WebSocketAdapter {

    private DataPass host;
    // Creating interface to be implemented in RoomActivity
    // Way to pass information without having to create static variables
    public interface DataPass{
        void passDataToActivity(String msg);
    }

    void passActivity(Activity activity){
        if(activity instanceof DataPass){
            host = (DataPass) activity;
        }
        else{
            throw new ClassCastException("Activity must implement DataPass");
        }
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
        super.onConnected(websocket, headers);
        Log.d("CC:WebSocketListener", "Connected successfully");
    }

    @Override
    public void onTextMessage(WebSocket websocket, String text) throws Exception {
        /* Called when a text message is received */
        String msg = "", message = "", type = "", room = "", user = "";
        JSONObject json = null;
        try {
            json = new JSONObject(text);

            type = (String) json.get("type");

            if(type.equals("join")){
                room = (String) json.get("room");
                user = (String) json.get("user");
                msg = user + " has joined the " + room + " room";
            }
            else if (type.equals("leave")){
                room = (String) json.get("room");
                user = (String) json.get("user");
                msg = user + " has left the " + room + " room";
            }
            else{
                if(json.has("message")){
                    user = (String) json.get("user");
                    message = (String) json.get("message");
                    msg = user + ": " + message;
                }
                else{
                    room = (String) json.get("room");
                    msg = room + ": " + message;
                }

            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        host.passDataToActivity(msg);
    }

    @Override
    public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
        super.onConnectError(websocket, exception);
        Log.d("CC:WebSocketListener", "onConnectError");
    }
}

