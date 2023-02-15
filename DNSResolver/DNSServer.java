import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class DNSServer {
    private DatagramSocket datagramSocket_;
    private DatagramPacket datagramPacket_;
    private InetAddress address;
    private int port;
    private byte[] request_, response_;
    static DNSCache currentCache_ = new DNSCache();

    public DNSServer () {
        try{
//            System.out.println("Before opening DatagramSocket");
            // Open datagram socket
            datagramSocket_ = new DatagramSocket(8053);
//            System.out.println("After opening DatagramSocket");

        } catch (SocketException e){
            throw new RuntimeException(e);
        }

        while ( true ){
            request_ = new byte[512];
            datagramPacket_ = new DatagramPacket(request_, request_.length);
            try {
                // When it gets a request ...
                datagramSocket_.receive(datagramPacket_);
                address = datagramPacket_.getAddress();
                port = datagramPacket_.getPort();

                request_ = datagramPacket_.getData();

                DNSMessage dnsMessage =  DNSMessage.decodeMessage(request_);

                // If it's NOT in the cache
                if(!currentCache_.hasQuestion(dnsMessage.getDnsQuestion())){
                    requestGoogle(request_);
//                    System.out.println("**********************************");
//                    System.out.println("Not in cache yet");
//                    for (byte b: response_){
//                        System.out.printf("%2X", b);
//                        System.out.print(", ");
//                    }
//                    System.out.println();
//                    System.out.println("Should be in cache now");
//                    System.out.println("**********************************");
                }
                // If it's in the cache but EXPIRED
                else if(currentCache_.hasQuestion(dnsMessage.getDnsQuestion()) && currentCache_.getRecord(dnsMessage.getDnsQuestion()).isExpired()){
                    currentCache_.removeRecord(dnsMessage.getDnsQuestion());
//                    System.out.println("Removed from cache");
//                    System.out.println();
                    requestGoogle(request_);
//                    System.out.println("**********************************");
//                    System.out.println("Not in cache yet");
//                    for (byte b: response_){
//                        System.out.printf("%2X", b);
//                        System.out.print(", ");
//                    }
//                    System.out.println();
//                    System.out.println("Should be in cache now");
//                    System.out.println("**********************************");
//                    System.out.println();
                }
                else{
//                    System.out.println("**********************************");
//                    System.out.println();
//                    System.out.println("Hello from cache");
                    DNSRecord responseRecord = currentCache_.getRecord(dnsMessage.getDnsQuestion());
                    DNSMessage responseMessage = DNSMessage.buildResponse(dnsMessage, responseRecord);
                    response_ = responseMessage.toBytes();
                    // Put response in a byte array
                    DatagramPacket sendToClient = new DatagramPacket(response_, response_.length, address, port);
                    // Send it back to client on the same socket
                    datagramSocket_.send(sendToClient);
                    System.out.println(responseMessage.getHeader().toString());
//                    for (byte b: response_){
//                        System.out.printf("%2X", b);
//                        System.out.print(", ");
//                    }
//                    System.out.println();
//                    System.out.println("**********************************");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void requestGoogle(byte[] data) throws Exception{
        byte[] receiveResponse = new byte[512];
        response_ = new byte[512];
        DatagramPacket googlePacket = new DatagramPacket(data, data.length, InetAddress.getByName("8.8.8.8"), 53);
        datagramSocket_.send(googlePacket);
        DatagramPacket responseFromGoogle = new DatagramPacket(receiveResponse, receiveResponse.length);
        datagramSocket_.receive(responseFromGoogle);
        response_ = responseFromGoogle.getData();
//        response_ = googlePacket.getData(); // to fill out response byte array
        DNSMessage dnsMessageResponse = DNSMessage.decodeMessage(response_);
        if(dnsMessageResponse.getDnsAnswer().getRData() != null){
            System.out.println("Storing in cache");
            currentCache_.insert(dnsMessageResponse.getDnsQuestion(), dnsMessageResponse.getDnsAnswer());
        }
        // Put response in a byte array
        DatagramPacket sendToClient = new DatagramPacket(response_, response_.length, address, port);
        // Send it back to client on the same socket
        datagramSocket_.send(sendToClient);
        System.out.println(dnsMessageResponse.getHeader().toString());
    }

    /* Getter */
    public DNSServer getServer(){
        return this;
    }

    public static void main (String[] args) throws IOException {
        new DNSServer();
    }
}
