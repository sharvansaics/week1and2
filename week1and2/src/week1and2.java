import java.util.*;

public class week1and2 {

    // Trie Node
    class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEndOfWord;
        Set<String> words = new HashSet<>(); // store words passing through this node
    }

    private TrieNode root;
    private Map<String, Integer> frequencyMap;

    public week1and2() {
        root = new TrieNode();
        frequencyMap = new HashMap<>();
    }

    // Add query to system
    public void addQuery(String query) {
        frequencyMap.put(query, frequencyMap.getOrDefault(query, 0) + 1);

        TrieNode node = root;
        for (char ch : query.toCharArray()) {
            node.children.putIfAbsent(ch, new TrieNode());
            node = node.children.get(ch);
            node.words.add(query);
        }
        node.isEndOfWord = true;
    }

    // Get top k suggestions for prefix
    public List<String> getSuggestions(String prefix, int k) {
        TrieNode node = root;
        for (char ch : prefix.toCharArray()) {
            if (!node.children.containsKey(ch)) return new ArrayList<>();
            node = node.children.get(ch);
        }

        PriorityQueue<String> pq = new PriorityQueue<>(
                (a, b) -> frequencyMap.get(a).equals(frequencyMap.get(b))
                        ? a.compareTo(b)
                        : frequencyMap.get(a) - frequencyMap.get(b)
        );

        for (String word : node.words) {
            pq.offer(word);
            if (pq.size() > k) pq.poll();
        }

        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) result.add(pq.poll());

        Collections.reverse(result);
        return result;
    }

    // Example typo correction: return closest by edit distance
    public String suggestCorrection(String query) {
        int minDist = Integer.MAX_VALUE;
        String closest = query;

        for (String q : frequencyMap.keySet()) {
            int dist = editDistance(query, q);
            if (dist < minDist) {
                minDist = dist;
                closest = q;
            }
        }
        return closest;
    }

    // Compute Levenshtein distance
    private int editDistance(String a, String b) {
        int m = a.length(), n = b.length();
        int[][] dp = new int[m+1][n+1];

        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (a.charAt(i-1) == b.charAt(j-1)) dp[i][j] = dp[i-1][j-1];
                else dp[i][j] = 1 + Math.min(dp[i-1][j-1],
                        Math.min(dp[i-1][j], dp[i][j-1]));
            }
        }
        return dp[m][n];
    }

    public static void main(String[] args) {
        week1and2 autocomplete = new week1and2();

        // Add sample queries
        autocomplete.addQuery("machine learning");
        autocomplete.addQuery("machine learning");
        autocomplete.addQuery("machine");
        autocomplete.addQuery("math tricks");
        autocomplete.addQuery("math games");
        autocomplete.addQuery("mars rover");
        autocomplete.addQuery("martin luther king");

        System.out.println("Suggestions for 'ma': " + autocomplete.getSuggestions("ma", 5));
        System.out.println("Suggestions for 'mach': " + autocomplete.getSuggestions("mach", 5));
        System.out.println("Correction for 'mashine learning': " + autocomplete.suggestCorrection("mashine learning"));
    }
}