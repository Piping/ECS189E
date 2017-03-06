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
public class TestIStudent {
    private IAdmin admin;
    private IStudent student;

    @Before
    public void setup(){
        this.admin = new Admin();
        this.student = new Student();
        /**
         * @ Prerequisite
         * @ TestData
         */
        this.admin.createClass("class",2017,"A",100);
        this.admin.createClass("capacity",2017,"A",2);
        this.admin.createClass("testclass",2017,"abc",10);

    }
    @Test
    public void test_registerForClass(){
        /**
         * @ Prerequisite: Class Existed,Student Existed
         * @ TestData: class,2017,A,100
         */
        student.registerForClass("OK","testclass",2017);
        assertTrue("student failed to register class", student.isRegisteredFor("OK","testclass",2017));
        student.registerForClass("OK","capacity",2017);
        assertTrue("student failed to register class", student.isRegisteredFor("OK","capacity",2017));
        student.registerForClass("OK","class",2017);
        assertTrue("student failed to register class", student.isRegisteredFor("OK","class",2017));
    }
    @Test
    public void test_registerForClass_param_studentName(){

        student.registerForClass("A","testclass",2017);
        assertTrue("student failed to register class", student.isRegisteredFor("A","testclass",2017));
        //allow students with same name register??
        student.registerForClass("A","testclass",2017);
        admin.changeCapacity("testclass",2017,1);
        assertFalse("student with same name failed to register class", admin.getClassCapacity("testclass",2017)==1);

        student.registerForClass("","class",2017);
        assertFalse("Failed to check null @param studentName",
                student.isRegisteredFor("","class",2017));
    }
    @Test
    public void test_registerForClass_enrollment_limit(){

        student.registerForClass("A","capacity",2017);
        assertTrue("Unsuccesfull regisration",
                student.isRegisteredFor("A","capacity",2017));
        student.registerForClass("B","capacity",2017);
        assertTrue("Unsuccesfull regisration",
                student.isRegisteredFor("B","capacity",2017));
        student.registerForClass("C","capacity",2017);
        assertFalse("Regisration exceed enrollment",
                student.isRegisteredFor("C","capacity",2017));
    }
    @Test
    public void test_registerForClass_param_class_year(){//check for class existence
        student.registerForClass("A","",2017);
        assertFalse("Registered for a non-existed class",
                student.isRegisteredFor("A","",2017));

        student.registerForClass("A","class",2016);
        assertFalse("Registered for a non-existed class",
                student.isRegisteredFor("A","class",2016));
    }
    @Test
    public void test_dropClass(){
        student.registerForClass("OK","class",2017);
        student.dropClass("OK","class",2017);
        assertFalse("Drop class doesnt work", student.isRegisteredFor("OK","class",2017));
    }
    @Test
    public void test_dropClass_param_studentName(){
        student.dropClass("","class",2017);
        assertFalse("Drop class doesnt work", student.isRegisteredFor("","class",2017));

    }
    @Test
    public void test_dropClass_condition_registered_student(){
        student.registerForClass("A","capacity",2017);
        assertTrue("Unsuccesfull regisration",
                student.isRegisteredFor("A","capacity",2017));
        student.registerForClass("B","capacity",2017);
        assertTrue("Unsuccesfull regisration",
                student.isRegisteredFor("B","capacity",2017));
        student.dropClass("C","capacity",2017);
        admin.changeCapacity("capacity",2017,1);
        assertFalse("Non-Registered Student Cannot Drop class",
                admin.getClassCapacity("capacity",2017)==1);
    }
    @Test
    public void test_dropClass_condition_ended_course(){
        student.registerForClass("A","capacity",2017);
        assertTrue("Unsuccesfull regisration",
                student.isRegisteredFor("A","capacity",2017));
        student.registerForClass("B","capacity",2017);
        assertTrue("Unsuccesfull regisration",
                student.isRegisteredFor("B","capacity",2017));
        student.dropClass("B","capacity",2018);
        admin.changeCapacity("capacity",2017,1);
        assertFalse("Student Cannot Drop Ended Class",
                admin.getClassCapacity("capacity",2017)==1);
    }
    @Test
    public void test_submitHomework(){
        IInstructor teacher = new Instructor();
        student.registerForClass("OK","class",2017);
        teacher.addHomework("A","class",2017,"hw1","hw1_description");
        student.submitHomework("OK","hw1","answer","class",2017);
        assertTrue(student.hasSubmitted("OK","hw1","class",2017));
    }
    @Test
    public void test_submitHomework_condition_student_registered(){
        IInstructor teacher = new Instructor();
        teacher.addHomework("A","class",2017,"hw1","hw1_description");
        student.submitHomework("OK","hw1","answer","class",2017);
        assertFalse("Student need to register a class to submit homework",
                student.hasSubmitted("OK","hw1","class",2017));
    }
    @Test
    public void test_submitHomework_condition_homework_existed(){
        student.registerForClass("OK","class",2017);
        student.submitHomework("OK","hw1","answer","class",2017);
        assertFalse("Homework must be existed to submit homework",
                student.hasSubmitted("OK","hw1","class",2017));
    }
    @Test
    public void test_submitHomework_condition_class_ended(){
        IInstructor teacher = new Instructor();
        student.registerForClass("OK","class",2017);
        teacher.addHomework("A","class",2017,"hw1","hw1_description");
        student.submitHomework("OK","hw1","answer","class",2018);
        assertFalse("Student cannot submit homework after a class ended",
                student.hasSubmitted("OK","hw1","class",2017));
    }
    @Test
    public void test_submitHomework_params(){
        IInstructor teacher = new Instructor();
        student.registerForClass("OK","class",2017);
        teacher.addHomework("A","class",2017,"hw1","hw1_description");

        student.submitHomework("","hw1","answer","class",2017);
        assertFalse(student.hasSubmitted("OK","hw1","class",2017));

        student.submitHomework("OK","","answer","class",2017);
        assertFalse(student.hasSubmitted("OK","hw1","class",2017));

        student.submitHomework("OK","hw1","answer","",2017);
        assertFalse(student.hasSubmitted("OK","hw1","class",2017));

        student.submitHomework("OK","hw1","","class",0);
        assertFalse(student.hasSubmitted("OK","hw1","class",2017));
    }
}
