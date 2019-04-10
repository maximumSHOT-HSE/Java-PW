package com.example;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.function.Supplier;

public class LocklessMultiThreadLazy<T> implements Lazy<T> {

    private Supplier<T> supplier;
    private volatile Optional<T> data;

    public LocklessMultiThreadLazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    private static final AtomicReferenceFieldUpdater<Optional, Optional> update =
            AtomicReferenceFieldUpdater.newUpdater(Optional.class, Optional.class, "data");

    @Override
    public T get() {
        update.compareAndSet(
            data, null, Optional.ofNullable(supplier.get())
        );
        return data.orElse(null);
    }
}
