package main.java.com.service;

import main.java.com.domain.Friendship;
import main.java.com.exceptions.ValidationException;
import main.java.com.repo.AbstractRepository;
import main.java.com.validators.FriendshipValidator;

import java.util.*;

public class FriendshipService extends AbstractService<String, Friendship>{
    private final FriendshipValidator friendshipValidator = new FriendshipValidator();

    public FriendshipService(AbstractRepository<String, Friendship> repository, AbstractRepository<?, ?> userRepository) {
        super(repository, userRepository);
    }

    public void add(long userId1, long userId2){
        Friendship friendship = new Friendship(userId1, userId2);
        friendshipValidator.validate(friendship);

        if(dependencyRepository != null){
            if(!dependencyRepository.getKeys().contains(userId1) || !dependencyRepository.getKeys().contains(userId2))
                throw new ValidationException("User with an id cannot be found");
        }

        repository.add(friendship.getFriendshipId(),friendship);
    }

    public void remove(long userId1, long userId2){
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

        // initialize from user repo
        if (dependencyRepository != null) {
            for (Object key : dependencyRepository.getKeys()) {
                if (key instanceof Long id) graph.put(id, new HashSet<>());
            }
        }

        // connect friendships
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
}
