import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
public class HttpRequest {
    private Socket clientSocket_;
    private String fileName_;
    private String result_;
    private File myFile_;
    private HashMap<String, String> headers_ = new HashMap<>();
    private boolean isWSRequest_ = false;
    private String secWSKey_;

    public HttpRequest(Socket clientSocket) {
        clientSocket_ = clientSocket;
        createInputStream();
    }
    private void createInputStream(){
        //Create an input stream
        InputStream inputStreamForHTTPRequest;
        try {
            inputStreamForHTTPRequest = clientSocket_.getInputStream();
            //Saving info in Scanner so that we don't have to parse byte by byte
            Scanner inputStreamScanner = new Scanner(inputStreamForHTTPRequest);

            //Read the first line
            String line = inputStreamScanner.nextLine();

            //Split at the space; there are 2 of them
            String[] firstLine = line.split(" ");

            if (firstLine.length <= 1) {
                throw new IOException("First line of header is malformed");
            }

            //Store only the path. The second piece of string technically is a path.
            //Added cause of how get request responds to chat.html
            //String[] correctFileName = firstLine[1].split("/");
            fileName_ = firstLine[1];

            //System.out.println("path is: " + path); // for debugging

            //Read all the other lines and close the scanner
            while (!line.equals("")) {
                // grab the next line
                line = inputStreamScanner.nextLine();
                String[] lines = line.split(": ");
                if (lines.length >= 2){
                    headers_.put(lines[0],lines[1]);
                }
                if(headers_.containsKey("Sec-WebSocket-Key")){
                    isWSRequest_ = true;
                    System.out.println("This is a websocket connection"); // used for debugging
                    secWSKey_ = headers_.get("Sec-WebSocket-Key");
                }
                //Determine what the input file type is, return the same thing back
                System.out.println(line);
            }
            openRequestedFile();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    private void openRequestedFile() {
        //Create a string to store
        result_ = " ";

        //Open the requested file ('path'), our file
        if (fileName_.equals("/")) {
            fileName_ = "/chat.html";
        }

        if (fileName_.indexOf('?') != -1) {
            String[] newPath = fileName_.split("html");
            fileName_ = newPath[0] + "html";
        }

        //This is going into resources folder and grabbing html, css
        //and js at the same time
        fileName_ = "resources" + fileName_; // fileName_ = "resources/" + fileName_;

        myFile_ = new File(fileName_);
        //If open successfully, then result = "200: OK"
        if (myFile_.exists()) {
            result_ = "200: OK";
            //System.out.println("File exists"); // used for debugging
        } else {
            result_ = "404: NOT FOUND";
            //System.out.println("File does not exist"); // used for debugging
        }
    }

    public String getResult() {
        return result_;
    }

    public File getFile() {
        return myFile_;
    }

    public String getFileName() {
        return fileName_;
    }

    public Socket getClientSocket() {
        return clientSocket_;
    }

    public HashMap<String, String> getRequestMap() {
        return headers_;
    }

    public String getSecWSKey(){
        return secWSKey_;
    }

    public boolean isWSRequest(){
        return isWSRequest_;
    }
}