import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.LinkedList.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ListTest {

    List<String> list;

    @BeforeEach
    void createList() {
        list = new LinkedList<String>();
    }

    @Test
    void test() {

    }

    @Test
    void testSizeEmptyList() {
        assertEquals(0, list.size());
    }

    @Test
    void testSizeFilledList() {
        for (int i = 1; i <= 10; i++) {
            list.add(Integer.toString(i));
            assertEquals(i, list.size());
        }
    }

    @Test
    void testIsEmptyWithEmptyList() {
        assertTrue(list.isEmpty());
    }

    @Test
    void testIsEmptyfilledList() {
        for (int i = 1; i <= 10; i++) {
            list.add(Integer.toString(i));
            assertFalse(list.isEmpty());
        }
    }

    @Test
    void testContains() {
        for (int i = 1; i <= 10; i += 2) {
            list.add(Integer.toString(i));
        }
        for (int i = 1; i <= 10; i++) {
            assertEquals(i % 2 == 1, list.contains(Integer.toString(i)));
        }
    }

    @Test
    void testFor() {
        for (int i = 1; i <= 100; i++) {
            list.add(Integer.toString(i));
        }
        List<String> helper = new LinkedList<>();
        for (var s : list) {
            helper.add(s);
        }
        for (int i = 1; i <= 200; i++) {
            assertEquals(i <= 100, helper.contains(Integer.toString(i)));
        }
    }

    @Test
    void testRemove() {
        for (int i = 1; i <= 100; i++) {
            list.add(Integer.toString(i));
        }
        for (int i = 1; i <= 100; i += 2) {
            assertTrue(list.remove(Integer.toString(i)));
            assertFalse(list.remove(Integer.toString(i)));
        }
        for (int i = 1; i <= 100; i++) {
            assertEquals(i % 2 == 1, list.contains(Integer.toString(i)));
        }
    }

    @Test
    void testAdd() {
        for (int i = 1; i <= 100; i++) {
            assertFalse(list.contains(Integer.toString(i)));
            assertTrue(list.add(Integer.toString(i)));
            assertTrue(list.contains(Integer.toString(i)));
        }
        for (int i = 1; i <= 100; i++) {
            assertEquals(Integer.toString(i), list.get(i - 1));
        }
        list.add(0, "a");
        assertEquals("a", list.get(0));
        list.add(3, "c");
        assertEquals("c", list.get(3));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1000000));
    }

}
