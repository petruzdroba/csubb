package main.java.com.service;

import main.java.com.domain.Friendship;
import main.java.com.domain.User;
import main.java.com.exceptions.ValidationException;
import main.java.com.repo.AbstractRepository;
import main.java.com.validators.FriendshipValidator;

import java.util.*;

public class FriendshipService extends AbstractService<String, Friendship> {
    private final FriendshipValidator friendshipValidator = new FriendshipValidator();

    public FriendshipService(AbstractRepository<String, Friendship> repository, AbstractRepository<?, ?> userRepository) {
        super(repository, userRepository);
    }

    public void add(long userId1, long userId2) {
        Friendship friendship = new Friendship(userId1, userId2);
        friendshipValidator.validate(friendship);

        if (dependencyRepository != null) {
            if (!dependencyRepository.getKeys().contains(userId1) || !dependencyRepository.getKeys().contains(userId2))
                throw new ValidationException("User with an id cannot be found");
        }

        repository.add(friendship.getFriendshipId(), friendship);
    }

    public void remove(long userId1, long userId2) {
        Friendship friendship = new Friendship(userId1, userId2);
        friendshipValidator.validate(friendship);

        repository.remove(friendship.getFriendshipId());
    }

    public int getCommunityCount() {
        Map<Long, Set<Long>> graph = buildGraph();
        Set<Long> visited = new HashSet<>();
        int count = 0;

        for (Long userId : graph.keySet()) {
            if (!visited.contains(userId)) {
                bfs(userId, graph, visited);
                count++;
            }
        }
        return count;
    }

    private Map<Long, Set<Long>> buildGraph() {
        Map<Long, Set<Long>> graph = new HashMap<>();

        if (dependencyRepository != null) {
            for (Object key : dependencyRepository.getKeys()) {//using UserService to get access to all user keys
                if (key instanceof Long id) graph.put(id, new HashSet<>());
            }
        }

        for (Friendship f : repository.getAll()) {
            graph.putIfAbsent(f.getUserId1(), new HashSet<>());
            graph.putIfAbsent(f.getUserId2(), new HashSet<>());
            graph.get(f.getUserId1()).add(f.getUserId2());
            graph.get(f.getUserId2()).add(f.getUserId1());
        }

        return graph;
    }

    private void bfs(Long start, Map<Long, Set<Long>> graph, Set<Long> visited) {
        Queue<Long> q = new LinkedList<>();
        q.add(start);
        visited.add(start);

        while (!q.isEmpty()) {
            Long current = q.poll();
            for (Long neighbor : graph.getOrDefault(current, Collections.emptySet())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    q.add(neighbor);
                }
            }
        }
    }

    public Set<User> getMostSociableCommunity() {
        Map<Long, Set<Long>> graph = buildGraph();
        Set<Long> visited = new HashSet<>();

        Set<Long> bestComponent = new HashSet<>();
        int maxDiameter = -1;

        for (Long userId : graph.keySet()) {
            if (!visited.contains(userId)) {
                Set<Long> component = bfsCollect(userId, graph, visited);
                int diameter = computeDiameter(component, graph);
                if (diameter > maxDiameter) {
                    maxDiameter = diameter;
                    bestComponent = component;
                }
            }
        }

        Set<User> result = new HashSet<>();
        if (dependencyRepository != null) {
            AbstractRepository<Long, User> userRepo = (AbstractRepository<Long, User>) dependencyRepository;

            for (Long userId : bestComponent) {
                User user = userRepo.find(userId);
                if (user != null) result.add(user);
            }

        }

        return result;
    }


    private Set<Long> bfsCollect(Long start, Map<Long, Set<Long>> graph, Set<Long> visited) {
        Set<Long> component = new HashSet<>();
        Queue<Long> q = new LinkedList<>();
        q.add(start);
        visited.add(start);

        while (!q.isEmpty()) {
            Long current = q.poll();
            component.add(current);
            for (Long neighbor : graph.getOrDefault(current, Collections.emptySet())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    q.add(neighbor);
                }
            }
        }
        return component;
    }

    private int computeDiameter(Set<Long> component, Map<Long, Set<Long>> graph) {
        int diameter = 0;
        for (Long node : component) {
            int localMax = bfsDistance(node, graph);
            diameter = Math.max(diameter, localMax);
        }
        return diameter;
    }

    private int bfsDistance(Long start, Map<Long, Set<Long>> graph) {
        Map<Long, Integer> dist = new HashMap<>();
        Queue<Long> q = new LinkedList<>();
        q.add(start);
        dist.put(start, 0);

        while (!q.isEmpty()) {
            Long current = q.poll();
            for (Long neighbor : graph.getOrDefault(current, Collections.emptySet())) {
                if (!dist.containsKey(neighbor)) {
                    dist.put(neighbor, dist.get(current) + 1);
                    q.add(neighbor);
                }
            }
        }

        return dist.values().stream().mapToInt(i -> i).max().orElse(0);
    }

    public Collection<Map.Entry<User, User>> getAllPretty() {
        List<Map.Entry<User, User>> prettyList = new ArrayList<>();

        if (dependencyRepository == null)
            throw new IllegalStateException("User repository not linked to FriendshipService.");

        @SuppressWarnings("unchecked")
        AbstractRepository<Long, User> userRepo = (AbstractRepository<Long, User>) dependencyRepository;

        for (Friendship f : repository.getAll()) {
            User user1 = userRepo.find(f.getUserId1());
            User user2 = userRepo.find(f.getUserId2());

            if (user1 != null && user2 != null) {
                prettyList.add(new AbstractMap.SimpleEntry<>(user1, user2));
            } else {
                System.out.println("Warning: friendship references missing user(s): " + f);
            }
        }

        return prettyList;
    }

}
