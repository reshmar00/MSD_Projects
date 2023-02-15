import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionHandler {

    private Socket clientSocket_;
    private HttpRequest request_;

    public ConnectionHandler() throws IOException {
        clientSocket_ = Server.getServerSocket().accept();
    }

    public void handleConnection() {
       new Thread(() -> {
            try{
                // Create request
                request_ = new HttpRequest(clientSocket_);

                // Send response
                if(request_.isWSRequest()){
                    new WSResponse(request_);
                }
                else {
                    new HttpResponse(request_);
                }
                //System.out.println("Client socket is closed"); // used for debugging
            } catch(Exception e){
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }).start();
    }
    public HttpRequest getRequest(){
        return request_;
    }
}