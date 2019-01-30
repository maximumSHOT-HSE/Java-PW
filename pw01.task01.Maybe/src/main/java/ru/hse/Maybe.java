package ru.hse;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Maybe {

    /*
     * Args: file name
     * */
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Exactly 2 arguments should be provided");
        }
        String inputFileName = args[0];
        String outputFileName = args[1];
        Scanner scanner = new Scanner(
            new BufferedInputStream(
                new FileInputStream(
                    new File(inputFileName)
                )
            )
        );

        for (; scanner.hasNext();) {

        }
    }

}
