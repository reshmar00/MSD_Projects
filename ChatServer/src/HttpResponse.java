import java.io.*;
import java.io.OutputStream;

public class HttpResponse
{
    private OutputStream clientOutputStream_;
    private PrintWriter writeResponse_;
    private HttpRequest request_;

    public HttpResponse(HttpRequest request) throws IOException {
        //Need a socket to send stuff back to client
        request_ = request;
        clientOutputStream_ = request_.getClientSocket().getOutputStream();
        createPrintWriter();
    }

    public void createPrintWriter() throws FileNotFoundException {
        //Shove the output stream into a print writer
        writeResponse_ = new PrintWriter(clientOutputStream_);
        sendResponse();
    }

    public void sendResponse() throws FileNotFoundException {
        //Send response header
        String result = request_.getResult();
        writeResponse_.print("http/1.x "+ result + "\n");

        //Grabbing end of file name .html or .css or .js
        String fileName = request_.getFileName();
        String extension = fileName.substring(fileName.lastIndexOf('.')+1);
        writeResponse_.println("Content-Type: text/" + extension);

        //Size will be the length
        writeResponse_.println("Size: " + fileName.length());

        //End response header with a blank line > Client
        writeResponse_.print("\n");
        writeResponse_.flush();

        OutputFileInputStreamToClient();
    }

    public void OutputFileInputStreamToClient () throws FileNotFoundException
    {
        try{
            //Create a FileInputStream, hand myFile to it
            //FileInputStream into my program so that I can output it to client (using transferTo)
            FileInputStream fileStream = new FileInputStream(request_.getFileName());
            //int fileSize = (int)request_.getFileName().length();

            //InputStream from resource folder to server that we're transferring to OutputStream that goes to client
            //Now send the data from the file
            fileStream.transferTo(clientOutputStream_);


            //Note: you'll want to use the flush() method on your PrintWriter/FileOutputStream to
            //make sure your data is actually sent out over the network.
            writeResponse_.flush();

            clientOutputStream_.close();
            writeResponse_.close();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}