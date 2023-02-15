import java.io.*;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class DNSMessage {

    private DNSMessage response;
    private DNSHeader dnsHeader_;
    private DNSQuestion dnsQuestion_;
    private DNSRecord dnsRecord_;
    private DNSRecord dnsAnswer_;
    private DNSRecord dnsAuthority_;
    private DNSRecord dnsAdditional_ = new DNSRecord();
    private DNSQuestion[] dnsQuestions_ = new DNSQuestion[2];
    private DNSRecord[] dnsRecords_ = new DNSRecord[2];
//    DNSRecord[] dnsAnswer_;
//    DNSRecord[] dnsAuthority_;
//    DNSRecord[] dnsAdditional_;
    private byte[] messageBytes_;

    public static DNSMessage decodeMessage(byte[] bytes) throws IOException {
        DNSMessage dnsMessage = new DNSMessage();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        dnsMessage.messageBytes_ = bytes;
        dnsMessage.dnsHeader_ = DNSHeader.decodeHeader(byteArrayInputStream);
        dnsMessage.dnsQuestion_ = DNSQuestion.decodeQuestion(byteArrayInputStream, dnsMessage);
        dnsMessage.dnsQuestions_[0] = dnsMessage.dnsQuestion_;
        if(dnsMessage.dnsHeader_.getQR() == 1){
            dnsMessage.dnsAnswer_ = DNSRecord.decodeRecord(byteArrayInputStream, dnsMessage);
            dnsMessage.dnsRecords_[0] = dnsMessage.dnsAnswer_;
            dnsMessage.dnsAuthority_ = DNSRecord.decodeRecord(byteArrayInputStream, dnsMessage);
            dnsMessage.dnsAdditional_ = DNSRecord.decodeRecord(byteArrayInputStream, dnsMessage);
        }

//        dnsMessage.dnsRecord_ = DNSRecord.decodeRecord(byteArrayInputStream, dnsMessage);

//        dnsMessage.populateQuestionArray(byteArrayInputStream);
//        dnsMessage.populateAnswerRecords(byteArrayInputStream);
//        dnsMessage.populateAuthorityRecords(byteArrayInputStream);
//        dnsMessage.populateAdditionalRecords(byteArrayInputStream);
        return dnsMessage;
    }
    /* Read the pieces of a domain name starting from the current position of the input stream */
    public ArrayList<String> readDomainName(InputStream inputStream) throws IOException {
        // read in first byte.
        // if not zero, use that no. to read in more
        ArrayList<String> labels = new ArrayList<>();

        byte name = (byte)inputStream.read();

        while (name != 0){
            String section = new String(inputStream.readNBytes(name));
            labels.add(section);
            name = (byte)inputStream.read();
        }
        System.out.print("Word: ");
        for(int j = 0; j < labels.size(); j++){
            System.out.print(labels.get(j)); // for debugging
        }
        return labels;
    }

    /* Same as above but used when there's compression,
    and we need to find the domain from earlier in the message.
    This method should make a ByteArrayInputStream that starts at the specified byte
    and call the other version of this method */
    public ArrayList<String> readDomainName(int firstByte) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(messageBytes_, firstByte, messageBytes_.length);
        return readDomainName(byteArrayInputStream);
    }

    /* Build a response based on the request and the answers you intend to send back */
    public static DNSMessage buildResponse(DNSMessage request, DNSRecord answer) throws IOException {
        DNSMessage response = new DNSMessage();
        response.dnsHeader_ = DNSHeader.buildHeaderForResponse(request);
        response.dnsQuestion_ = request.getDnsQuestion();
        response.dnsAnswer_ = answer;
        response.dnsAuthority_ = request.getDnsAuthority();
        response.dnsAdditional_= request.getDnsAdditional();
        return response;
    }

    /* Get the bytes to put in a packet and send back */
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        HashMap<String,Integer> domainNameLocations = new HashMap<>();
        dnsHeader_.writeBytes(byteArrayOutputStream);
        dnsQuestion_.writeBytes(byteArrayOutputStream, domainNameLocations);
            if (dnsHeader_.getAnswerCount() > 0){
                dnsAnswer_.writeBytes(byteArrayOutputStream, domainNameLocations);
            }
            if (dnsHeader_.getAuthorityCount() > 0){
                dnsAuthority_.writeBytes(byteArrayOutputStream, domainNameLocations);
            }
            if (dnsHeader_.getAdditionalCount() > 0){
                dnsAdditional_.writeBytes(byteArrayOutputStream, domainNameLocations);
            }

