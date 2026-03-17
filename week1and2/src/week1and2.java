import java.util.*;

public class week1and2 {

    // Video object
    static class Video {
        String id;
        String content; // simplified for example

        Video(String id, String content) {
            this.id = id;
            this.content = content;
        }
    }

    // LRU Cache implementation
    static class LRUCache {
        private final int capacity;
        private LinkedHashMap<String, Video> map;

        public LRUCache(int capacity) {
            this.capacity = capacity;
            this.map = new LinkedHashMap<String, Video>(capacity, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, Video> eldest) {
                    return size() > capacity;
                }
            };
        }

        public Video get(String key) {
            return map.getOrDefault(key, null);
        }

        public void put(String key, Video value) {
            map.put(key, value);
        }

        public boolean contains(String key) {
            return map.containsKey(key);
        }

        public int size() {
            return map.size();
        }
    }

    // Multi-Level Cache System
    private LRUCache l1; // in-memory
    private LRUCache l2; // SSD (simulated with another LinkedHashMap)
    private Map<String, Video> l3; // Database
    private int l1Hits = 0, l2Hits = 0, l3Hits = 0, totalRequests = 0;

    public week1and2(int l1Capacity, int l2Capacity, Map<String, Video> database) {
        l1 = new LRUCache(l1Capacity);
        l2 = new LRUCache(l2Capacity);
        l3 = database;
    }

    // Fetch video from multi-level cache
    public Video fetchVideo(String videoId) {
        totalRequests++;

        // Check L1
        Video video = l1.get(videoId);
        if (video != null) {
            l1Hits++;
            return video;
        }

        // Check L2
        video = l2.get(videoId);
        if (video != null) {
            l2Hits++;
            // Promote to L1
            l1.put(videoId, video);
            return video;
        }

        // Check L3
        video = l3.get(videoId);
        if (video != null) {
            l3Hits++;
            // Promote to L2 and L1
            l2.put(videoId, video);
            l1.put(videoId, video);
            return video;
        }

        // Video not found
        return null;
    }

    // Update video content (invalidate caches)
    public void updateVideo(String videoId, String newContent) {
        Video updated = new Video(videoId, newContent);
        l3.put(videoId, updated);
        if (l1.contains(videoId)) l1.put(videoId, updated);
        if (l2.contains(videoId)) l2.put(videoId, updated);
    }

    // Show cache hit ratios
    public void showCacheStats() {
        System.out.printf("Total Requests: %d%n", totalRequests);
        System.out.printf("L1 Hit Ratio: %.2f%%%n", l1Hits * 100.0 / totalRequests);
        System.out.printf("L2 Hit Ratio: %.2f%%%n", l2Hits * 100.0 / totalRequests);
        System.out.printf("L3 Hit Ratio: %.2f%%%n", l3Hits * 100.0 / totalRequests);
    }

    public static void main(String[] args) {
        // Simulate database with 1M videos
        Map<String, Video> database = new HashMap<>();
        for (int i = 1; i <= 1000; i++) {
            database.put("V" + i, new Video("V" + i, "Content of Video " + i));
        }

        week1and2 cacheSystem = new week1and2(5, 10, database);

        // Simulate requests
        cacheSystem.fetchVideo("V1");
        cacheSystem.fetchVideo("V2");
        cacheSystem.fetchVideo("V3");
        cacheSystem.fetchVideo("V1");
        cacheSystem.fetchVideo("V4");
        cacheSystem.fetchVideo("V5");
        cacheSystem.fetchVideo("V6"); // causes L1 eviction
        cacheSystem.fetchVideo("V1"); // promoted back
        cacheSystem.fetchVideo("V2");
        cacheSystem.fetchVideo("V7");

        // Update video content
        cacheSystem.updateVideo("V3", "Updated Content V3");

        cacheSystem.showCacheStats();
    }
}