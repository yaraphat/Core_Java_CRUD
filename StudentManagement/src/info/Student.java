package info;

public class Student implements Cloneable, Comparable<Student> {

    public int id;
    public String name;
    public int age;
    public int admissionYear;
    public double gpa;


    @Override
    public int compareTo(Student o) {
        return Integer.compare(this.id, o.id);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }


}
