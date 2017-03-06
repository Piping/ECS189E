import api.IAdmin;
import api.core.impl.Admin;
import api.core.impl.Student;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created by dequan on 2/27/17.
 * @ BeforeClass runs once before the entire test file
 * @ Before is executed before each test
 * @ Test is independent from each other test
 * In JUnit 5, use tags @BeforeEach and @BeforeAll
 */
public class TestIAdmin {
    private IAdmin admin;

    @Before
    public void setup() {
        this.admin = new Admin();
    }

    @Test
    public void test_createClass_init() {
        admin.createClass("ClassName", 2017, "Instructor1", 15);
        assertTrue(admin.classExists("ClassName", 2017));
        assertTrue(admin.getClassInstructor("ClassName",2017).equals("Instructor1"));
        assertTrue(admin.getClassCapacity("ClassName",2017) == 15);
    }

    @Test
    public void test_createClass_param_className(){ //Can Classname be empty?
        //className to be empty String
        admin.createClass("", 2017, "Instructor2", 15);
        assertFalse("Classname CANNOT be Empty", admin.classExists("",2017));
    }
    @Test
    public void test_createClass_param_year() {
        admin.createClass("classA", 2016, "Instructor3", 15);
        assertFalse("Shouldn't Allow Past Year", admin.classExists("classA",2016));

        admin.createClass("classB", 2008, "Instructor3", 15);
        assertFalse("Shouldn't Allow Past Year", admin.classExists("classB",2008));
    }
    @Test
    public void test_createClass_param_instructor() {
        admin.createClass("classC", 2017, "", 15);
        assertFalse("InstructorName CANNOT be Empty", admin.classExists("classC",2017));
    }
    @Test
    public void test_createClass_param_capacity() { // capacity > 0
        int capacity = 0;
        admin.createClass("classD", 2017, "random", capacity);
        assertFalse("Capacity Cannot be ZERO", admin.classExists("classD",2017));

        capacity = -1;
        admin.createClass("classE", 2017, "random", capacity);
        assertFalse("Capacity Cannot be NGEATIVE", admin.classExists("classE",2017));
    }

    @Test
    public void test_createClass_unique_pair() {
        admin.createClass("classF", 2017, "A", 10);
        assertTrue(admin.classExists("classF",2017));
        admin.createClass("classF", 2017, "B", 10);
        assertTrue("className and year pair must be unique even instructor is different",
                admin.getClassInstructor("classF",2017).equals("A"));
        assertFalse("className and year pair must be unique even instructor is different",
                    admin.getClassInstructor("classF",2017).equals("B"));

        admin.createClass("classF", 2017, "A", 15);
        assertFalse("className and instructorName must be unique pair and capacity must updated through changeCapacity",
                admin.getClassCapacity("classF",2017)==15);
        assertTrue("className and instructorName must be unique pair and capacity must updated through changeCapacity",
                admin.getClassCapacity("classF",2017)==10);
    }
    @Test
    public void test_createClass_instructor_course_limit() {
        admin.createClass("class1", 2017, "Z", 10);
        assertTrue("Error", admin.classExists("class1",2017));
        admin.createClass("class2", 2017, "Z", 15);
        assertTrue("Error", admin.classExists("class2",2017));
        admin.createClass("class3", 2017, "Z", 15);
        assertFalse("One instructor cannot have more than two courses at a time",
                admin.classExists("class3",2017));
    }
    @Test
    public void test_changeCapacity_param_className_year(){
        admin.createClass("classG", 2017, "Instructor4", 2);

        admin.changeCapacity("",2017,0);
        assertFalse("Data structure is corrupted with null classname",
                admin.getClassCapacity("classG",2017)==0);
        assertTrue("capactiy is corrupted after change",
                admin.getClassCapacity("classG",2017)==2);

        admin.changeCapacity("classG",0,3);
        assertFalse("Data structure is corrupted with invalid year",
                admin.getClassCapacity("classG",2017)==3);
        assertTrue("capactiy is corrupted after change",
                admin.getClassCapacity("classG",2017)==2);
    }
    @Test
    public void test_changeCapacity_param_capacity(){
        admin.createClass("classH", 2017, "Y", 2);

        admin.changeCapacity("classH",2017,0);
        assertFalse("Capacity cannot be changed below nor equal to 0",
                admin.getClassCapacity("classH",2017)==0);
        assertTrue("capactiy is corrupted after change",
                admin.getClassCapacity("classH",2017)==2);

        admin.changeCapacity("classH",2017,-1);
        assertFalse("Capacity cannot be changed below nor equal to 0",
                admin.getClassCapacity("classH",2017)==-1);
        assertTrue("capactiy is corrupted after change",
                admin.getClassCapacity("classH",2017)==2);
    }
    @Test
    public void test_changeCapacity_enrollment_condition(){
        admin.createClass("classI", 2017, "X", 2);
        Student student = new Student();
        student.registerForClass("A","classI",2017);
        student.registerForClass("B","classI",2017);

        admin.changeCapacity("classI",2017,3);
        assertTrue("Error on changing legit capacity",
                admin.getClassCapacity("classI",2017)==3);

        admin.changeCapacity("classI",2017,2);
        assertTrue("Capacity allows to be equal to enrollment number",
                admin.getClassCapacity("classI",2017)==2);

        admin.changeCapacity("classI",2017,1);
        assertFalse("Capacity canont below the enrollment number",
                admin.getClassCapacity("classA",2017)==1);
    }

}
