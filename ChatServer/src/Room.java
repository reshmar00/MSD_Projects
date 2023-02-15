import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Room {
    private String roomName_;
    //private static boolean roomExists_ = false; // can be static?

    private Socket clientSocket_;

    /* Keeping track of clients who have joined, removing clients who have left*/
    private ArrayList<Socket> clientsConnectedToRoom_ = new ArrayList<>();

    /* Keeping track of messages sent to the room */
    private ArrayList<String> messagesSentToRoom_;

    private ArrayList<String> joinMessages_ = new ArrayList<>();

    private static HashMap<String, Room> activeRooms_ = new HashMap<>();

    /* Made constructor private because we don't want to
     *  have access to the constructor outside the class */
    private Room(String name){
        roomName_ = name;
        //roomExists_ = true;
    }

    public String getRoomName(){
        return roomName_;
    }
    /* If room already exists, return it
    *  Else, create the room, add it to the list of rooms
    *  and return the new room */
    public synchronized static Room getRoom( String name ){
        if (activeRooms_.containsKey( name )){
            return activeRooms_.get( name );
        }
        else {
            Room room = new Room( name );
            activeRooms_.put(name, room);
            return room;
        }
    }

    public synchronized void joinRoom(Socket clientSocket){
        if(!clientsConnectedToRoom_.contains(clientSocket)){
            clientsConnectedToRoom_.add(clientSocket);
        }
    }

    public synchronized void leaveRoom(Socket clientSocket){
        clientsConnectedToRoom_.remove(clientSocket);
    }

    public synchronized void sendMessagesToClients(String message) throws IOException {
        messagesSentToRoom_ = new ArrayList<>();
        messagesSentToRoom_.add(message);
        for (String msg: messagesSentToRoom_){
            for (Socket client: clientsConnectedToRoom_){
                WSResponse.sendMessage(msg, client);
            }
            System.out.println(message);
        }
    }

//    public synchronized void sendJoinMessagesToClients(String message) throws IOException {
//        joinMessages_.add(message);
//        for (String msg: joinMessages_){
//            for (Socket client: clientsConnectedToRoom_){
//                WSResponse.sendMessage(msg, client);
//            }
//            System.out.println(message);
//        }
//    }

    public static HashMap<String, Room> getActiveRooms() {
        return activeRooms_;
    }

    public ArrayList<String> getMessagesSentToRoom(){
        return messagesSentToRoom_;
    }

//    public static boolean doesRoomExist() {
//        return roomExists_;
//    }

}
