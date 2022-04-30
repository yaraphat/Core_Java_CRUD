package studentManagement;

import java.util.*;

import studentDAO.StudentDAO;

public class StudentManagement {

    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {
            System.out.println("\nEnter command like 'add, list, delete, search, update'.\n");
            try {
                String command = scanner.nextLine();
                if (command.equalsIgnoreCase("add")) {
                    StudentDAO.addStudent();
                } else if (command.equalsIgnoreCase("search")) {
                    StudentDAO.searchStudents();
                } else if (command.equalsIgnoreCase("list")) {
                    StudentDAO.listStudents();
                } else if (command.equalsIgnoreCase("delete")) {
                    StudentDAO.deleteStudents();
                } else if (command.equalsIgnoreCase("update")) {
                    StudentDAO.updateStudent();
                } else if (command.equalsIgnoreCase("exit")) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}


