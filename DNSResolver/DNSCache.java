import java.util.HashMap;

public class DNSCache {
    private HashMap<DNSQuestion, DNSRecord> cache_ = new HashMap<>();

    public void insert(DNSQuestion dnsQuestion, DNSRecord dnsRecord){
        cache_.put(dnsQuestion, dnsRecord);
    }
    public boolean hasQuestion(DNSQuestion dnsQuestion){
        return cache_.containsKey(dnsQuestion);
    }

    public DNSRecord getRecord(DNSQuestion dnsQuestion){
        return cache_.get(dnsQuestion);
    }

    public void removeRecord(DNSQuestion dnsQuestion){
        cache_.remove(dnsQuestion);
    }
}
