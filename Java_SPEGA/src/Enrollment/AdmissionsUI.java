package Enrollment;
import java.util.*;

import com.mysql.cj.jdbc.*;

import java.math.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class AdmissionsUI{
    private Scanner keyboard = null;
    private Student[] students = null;


    public static void main(String[] args) throws SQLException  {

        Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/admissiondb?serverTimezone=America/Chicago","root","711061Ggn+");
        Course.importFromDB();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Loaded driver");

            System.out.println("Connected to MySQL");

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        AdmissionsUI adminInterface = new AdmissionsUI();
        try( Scanner keyboardAutoClose = new Scanner(System.in) ) {
            adminInterface.keyboard = keyboardAutoClose;

            System.out.println("Please enter the number of students you wish to add to the system");
            int size = adminInterface.keyboard.nextInt();
            adminInterface.keyboard.nextLine();
            adminInterface.students = new Student[size];

            for (int i = 0; i < size; i++) {

                System.out.println("Please enter your first name for Student ");
                String firstName = adminInterface.keyboard.nextLine();
                System.out.println("Please enter your last name");
                String lastName = adminInterface.keyboard.nextLine();
                String id = adminInterface.makeID();

                Student student = new Student(id, firstName, lastName);
                adminInterface.students[i] = student;

                adminInterface.addCourses(student);
                adminInterface.payForCourses(student);

                if (i == size - 1)
                    adminInterface.displayStudentsInfo();

                Statement statement =  con.createStatement();

                // insert the data into student
                String tableName = "student";
                String query = "insert into " + tableName + " values (" + "'" + id + "'" + "," + "'" + firstName + "'" + "," + "'" + lastName + "'" + "," + id.charAt(0) + "," + student.getTuition().toString() +");";
                System.out.println(query);
                statement.executeUpdate(query);
                // insert the data into studentCourse
                tableName = "studentCourse";

                for(int j = 0; j < student.getCourses().size(); j++) {
                    query = "insert into " + tableName + " values (" + "'" + id + "'" + "," + "'" + student.getCourses().get(j).getCourseID() + "'" +");";
                    statement.executeUpdate(query);
                }
            }
        } catch (NegativeArraySizeException e) {
            System.out.println("You can't use a negative number for size");

        }
        con.close();
    }

    /**
     * Prints out each student's name, id, courses, and the current balance for
     * tuition
     *
     * @param studentList
     *            - All the students enrolled and in the list
     */
    public void displayStudentsInfo() {
        for (Student student : students) {
            System.out.println("Student Name: " + student.getName());
            System.out.println("Student ID: " + student.getId());


            if (student.getCourses().size() > 0) {
                System.out.print("Student's Current Courses:" );
                for(Course sc:student.getCourses()) {
                    System.out.print(sc.getTitle() + " ");
                }
                System.out.println();
            } else {
                System.out.println("Student's Current Courses: The student isn't enrolled in any courses");
            }
            System.out.println("Student's Current Balance: $" + student.getTuition());
            System.out.println("------------------------------------------------------");
        }

    }

    /**
     * Allows the user to add classes keeping track of classes they already added
     * and setting the new tuition the user has.
     */
    public void addCourses(Student student) {
        List<Course> classes = new LinkedList<>();


        String answer;
        int nextCourse;

        System.out.println("Do you want to add any courses? yes or no");
        answer = keyboard.nextLine();
        while (!answer.toLowerCase().equals("no")) {
            if (answer.toLowerCase().equals("yes")) {
                System.out
                        .println("Which classes would you like to add now? Please choose from the following selection. "
                                + "Choose the number for the courses");

                int i=1;
                for(Course c:Course.getCourseList()) {
                    System.out.println(i++ + " " + c.getTitle());
                }

                if (keyboard.hasNextInt()) {
                    nextCourse = keyboard.nextInt();
                    keyboard.nextLine();
                    classes.add(Course.getCourseList().get(nextCourse - 1));

                } else {
                    System.out.println("You put in the wrong input: Enter a number 1 - 5 for each class");
                    keyboard.nextLine();
                }

            } else {
                System.out.println("You put in the wrong input: Enter either yes or no next time");
            }

            System.out.println("Do you want to add any more courses?");
            answer = keyboard.nextLine();
        }
        student.addCourses(classes);
    }

    /**
     * A payment system that allows the user to make multiple payments on their
     * tuition
     */
    public void payForCourses(Student student) {
        while (student.getTuition().compareTo(BigDecimal.ZERO) > 0) {
            System.out.println("Your current balance is $" + student.getTuition());
            System.out.println("Do you want pay off you balance right now");

            String answer = keyboard.nextLine();

            if (answer.toLowerCase().equals("yes")) {
                System.out.println("How much would you like to pay right now");
//update statement

                if (keyboard.hasNextBigDecimal()) {
                    BigDecimal payment = keyboard.nextBigDecimal();
                    payment = payment.setScale(2, RoundingMode.HALF_UP);
                    keyboard.nextLine();
                    if ((payment.compareTo(BigDecimal.ZERO) > 0) && payment.compareTo( student.getTuition()) <= 0) {
                        student.makePayment(payment);
                    } else if (payment.compareTo(student.getTuition()) > 0) {
                        System.out.println("The value you have given is greater than your tuition");
                    } else if (payment.compareTo(BigDecimal.ZERO) < 0) {
                        System.out.println(
                                "You gave an negative number as a payment value. Please enter a positive value next time");
                    }

                } else {
                    keyboard.nextLine();
                    System.out.println("You entered the wrong input so please input a number next time.");
                }

            } else if (answer.toLowerCase().equals("no")) {
                break;
            } else {
                System.out.println("You gave the wrong input either enter yes or no");
            }
        }
    }

    /**
     * Creates a id using a number from 1 - 4 given by the user and a random string
     * of length 4.
     */
    public String makeID() {
        String grade;
        while (true) { //Returns from method when done
            System.out.println("Enter your school year 1. Freshman, 2. Sophomore, 3.Junior and 4. Senior ");
            grade = keyboard.nextLine();
            if (grade.length() == 1 && Integer.parseInt(grade) > 0 && Integer.parseInt(grade) < 5) {
                return grade.concat(randomString());
            } else {
                System.out.println("The input you enter is incorrect please try again");
            }
        }
    }

    /**
     * Returns a randomly generated 4 character string that will combined with a
     * number entered by the user to make the student id.
     *
     * @return The four character random string
     */
    public String randomString() {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        int great = AB.length();
        int temp;
        String codeword = "";
        for (int i = 0; i < 4; i++) {
            temp = (int) (random.nextFloat() * great);
            codeword = codeword.concat(Character.toString(AB.charAt(temp)));
        }
        return codeword;
    }
}

