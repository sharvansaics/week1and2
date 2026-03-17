import java.util.*;

public class week1and2 {

    class TokenBucket {
        int tokens;
        long lastRefillTime;

        TokenBucket(int capacity) {
            this.tokens = capacity;
            this.lastRefillTime = System.currentTimeMillis();
        }
    }

    private Map<String, TokenBucket> clients = new HashMap<>();

    private final int MAX_TOKENS = 1000;
    private final long REFILL_TIME = 3600000; // 1 hour

    public synchronized boolean allowRequest(String clientId) {

        clients.putIfAbsent(clientId, new TokenBucket(MAX_TOKENS));

        TokenBucket bucket = clients.get(clientId);

        refillTokens(bucket);

        if (bucket.tokens > 0) {
            bucket.tokens--;
            return true;
        }

        return false;
    }

    private void refillTokens(TokenBucket bucket) {

        long currentTime = System.currentTimeMillis();

        if (currentTime - bucket.lastRefillTime >= REFILL_TIME) {
            bucket.tokens = MAX_TOKENS;
            bucket.lastRefillTime = currentTime;
        }
    }

    public static void main(String[] args) {

        week1and2 limiter = new week1and2();

        String client = "Client1";

        for (int i = 0; i < 1005; i++) {

            if (limiter.allowRequest(client)) {
                System.out.println("Request " + (i + 1) + " allowed");
            } else {
                System.out.println("Rate limit exceeded for " + client);
            }
        }
    }
}