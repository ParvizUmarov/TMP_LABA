package org.example;

import java.io.PrintWriter;
import java.util.Scanner;

public class IO {
    private final PrintWriter writer = new PrintWriter(System.out);
    private final Scanner scanner = new Scanner(System.in);

    public String readln() {
        return scanner.nextLine();
    }

    public void print(Object o) {
        writer.print(o);
        writer.flush();
    }

    public void println(Object o) {
        writer.println(o);
        writer.flush();
    }

}
