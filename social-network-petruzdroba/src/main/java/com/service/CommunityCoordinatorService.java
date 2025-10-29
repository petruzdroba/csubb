//package main.java.com.service;
//
//import main.java.com.domain.Card;
//import main.java.com.domain.User;
//
//import java.util.*;
//
//public class CommunityCoordinatorService extends AbstractCoordinatorService<Object, Object>{
//    public CommunityCoordinatorService(AbstractService<?, ?> serviceA, AbstractService<?, ?> serviceB) {
//        super(serviceA, serviceB);
//    }
//
//    public Card getLargestDiameterCommunityCard(long cardId, String cardName) {
//        UserService userService = (UserService) serviceA;
//        FriendshipService friendshipService = (FriendshipService) serviceB;
//
//        var users = userService.getAll();
//        var friendships = friendshipService.getAll();
//
//        // Build adjacency map
//        Map<Long, Set<Long>> graph = new HashMap<>();
//        for (User u : users) graph.put(u.getId(), new HashSet<>());
//        for (var f : friendships) {
//            graph.get(f.getUserId1()).add(f.getUserId2());
//            graph.get(f.getUserId2()).add(f.getUserId1());
//        }
//
//        Set<Long> visited = new HashSet<>();
//        List<Set<Long>> components = new ArrayList<>();
//
//        // Find connected components
//        for (Long userId : graph.keySet()) {
//            if (!visited.contains(userId)) {
//                Set<Long> component = new HashSet<>();
//                Queue<Long> q = new LinkedList<>();
//                q.add(userId);
//                visited.add(userId);
//
//                while (!q.isEmpty()) {
//                    Long current = q.poll();
//                    component.add(current);
//                    for (Long neighbor : graph.get(current)) {
//                        if (!visited.contains(neighbor)) {
//                            visited.add(neighbor);
//                            q.add(neighbor);
//                        }
//                    }
//                }
//                components.add(component);
//            }
//        }
//
//        // Compute diameters and pick largest
//        Set<Long> largestDiameterComponent = null;
//        int maxDiameter = -1;
//
//        for (Set<Long> component : components) {
//            int diameter = computeDiameter(component, graph);
//            if (diameter > maxDiameter) {
//                maxDiameter = diameter;
//                largestDiameterComponent = component;
//            }
//        }
//
//        // Create Card for this community
//        Card communityCard = new Card(cardId, cardName);
//        for (Long userId : largestDiameterComponent) {
//            communityCard.addDuck(userId); // assuming addDuck adds member IDs
//        }
//
//        return communityCard;
//    }
//
//    // Compute diameter same as before
//    private int computeDiameter(Set<Long> component, Map<Long, Set<Long>> graph) {
//        int diameter = 0;
//        for (Long node : component) {
//            Map<Long, Integer> dist = new HashMap<>();
//            Queue<Long> q = new LinkedList<>();
//            q.add(node);
//            dist.put(node, 0);
//
//            while (!q.isEmpty()) {
//                Long current = q.poll();
//                for (Long neighbor : graph.get(current)) {
//                    if (!dist.containsKey(neighbor)) {
//                        dist.put(neighbor, dist.get(current) + 1);
//                        q.add(neighbor);
//                    }
//                }
//            }
//
//            int localMax = dist.values().stream().mapToInt(i -> i).max().orElse(0);
//            if (localMax > diameter) diameter = localMax;
//        }
//        return diameter;
//    }
//
//}
