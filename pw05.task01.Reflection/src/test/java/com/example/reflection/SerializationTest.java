package com.example.reflection;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.*;

class SerializationTest {

    final class SimpleSquareClass {
        private String name = "square";
        private double width = 5;
        private double area = 25.0;
        public int version = 7;
        /*
        * Lexicographical order:
        * area, name, version, width
        * */
    }

    @Test
    void testSerializeSimpleSquareClass() throws IOException, IllegalAccessException {
        var simpleSquareClass = new SimpleSquareClass();

        try (var expectedByteArrayOutputStream = new ByteArrayOutputStream();
             var expectedOutputStream = new DataOutputStream(expectedByteArrayOutputStream);
             var receivedOutputStream = new ByteArrayOutputStream()) {

            expectedOutputStream.writeDouble(simpleSquareClass.area);
            expectedOutputStream.writeInt(simpleSquareClass.name.length());
            expectedOutputStream.writeChars(simpleSquareClass.name);
            expectedOutputStream.writeInt(simpleSquareClass.version);
            expectedOutputStream.writeDouble(simpleSquareClass.width);

            Serialization.serialize(simpleSquareClass, receivedOutputStream);
            assertArrayEquals(expectedByteArrayOutputStream.toByteArray(), receivedOutputStream.toByteArray());
        }
    }

    static class Shape {
        public final static float pi = (float) 3.14;

        private int version = 9;
        public String name = "shape";
        protected String buffer = "???";
        /*
         * Lexicographical order:
         * buffer, name, version
         * */
    }

    static public class Square extends Shape {

        private double width = 3;
        public byte byteVar = 12;
        /*
         * Lexicographical order:
         * byteVar, widthversion
         * */
    }

    static public class Rectangle extends Square {

        private double height = 10;
        public long longVar = 1000000000000000000L;
        /*
         * Lexicographical order:
         * height, longVar
         * */
    }

    static public class Circle extends Shape {

        protected double radius = 3.0;
        private short shortVar = (short) 214.124;
        private boolean someFlag = false;
        /*
         * Lexicographical order:
         * radius, shortVar, someFlag
         * */
    }

    static public class Ellipse extends Circle {
        private double focus = 5.0;
        public char symbol = 'y';
        public String message = "message";
        /*
         * Lexicographical order:
         * focus, message, symbol
         * */
    }

    @Test
    void testSerializingShape() throws IOException, IllegalAccessException {
        var shape = new Shape();

        try (var expectedByteArrayOutputStream = new ByteArrayOutputStream();
             var expectedOutputStream = new DataOutputStream(expectedByteArrayOutputStream);
             var receivedOutputStream = new ByteArrayOutputStream()) {

            expectedOutputStream.writeInt(shape.buffer.length());
            expectedOutputStream.writeChars(shape.buffer);
            expectedOutputStream.writeInt(shape.name.length());
            expectedOutputStream.writeChars(shape.name);
            expectedOutputStream.writeInt(shape.version);

            Serialization.serialize(shape, receivedOutputStream);
            assertArrayEquals(expectedByteArrayOutputStream.toByteArray(), receivedOutputStream.toByteArray());
        }
    }

    @Test
    void testSerializingSquare() throws IOException, IllegalAccessException {
        var square = new Square();

        try (var expectedByteArrayOutputStream = new ByteArrayOutputStream();
             var expectedOutputStream = new DataOutputStream(expectedByteArrayOutputStream);
             var receivedOutputStream = new ByteArrayOutputStream()) {

            expectedOutputStream.writeByte(square.byteVar);
            expectedOutputStream.writeDouble(square.width);

            expectedOutputStream.writeInt(square.buffer.length());
            expectedOutputStream.writeChars(square.buffer);
            expectedOutputStream.writeInt(square.name.length());
            expectedOutputStream.writeChars(square.name);
            expectedOutputStream.writeInt(((Shape) square).version);

            Serialization.serialize(square, receivedOutputStream);
            assertArrayEquals(expectedByteArrayOutputStream.toByteArray(), receivedOutputStream.toByteArray());
        }
    }

