package com.example;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class LazyFactory {

    public static <T> Lazy<T> createOneThreadLazy(Supplier<T> supplier) {

        return new Lazy<T>() {
            @Nullable private Optional<T> data;

            @Override
            public T get() {
                if (data == null) {
                    data = Optional.ofNullable(supplier.get());
                }
                return data.orElse(null);
            }
        };
    }

    public static <T> Lazy<T> createMultiThreadLazy(Supplier<T> supplier) {

        return new Lazy<T>() {

            @Nullable private volatile Optional<T> data;

            @Override
            public T get() {
                if (data == null) {
                    synchronized (this) {
                        if (data == null) {
                            data = Optional.ofNullable(supplier.get());
                        }
                    }
                }
                return data.orElse(null);
            }
        };
    }

    public static <T> Lazy<T> createLocklessMultiThreadLazy(Supplier<T> supplier) {

        return new Lazy<>() {
            private AtomicReference<Optional<T>> dataReference = new AtomicReference<>();

            @Override
            public T get() {
                Optional<T> data = dataReference.get();

                if (data == null) {
                    data = Optional.ofNullable(supplier.get());
                    if (dataReference.compareAndSet(null, data)) {
                        return data.orElse(null);
                    } else {
                        return dataReference.get().orElse(null);
                    }
                }
                return data.orElse(null);
            }
        };
    }
}
