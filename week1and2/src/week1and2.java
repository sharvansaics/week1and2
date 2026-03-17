import java.util.*;

public class FlashSaleInventoryManager {

    // Product stock storage
    private Map<String, Integer> stock;

    // Waiting list for each product
    private Map<String, Queue<String>> waitingList;

    public FlashSaleInventoryManager() {
        stock = new HashMap<>();
        waitingList = new HashMap<>();
    }

    // Add product with stock
    public void addProduct(String productId, int quantity) {
        stock.put(productId, quantity);
        waitingList.put(productId, new LinkedList<>());
    }

    // Check stock availability
    public boolean isAvailable(String productId) {
        return stock.getOrDefault(productId, 0) > 0;
    }

    // Purchase request
    public synchronized void purchase(String userId, String productId) {

        int currentStock = stock.getOrDefault(productId, 0);

        if (currentStock > 0) {
            stock.put(productId, currentStock - 1);
            System.out.println(userId + " successfully purchased " + productId);
        } else {
            waitingList.get(productId).add(userId);
            System.out.println(userId + " added to waiting list for " + productId);
        }
    }

    // Restock product and serve waiting list
    public synchronized void restock(String productId, int quantity) {

        int newStock = stock.getOrDefault(productId, 0) + quantity;
        stock.put(productId, newStock);

        Queue<String> queue = waitingList.get(productId);

        while (stock.get(productId) > 0 && !queue.isEmpty()) {
            String user = queue.poll();
            stock.put(productId, stock.get(productId) - 1);
            System.out.println(user + " purchased from waiting list: " + productId);
        }
    }

    // Display stock
    public void showStock(String productId) {
        System.out.println("Stock for " + productId + ": " + stock.getOrDefault(productId, 0));
    }

    public static void main(String[] args) {

        FlashSaleInventoryManager manager = new FlashSaleInventoryManager();

        manager.addProduct("Laptop", 3);

        manager.purchase("User1", "Laptop");
        manager.purchase("User2", "Laptop");
        manager.purchase("User3", "Laptop");
        manager.purchase("User4", "Laptop");
        manager.purchase("User5", "Laptop");

        manager.showStock("Laptop");

        manager.restock("Laptop", 2);
    }
}