//        for (byte b: byteArrayOutputStream.toByteArray()){
//            System.out.printf("%2X", b);
//            System.out.print(", ");
//        }

        return byteArrayOutputStream.toByteArray();
    }

    /* If this is the first time we've seen this domain name in the packet,
    write it using the DNS encoding (each segment of the domain prefixed with its length,
    0 at the end), and add it to the hash map. Otherwise, write a back pointer to where
    the domain has been seen previously. */
    public static void writeDomainName(ByteArrayOutputStream byteArrayOutputStream, HashMap<String,Integer> domainLocations, ArrayList<String> domainPieces) throws IOException {
        // check if it's in the hashmap
        String domain = DNSMessage.joinDomainName(domainPieces);
        int location;

        if (domainLocations.containsKey(domain)){ // if yes, use DNS encoding -> write using the pointer
            location = domainLocations.get(domain);
            byteArrayOutputStream.write((byte)(0xC0 | (byte)(location >>> 8) & 0xFF ));
            byteArrayOutputStream.write((byte)(location & 0xFF));
        }
        // if not, create it and put it in the hashmap
        else{
            domainLocations.put(domain, byteArrayOutputStream.size());
            for(int i = 0; i < domainPieces.size(); i++){
                byte length = (byte)domainPieces.get(i).length();
                byteArrayOutputStream.write(length);
                byteArrayOutputStream.write(domainPieces.get(i).getBytes());
            }
            byteArrayOutputStream.write(0);
        }
    }

    /* Join the pieces of a domain name with dots ([ "utah", "edu"] -> "utah.edu" ) */
    public static String joinDomainName(ArrayList<String> pieces){
        StringBuilder fullDomain = new StringBuilder();
        for (int i = 0; i < pieces.size(); i++){
            fullDomain.append(pieces.get(i));
            if( i != pieces.size()-1){
                fullDomain.append(".");
            }
        }
        return fullDomain.toString();
    }

    @Override
    public String toString(){
        return "DNSMessage: " + "\n" +
                "\t" + "DNSHeader: " + dnsHeader_.toString() + "\n" +
                "\t" + "DNSQuestion: " + dnsQuestion_.toString() + "\n" +
                "\t" + "DNSRecord: " + dnsRecord_.toString() + "\n";
    }

    /* Getters */
    public DNSQuestion getDnsQuestion(){
        return dnsQuestion_;
    }
    public DNSRecord getDnsRecord(){
        return dnsRecord_;
    }

    public DNSRecord getDnsAnswer(){
        return dnsAnswer_;
    }

    public DNSRecord getDnsAuthority(){
        return dnsAuthority_;
    }

    public DNSRecord getDnsAdditional(){
        return dnsAdditional_;
    }
    public DNSMessage getDnsMessage(){
        return this;
    }

//    public DNSQuestion[] populateQuestionArray(ByteArrayInputStream byteArrayInputStream) throws IOException {
//        int size = dnsHeader_.getQuestionCount();
//        dnsQuestions_ = new DNSQuestion[size];
//        for(int i = 0; i < size; i++){
//            dnsQuestions_[i] = DNSQuestion.decodeQuestion(byteArrayInputStream, getDnsMessage());
//        }
//        return dnsQuestions_;
//    }
//
//    public DNSRecord[] populateAnswerRecords(ByteArrayInputStream byteArrayInputStream) throws IOException {
//        int size = dnsHeader_.getAnswerCount();
//        dnsAnswer_ = new DNSRecord[size];
//        if(size > 0){
//            for(int i = 0; i < size; i++){
//                dnsAnswer_[i] = DNSRecord.decodeRecord(byteArrayInputStream, getDnsMessage());
//            }
//            return dnsAnswer_;
//        }
//        else{
//            return null;
//        }
//    }
//
//    public DNSRecord[] populateAuthorityRecords(ByteArrayInputStream byteArrayInputStream) throws IOException {
//        int size = dnsHeader_.getAuthorityCount();
//        dnsAuthority_ = new DNSRecord[size];
//        if(size > 0){
//            for(int i = 0; i < size; i++){
//                dnsAuthority_[i] = DNSRecord.decodeRecord(byteArrayInputStream, getDnsMessage());
//            }
//            return dnsAuthority_;
//        }
//        return null;
//    }
//
//    public DNSRecord[] populateAdditionalRecords(ByteArrayInputStream byteArrayInputStream) throws IOException {
//        int size = dnsHeader_.getAdditionalCount();
//        dnsAdditional_ = new DNSRecord[size];
//        if(size > 0){
//            for(int i = 0; i < size; i++){
//                dnsAdditional_[i] = DNSRecord.decodeRecord(byteArrayInputStream, getDnsMessage());
//            }
//            return dnsAdditional_;
//        }
//        else{
//            return null;
//        }
//    }

    public DNSHeader getHeader() {
        return dnsHeader_;
    }

    private static byte[] intToBytes(int data) {
        return new byte[] {
                (byte)((data >> 24) & 0xff),
                (byte)((data >> 16) & 0xff),
                (byte)((data >> 8) & 0xff),
                (byte)((data) & 0xff),
        };
    }

    private static int bytesToInt(byte[] data) {
        return ((data[0] & 0xFF) << 24) |
                ((data[1] & 0xFF) << 16) |
                ((data[2] & 0xFF) << 8) |
                ((data[3] & 0xFF));
    }

}
