/*
 * GNU GENERAL PUBLIC LICENSE
 */
package com.sliva.tail;

import java.io.File;
import java.nio.charset.StandardCharsets;
import static java.util.stream.Collectors.joining;
import java.util.stream.Stream;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Sliva Co
 */
public class ReverseLineReaderTest {

    @Test
    public void testReadLine() throws Exception {
        ReverseLineReader instance = new ReverseLineReader(new File("target/test-classes/one-line.txt"), StandardCharsets.UTF_8);
        String expected = "test-line-1";
        String result = instance.readLine();
        assertEquals(expected, result);
    }

    @Test
    public void testReadLine_empty() throws Exception {
        ReverseLineReader instance = new ReverseLineReader(new File("target/test-classes/empty.txt"), StandardCharsets.UTF_8);
        String result = instance.readLine();
        assertNull(result);
    }

    @Test
    public void testReadLine_with_cr() throws Exception {
        ReverseLineReader instance = new ReverseLineReader(new File("target/test-classes/one-line-with-CR.txt"), StandardCharsets.UTF_8);
        String expected = "test-line-1";
        String result = instance.readLine();
        assertEquals(expected, result);
    }

    @Test
    public void testReadLine_multi() throws Exception {
        ReverseLineReader instance = new ReverseLineReader(new File("target/test-classes/two-lines.txt"), StandardCharsets.UTF_8);
        String result = instance.readLine();
        assertEquals("test-line-2", result);
        result = instance.readLine();
        assertEquals("test-line-1", result);
        result = instance.readLine();
        assertNull(result);
    }

    @Test
    public void testReadLine_long() throws Exception {
        ReverseLineReader instance = new ReverseLineReader(new File("target/test-classes/long-line.txt"), StandardCharsets.UTF_8);
        String expected = Stream.generate(() -> "test-long-line").limit(100).collect(joining(" "));
        String result = instance.readLine();
        assertEquals(expected, result);
        result = instance.readLine();
        assertNull(result);
    }

    @Test
    public void testReadLine_long_multi() throws Exception {
        ReverseLineReader instance = new ReverseLineReader(new File("target/test-classes/long-lines-4.txt"), StandardCharsets.UTF_8);
        String expected = Stream.generate(() -> "test-long-line").limit(100).collect(joining(" "));
        String result = instance.readLine();
        assertEquals(expected, result);
        result = instance.readLine();
        assertEquals(expected, result);
        result = instance.readLine();
        assertEquals(expected, result);
        result = instance.readLine();
        assertEquals(expected, result);
        result = instance.readLine();
        assertNull(result);
    }
}
