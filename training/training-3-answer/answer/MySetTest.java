import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertTrue;

public class MySetTest {
    private static MySet mySet = new MySet();
    private static Integer size = 0;

    public boolean deletepreOK(int x) {
        if (mySet.contains(x)) {
            return true;
        } else {
            System.out.println("Fail before we delete " + x);
            return false;
        }
    }

    public boolean insertpreOK(int x) {
        if (!mySet.contains(x)) {
            return true;
        } else {
            System.out.println("Fail before we insert " + x);
            return false;
        }
    }

    public boolean deleteafterOK(int x) {
        if (!mySet.contains(x) && mySet.size() == --size) {
            return true;
        } else {
            System.out.println("Fail after we delete " + x);
            return false;
        }
    }

    public boolean insertafterOK(int x) {
        if (mySet.contains(x) && mySet.size() == ++size) {
            return true;
        } else {
            System.out.println("Fail after we insert " + x);
            return false;
        }
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        System.out.println("Test Start!");
    }

    @AfterClass
    public static void afterClass() throws Exception {
        System.out.println("Test End!");
    }

    @Before
    public void setUp() throws Exception {
        mySet = new MySet();
        size = 0;
    }

    @Test
    public void insert() {
        System.out.println("----------");
        System.out.println("Test insert!");

        for (int i = 4; i >= 0; i--) {
            assertTrue(mySet.repOK());
            assertTrue(insertpreOK(i));
            mySet.insert(i);
            assertTrue(insertafterOK(i));
            assertTrue(mySet.repOK());
            System.out.println("#Insert number: " + i);
            System.out.println("mySet.size(): " + mySet.size());
            System.out.println("size: " + size);
        }
        Random r = new Random();
        for (int i = 0; i < 5; ++i) {
            int k = r.nextInt(900);
            assertTrue(mySet.repOK());
            assertTrue(insertpreOK(k));
            mySet.insert(k);
            assertTrue(insertafterOK(k));
            assertTrue(mySet.repOK());
            System.out.println("#Insert number: " + k);
            System.out.println("mySet.size(): " + mySet.size());
            System.out.println("size: " + size);
        }
        System.out.println("Test insert finish!");
        System.out.println("----------");
    }

    @Test
    public void delete() {
        System.out.println("Test delete!");
        System.out.println("----------");

        ArrayList<Integer> rev = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 5; ++i) {
            int k = r.nextInt(900);
            rev.add(k);
            mySet.insert(k);
            size++;
        }
        for (int i : rev) {
            assertTrue(mySet.repOK());
            assertTrue(deletepreOK(i));
            mySet.delete(i);
            assertTrue(deleteafterOK(i));
            assertTrue(mySet.repOK());
            System.out.println("#Delete number: " + i);
            System.out.println("mySet.size(): " + mySet.size());
            System.out.println("size: " + size);
        }
        System.out.println("Test delete finish!");
        System.out.println("----------");
    }
}