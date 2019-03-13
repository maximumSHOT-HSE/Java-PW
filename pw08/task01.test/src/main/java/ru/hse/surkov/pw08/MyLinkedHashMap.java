package ru.hse.surkov.pw08;

import jdk.jshell.spi.ExecutionControl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

public class MyLinkedHashMap <K, V> extends AbstractMap<K, V> implements Map<K, V> {

    private static final int START_CAPACITY = 10;

    private int currentVersionCounter;
    private int elementsNumber;
    private MyArray<Pair<K, V>> table = new MyArray<>(START_CAPACITY);
    private MyList<Pair<K, V>> order = new MyList<>();

    private int getHash(Object object, int arraySize) {
        int hash = object.hashCode();
        hash = (hash % arraySize + arraySize) % arraySize;
        return hash;
    }

    /**
     * If there is value related with given key
     * then method finds position appropriate.
     * Otherwise method finds first position
     * without any entries.
     * */
    private int getFirstValidPosition(@NotNull K key) {
        int position = getHash(key, table.size());
        int tableSize = table.size();
        while (true) {
            Pair<K, V> entry = table.get(position);
            if (entry == null || key.equals(entry.first)) {
                return position;
            }
            if (++position == tableSize) {
                position = 0;
            }
        }
    }

    private Pair<K, V> getEntry(K key) {
        int position = getFirstValidPosition(key);
        return table.get(position);
    }

    private void twiceCapacity() {
        MyList<Pair<K, V>> updatedOrder = new MyList<>();
        for (var entryObject : order) {
            Pair<K, V> entry = (Pair<K, V>) entryObject;
            Pair<K, V> entryInTable = getEntry(entry.first);
            if (entryInTable != null && entry.getVersion() == entryInTable.getVersion()) {
                updatedOrder.add(entry);
            }
        }
        order = updatedOrder;
        table = new MyArray<>(table.size() * 2);
        for (var entryObject : order) {
            Pair<K, V> entry = (Pair<K, V>) entryObject;
            put(entry.first, entry.second);
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    @Override
    public int size() {
        return elementsNumber;
    }

    @Override
    public boolean isEmpty() {
        return elementsNumber == 0;
    }

    @Override
    public boolean containsKey(@NotNull Object keyObject) {
        V value = get(keyObject);
        return value != null;
    }

    @Override
    public V get(@NotNull Object keyObject) {
        K key = (K) keyObject;
        Pair<K, V> entry = getEntry(key);
        return entry == null ? null : entry.second;
    }

    @Override
    @Nullable public V put(@NotNull K key, @NotNull V value) {
        if (elementsNumber == table.size()) {
            twiceCapacity();
        }
        int position = getFirstValidPosition(key);
        Pair<K, V> entry = getEntry(key);
        V previousValue = null;
        if (entry != null) {
            previousValue = entry.second;
        } else {
            elementsNumber++;
            entry = new Pair<>(key, value);
        }
        entry.setVersion(currentVersionCounter++);
        entry.second = value;
        table.put(position, entry);
        order.add(entry);
        return previousValue;
    }

    @Override
    public V remove(@NotNull Object keyObject) {
        K key = (K) keyObject;
        Pair<K, V> entry = getEntry(key);
        int position = getFirstValidPosition(key);
        table.remove(position);
        if (entry != null) {
            elementsNumber--;
        }
        return entry == null ? null : entry.second;
    }

    @Override
    public void clear() {
        elementsNumber = 0;
        table.clear();
        order.clear();
    }
}
