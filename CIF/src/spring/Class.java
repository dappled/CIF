package spring;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: eran
 * Date: 10/25/12
 * Time: 5:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class Class {

    List<Student> students = new LinkedList<Student>();

    public void addNewStudent(Student student){
        students.add(student);
    }

    public int getNumberOfStudents(){
        return students.size();
    }

    public Integer getId(String name){
        for (Student student : students){
            if (student.getName().equals(name)){
                return student.getId();
            }
        }
        return null;
    }




}
