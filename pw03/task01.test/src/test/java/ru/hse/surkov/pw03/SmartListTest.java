package ru.hse.surkov.pw03;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SmartListTest {

    private List<Integer> l;

    @BeforeEach
    void setUp() {
        l = new SmartList<>();
    }

    @Test
    void testDefaultConstructor() {
        assertEquals(0, l.size());
        for (int i = 0; i <= 10; i++) {
            assertFalse(l.contains(i));
        }
    }

    @Test
    void testConstructorSet() {
        for (int sz = 0; sz <= 10; sz++) {
            Set<Integer> s = new TreeSet<>();
            for (int i = sz; i >= 1; i--) {
                s.add(i);
            }
            l = new SmartList<>(s);
            for (var x : s) {
                assertTrue(l.contains(x));
            }
            for (int i = 1; i <= sz; i++) {
                Integer xx = l.get(i - 1);
                assertNotNull(xx);
                int x = xx;
                assertEquals(i, x);
            }
        }
    }

    @Test
    void testSize() {
        for (int sz = 0; sz <= 10; sz++) {
            l = new SmartList<>();
            for (int i = 1; i <= sz; i++) {
                l.add(i);
            }
            assertEquals(sz, l.size());
        }
    }

    @Test
    void testADddNulls() {
        for (int sz = 0; sz <= 10; sz++) {
            l = new SmartList<>();
            for (int i = 0; i < sz; i++) {
                l.add(null);
            }
            for (int i = 0; i < sz; i++) {
                assertNull(l.get(i));
            }
        }
    }

    @Test
    void testRemoveException() {
        for (int sz = 0; sz <= 10; sz++) {
            l = new SmartList<>();
            for (int i = 1; i <= sz; i++) {
                l.add(i);
            }
            for (int i = -20; i <= 20; i++) {
                if (0 <= i && i < sz) {
                    continue;
                }
                int finalI = i;
                assertThrows(IndexOutOfBoundsException.class, () -> l.remove(finalI));
            }
        }

        for (int sz = 0; sz <= 10; sz++) {
            l = new SmartList<>();
            for (int i = 1; i <= sz; i++) {
                l.add(i);
            }
            for (int i = 1; i <= sz + 10; i++) {
                if (i <= sz) {
                    assertEquals(Optional.of(i), Optional.ofNullable(l.remove(0)));
                }
            }
        }
    }

    @Test
    void testGetException() {
        for (int sz = 0; sz <= 10; sz++) {
            l = new SmartList<>();
            for (int i = 1; i <= sz; i++) {
                l.add(i);
            }
            for (int i = -20; i <= 20; i++) {
                if (0 <= i && i < sz) {
                    assertEquals(Optional.of(i + 1), Optional.ofNullable(l.get(i)));
                } else {
                    int finalI = i;
                    assertThrows(IndexOutOfBoundsException.class, () -> l.get(finalI));
                }
            }
        }
    }



    // MUST HAVE TESTS BEGIN

    @Test
    public void testSimple() {
        List<Integer> list = newList();

        assertEquals(Collections.<Integer>emptyList(), list);

        list.add(1);
        assertEquals(Collections.singletonList(1), list);

        list.add(2);
        assertEquals(Arrays.asList(1, 2), list);
    }

    @Test
    public void testGetSet() {
        List<Object> list = newList();

        list.add(1);

        assertEquals(1, list.get(0));
        assertEquals(1, list.set(0, 2));
        assertEquals(2, list.get(0));
        assertEquals(2, list.set(0, 1));

        list.add(2);

        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));

        assertEquals(1, list.set(0, 2));

        assertEquals(Arrays.asList(2, 2), list);
    }

    @Test
    public void testRemove() throws Exception {
        List<Object> list = newList();

        list.add(1);
        list.remove(0);
        assertEquals(Collections.emptyList(), list);

        list.add(2);
        list.remove((Object) 2);
        assertEquals(Collections.emptyList(), list);

        list.add(1);
        list.add(2);
        assertEquals(Arrays.asList(1, 2), list);

        list.remove(0);
        assertEquals(Collections.singletonList(2), list);

        list.remove(0);
        assertEquals(Collections.emptyList(), list);
    }

    @Test
    public void testIteratorRemove() throws Exception {
        List<Object> list = newList();
        assertFalse(list.iterator().hasNext());

        list.add(1);

        Iterator<Object> iterator = list.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next());

        iterator.remove();
        assertFalse(iterator.hasNext());
        assertEquals(Collections.emptyList(), list);

        list.addAll(Arrays.asList(1, 2));

        iterator = list.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next());

        iterator.remove();
        assertTrue(iterator.hasNext());
        assertEquals(Collections.singletonList(2), list);
        assertEquals(2, iterator.next());

        iterator.remove();
        assertFalse(iterator.hasNext());
        assertEquals(Collections.emptyList(), list);
    }


    @Test
    public void testCollectionConstructor() throws Exception {
        assertEquals(Collections.emptyList(), newList(Collections.emptyList()));
        assertEquals(
                Collections.singletonList(1),
                newList(Collections.singletonList(1)));

        assertEquals(
                Arrays.asList(1, 2),
                newList(Arrays.asList(1, 2)));
    }

    @Test
    public void testAddManyElementsThenRemove() throws Exception {
        List<Object> list = newList();
        for (int i = 0; i < 7; i++) {
            list.add(i + 1);
        }

        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7), list);

        for (int i = 0; i < 7; i++) {
            list.remove(list.size() - 1);
            assertEquals(6 - i, list.size());
        }

        assertEquals(Collections.emptyList(), list);
    }

    private static <T> List<T> newList() {
        try {
            return (List<T>) getListClass().getConstructor().newInstance();
        } catch (InstantiationException | ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> List<T> newList(Collection<T> collection) {
        try {
            return (List<T>) getListClass().getConstructor(Collection.class).newInstance(collection);
        } catch (InstantiationException | ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static Class<?> getListClass() throws ClassNotFoundException {
        return Class.forName("ru.hse.surkov.pw03.SmartList");
    }
}