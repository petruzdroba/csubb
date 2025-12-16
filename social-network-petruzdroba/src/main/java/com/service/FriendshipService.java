package com.service;

import com.domain.Friendship;
import com.domain.User;
import com.exceptions.NotLoggedIn;
import com.exceptions.RepositoryException;
import com.exceptions.ValidationException;
import com.repo.AbstractDatabaseRepository;
import com.repo.AbstractRepository;
import com.repo.FriendshipRepository;
import com.repo.UserRepository;
import com.validators.FriendshipValidator;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FriendshipService extends AbstractService<String, Friendship> {
    private final FriendshipValidator friendshipValidator = new FriendshipValidator();
    private final UserRepository userRepository;

    public FriendshipService(AbstractDatabaseRepository<String, Friendship> repository, UserRepository userRepository) {
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
    public void add(long userId1, long userId2) throws SQLException {
        Friendship friendship = new Friendship(userId1, userId2);
        friendshipValidator.validate(friendship);

        if (!userRepository.getKeys().contains(userId1) || !userRepository.getKeys().contains(userId2))
            throw new ValidationException("User with an id cannot be found");

        repository.add(friendship.getFriendshipId(), friendship);
        notifyObservers();
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
    public void remove(long userId1, long userId2) throws SQLException {
        Friendship friendship = new Friendship(userId1, userId2);
        friendshipValidator.validate(friendship);

        repository.remove(friendship.getFriendshipId());
        notifyObservers();
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
    public Set<User> getMostSociableCommunity() throws RepositoryException {
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
            try {
                User user = userRepository.find(userId);
                if (user != null) result.add(user);
            } catch (SQLException e) {
                throw new RepositoryException(e.getMessage());
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
            try {
                User user1 = userRepository.find(f.getUserId1());
                User user2 = userRepository.find(f.getUserId2());

                if (user1 != null && user2 != null) {
                    prettyList.add(new AbstractMap.SimpleEntry<>(user1, user2));
                } else {
                    System.out.println("Warning: friendship references missing user(s): " + f);
                }
            } catch (SQLException e) {
                throw new RepositoryException(e.getMessage());
            }
        }

        return prettyList;
    }

    public Collection<Map.Entry<User, User>> getAllPretty(int offset, int limit) {
        List<Map.Entry<User, User>> prettyList = new ArrayList<>();


        for (Friendship f : repository.getPage(offset, limit)) {
            try {
                User user1 = userRepository.find(f.getUserId1());
                User user2 = userRepository.find(f.getUserId2());

                if (user1 != null && user2 != null) {
                    prettyList.add(new AbstractMap.SimpleEntry<>(user1, user2));
                } else {
                    System.out.println("Warning: friendship references missing user(s): " + f);
                }
            } catch (SQLException e) {
                throw new RepositoryException(e.getMessage());
            }
        }

        return prettyList;
    }


    public int pageCountByUser(User user, int pageSize) {
        if (user == null) throw new NotLoggedIn("User must not be null");
        if (pageSize < 1) throw new ValidationException("Page size must be >= 1");
        return ((FriendshipRepository) repository).pageCountByUser(user.getId(), pageSize);
    }

    public Collection<Map.Entry<User, User>> getAllPrettyByUser(User user) {
        if (user == null) throw new NotLoggedIn("User must not be null");

        List<Map.Entry<User, User>> prettyList = new ArrayList<>();

        try {
            for (Friendship f : ((FriendshipRepository) repository).getAll()) {
                if (f.getUserId1() == user.getId() || f.getUserId2() == user.getId()) {
                    User u1 = userRepository.find(f.getUserId1());
                    User u2 = userRepository.find(f.getUserId2());
                    if (u1 != null && u2 != null) {
                        prettyList.add(new AbstractMap.SimpleEntry<>(u1, u2));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return prettyList;
    }

    public Collection<Map.Entry<User, User>> getAllPrettyByUser(User user, int offset, int limit) {
        if (user == null) throw new NotLoggedIn("User must not be null");
        if (offset < 0 || limit < 1) throw new ValidationException("Offset/Limit invalid");

        List<Map.Entry<User, User>> prettyList = new ArrayList<>();

        try {
            for (Friendship f : ((FriendshipRepository) repository).getFriendshipsPageByUser(user.getId(), offset, limit)) {
                User u1 = userRepository.find(f.getUserId1());
                User u2 = userRepository.find(f.getUserId2());
                if (u1 != null && u2 != null) {
                    prettyList.add(new AbstractMap.SimpleEntry<>(u1, u2));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return prettyList;
    }

}
