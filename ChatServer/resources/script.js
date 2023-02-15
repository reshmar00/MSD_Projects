"use strict"

///////////////////////////////////////////////////////////////
//Callback Functions

let wsOpen = false;

function handleOpenCB( event ){
    console.log("WebSocket is open.");
    wsOpen = true;
}

function handleMsgFromServer( event ){
    console.log( "In the msg display function" );

    let msg = event.data;
    let msgObj = JSON.parse( msg );
    console.log( msgObj );

    let type = msgObj.type;
    let user = msgObj.user;
    let room = msgObj.room;

    console.log( "message received" );

    if (type === "join"){
        // display user in user list
        let userlistItem = document.createElement('li');
        userlistItem.id = msgObj.user;
        let userlistItemText = document.createTextNode(msgObj.user);
        userlistItem.appendChild(userlistItemText);
        userList.appendChild(userlistItem);
        // display user joined room in message board
        let msglistItem = document.createElement('li');
        // msglistItem.id = msg;
        let msglistItemText = document.createTextNode(user + " has joined the " + room + " room" );            
        msglistItem.appendChild(msglistItemText);
        msgList.appendChild(msglistItem);
    }

    else if (type === "message"){
        let msglistItem = document.createElement('li');
        let msglistItemText = document.createTextNode(user + ": " + msgObj.message);            
        msglistItem.appendChild(msglistItemText);
        msgList.appendChild(msglistItem);
    }
    
    else if (type === "leave"){       
        let msglistItem = document.createElement('li');
        let msglistItemText = document.createTextNode(user + " has left the " + room + " room" );
        msglistItem.appendChild(msglistItemText);
        msgList.appendChild(msglistItem);

        let userlistItem = document.createElement('li');
        userlistItem.id = msgObj.user;
        let userlistItemText = document.createTextNode(msgObj.user);
        userlistItem.appendChild(userlistItemText);
        userList.appendChild(userlistItem);
        userlistItem.removeChild(userlistItemText);
        userList.removeChild(userlistItem);
    }

}

function handleMsgToServer ( event ) {
    if (event.keyCode === 13) {
        event.preventDefault();
        let user = userNameText.value;
        let room = roomNameText.value;
        console.log("In 1st if statement");
        if( wsOpen && room != null && user != null){
            ws.send(`${userNameText.value} ${roomNameText.value} ${messageText.value}`);
            console.log("Sent message from text box to server");
        }
        else{
            console.log("Websocket is not open...");
        }
    }
}

function handleCloseCB( event ){
    console.log("WebSocket is closed.")
    ws.send(`leave ${userNameText.value} ${roomNameText.value}`);
    ws.close();
    wsOpen = false;
    //rightDiv.innerText = "The WebSocket has left the building... Goodbye.";
    
    // open up div, put a msg saying server is gone
    //event.value = "The server has left the building... Goodbye.";
}

function handleErrorCB(){
    console.log("Encountered an error.")
}

// Function to handle ENTER
function handleUserNameAndRoomNameCB( event ) {
    if (event.keyCode === 13) {
        // && userNameText.value != null && roomNameMeetsValue
        //check that conditions are met
        //say the user joined the room
        event.preventDefault();
        let room = roomNameText.value;
        console.log("In 1st if statement");
        for (let r of room) {
            if (r < 'a' || r > 'z') {
                console.log("In 2nd if statement");
                alert("Incorrect room name format. Try again without uppercase letters and/or spaces");
                return;
            }
        }
        if( wsOpen ){
            console.log("Sending join message");
            ws.send(`join ${userNameText.value} ${roomNameText.value}`);
        }
        else{
            console.log("Websocket is not open...");
        }

    }
}

///////////////////////////////////////////////////////////////

let userNameText = document.getElementById("userNameText");
let roomNameText = document.getElementById("roomNameText");
let messageText = document.getElementById("messageText");

let userList = document.getElementById("userList");
let msgList = document.getElementById("msgList");

let leftDiv = document.getElementById("left");
let rightDiv = document.getElementById("right");

userNameText.addEventListener( "keypress", handleUserNameAndRoomNameCB );
roomNameText.addEventListener( "keypress", handleUserNameAndRoomNameCB );
messageText.addEventListener( "keypress", handleMsgToServer );


///// Create a WebSocket
let ws = new WebSocket("ws://localhost:8080");
ws.onopen = handleOpenCB;
ws.onmessage = handleMsgFromServer;
ws.onerror = handleErrorCB;
ws.onclose = handleCloseCB;
window.onunload = handleCloseCB;
// handle send message to server -> let msg = get msg (id) ->
// add event listener "keypress" -> handle send message to server