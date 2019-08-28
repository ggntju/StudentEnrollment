package Enrollment;
import java.sql.*;
import java.util.*;

public class Course{
    private static List<Course> courseList = new ArrayList<>();
    private String title;
    private String courseID;
    Course(String title, String courseID) {
        this.courseID = courseID;
        this.title = title;
    }
    public String getTitle() {

        return title;
    }
    public String getCourseID() {
        return courseID;
    }
    public static List<Course> getCourseList() {
        return courseList;
    }
    public static void add2CourseList(Course newCourse) {
        courseList.add(newCourse);
    }

    public static void importFromDB() throws SQLException{
        Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/admissiondb?serverTimezone=America/Chicago","root","711061Ggn+");
        Statement statement =  con.createStatement();
        String tableName = "course";
        String query = "select * from " + tableName;
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            String courseName = resultSet.getString("coursename");
            String courseID = resultSet.getString("courseID");
            System.out.println(courseName);
            Course newCourse = new Course(courseName, courseID);
            if(!Course.courseList.contains(newCourse)) {
                Course.courseList.add(newCourse);
            }
        }
        con.close();
    }
}
