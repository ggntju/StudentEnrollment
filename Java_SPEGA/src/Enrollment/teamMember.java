package Enrollment;

import java.io.*;
import java.sql.*;
import java.util.*;

public class teamMember {
    private Scanner keyboard = null;
    teamMember(){
    }

    public static void main(String[] args) {
        teamMember t1 = new teamMember();
        t1.updateCourseFees();
        //t1.addCourse();
        //t1.print2File();
    }
    public void updateCourseFees() {
        System.out.println("Please enter the course ID needs to be updated");
        try( Scanner keyboardAutoClose = new Scanner(System.in) ) {
            this.keyboard = keyboardAutoClose;
            String courseID = this.keyboard.nextLine();
            System.out.println("The course ID you entered is: " + courseID);
            System.out.println("Please enter the updated course fees");
            double newCourseFee = this.keyboard.nextDouble();
            System.out.println("The new course fee you entered is: " + newCourseFee);
            synchronized (this) {
                Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/admissiondb?serverTimezone=America/Chicago","root","711061Ggn+");
                Statement statement =  con.createStatement();
                String tableName = "course";
                String query = "update " + tableName + " set fees = " + newCourseFee + " where courseID = " + "'" + courseID + "'";
                System.out.println(query);
                statement.executeUpdate(query);
                con.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void addCourse() {
        System.out.println("Please enter the course ID");
        try( Scanner keyboardAutoClose = new Scanner(System.in) ) {
            this.keyboard = keyboardAutoClose;
            String courseID = this.keyboard.nextLine();
            System.out.println("The course ID you entered is: " + courseID);
            System.out.println("Please enter the course name");
            String courseName = this.keyboard.nextLine();
            System.out.println("The course name you entered is: " + courseName);
            Course newCourse = new Course(courseName, courseID);
            Course.add2CourseList(newCourse);
            System.out.println("Please enter the course fees");
            double newCourseFee = this.keyboard.nextDouble();
            System.out.println("The course fee you entered is: " + newCourseFee);
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/admissiondb?serverTimezone=America/Chicago","root","711061Ggn+");
            Statement statement =  con.createStatement();
            String tableName = "course";
            String query = "insert into " + tableName + " values (" + "'" + courseID + "'" + "," + "'" + courseName + "'" + "," + newCourseFee + ");";
            System.out.println(query);
            statement.executeUpdate(query);
            con.close();

        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public void print2File() {
        System.out.println("Print to courseInfo.txt");
        try {
            File outputFile = new File("./courseInfo.txt");
            if(!outputFile.exists()) {
                outputFile.createNewFile();
            }
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/admissiondb?serverTimezone=America/Chicago","root","711061Ggn+");
            Statement statement =  con.createStatement();
            String tableName = "course";
            String query = "select * from " + tableName;
            ResultSet resultSet = statement.executeQuery(query);
            bufferedWriter.write("courseID" + '\t' + "courseName" + '\t' + "fees" + '\n');
            while (resultSet.next()) {
                String courseID = resultSet.getString("courseID");
                String courseName = resultSet.getString("coursename");
                double courseFee = resultSet.getDouble("fees");
                bufferedWriter.write(courseID + '\t' + courseName + '\t' + courseFee + '\t' + '\n');
            }
            con.close();
            bufferedWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
