package com.example.reflection;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class SerializationTest {

    static public final class SimpleSquareClass {
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

    @Test
    void testDeserializeSimpleSquareClass() throws IOException, IllegalAccessException {
        var simpleSquare = new SimpleSquareClass();

        try (var generatedByteArrayOutputStream = new ByteArrayOutputStream();
             var generatedOutputStream = new DataOutputStream(generatedByteArrayOutputStream)) {

            generatedOutputStream.writeDouble(simpleSquare.area);
            generatedOutputStream.writeInt(simpleSquare.name.length());
            generatedOutputStream.writeChars(simpleSquare.name);
            generatedOutputStream.writeInt(simpleSquare.version);
            generatedOutputStream.writeDouble(simpleSquare.width);

            var generatedInputStream = new ByteArrayInputStream(generatedByteArrayOutputStream.toByteArray());
            var deserializedSimpleSquare = Serialization.deserialize(generatedInputStream, SimpleSquareClass.class);

            assertEquals(simpleSquare.area, deserializedSimpleSquare.area);
            assertEquals(simpleSquare.name, deserializedSimpleSquare.name);
            assertEquals(simpleSquare.version, deserializedSimpleSquare.version);
            assertEquals(simpleSquare.width, deserializedSimpleSquare.width);
        }
    }


    public static class Shape {
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
    void testDeserializingShape() throws IOException, IllegalAccessException {
        var shape = new Shape();

        try (var generatedByteArrayOutputStream = new ByteArrayOutputStream();
             var generatedOutputStream = new DataOutputStream(generatedByteArrayOutputStream)) {

            generatedOutputStream.writeInt(shape.buffer.length());
            generatedOutputStream.writeChars(shape.buffer);
            generatedOutputStream.writeInt(shape.name.length());
            generatedOutputStream.writeChars(shape.name);
            generatedOutputStream.writeInt(shape.version);

            var generatedInputStream = new ByteArrayInputStream(generatedByteArrayOutputStream.toByteArray());
            var deserializedShape = Serialization.deserialize(generatedInputStream, Shape.class);

            assertEquals(shape.buffer, deserializedShape.buffer);
            assertEquals(shape.name, deserializedShape.name);
            assertEquals(shape.version, deserializedShape.version);
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
    void testDeserializingSquare() throws IOException, IllegalAccessException {
        var square = new Square();

        try (var generatedByteArrayOutputStream = new ByteArrayOutputStream();
             var generatedOutputStream = new DataOutputStream(generatedByteArrayOutputStream)) {

            generatedOutputStream.writeByte(square.byteVar);
            generatedOutputStream.writeDouble(square.width);

            generatedOutputStream.writeInt(square.buffer.length());
            generatedOutputStream.writeChars(square.buffer);
            generatedOutputStream.writeInt(square.name.length());
            generatedOutputStream.writeChars(square.name);
            generatedOutputStream.writeInt(((Shape) square).version);

            var generatedInputStream = new ByteArrayInputStream(generatedByteArrayOutputStream.toByteArray());
            var deserializedSquare = Serialization.deserialize(generatedInputStream, Square.class);

            assertEquals(square.byteVar, deserializedSquare.byteVar);
            assertEquals(square.width, deserializedSquare.width);
            assertEquals(square.buffer, deserializedSquare.buffer);
            assertEquals(square.name, deserializedSquare.name);
            assertEquals(((Shape) square).version, ((Shape) deserializedSquare).version);
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
    void testDeserializingRectangle() throws IOException, IllegalAccessException {
        var rectangle = new Rectangle();

        try (var generatedByteArrayOutputStream = new ByteArrayOutputStream();
             var generatedOutputStream = new DataOutputStream(generatedByteArrayOutputStream)) {

            generatedOutputStream.writeDouble(rectangle.height);
            generatedOutputStream.writeLong(rectangle.longVar);

            generatedOutputStream.writeByte(rectangle.byteVar);
            generatedOutputStream.writeDouble(((Square) rectangle).width);

            generatedOutputStream.writeInt(rectangle.buffer.length());
            generatedOutputStream.writeChars(rectangle.buffer);
            generatedOutputStream.writeInt(rectangle.name.length());
            generatedOutputStream.writeChars(rectangle.name);
            generatedOutputStream.writeInt(((Shape) rectangle).version);

            var generatedInputStream = new ByteArrayInputStream(generatedByteArrayOutputStream.toByteArray());
            var deserializedRectangle = Serialization.deserialize(generatedInputStream, Rectangle.class);

            assertEquals(rectangle.height, deserializedRectangle.height);
            assertEquals(rectangle.longVar, deserializedRectangle.longVar);
            assertEquals(rectangle.byteVar, deserializedRectangle.byteVar);
            assertEquals(((Square) rectangle).width, ((Square) deserializedRectangle).width);
            assertEquals(rectangle.buffer, deserializedRectangle.buffer);
            assertEquals(rectangle.name, deserializedRectangle.name);
            assertEquals(((Shape) rectangle).version, ((Shape) deserializedRectangle).version);
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
    void testDeserializingCircle() throws IOException, IllegalAccessException {
        var circle = new Circle();

        try (var generatedByteArrayOutputStream = new ByteArrayOutputStream();
             var generatedOutputStream = new DataOutputStream(generatedByteArrayOutputStream);) {

            generatedOutputStream.writeDouble(circle.radius);
            generatedOutputStream.writeShort(circle.shortVar);
            generatedOutputStream.writeBoolean(circle.someFlag);

            generatedOutputStream.writeInt(circle.buffer.length());
            generatedOutputStream.writeChars(circle.buffer);
            generatedOutputStream.writeInt(circle.name.length());
            generatedOutputStream.writeChars(circle.name);
            generatedOutputStream.writeInt(((Shape) circle).version);

            var generatedInputStream = new ByteArrayInputStream(generatedByteArrayOutputStream.toByteArray());
            var deserializedCircle = Serialization.deserialize(generatedInputStream, Circle.class);

            assertEquals(circle.radius, deserializedCircle.radius);
            assertEquals(circle.shortVar, deserializedCircle.shortVar);
            assertEquals(circle.someFlag, deserializedCircle.someFlag);

            assertEquals(circle.buffer, deserializedCircle.buffer);
            assertEquals(circle.name, deserializedCircle.name);
            assertEquals(((Shape) circle).version, ((Shape) deserializedCircle).version);
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
    void testDeserializingEllipse() throws IOException, IllegalAccessException {
        var ellipse = new Ellipse();

        try (var generatedByteArrayOutputStream = new ByteArrayOutputStream();
             var generatedOutputStream = new DataOutputStream(generatedByteArrayOutputStream)) {

            generatedOutputStream.writeDouble(ellipse.focus);
            generatedOutputStream.writeInt(ellipse.message.length());
            generatedOutputStream.writeChars(ellipse.message);
            generatedOutputStream.writeChar(ellipse.symbol);

            generatedOutputStream.writeDouble(ellipse.radius);
            generatedOutputStream.writeShort(((Circle) ellipse).shortVar);
            generatedOutputStream.writeBoolean(((Circle) ellipse).someFlag);

            generatedOutputStream.writeInt(ellipse.buffer.length());
            generatedOutputStream.writeChars(ellipse.buffer);
            generatedOutputStream.writeInt(ellipse.name.length());
            generatedOutputStream.writeChars(ellipse.name);
            generatedOutputStream.writeInt(((Shape) ellipse).version);

            var generatedInputStream = new ByteArrayInputStream(generatedByteArrayOutputStream.toByteArray());
            var deserializedEllipse = Serialization.deserialize(generatedInputStream, Ellipse.class);

            assertEquals(ellipse.focus, deserializedEllipse.focus);
            assertEquals(ellipse.message, deserializedEllipse.message);
            assertEquals(ellipse.symbol, deserializedEllipse.symbol);

            assertEquals(ellipse.radius, deserializedEllipse.radius);
            assertEquals(((Circle) ellipse).shortVar, ((Circle) deserializedEllipse).shortVar);
            assertEquals(((Circle) ellipse).someFlag, ((Circle) deserializedEllipse).someFlag);

            assertEquals(ellipse.buffer, deserializedEllipse.buffer);
            assertEquals(ellipse.name, deserializedEllipse.name);
            assertEquals(((Shape) ellipse).version, ((Shape) deserializedEllipse).version);
        }
    }

    @Test
    void testSerializingWithNullArguments() {
        var ellipse = new Ellipse();
        assertThrows(IllegalArgumentException.class, () -> Serialization.serialize(ellipse, null));
    }

    @Test
    void testDeserializingWithNullArguments() {
        assertThrows(IllegalArgumentException.class, () -> Serialization.deserialize(null, Ellipse.class));
    }
}