/*
 * GNU GENERAL PUBLIC LICENSE
 */
package com.sliva.tail;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Sliva Co
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Read last N lines from a file. Usage: tail.jar [-N] file-name, where N is number of lines to read (default is 10)");
            return;
        }
        String fileName = args[args.length - 1];
        int n = 10;
        if (args[0].startsWith("-")) {
            n = Integer.parseInt(args[0].substring(1));
        }
        ReverseLineReader reader = new ReverseLineReader(new File(fileName), StandardCharsets.UTF_8);
        for (int i = 0; i < n; i++) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            System.out.println(line);
        }
    }

}
