package spring;

import junit.framework.TestCase;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: eran
 * Date: 11/7/13
 * Time: 10:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClassTest extends TestCase{

    public static void test1(){
        Class cif = new Class();
        Student s1 = mock(Student.class);
        cif.addNewStudent(s1);
        Assert.assertThat("Class has more than one element", cif.getNumberOfStudents(), CoreMatchers.equalTo(1));
        Student s2 = mock(Student.class);
        cif.addNewStudent(s2);
        when(s1.getName()).thenReturn("John");
        System.out.println(s1.getName());
        when(s1.getId()).thenReturn(10);
        when(s2.getName()).thenReturn("Elinor");
        when(s2.getId()).thenReturn(12);
        Assert.assertEquals(12,(int) cif.getId("Elinor"));
        verify(s2, never()).getName();

    }
}
