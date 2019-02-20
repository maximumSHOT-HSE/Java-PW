package com.example.reflection;

import java.io.InputStream;
import java.io.OutputStream;

public class Serialization {
    public static void serialize(Object object, OutputStream out) {

    }

    public static <T> T deserialize(InputStream in, Class<T> clazz) {
        return null;
    }
}
