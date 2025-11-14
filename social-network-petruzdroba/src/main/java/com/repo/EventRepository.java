package com.repo;

import com.containers.DuckRaceContainer;
import com.domain.*;
import com.exceptions.RepositoryException;

import java.io.*;
import java.util.*;

public class EventRepository extends AbstractFileRepository<Long, Event> {
    private final UserRepository userRepo;

    public EventRepository(String filePath, UserRepository userRepo) {
        this.userRepo = userRepo;
        super(filePath);
    }

    public void subscribe(long eventId, User u) throws RepositoryException{
        Event e = find(eventId);
        if(e == null)
            throw new RepositoryException("Event not found");

        e.subscribe(u);
        overwriteFile();
    }

    public void unsubscribe(long eventId, User u) throws RepositoryException{
        Event e = find(eventId);
        if(e == null)
            throw new RepositoryException("Event not found");

        e.unsubscribe(u);
        overwriteFile();
    }

    @Override
    protected void loadFile() {
        File file = new File(filePath);
        if (!file.exists()) return;

        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNext()) {

                long eventId = sc.nextLong();

                int duckCount = sc.nextInt();
                List<Duck> ducks = new ArrayList<>();
                for (int i = 0; i < duckCount; i++) {
                    long duckId = sc.nextLong();
                    Duck d = (Duck) userRepo.find(duckId);
                    if (d == null) continue;
                    ducks.add(d);
                }

                int laneCount = sc.nextInt();
                List<Culoar> lanes = new ArrayList<>();
                for (int i = 0; i < laneCount; i++) {
                    int dist = sc.nextInt();
                    int index = sc.nextInt();
                    lanes.add(new Culoar(dist, index));
                }

                int subCount = sc.nextInt();
                List<User> subs = new ArrayList<>();
                for (int i = 0; i < subCount; i++) {
                    long uid = sc.nextLong();
                    User u = userRepo.find(uid);
                    if (u != null) subs.add(u);
                }

                DuckRaceContainer c = new DuckRaceContainer(ducks, lanes);
                RaceEvent event = new RaceEvent(eventId, c);
                event.setSubscribers(subs);

                data.put(eventId, event);
            }
        } catch (Exception e) {
            throw new RuntimeException("Event load failed", e);
        }
    }

    @Override
    protected void overwriteFile() {
        try (PrintWriter out = new PrintWriter(new FileWriter(filePath))) {
            for (Event e : data.values()) {
                RaceEvent re = (RaceEvent) e;
                DuckRaceContainer c = re.getContainer();

                out.println(re.getId());

                out.print(c.getDucks().size());
                for (Duck d : c.getDucks()) out.print(" " + d.getId());
                out.println();

                out.println(c.getCuloare().size());
                for (Culoar ln : c.getCuloare()) {
                    out.println(ln.getDistanta() + " " + ln.getId());
                }

                out.print(re.getSubscribers().size());
                for (User u : re.getSubscribers()) out.print(" " + u.getId());
                out.println();

            }
        } catch (Exception ex) {
            throw new RuntimeException("Event save failed", ex);
        }
    }
}
