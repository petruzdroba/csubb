package main.java.com.repo;

import main.java.com.domain.*;
import main.java.com.exceptions.RepositoryException;

import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;

public class RepositoryFile extends Repository{
    private final String userFile;
    private final String friendshipFile;

    public RepositoryFile(String userFile, String friendshipFile) {
        this.userFile = userFile;
        this.friendshipFile = friendshipFile;
        loadUsers();
        loadFriendships();
    }


    private void loadUsers() {
        try (Scanner sc = new Scanner(new File(userFile))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                String type = parts[0];

                if (type.equalsIgnoreCase("PERSONA")) {
                    long id = Long.parseLong(parts[1]);
                    String username = parts[2];
                    String email = parts[3];
                    String password = parts[4];
                    String nume = parts[5];
                    String prenume = parts[6];
                    LocalDate dataNasterii = LocalDate.parse(parts[7]);
                    String ocupatie = parts[8];
                    int empatie = Integer.parseInt(parts[9]);

                    Persoana p = new Persoana(id, username, email, password, nume, prenume, dataNasterii, ocupatie, empatie);
                    super.addUser(p);

                } else if (type.equalsIgnoreCase("DUCK")) {
                    long id = Long.parseLong(parts[1]);
                    String username = parts[2];
                    String email = parts[3];
                    String password = parts[4];
                    Duck.TipRata tip = Duck.TipRata.valueOf(parts[5]);
                    double viteza = Double.parseDouble(parts[6]);
                    double rezistenta = Double.parseDouble(parts[7]);
                    int cardId = Integer.parseInt(parts[8]);

                    Duck duck = new Duck(id, username, email, password, tip, viteza, rezistenta, cardId);
                    super.addUser(duck);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("file not found");
        }
    }

    private void loadFriendships() {
        try (Scanner sc = new Scanner(new File(friendshipFile))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                long u1 = Long.parseLong(parts[0]);
                long u2 = Long.parseLong(parts[1]);

                super.addFriendShip(new Friendship(u1, u2));
            }
        } catch (FileNotFoundException e) {
            System.err.println("file not found");
        }
    }

    private void overwriteToUser() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(userFile, false))) {
            for (User u : getAllUsers()) {
                if (u instanceof Persoana p) {
                    pw.println(String.join(",",
                            "PERSONA",
                            String.valueOf(p.getId()),
                            p.getUsername(),
                            p.getEmail(),
                            p.getPassword(),
                            p.getNume(),
                            p.getPrenume(),
                            p.getDataNasterii().toString(),
                            p.getOcupatie(),
                            String.valueOf(p.getNivelEmpatie())
                    ));
                } else if (u instanceof Duck d) {
                    pw.println(String.join(",",
                            "DUCK",
                            String.valueOf(d.getId()),
                            d.getUsername(),
                            d.getEmail(),
                            d.getPassword(),
                            d.getTip().name(),
                            String.valueOf(d.getViteza()),
                            String.valueOf(d.getRezistenta()),
                            String.valueOf(d.getId())
                    ));
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing users: " + e.getMessage());
        }
    }

    private void overwriteToFriendship() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(friendshipFile, false))) {
            for (Friendship f : getAllFriendships()) {
                pw.println(String.join(",",
                        String.valueOf(f.getUserId1()),
                        String.valueOf(f.getUserId2())
                ));
            }
        } catch (IOException e) {
            System.err.println("Error writing friendships: " + e.getMessage());
        }
    }

    @Override
    public void addUser(User u) throws RepositoryException {
        super.addUser(u);
        overwriteToUser();
    }

    @Override
    public void removeUser(long userId) throws RepositoryException{
        super.removeUser(userId);
        overwriteToUser();
    }

    @Override
    public void modifyUser(User u) throws  RepositoryException{
        super.modifyUser(u);
        overwriteToUser();
    }

    @Override
    public void addFriendShip(Friendship friendship) throws RepositoryException{
        super.addFriendShip(friendship);
        overwriteToFriendship();
    }

    @Override
    public void removeFriendship(String friendshipId) throws RepositoryException{
        super.removeFriendship(friendshipId);
        overwriteToFriendship();
    }
}
