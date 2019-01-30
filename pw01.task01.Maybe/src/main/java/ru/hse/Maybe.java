package ru.hse;

import java.util.NoSuchElementException;
import java.util.function.Function;

public class Maybe<T> {

    private T value;
    private boolean nothing;

    public static <T> Maybe<T> just(T t) {
        var result = new Maybe<T>();
        result.value = t;
        result.nothing = false;
        return result;
    }

    public static <T> Maybe<T> nothing() {
        var result = new Maybe<T>();
        result.nothing = true;
        return result;
    }

    public T get() {
        if (nothing) {
            throw new NoSuchElementException("get on nothing is forbidden");
        }
        return value;
    }

    public boolean isPresent() {
        return !nothing;
    }

    public <U> Maybe<U> map(Function<? super T, U> mapper) {
        if (nothing) {
            return nothing();
        }
        return just(mapper.apply(value));
    }
}
