package ru.hse;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Scenario {
    /*
     * Args: file name
     * */
    public static void main(String[] args) throws IOException {
//        if (args.length != 2) {
//            throw new IllegalArgumentException("Exactly 2 arguments should be provided");
//        }
//        String inputFileName = args[0];
//        String outputFileName = args[1];
        String inputFileName = "/home/maximumshot/HSE/Java/Java-PW/pw01.task01.Maybe/src/main/resources/input.txt";
        String outputFileName = "/home/maximumshot/HSE/Java/Java-PW/pw01.task01.Maybe/src/main/resources/output.txt";
        try( Scanner scanner = new Scanner(
                new BufferedInputStream(
                        new FileInputStream(
                                new File(inputFileName)
                        )
                )
        );
        Writer writer = new OutputStreamWriter(
            new FileOutputStream(
                new File(outputFileName)
            )
        )) {
            List<Maybe<Integer>> intList = new ArrayList<>();
            for (; scanner.hasNext(); ) {
                String nxtLine = scanner.nextLine();
                try {
                    intList.add(Maybe.just(Integer.parseInt(nxtLine)));
                } catch (NumberFormatException e) {
                    intList.add(Maybe.nothing());
                }
            }
            List<Maybe<Integer>> mappedList = new ArrayList<>();
            for (var x : intList) {
                mappedList.add(x.map((y) -> y * y));
            }
            for (var x : mappedList) {
                if (x.isPresent()) {
                    writer.write(x.get().toString() + "\n");
                } else {
                    writer.write("nothing\n");
                }
            }
        }
    }
}
