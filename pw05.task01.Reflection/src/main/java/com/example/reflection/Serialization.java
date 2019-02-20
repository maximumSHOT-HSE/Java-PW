package com.example.reflection;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Serialization {
    public static void serialize(Object object, OutputStream out) throws IllegalAccessException, IOException {
        if (out == null) {
            throw new IllegalArgumentException();
        }

        var dataOut = new DataOutputStream(out);

        for (var field: getAllFields(object.getClass())) {
            field.setAccessible(true);

            if (field.getType() == boolean.class) {
                dataOut.writeBoolean((Boolean) field.get(object));
            } else if (field.getType() == byte.class) {
                dataOut.writeByte((Byte) field.get(object));
            } else if (field.getType() == char.class) {
                dataOut.writeChar((Character) field.get(object));
            } else if (field.getType() == short.class) {
                dataOut.writeShort((Short) field.get(object));
            } else if (field.getType() == int.class) {
                dataOut.writeInt((Integer) field.get(object));
            } else if (field.getType() == long.class) {
                dataOut.writeLong((Long) field.get(object));
            } else if (field.getType() == float.class) {
                dataOut.writeFloat((Float) field.get(object));
            } else if (field.getType() == double.class) {
                dataOut.writeDouble((Double) field.get(object));
            } else if (field.getType() == String.class) {
                dataOut.writeInt(((String) field.get(object)).length());
                dataOut.writeChars((String) field.get(object));
            }
        }

        dataOut.close();
    }

    public static <T> T deserialize(InputStream in, Class<T> clazz) throws IllegalAccessException, IOException {
        if (in == null) {
            throw new IllegalArgumentException();
        }

        T result = null;
        try {
            result = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception ignored) {
//             unreachable
        }

        assert result != null;

        var dataIn = new DataInputStream(in);

        for (var field: getAllFields(result.getClass())) {
            field.setAccessible(true);

            if (field.getType() == boolean.class) {
                field.setBoolean(result, dataIn.readBoolean());
            } else if (field.getType() == byte.class) {
                field.setByte(result, dataIn.readByte());
            } else if (field.getType() == char.class) {
                field.setChar(result, dataIn.readChar());
            } else if (field.getType() == short.class) {
                field.setShort(result, dataIn.readShort());
            } else if (field.getType() == int.class) {
                field.setInt(result, dataIn.readInt());
            } else if (field.getType() == long.class) {
                field.setLong(result, dataIn.readLong());
            } else if (field.getType() == float.class) {
                field.setFloat(result, dataIn.readFloat());
            } else if (field.getType() == double.class) {
                field.setDouble(result, dataIn.readDouble());
            } else if (field.getType() == String.class) {
                int size = dataIn.readInt();
                var resultStringBuilder = new StringBuilder();
                for (int i = 0; i < size; ++i) {
                    resultStringBuilder.append(dataIn.readChar());
                }
                field.set(result, resultStringBuilder.toString());
            }
        }

        dataIn.close();

        return result;
    }

    private static List<Field> getAllFields(Class<?> clazz) {
        var currentClass = clazz;

        var result = new LinkedList<Field>();

        while (currentClass != null) {
            var fields = currentClass.getDeclaredFields();
            Arrays.sort(fields, Comparator.comparing(Field::getName));

            for (var field: fields) {
                if (field.isSynthetic() || Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                result.add(field);
            }

            currentClass = currentClass.getSuperclass();
        }
        return result;
    }
}
