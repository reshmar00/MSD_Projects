package com.example.androidchatclient;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;


public class MainActivity extends AppCompatActivity {

    AutoCompleteTextView userName_;
    AutoCompleteTextView roomName_;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        userName_ = findViewById( R.id.userNameText );
        roomName_ = findViewById( R.id.roomText );
    }

    public void handleJoinClick(View view ) {
        String getUserName = userName_.getText().toString();
        String getRoomName = roomName_.getText().toString();
        Intent intent = new Intent( this, RoomActivity.class );
        intent.putExtra( "userName_", getUserName );
        intent.putExtra( "roomName_", getRoomName );
        startActivity( intent );
    }
}