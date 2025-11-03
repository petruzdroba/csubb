package main.java.com.repo;

import main.java.com.domain.*;

import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;

public class UserRepository extends AbstractFileRepository<Long, User>{

    public UserRepository(String filePath) {
        super(filePath);
    }

    @Override
    protected void loadFile() {
        try (Scanner sc = new Scanner(new File(filePath))) {
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

                    User p = new Persoana(id, username, email, password, nume, prenume, dataNasterii, ocupatie, empatie);
                    data.put(id, p);

                } else if (type.equalsIgnoreCase("DUCK")) {
                    long id = Long.parseLong(parts[1]);
                    String username = parts[2];
                    String email = parts[3];
                    String password = parts[4];
                    Duck.TipRata tip = Duck.TipRata.valueOf(parts[5]);
                    double viteza = Double.parseDouble(parts[6]);
                    double rezistenta = Double.parseDouble(parts[7]);

                    User duck;
                    if(tip == Duck.TipRata.FLYING) {
                        duck = new FlyingDuck(id, username, email, password, tip, viteza, rezistenta);
                    }else if(tip == Duck.TipRata.SWIMMING){
                        duck = new SwimmingDuck(id, username, email, password, tip, viteza, rezistenta);
                    }else {
                        duck = new SwimmingFlyingDuck(id, username, email, password, tip, viteza, rezistenta);
                    }

                    data.put(id, duck);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("file not found");
        }
    }

    @Override
    protected void overwriteFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, false))) {
            for (User u : getAll()) {
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
                            String.valueOf(d.getRezistenta())
                    ));
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing users: " + e.getMessage());
        }
    }
}
