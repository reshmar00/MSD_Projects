import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class DNSRecord {
    private ArrayList<String> NAME_ = new ArrayList<>();

    private short TYPE_, CLASS_, RDLENGTH_;
    private int TTL_;
    private byte [] RDATA_;
    private long creationTime;
    public static DNSRecord decodeRecord(InputStream inputStream, DNSMessage dnsMessage) throws IOException {
        DNSRecord dnsRecord = new DNSRecord();
        dnsRecord.creationTime = Instant.now().getEpochSecond();
        inputStream.mark(Integer.MAX_VALUE);
        byte firstByte = inputStream.readNBytes(1)[0];
        // Check if there's compression
        int compressedBits = firstByte & 0xC0;
        // if yes, get to the pointer and read the label from there
        if (compressedBits == 0xC0){
            int offset = (firstByte & 0b00111111) << 2;
            byte secondByte = inputStream.readNBytes(1)[0];
            offset += secondByte;
            // determine offset: remaining 6 bits + second byte
            dnsRecord.NAME_ = dnsMessage.readDomainName(offset); // pass in offset
        }
        else{
            inputStream.reset(); // takes it back to the last position
            dnsRecord.NAME_ = dnsMessage.readDomainName(inputStream);
        }

//        System.out.print("NAME: " + dnsRecord.NAME_);
//        System.out.println();
        byte typeByte1 = inputStream.readNBytes(1)[0];
        byte typeByte2 = inputStream.readNBytes(1)[0];



        dnsRecord.TYPE_ = (byte)((typeByte1 << 8) | (typeByte2 & 0xFF));
//        System.out.print("TYPE: ");
//        System.out.printf("%2X", dnsRecord.TYPE_[0] + dnsRecord.TYPE_[1]);
//        System.out.println();

        byte classByte1 = inputStream.readNBytes(1)[0];
        byte classByte2 = inputStream.readNBytes(1)[0];

        dnsRecord.CLASS_ = (byte)((classByte1 << 8) | (classByte2 & 0xFF));
//        System.out.print("CLASS: ");
//        System.out.printf("%2X", dnsRecord.CLASS_[0] + dnsRecord.CLASS_[1]);
//        System.out.println();


        byte ttlByte1 = inputStream.readNBytes(1)[0];
        byte ttlByte2 = inputStream.readNBytes(1)[0];
        byte ttlByte3 = inputStream.readNBytes(1)[0];
        byte ttlByte4 = inputStream.readNBytes(1)[0];


        dnsRecord.TTL_ = (((ttlByte1 & 0xFF) << 24) | ((ttlByte2 & 0xFF) << 16) | ((ttlByte3 & 0xFF) << 8) | (ttlByte4 & 0xFF));
//        System.out.print("TTL: ");
//        System.out.printf("%2X", dnsRecord.TTL_[0] + dnsRecord.TTL_[1] + dnsRecord.TTL_[2] + dnsRecord.TTL_[3]);
//        System.out.println();

       // dnsRecord.RDLENGTH_ =;

        byte rdLengthByte1 = inputStream.readNBytes(1)[0];
        byte rdLengthByte2 = inputStream.readNBytes(1)[0];

        dnsRecord.RDLENGTH_ = (byte)((rdLengthByte1 << 8) | (rdLengthByte2 & 0xFF));

//        System.out.print("RDLENGTH: ");
//        System.out.printf("%2X", dnsRecord.RDLENGTH_[0] + dnsRecord.RDLENGTH_[1]);
//        System.out.println();
        dnsRecord.RDATA_ = inputStream.readNBytes(dnsRecord.RDLENGTH_); // reads remaining bytes from InputStream
 // reads remaining bytes from InputStream

//        System.out.print("RDATA: ");
//        for (byte rDatum : dnsRecord.RDATA_) {
//            System.out.printf("%2X", rDatum);
//        }
//        System.out.println();
        return dnsRecord;
    }

    public void writeBytes(ByteArrayOutputStream byteArrayOutputStream, HashMap<String, Integer> hashMap) throws IOException {
        DNSMessage.writeDomainName(byteArrayOutputStream, hashMap, NAME_);
        byte typeByte1 = (byte)(TYPE_ >>> 8);
        byte typeByte2 = (byte)(TYPE_);
        byteArrayOutputStream.write(typeByte1);
        byteArrayOutputStream.write(typeByte2);

        byte classByte1 = (byte)(CLASS_ >>> 8);
        byte classByte2 = (byte)(CLASS_);
        byteArrayOutputStream.write(classByte1);
        byteArrayOutputStream.write(classByte2);

        byte ttlByte1 = (byte)(TTL_ >>> 24);
        byte ttlByte2 = (byte)(TTL_ >>> 16);
        byte ttlByte3 = (byte)(TTL_ >>> 8);
        byte ttlByte4 = (byte)(TTL_);
        byteArrayOutputStream.write(ttlByte1);
        byteArrayOutputStream.write(ttlByte2);
        byteArrayOutputStream.write(ttlByte3);
        byteArrayOutputStream.write(ttlByte4);

        byte rdLengthByte1 = (byte)(RDLENGTH_ >>> 8);
        byte rdLengthByte2 = (byte)(RDLENGTH_);
        byteArrayOutputStream.write(rdLengthByte1);
        byteArrayOutputStream.write(rdLengthByte2);

        byteArrayOutputStream.write(RDLENGTH_);
        if (RDLENGTH_ > 0){
            byteArrayOutputStream.write(RDATA_);
        }
    }

    @Override
    public String toString(){
        return "DNSRecord: " + "\n" +
                "\t" + "Name(s): " + NAME_ + "\n" +
                "\t" + "Type: " + TYPE_ + "\n" +
                "\t" + "Class: " + CLASS_ + "\n" +
                "\t" + "Time to Live " + TTL_ + "\n" +
                "\t" + "RD Length: " + RDLENGTH_ + "\n" +
                "\t" + "R Data: " + Arrays.toString(RDATA_) + "\n";
    }
    /* Return whether the creation date + the time to live
    is after the current time. The Date and Calendar classes will be useful for this. */
    boolean isExpired(){
        if(TTL_ == 0 ){
            return false;
        }
        //This method returns the time in millis
        long currentTime = Instant.now().getEpochSecond();
        return (currentTime > (creationTime + TTL_));
    }

    public byte[] getRData(){
        return RDATA_;
    }
}
