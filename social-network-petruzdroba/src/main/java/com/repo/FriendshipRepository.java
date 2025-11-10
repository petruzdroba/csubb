package com.repo;

import com.domain.Friendship;

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
    
    /**
    *Gaseste toate prieteniile care il au pe user {@link com.domain.User} cu userId
     *  si sterge prieteniile care il contin
     *  
     * @param userId, id-ul userului care a fost sters, cascade delete freindships
     * @see com.repo.AbstractRepository#remove(Object)
     * */
    public void removeUserFriendships(long userId){
        getAll().forEach(f -> {
            if(f.getUserId1() == userId || f.getUserId2() == userId)
                remove(f.getFriendshipId());
        });
    }
}
