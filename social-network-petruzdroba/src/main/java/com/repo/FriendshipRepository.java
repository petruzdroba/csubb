package main.java.com.repo;

import main.java.com.domain.Friendship;

import java.io.*;
import java.util.Scanner;

public class FriendshipRepository extends AbstractFileRepository<String, Friendship> {
    public FriendshipRepository(String filePath) {
        super(filePath);
    }

    @Override
    protected void loadFile() {
        try (Scanner sc = new Scanner(new File(filePath))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                long u1 = Long.parseLong(parts[0]);
                long u2 = Long.parseLong(parts[1]);

                Friendship f = new Friendship(u1, u2);
                data.put(f.getFriendshipId(), f);
            }
        } catch (FileNotFoundException e) {
            System.err.println("file not found");
        }
    }

    @Override
    protected void overwriteFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, false))) {
            for (Friendship f : getAll()) {
                pw.println(String.join(",",
                        String.valueOf(f.getUserId1()),
                        String.valueOf(f.getUserId2())
                ));
            }
        } catch (IOException e) {
            System.err.println("Error writing friendships: " + e.getMessage());
        }
    }
}
