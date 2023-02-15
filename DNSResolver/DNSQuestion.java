import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class DNSQuestion {
    /* Read a question from the input stream
    Due to compression, you may have to ask the DNSMessage
    containing this question to read some fields */
    private ArrayList<String> QName_ = new ArrayList<>();
    private short QType_, QClass_;

    public static DNSQuestion decodeQuestion(InputStream inputStream, DNSMessage dNSMsg) throws IOException {
        DNSQuestion dnsQuestion = new DNSQuestion();
        dnsQuestion.QName_ = dNSMsg.readDomainName(inputStream);
//        System.out.print("QName: " + QName_);
//        System.out.println();

        byte typeByte1 = inputStream.readNBytes(1)[0];
        byte typeByte2 = inputStream.readNBytes(1)[0];
        dnsQuestion.QType_ = (byte)((typeByte1 << 8) | (typeByte2 & 0xFF));
//        System.out.print("QType: ");
//        System.out.printf("%2X", QType_[0] + QType_[1]);
//        System.out.println();
        byte classByte1 = inputStream.readNBytes(1)[0];
        byte classByte2 = inputStream.readNBytes(1)[0];
        dnsQuestion.QClass_ = (byte)((classByte1 << 8) | (classByte2 & 0xFF));
//        System.out.print("QClass: ");
//        System.out.printf("%2X", QClass_[0] + QClass_[1]);
//        System.out.println();
        return dnsQuestion;
    }

    /* Write the question bytes which will be sent to the client
    The hash map is used for us to compress the message; see the DNSMessage class */
    public void writeBytes(ByteArrayOutputStream byteArrayOutputStream, HashMap<String,Integer> domainNameLocations) throws IOException {
        DNSMessage.writeDomainName(byteArrayOutputStream, domainNameLocations, QName_);
        byte typeByte1 = (byte)(QType_ >>> 8);
        byte typeByte2 = (byte)(QType_);
        byteArrayOutputStream.write(typeByte1);
        byteArrayOutputStream.write(typeByte2);

        byte classByte1 = (byte)(QClass_ >>> 8);
        byte classByte2 = (byte)(QClass_);
        byteArrayOutputStream.write(classByte1);
        byteArrayOutputStream.write(classByte2);
    }

    /* Let your IDE generate the following
    They're needed to use a question as a HashMap key,
    and to get a human-readable string. */
    @Override
    public String toString(){
        return "DNSQuestion: " + "\n" +
                "\t" + "Q Name: " + QName_ + "\n" +
                "\t" + "Q Type: " + QType_ + "\n" +
                "\t" + "Q Class: " + QClass_ + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DNSQuestion question = (DNSQuestion) o;
        return QType_ == question.QType_ && QClass_ == question.QClass_ && Objects.equals(QName_, question.QName_);
    }

    @Override
    public int hashCode() {
        return Objects.hash(QName_, QType_, QClass_);
    }
}
