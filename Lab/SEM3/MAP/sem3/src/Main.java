import org.example.model.Student;

import javax.swing.*;
import java.util.*;

public class Main{
    public static void main(String args[]){
        Student s1 = new Student("s1", 9.0f);
        Student s2 = new Student("s2", 9.0f);
        Student s3 = new Student("s1", 9.0f);

        Set<Student> students = new HashSet<>();

        students.add(s1);
        students.add(s2);
        students.add(s3);

//        students.forEach(System.out::println);

        Map<String, Student> map = new HashMap<>();

        map.put(s1.getName(), s1);
        map.put(s2.getName(), s2);
        map.put(s3.getName(), s3);

        map.entrySet().forEach(entry-> System.out.println());


     }
}