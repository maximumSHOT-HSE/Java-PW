package ru.hse;

import java.util.NoSuchElementException;
import java.util.function.Function;

public class Maybe<T> {

    private T link;

    public static <T> Maybe<T> just(T t) {
        var result = new Maybe<T>();
        result.link = t;
        return result;
    }

    public static <T> Maybe<T> nothing() {
        return new Maybe<T>();
    }

    public T get() {
        if (link == null) {
            throw new NoSuchElementException("get on nothing is forbidden");
        }
        return link;
    }

    public <U> Maybe<U> map(Function<? super T, U> mapper) {
        if (link == null) {
            return nothing();
        }
        return just(mapper.apply(link));
    }
}
