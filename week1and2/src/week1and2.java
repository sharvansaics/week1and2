import java.util.*;

public class week1and2 {

    // Transaction class
    static class Transaction {
        String id;
        String account;
        String merchant;
        double amount;
        long timestamp; // in milliseconds

        Transaction(String id, String account, String merchant, double amount, long timestamp) {
            this.id = id;
            this.account = account;
            this.merchant = merchant;
            this.amount = amount;
            this.timestamp = timestamp;
        }
    }

    // 1. Classic Two-Sum
    public static List<List<Transaction>> twoSum(List<Transaction> transactions, double target) {
        Map<Double, Transaction> map = new HashMap<>();
        List<List<Transaction>> result = new ArrayList<>();

        for (Transaction t : transactions) {
            double complement = target - t.amount;
            if (map.containsKey(complement)) {
                result.add(Arrays.asList(map.get(complement), t));
            }
            map.put(t.amount, t);
        }
        return result;
    }

    // 2. Two-Sum with time window (e.g., 1 hour)
    public static List<List<Transaction>> twoSumTimeWindow(List<Transaction> transactions, double target, long windowMs) {
        Map<Double, List<Transaction>> map = new HashMap<>();
        List<List<Transaction>> result = new ArrayList<>();

        for (Transaction t : transactions) {
            double complement = target - t.amount;
            if (map.containsKey(complement)) {
                for (Transaction candidate : map.get(complement)) {
                    if (Math.abs(candidate.timestamp - t.timestamp) <= windowMs) {
                        result.add(Arrays.asList(candidate, t));
                    }
                }
            }
            map.putIfAbsent(t.amount, new ArrayList<>());
            map.get(t.amount).add(t);
        }
        return result;
    }

    // 3. K-Sum (using recursion)
    public static List<List<Transaction>> kSum(List<Transaction> transactions, int k, double target) {
        List<List<Transaction>> result = new ArrayList<>();
        kSumHelper(transactions, k, 0, target, new ArrayList<>(), result);
        return result;
    }

    private static void kSumHelper(List<Transaction> transactions, int k, int start, double target,
                                   List<Transaction> current, List<List<Transaction>> result) {
        if (k == 2) {
            // Classic two-sum for remaining transactions
            Map<Double, Transaction> map = new HashMap<>();
            for (int i = start; i < transactions.size(); i++) {
                Transaction t = transactions.get(i);
                double complement = target - t.amount;
                if (map.containsKey(complement)) {
                    List<Transaction> temp = new ArrayList<>(current);
                    temp.add(map.get(complement));
                    temp.add(t);
                    result.add(temp);
                }
                map.put(t.amount, t);
            }
            return;
        }

        for (int i = start; i <= transactions.size() - k; i++) {
            current.add(transactions.get(i));
            kSumHelper(transactions, k - 1, i + 1, target - transactions.get(i).amount, current, result);
            current.remove(current.size() - 1);
        }
    }

    // 4. Duplicate detection: same amount, same merchant, different accounts
    public static List<List<Transaction>> detectDuplicates(List<Transaction> transactions) {
        Map<String, List<Transaction>> map = new HashMap<>();
        List<List<Transaction>> result = new ArrayList<>();

        for (Transaction t : transactions) {
            String key = t.amount + "_" + t.merchant;
            if (map.containsKey(key)) {
                for (Transaction prev : map.get(key)) {
                    if (!prev.account.equals(t.account)) {
                        result.add(Arrays.asList(prev, t));
                    }
                }
            }
            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t);
        }
        return result;
    }

    // Example usage
    public static void main(String[] args) {
        long now = System.currentTimeMillis();
        List<Transaction> transactions = Arrays.asList(
                new Transaction("T1", "A1", "Amazon", 100, now),
                new Transaction("T2", "A2", "Amazon", 200, now + 1000),
                new Transaction("T3", "A3", "Walmart", 150, now + 2000),
                new Transaction("T4", "A4", "Amazon", 100, now + 3000),
                new Transaction("T5", "A5", "Amazon", 200, now + 3500)
        );

        System.out.println("Classic Two-Sum (target=300):");
        for (List<Transaction> pair : twoSum(transactions, 300)) {
            System.out.println(pair.get(0).id + " + " + pair.get(1).id);
        }

        System.out.println("\nTwo-Sum with 1 hour window (target=300):");
        for (List<Transaction> pair : twoSumTimeWindow(transactions, 300, 3600 * 1000)) {
            System.out.println(pair.get(0).id + " + " + pair.get(1).id);
        }

        System.out.println("\nK-Sum k=3, target=450:");
        for (List<Transaction> triplet : kSum(transactions, 3, 450)) {
            System.out.println(triplet.get(0).id + " + " + triplet.get(1).id + " + " + triplet.get(2).id);
        }

        System.out.println("\nDuplicate detection (same amount, same merchant, different accounts):");
        for (List<Transaction> dup : detectDuplicates(transactions)) {
            System.out.println(dup.get(0).id + " & " + dup.get(1).id);
        }
    }
}