package com.example;

/** Interface for lazy calculation of result of Supplier function. */
public interface Lazy<T> {
    T get();
}
