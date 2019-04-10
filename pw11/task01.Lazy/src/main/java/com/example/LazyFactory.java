package com.example;

import java.util.Optional;
import java.util.function.Supplier;

public class LazyFactory {

    public static <T> Lazy<T> createOneThreadLazy(Supplier<T> supplier) {

        return new Lazy<T>() {
            private Optional<T> data;

            @Override
            public T get() {
                if (data == null) {
                    data = Optional.ofNullable(supplier.get());
                }
                return data.get();
            }
        };
    }

    public static <T> Lazy<T> createMultiThreadLazy(Supplier<T> supplier) {
        return new Lazy<T>() {

            private volatile Optional<T> data;

            @Override
            public T get() {
                if (data == null) {
                    synchronized (this) {

                    }
                }
            }
        }
    }
}
