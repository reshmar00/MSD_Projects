
import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.Base64;
import org.json.simple.JSONObject;

public class WSResponse {
    private HttpRequest request_;
//    public Socket clientSocket_;
    private OutputStream wsOutputStream_;
    private DataInputStream wsDataInputStream_;
    private PrintWriter wsPrintWriter_;
    private long payloadLength_;
    private String encodedStringUsingBase64_;

    public WSResponse(HttpRequest request) throws Exception {
        request_ = request;
        try {
            handShake();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void handShake() throws Exception {
        //System.out.println("Before handshake"); // used for debugging
        Socket clientSocket = request_.getClientSocket();
        String magicString = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
        // Take key that was sent and concatenate it with the magic string
        String wsAccept = request_.getSecWSKey() + magicString;
        // Get SHA-1 hash from this
        MessageDigest shaHash = MessageDigest.getInstance("SHA-1");
        shaHash.update(wsAccept.getBytes());
        byte[] digest = shaHash.digest();
        // Encode using Base64
        Base64.Encoder base64Encoder = Base64.getEncoder();
        encodedStringUsingBase64_ = base64Encoder.encodeToString(digest);
        try {
            sendWSResponse(clientSocket); // send web socket response
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void sendWSResponse(Socket clientSocket) throws Exception {
        wsOutputStream_ = clientSocket.getOutputStream();
        wsPrintWriter_ = new PrintWriter(wsOutputStream_);

        //Send response headers in exact WS protocol syntax
        //System.out.println("After handshake"); // used for debugging
        wsPrintWriter_.print("http/1.1 101 Switching Protocols" + "\r\n");
        wsPrintWriter_.print("Upgrade: websocket" + "\r\n");
        wsPrintWriter_.print("Connection: Upgrade" + "\r\n");
        wsPrintWriter_.print("Sec-WebSocket-Accept: " + encodedStringUsingBase64_ + "\r\n");
        wsPrintWriter_.print("\n"); // end with a new line
        wsPrintWriter_.flush(); // push out message
        try {
            WSDecodeStream(clientSocket);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Now, we need to open the web socket
    private void WSDecodeStream(Socket clientSocket) throws IOException {
        //System.out.println("After first few headers are sent"); // used for debugging
        while (true) {
            wsDataInputStream_ = new DataInputStream(clientSocket.getInputStream());
            if (wsDataInputStream_.available() >= 1) {
                try {
                    wsDataInputStream_.readByte(); // reading in the first byte
                    byte byte2 = wsDataInputStream_.readByte(); // reading in the second byte
                    boolean isMask = ((byte2 & 0x80) != 0); // checking if it's masked
                    // Get payload length by using the correct mask; get the last 7 bits
                    payloadLength_ = byte2 & 0x7F;
                    if (payloadLength_ == 126) {
                        // if payload length is 126, we need to read in 2 more bytes to get the full length
                        payloadLength_ = wsDataInputStream_.readShort();
                    } else if (payloadLength_ == 127) {
                        // if payload length is 127, we need to read in 8 more bytes to get the full length
                        payloadLength_ = wsDataInputStream_.readLong();
                    }
                    // if payload length is 125 bytes or lower, we don't have to get more info
                    byte[] ENCODED = new byte[(int) payloadLength_];
                    byte[] DECODED = new byte[(int) payloadLength_];
                    byte[] MASK = new byte[4];

                    if (isMask) { // there IS a mask
                        for (int i = 0; i < 4; i++) {
                            MASK[i] = wsDataInputStream_.readByte(); // get the masking key
                        }
                    }

                    // At this point, you have the masking key
                    // what's remaining is the actual payload info
                    for (int j = 0; j < payloadLength_; j++) {
                        ENCODED[j] = wsDataInputStream_.readByte();
                    }
                    // Finally decode the message
                    for (int k = 0; k < payloadLength_; k++) {
                        DECODED[k] = (byte) (ENCODED[k] ^ MASK[k % 4]);
                    }

                    //System.out.println("Message decoded"); // used for debugging
                    String payload = new String(DECODED);

                    createJSONResponse(payload);
                } catch (EOFException e) {
                    //System.out.println("End of file reached"); // used for debugging
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void createJSONResponse(String payload) throws IOException {
        //Send it back
        String[] arrPayload = payload.split("\\s", 3);
//        for (String s: arrPayload){
//            System.out.println(s);
//        }
        String command = arrPayload[0];
        String username = "", room = "", message = "";

        Socket clientSocket = request_.getClientSocket();
        JSONObject jsonObject = new JSONObject();

        if (arrPayload.length > 1){
            if (command.equals("join")) {
                username = arrPayload[1];
                room = arrPayload[2];

                jsonObject.put("type", "join");
                jsonObject.put("room", room);
                jsonObject.put("user", username);

                Room myRoom = Room.getRoom(room);
                myRoom.joinRoom(clientSocket);
                myRoom.sendMessagesToClients(jsonObject.toString());

                //System.out.println("Displayed in div"); // used for debugging
            }

            else if(command.equals("leave")){
                username = arrPayload[1];
                room = arrPayload[2];

                jsonObject.put("type", "leave");
                jsonObject.put("room", room);
                jsonObject.put("user", username);

                Room myRoom = Room.getActiveRooms().get(room);
                myRoom.leaveRoom(clientSocket);
                myRoom.sendMessagesToClients(jsonObject.toString());

                //System.out.println("Displayed in div"); // used for debugging
            }

            else {
                message = arrPayload[2];
                room = arrPayload[1];

                jsonObject.put("type", "message");
                jsonObject.put("user", command);
                jsonObject.put("message", message);

                Room myRoom = Room.getActiveRooms().get(room);
                myRoom.sendMessagesToClients(jsonObject.toString());

                //System.out.println("Displayed in div"); // used for debugging
            }
        }

        //System.out.println("Done with createAndSendJSONResponse"); // used for debugging
    }

    public static void sendMessage (String JSONString, Socket clientSocket) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

        dataOutputStream.writeByte(0x81);
        byte jsonStringLengthInBytes = (byte) JSONString.length();
        dataOutputStream.writeByte(jsonStringLengthInBytes);
        for (byte b: JSONString.getBytes()){
            dataOutputStream.writeByte(b);
        }
        dataOutputStream.flush();
    }
    public String getEncodedStringUsingBase64 () {
        return encodedStringUsingBase64_;
    }

}