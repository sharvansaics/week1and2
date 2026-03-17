import java.util.*;

public class week1and2 {

    // Page visit counts
    private Map<String, Integer> pageVisits = new HashMap<>();

    // Unique visitors per page
    private Map<String, Set<String>> uniqueVisitors = new HashMap<>();

    // Traffic source tracking
    private Map<String, Integer> trafficSources = new HashMap<>();

    // Location tracking
    private Map<String, Integer> locations = new HashMap<>();

    // Process page view event
    public void processEvent(String userId, String pageUrl,
                             String source, String location) {

        // Update page visits
        pageVisits.put(pageUrl,
                pageVisits.getOrDefault(pageUrl, 0) + 1);

        // Update unique visitors
        uniqueVisitors.putIfAbsent(pageUrl, new HashSet<>());
        uniqueVisitors.get(pageUrl).add(userId);

        // Update traffic source
        trafficSources.put(source,
                trafficSources.getOrDefault(source, 0) + 1);

        // Update location count
        locations.put(location,
                locations.getOrDefault(location, 0) + 1);
    }

    // Get top 10 pages
    public List<String> getTopPages() {

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());

        pq.addAll(pageVisits.entrySet());

        List<String> topPages = new ArrayList<>();

        for (int i = 0; i < 10 && !pq.isEmpty(); i++) {
            topPages.add(pq.poll().getKey());
        }

        return topPages;
    }

    // Dashboard update
    public void showDashboard() {

        System.out.println("Top Pages: " + getTopPages());

        System.out.println("Traffic Sources: " + trafficSources);

        System.out.println("Locations: " + locations);

        System.out.println("Unique Visitors Per Page:");
        for (String page : uniqueVisitors.keySet()) {
            System.out.println(page + " : " + uniqueVisitors.get(page).size());
        }
    }

    public static void main(String[] args) {

        week1and2 dashboard =
                new week1and2();

        // Simulated traffic events
        dashboard.processEvent("U1", "/home", "Google", "India");
        dashboard.processEvent("U2", "/sports", "Facebook", "USA");
        dashboard.processEvent("U3", "/home", "Direct", "India");
        dashboard.processEvent("U4", "/tech", "Google", "UK");
        dashboard.processEvent("U1", "/sports", "Google", "India");

        dashboard.showDashboard();
    }
}