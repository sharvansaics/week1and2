import java.util.*;

public class week1and2 {

    // Entry class
    class Entry {
        String ip;
        long expiryTime;

        Entry(String ip, long ttlSeconds) {
            this.ip = ip;
            this.expiryTime = System.currentTimeMillis() + ttlSeconds * 1000;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    private int capacity;
    private Map<String, Entry> cache;
    private int hits = 0;
    private int misses = 0;

    public week1and2(int capacity) {

        this.capacity = capacity;

        cache = new LinkedHashMap<String, Entry>(capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, Entry> eldest) {
                return size() > week1and2.this.capacity;
            }
        };
    }

    // Add domain
    public void put(String domain, String ip, int ttlSeconds) {
        cache.put(domain, new Entry(ip, ttlSeconds));
    }

    // Get IP
    public String get(String domain) {

        Entry entry = cache.get(domain);

        if (entry == null) {
            misses++;
            return queryUpstreamDNS(domain);
        }

        if (entry.isExpired()) {
            cache.remove(domain);
            misses++;
            return queryUpstreamDNS(domain);
        }

        hits++;
        return entry.ip;
    }

    // Simulate upstream DNS lookup
    private String queryUpstreamDNS(String domain) {

        String fakeIP = "192.168.1." + new Random().nextInt(255);
        System.out.println("Querying upstream DNS for " + domain);

        put(domain, fakeIP, 5); // TTL 5 seconds
        return fakeIP;
    }

    // Cache statistics
    public void printStats() {
        System.out.println("Cache Hits: " + hits);
        System.out.println("Cache Misses: " + misses);

        int total = hits + misses;
        if (total > 0) {
            System.out.println("Hit Ratio: " + (hits * 100.0 / total) + "%");
        }
    }

    public static void main(String[] args) throws InterruptedException {

        week1and2 dnsCache = new week1and2(3);

        System.out.println(dnsCache.get("google.com"));
        System.out.println(dnsCache.get("google.com")); // hit

        Thread.sleep(6000); // TTL expire

        System.out.println(dnsCache.get("google.com")); // miss again

        dnsCache.printStats();
    }
}