package org.example.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

public class MyMap {
    private TreeMap<Integer, List<Student>> studentsByGrades = new TreeMap<>();

    public MyMap(TreeMap<Integer, List<Student>> studentsByGrades) {
        this.studentsByGrades = studentsByGrades;
    }

    public void addStudent(Student s){
        int grade = Math.round(s.getGrade());
        var students = studentsByGrades.get(grade);

        if(students == null){
            students = new ArrayList<>();
            studentsByGrades.put(grade, students);
        }
        students.add(s);
    }


}


public class StudentComparator implements Comparator<Integer>{
    @Override
    public int compare(Integer i1, Integer i2){
        return i2- i1;
    }
}