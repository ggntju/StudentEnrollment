package Enrollment;
import java.math.*;
import java.util.*;


public class Student {

    private String firstName;
    private String lastName;
    private String id;
    private List<Course> courses = new ArrayList<>();
    private BigDecimal tuition = BigDecimal.ZERO; //Default zero

    /**
     * Package private, so that only admissions class can create this.
     */
    Student(String id, String fName, String lastName) {
        this.id = id;
        this.firstName = fName;
        this.lastName = lastName;
    }

    // Getters and Setters
    public BigDecimal getTuition() {
        return tuition;
    }

    public void setTuition(BigDecimal money) {
        this.tuition = money;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void addCourses(List<Course> courses) {
        if(courses!=null) {
            this.courses.addAll(courses);
            this.setTuition(this.getTuition().add(
                    new BigDecimal("600").multiply( new BigDecimal( courses.size() ) )
            ) );
        }
    }

    public void makePayment(BigDecimal thisPayment) {
        //Backup validation built into student class
        if(thisPayment==null || thisPayment.compareTo(BigDecimal.ZERO)<=0 ) {
            throw new IllegalArgumentException("Invalid payment amount.");
        }else if(thisPayment.compareTo(this.getTuition())>0) {
            throw new IllegalArgumentException("Payment exceeds tuition amount.");
        }
        this.setTuition( this.getTuition().subtract(thisPayment) );
    }
}
