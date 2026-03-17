import java.util.*;

public class week1and2 {

    // Store registered usernames
    private Set<String> usernames;

    // Track frequency of attempted usernames
    private Map<String, Integer> attemptCount;

    public week1and2() {
        usernames = new HashSet<>();
        attemptCount = new HashMap<>();
    }

    // Register username
    public void registerUsername(String username) {
        usernames.add(username.toLowerCase());
    }

    // Check availability
    public boolean isAvailable(String username) {
        username = username.toLowerCase();

        // Track popularity
        attemptCount.put(username, attemptCount.getOrDefault(username, 0) + 1);

        return !usernames.contains(username);
    }

    // Suggest similar usernames
    public List<String> suggestUsernames(String username) {
        List<String> suggestions = new ArrayList<>();
        username = username.toLowerCase();

        // Simple suggestions with numbers
        for (int i = 1; i <= 5; i++) {
            String suggestion = username + i;

            if (!usernames.contains(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        // Random suggestions if needed
        Random rand = new Random();
        while (suggestions.size() < 5) {
            String suggestion = username + rand.nextInt(1000);
            if (!usernames.contains(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        return suggestions;
    }

    // Get attempt count
    public int getAttemptCount(String username) {
        return attemptCount.getOrDefault(username.toLowerCase(), 0);
    }

    public static void main(String[] args) {

        week1and2 system = new week1and2();

        // Existing usernames
        system.registerUsername("alex");
        system.registerUsername("john");
        system.registerUsername("emma");

        String checkName = "alex";

        if (system.isAvailable(checkName)) {
            System.out.println("Username available!");
        } else {
            System.out.println("Username taken.");
            System.out.println("Suggestions: " + system.suggestUsernames(checkName));
        }

        System.out.println("Attempt count: " + system.getAttemptCount(checkName));
    }
}
