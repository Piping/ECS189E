import api.IAdmin;
import api.IInstructor;
import api.IStudent;
import api.core.impl.Admin;
import api.core.impl.Instructor;
import api.core.impl.Student;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by dequan on 2/27/17.
 */
public class TestIInstructor {
    private IAdmin admin;
    private IStudent student;
    private IInstructor instructor;

    @Before
    public void setup(){
        admin = new Admin();
        student = new Student();
        instructor = new Instructor();

        admin.createClass("CA",2017,"TA",20);
        admin.createClass("CB",2017,"TB",20);
        admin.createClass("CC",2017,"TC",20);

        student.registerForClass("SA","CA",2017);
        student.registerForClass("SB","CB",2017);

        instructor.addHomework("TA","CA",2017,"hwA1","homeworkA1");
        instructor.addHomework("TB","CB",2017,"hwB1","homeworkB1");

        student.submitHomework("SA","hwA1","??","CA",2017);
        student.submitHomework("SB","hwB1","??","CB",2017);

        instructor.assignGrade("TA","CA",2017,"hwA1","SA",90);
        instructor.assignGrade("TB","CB",2017,"hwB1","SB",95);

        //cases for parameters in assignGrade method
        instructor.addHomework("TA","CA",2017,"hwAA1","homeworkAA1");
        student.submitHomework("SA","hwAA1","??","CA",2017);

    }
    @Test
    public void test_addHomework(){
        assertTrue("instructor fail to add homework", instructor.homeworkExists("CA",2017,"hwA1"));
    }
    // Parameters Check
    @Test
    public void test_addHomework_param_instructorName(){
        instructor.addHomework("","CA",2017,
                "hwA2","homeworkA2");
        assertFalse("Failed to check parameter: instructorName",
                instructor.homeworkExists("CA",2017,"hwA2"));
    }
    @Test
    public void test_addHomework_param_className(){
        instructor.addHomework("TA","",2017,
                "hwA3","homeworkA3");
        assertFalse("Failed to check parameter: classsName",
                instructor.homeworkExists("CA",2017,"hwA3"));
    }
    @Test
    public void test_addHomework_param_year(){
        instructor.addHomework("TA","CA",2016,
                "hwA4","homeworkA4");
        assertFalse("Failed to check parameter: year",
                instructor.homeworkExists("CA",2017,"hwA4"));
    }
    @Test
    public void test_addHomework_param_homeworkName(){
        instructor.addHomework("TA","CA",2017,
                "","homeworkA5");
        assertFalse("Failed to check parameter: homeworkName",
                instructor.homeworkExists("CA",2017,""));

        //Cannot test duplicated homeworkName
    }
/*    @Test
    public void test_addHomework_param_hwDescription(){
        instructor.addHomework("TA","CA",2017,
                "hwA6","");
        assertFalse("Failed to check parameter: homeworkName",
                instructor.homeworkExists("CA",2017,"hwA6"));
    }*/
    // addHomework relation test
    @Test
    public void test_addHomework_instructor_class_match(){
        instructor.addHomework("TA","CB",2017,
                "hwB2","hwB2");
        assertFalse("Failed to check if the instructor is assigned for that class",
                instructor.homeworkExists("CB",2017,"hwB2"));
    }

    @Test
    public void test_assignGrade(){
        //precondition check
        assertTrue("admin fail to create class",admin.classExists("CA",2017));
        assertTrue("student fail to register",student.isRegisteredFor("SA","CA",2017));
        assertTrue("instructor fail to add homework", instructor.homeworkExists("CA",2017,"hwA1"));
        assertTrue("Student fail to submit",student.hasSubmitted("SA","hwA1","CA",2017));
        //assert the setup assignGrade
        assertTrue("instructor fail to assign grade",
                instructor.getGrade("CA",2017,"hwA1","SA")==90);
    }
    @Test
    public void test_assignGrade_param_instructorName(){
        student.registerForClass("SB","CA",2017);
        student.submitHomework("SB","hwA1","??","CA",2017);

        instructor.assignGrade("","CA",2017,"hwA1","SB",88);
        assertFalse("instructor fail to check instructorName",
                instructor.getGrade("CA",2017,"hwA1","SB")==88);
    }
    @Test
    public void test_assignGrade_param_className(){
        student.registerForClass("SB","CA",2017);
        student.submitHomework("SB","hwA1","??","CA",2017);

        instructor.assignGrade("TA","",2017,"hwA1","SB",88);
        assertFalse("instructor fail to check className",
                instructor.getGrade("CA",2017,"hwA1","SB")==88);
    }
    @Test
    public void test_assignGrade_param_year(){
        student.registerForClass("SB","CA",2017);
        student.submitHomework("SB","hwA1","??","CA",2017);

        instructor.assignGrade("TA","CA",2018,"hwA1","SB",88);
        assertFalse("instructor fail to check parameter: year",
                instructor.getGrade("CA",2017,"hwA1","SB")==88);
    }
    @Test
    public void test_assignGrade_param_homeworkName(){
        student.registerForClass("SB","CA",2017);
        student.submitHomework("SB","hwA1","??","CA",2017);

        instructor.assignGrade("TA","CA",2017,"","SB",88);
        assertFalse("instructor fail to check homeworkName",
                instructor.getGrade("CA",2017,"hwA1","SB")==88);
    }
    @Test
    public void test_assignGrade_param_studentName(){
        student.registerForClass("SB","CA",2017);
        student.submitHomework("SB","hwA1","??","CA",2017);

        instructor.assignGrade("TA","CA",2017,"hwA1","",88);
        assertFalse("instructor fail to check parameter: studentName",
                instructor.getGrade("CA",2017,"hwA1","SB")==88);
    }
    @Test
    public void test_assignGrade_param_studentGrade_below_zero(){
        instructor.assignGrade("TA","CA",2017,"hwAA1","SA",-10);
        assertFalse("instructor fail to check parameter: grade",
                instructor.getGrade("CA",2017,"hwAA1","SA")==-10);
    }
    @Test
    public void test_assignGrade_param_studentGrade_above_hundred(){
        instructor.assignGrade("TA","CA",2017,"hwAA1","SA",110);
        assertFalse("instructor fail to check instructorName",
                instructor.getGrade("CA",2017,"hwAA1","SA")==110);
    }
    //Method Constraint Test
    @Test
    public void test_assignGrade_instructor_class_match(){
        student.registerForClass("SB","CA",2017);
        student.submitHomework("SB","hwA1","??","CA",2017);

        instructor.assignGrade("TB","CA",2017,"hwA1","SB",91);
        assertFalse("instructor is not assigned for that class and cannot assign homework grade",
                instructor.getGrade("CA",2017,"hwA1","SB")==91);
    }
    @Test
    public void test_assignGrade_grade_before_hw_assigned_(){

        student.registerForClass("SB","CA",2017);
        student.submitHomework("SB","HWDoesntExisted","??","CA",2017);
        instructor.assignGrade("TA","CA",2017,"HWDoesntExisted","SB",91);
        assertFalse("Homework has not been assigned",
                instructor.getGrade("CA",2017,"HWDoesntExisted","SB")==91);
    }
    @Test
    public void test_assignGrade_grade_before_hw_submitted(){
        student.registerForClass("SB","CA",2017);

        instructor.assignGrade("TA","CA",2017,"hwA1","SB",91);
        assertFalse("The student has not submitted for that class",
                instructor.getGrade("CA",2017,"hwA1","SB")==91);
    }
}
