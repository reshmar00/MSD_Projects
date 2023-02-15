package com.example.androidchatclient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RoomActivity extends AppCompatActivity
                        implements WebSocketListener.DataPass {
    // Implementing dataPass (passing data to activity)
    // so that we have the info necessary to
    // move between this class and the WebSocketListener class
    private ArrayList<String> messages_ = new ArrayList<>();
    private ArrayAdapter<String> chatListAdapter_;
    private ListView lv_;
    private TextView userName_, roomName_;
    private JSONObject jsonObject_;
    private WebSocket webSocket_;
    String user, room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Bundle incomingBundle = getIntent().getExtras();
        user = "";
        room = "";
        if (incomingBundle != null){
            user = incomingBundle.getString("userName_");
            room = incomingBundle.getString("roomName_");
        }

        roomName_ = findViewById(R.id.setRoomName);
        userName_ = findViewById(R.id.messageText );

        roomName_.setText( room );
        userName_.setText( user );

        connectToSocket();
    }

    void connectToSocket(){
        try {
            webSocket_ = new WebSocketFactory().
                    createSocket("ws://10.0.2.2:8080/endpoint", 1000);
        } catch (IOException e) {
            Log.d("CC:", "WS error");
            e.printStackTrace();
        }
        Log.i("CC:MainActivity", "WebSocket created");
        WebSocketListener listener  = new WebSocketListener();
        listener.passActivity( this );
        try {
            webSocket_.addListener(listener);
        } catch( NullPointerException e ){
            Log.d("CC:", "NullPointerException when adding listener");
            e.printStackTrace();
        }
        ExecutorService es = Executors.newSingleThreadExecutor();

        // Connect to the server asynchronously.
        Future<WebSocket> future = webSocket_.connect(es);

        try {
            // Wait for the opening handshake to complete.
            future.get();
            webSocket_.sendText(joinMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String joinMessage(){
        return "join" + " " + user + " " + room;
    }

    private String sendMessage( String msg ){
        return  user + " " + room + " " + msg;
    }

    private String leaveMessage(){
        return "leave" + " " + user + " " + room;
    }

    public void passDataToActivity(String msg) {
        if (msg != null && !msg.isEmpty()){
            messages_.add( msg );
            runOnUiThread(() -> {
                lv_ = findViewById( R.id.chatLV );
                chatListAdapter_ = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messages_);
                lv_.setAdapter( chatListAdapter_ );
                chatListAdapter_.notifyDataSetChanged();
                lv_.smoothScrollToPosition( chatListAdapter_.getCount() );
            });
        }
    }

    public void handleSendClick(View view ) {
        TextInputEditText textInputEditText = findViewById(R.id.messageText );
        String message = "";
        if(textInputEditText!=null) {
            Editable txt = textInputEditText.getText();
            if(txt!=null) {
              message = txt.toString();
            }
        }
        if(!message.equals("")){
            webSocket_.sendText(sendMessage(message));
        }
    }

    public void onBackPressed(){
        webSocket_.sendText(leaveMessage());
    }

    public void handleConnectionError(){
        /* Send an alert dialog (with title "WARNING")
         * saying "Warning: Failed to connect to chat server..." */

        AlertDialog.Builder builderAlert = new AlertDialog.Builder(this);
        builderAlert.setTitle("WARNING");
        builderAlert.setMessage("Warning: Failed to connect to chat server...");
        /* Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface */
        builderAlert.setPositiveButton("OKAY", (DialogInterface.OnClickListener) (dialog, which) -> {
            /* If the user clicks "exit", then the dialog box is canceled */
            dialog.cancel();
        });

        /* Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface */
        builderAlert.setNegativeButton("EXIT", (DialogInterface.OnClickListener) (dialog, which) -> {
            /* If the user clicks the "okay" button, then the app will close */
            finish();
        });

        /* Create the Alert dialog */
        AlertDialog alertDialog = builderAlert.create();
        /* Show the Alert Dialog box */
        alertDialog.show();
    }
}