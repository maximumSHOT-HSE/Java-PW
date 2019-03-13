package ru.hse.surkov.pw08;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Pair<K, V> {
    @Nullable public K first;
    @Nullable public V second;
    private int version;

    public Pair(@NotNull K first, @NotNull V second) {
        this.first = first;
        this.second = second;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}