package ru.hse;

import java.util.NoSuchElementException;
import java.util.function.Function;

public class Maybe<T> {

    private T value;
    private boolean nothing;

    /** Wrap value into Maybe. */
    public static <T> Maybe<T> just(T t) {
        var result = new Maybe<T>();
        result.value = t;
        result.nothing = false;
        return result;
    }

    /** Returns Maybe without a value. */
    public static <T> Maybe<T> nothing() {
        var result = new Maybe<T>();
        result.nothing = true;
        return result;
    }

    /** Returns stored value.
     * @throws NoSuchElementException when there is nothing stored
     */
    public T get() {
        if (nothing) {
            throw new NoSuchElementException("get on nothing is forbidden");
        }
        return value;
    }

    /**  Check that the value is present. */
    public boolean isPresent() {
        return !nothing;
    }

    /** Apply function, taking absence of value into account. */
    public <U> Maybe<U> map(Function<? super T, U> mapper) {
        if (nothing) {
            return nothing();
        }
        return just(mapper.apply(value));
    }
}