    @Test
    void testSerializingRectangle() throws IOException, IllegalAccessException {
        var rectangle = new Rectangle();

        try (var expectedByteArrayOutputStream = new ByteArrayOutputStream();
             var expectedOutputStream = new DataOutputStream(expectedByteArrayOutputStream);
             var receivedOutputStream = new ByteArrayOutputStream()) {

            expectedOutputStream.writeDouble(rectangle.height);
            expectedOutputStream.writeLong(rectangle.longVar);

            expectedOutputStream.writeByte(rectangle.byteVar);
            expectedOutputStream.writeDouble(((Square) rectangle).width);

            expectedOutputStream.writeInt(rectangle.buffer.length());
            expectedOutputStream.writeChars(rectangle.buffer);
            expectedOutputStream.writeInt(rectangle.name.length());
            expectedOutputStream.writeChars(rectangle.name);
            expectedOutputStream.writeInt(((Shape) rectangle).version);

            Serialization.serialize(rectangle, receivedOutputStream);
            assertArrayEquals(expectedByteArrayOutputStream.toByteArray(), receivedOutputStream.toByteArray());
        }
    }

    @Test
    void testSerializingCircle() throws IOException, IllegalAccessException {
        var circle = new Circle();

        try (var expectedByteArrayOutputStream = new ByteArrayOutputStream();
             var expectedOutputStream = new DataOutputStream(expectedByteArrayOutputStream);
             var receivedOutputStream = new ByteArrayOutputStream()) {

            expectedOutputStream.writeDouble(circle.radius);
            expectedOutputStream.writeShort(circle.shortVar);
            expectedOutputStream.writeBoolean(circle.someFlag);

            expectedOutputStream.writeInt(circle.buffer.length());
            expectedOutputStream.writeChars(circle.buffer);
            expectedOutputStream.writeInt(circle.name.length());
            expectedOutputStream.writeChars(circle.name);
            expectedOutputStream.writeInt(((Shape) circle).version);

            Serialization.serialize(circle, receivedOutputStream);
            assertArrayEquals(expectedByteArrayOutputStream.toByteArray(), receivedOutputStream.toByteArray());
        }
    }

    @Test
    void testSerializingEllipse() throws IOException, IllegalAccessException {
        var ellipse = new Ellipse();

        try (var expectedByteArrayOutputStream = new ByteArrayOutputStream();
             var expectedOutputStream = new DataOutputStream(expectedByteArrayOutputStream);
             var receivedOutputStream = new ByteArrayOutputStream()) {

            expectedOutputStream.writeDouble(ellipse.focus);
            expectedOutputStream.writeInt(ellipse.message.length());
            expectedOutputStream.writeChars(ellipse.message);
            expectedOutputStream.writeChar(ellipse.symbol);

            expectedOutputStream.writeDouble(ellipse.radius);
            expectedOutputStream.writeShort(((Circle) ellipse).shortVar);
            expectedOutputStream.writeBoolean(((Circle) ellipse).someFlag);

            expectedOutputStream.writeInt(ellipse.buffer.length());
            expectedOutputStream.writeChars(ellipse.buffer);
            expectedOutputStream.writeInt(ellipse.name.length());
            expectedOutputStream.writeChars(ellipse.name);
            expectedOutputStream.writeInt(((Shape) ellipse).version);

            Serialization.serialize(ellipse, receivedOutputStream);
            assertArrayEquals(expectedByteArrayOutputStream.toByteArray(), receivedOutputStream.toByteArray());
        }
    }

    @Test
    void testSerializingWithNullArguments() {
        var ellipse = new Ellipse();
        assertThrows(IllegalArgumentException.class, () -> Serialization.serialize(ellipse, null));
    }


}