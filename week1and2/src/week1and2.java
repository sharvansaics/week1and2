import java.util.*;

public class PlagiarismDetector {

    private Map<String, List<String>> nGramIndex = new HashMap<>();
    private Map<String, String> documents = new HashMap<>();

    private int n = 3; // n-gram size

    // Add document to system
    public void addDocument(String docId, String text) {

        documents.put(docId, text);

        List<String> ngrams = generateNGrams(text);

        for (String gram : ngrams) {

            nGramIndex.putIfAbsent(gram, new ArrayList<>());
            nGramIndex.get(gram).add(docId);
        }
    }

    // Generate n-grams
    private List<String> generateNGrams(String text) {

        List<String> grams = new ArrayList<>();

        String[] words = text.toLowerCase().split("\\s+");

        for (int i = 0; i <= words.length - n; i++) {

            String gram = "";

            for (int j = 0; j < n; j++) {
                gram += words[i + j] + " ";
            }

            grams.add(gram.trim());
        }

        return grams;
    }

    // Check plagiarism
    public void checkDocument(String docId, String text) {

        List<String> ngrams = generateNGrams(text);

        Map<String, Integer> matchCount = new HashMap<>();

        for (String gram : ngrams) {

            if (nGramIndex.containsKey(gram)) {

                for (String existingDoc : nGramIndex.get(gram)) {

                    matchCount.put(existingDoc,
                            matchCount.getOrDefault(existingDoc, 0) + 1);
                }
            }
        }

        for (String doc : matchCount.keySet()) {

            int matches = matchCount.get(doc);

            double similarity = (matches * 100.0) / ngrams.size();

            System.out.println("Similarity with " + doc + " = " + similarity + "%");
        }
    }

    public static void main(String[] args) {

        PlagiarismDetector detector = new PlagiarismDetector();

        detector.addDocument("Doc1",
                "machine learning is very powerful technology");

        detector.addDocument("Doc2",
                "artificial intelligence and machine learning are related");

        detector.checkDocument("NewDoc",
                "machine learning is powerful technology");
    }
}