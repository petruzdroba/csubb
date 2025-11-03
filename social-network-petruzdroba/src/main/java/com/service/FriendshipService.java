package main.java.com.service;

import main.java.com.domain.Friendship;
import main.java.com.domain.User;
import main.java.com.exceptions.RepositoryException;
import main.java.com.exceptions.ValidationException;
import main.java.com.repo.AbstractRepository;
import main.java.com.repo.UserRepository;
import main.java.com.validators.FriendshipValidator;

import java.util.*;

public class FriendshipService extends AbstractService<String, Friendship> {
    private final FriendshipValidator friendshipValidator = new FriendshipValidator();
    private final UserRepository userRepository;

    public FriendshipService(AbstractRepository<String, Friendship> repository, UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
    }

    /**
     * Adauga o prietenie{@link Friendship} intre doi utilizatori.
     * <p>
     * Creeaza un obiect Friendship si il valideaza. Daca exista repository de utilizatori,
     * verifica ca ambele id-uri sa existe inainte de a adauga prietenia.
     *
     * @param userId1 Id-ul primului utilizator.
     * @param userId2 Id-ul celui de-al doilea utilizator.
     * @throws ValidationException Daca vreun id nu exista in repository-ul de utilizatori, sau id uri negative
     * @throws RepositoryException daca exista o prietenie deja intre utilizatori
     * @see FriendshipValidator
     * @see AbstractRepository#add(Object, Object)
     */
    public void add(long userId1, long userId2) {
        Friendship friendship = new Friendship(userId1, userId2);
        friendshipValidator.validate(friendship);

        if (!userRepository.getKeys().contains(userId1) || !userRepository.getKeys().contains(userId2))
            throw new ValidationException("User with an id cannot be found");

        repository.add(friendship.getFriendshipId(), friendship);
    }

    /**
     * Sterge o prietenie {@link Friendship} intre doi utilizatori.
     * <p>
     * Creeaza un obiect Friendship -> pentru friendship Id, apoi il sterge din repository.
     *
     * @param userId1 Id-ul primului utilizator.
     * @param userId2 Id-ul celui de-al doilea utilizator.
     * @throws ValidationException daca oricare dintre user id sunt negative
     * @throws RepositoryException daca utilizatorii nu sunt priteteni
     * @see FriendshipValidator
     * @see AbstractRepository#remove(Object)
     */
    public void remove(long userId1, long userId2) {
        Friendship friendship = new Friendship(userId1, userId2);
        friendshipValidator.validate(friendship);

        repository.remove(friendship.getFriendshipId());
    }

    /**
     * Returneaza numarul de comunitati din retea.
     * <p>
     * O comunitate este definita ca un component conectat in graful prieteniilor.
     *
     * @return Numarul de comunitati.
     * @see #buildGraph() pentru construirea grafului de prietenii.
     * @see #bfs(Long, Map, Set) pentru parcurgerea componentelor conectate.
     */
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

        for (Object key : userRepository.getKeys()) {//using UserService to get access to all user keys
            if (key instanceof Long id) graph.put(id, new HashSet<>());
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

    /**
     * Returneaza comunitatea cea mai sociabila.
     * <p>
     * Cauta toate comunitatile si returneaza setul de utilizatori {@link User} din componenta
     * cu diametru maxim (cel mai sociabil grup).
     *
     * @return Set de utilizatori din comunitatea cea mai sociabila.
     * @see #buildGraph()
     * @see #bfsCollect(Long, Map, Set)
     * @see #computeDiameter(Set, Map)
     */
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

        for (Long userId : bestComponent) {
            User user = userRepository.find(userId);
            if (user != null) result.add(user);
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

    /**
     * Returneaza toate prieteniile sub forma de perechi de utilizatori {@link User}.
     * <p>
     * Creeaza o colectie cu toate prieteniile, fiecare reprezentata ca o pereche de obiecte User.
     * Afiseaza un mesaj de warning daca vreun utilizator lipseste.
     *
     * @return Colectie de perechi User-User reprezentand prieteniile.
     * @throws IllegalStateException Daca repository-ul de utilizatori nu este conectat.
     */
    public Collection<Map.Entry<User, User>> getAllPretty() {
        List<Map.Entry<User, User>> prettyList = new ArrayList<>();



        for (Friendship f : repository.getAll()) {
            User user1 = userRepository.find(f.getUserId1());
            User user2 = userRepository.find(f.getUserId2());

            if (user1 != null && user2 != null) {
                prettyList.add(new AbstractMap.SimpleEntry<>(user1, user2));
            } else {
                System.out.println("Warning: friendship references missing user(s): " + f);
            }
        }

        return prettyList;
    }

}
