package studentDAO;

import info.Student;
import utils.CommonUtils;

import java.util.*;
import java.util.stream.Stream;

import static studentManagement.StudentManagement.scanner;

public class StudentDAO {

    public static Map<Integer, Student> studentDatabase = new HashMap<>();

    public static void addStudent() {

        Student student = studentData();

        studentDatabase.put(student.id, student);
        System.out.println("===========================");
        System.out.println("Database updated successfully");

    }

    public static void searchStudents() {
        String name = getStudentName().toLowerCase();
        List<Student> students = new LinkedList<>();
        for (Student student : studentDatabase.values()) {
            if (student.name.toLowerCase().contains(name)) {
                students.add(student);
            }
        }
        printStudents(students);
    }

    public static void listStudents() {
        List<Student> students = new LinkedList<>(studentDatabase.values());
        printStudents(students);
    }

    public static void deleteStudents() {
        int id = getStudentId(false);
        if (studentDatabase.containsKey(id)) {
            studentDatabase.remove(id);
            System.out.println("Student deleted successfully");
        } else {
            System.err.println("No data found for ID " + id);
        }
    }

    public static void updateStudent() {

        int id = getStudentId(false);
        Student oldStudent = studentDatabase.get(id);
        if (oldStudent == null) {
            System.out.println("No data found");
        } else {
            oldStudent.name = getStudentName();
            oldStudent.age = getStudentAge();
            oldStudent.admissionYear = getStudentAdmissionYear();
            oldStudent.gpa = getStudentGpa();

            System.out.println("Database updated successfully");
        }
    }

    //==================Print====================

    public static void printStudents(List<Student> students) {
        if (students == null || students.isEmpty()) {
            System.out.println("========================");
            System.out.println("No data found");
            System.out.println("========================");
        } else {
            Map<String, Object> args = new HashMap<>();
            args.put("shouldJustifyRowsToLeft", false);
            args.put("shouldWrapText", false);
            args.put("maxColumnWidth", 30);
            CommonUtils.printTableWithLinesAndMaxWidth(students, args);
        }
    }

    //==================Scanner ===================
    public static Student studentData() {
        int id = getStudentId(true);
        String name = getStudentName();
        int age = getStudentAge();
        int addmissionYear = getStudentAdmissionYear();
        double GPA = getStudentGpa();

        Student newStudent = new Student();

        newStudent.id = id;
        newStudent.name = name;
        newStudent.age = age;
        newStudent.admissionYear = addmissionYear;
        newStudent.gpa = GPA;

        return newStudent;
    }

    public static String getStudentName() {
        String name;
        System.out.println("Enter name of student");
        name = scanner.nextLine();
        return name;
    }

    public static int getStudentId(boolean checkDuplicate) {
        int id = getIntFromConsole("Enter Id");
        if (checkDuplicate && studentDatabase.containsKey(id)) {
            System.out.println("Id already exists");
            id = getStudentId(checkDuplicate);
        }
        return id;
    }

    public static int getStudentAge() {
        return getIntFromConsole("Enter age");
    }

    public static int getStudentAdmissionYear() {
        return getIntFromConsole("Enter admission year");
    }

    public static double getStudentGpa() {
        return getDoubleFromConsole("Enter gpa");
    }

    public static int getIntFromConsole(String consoleMessage) {
        int value;
        try {
            System.out.println(consoleMessage);
            value = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            scanner.nextLine();
            System.err.println("Invalid input! Please try again.");
            value = getIntFromConsole(consoleMessage);
        }
        return value;
    }

    public static double getDoubleFromConsole(String consoleMessage) {
        double value;
        try {
            System.out.println(consoleMessage);
            value = scanner.nextDouble();
            scanner.nextLine();
        } catch (Exception e) {
            scanner.nextLine();
            System.err.println("Invalid input! Please try again.");
            value = getDoubleFromConsole(consoleMessage);
        }
        return value;
    }

}
