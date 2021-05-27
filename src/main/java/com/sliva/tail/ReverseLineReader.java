/*
 * GNU GENERAL PUBLIC LICENSE
 */
package com.sliva.tail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Read non-empty lines from a file in reverse order starting from the end.
 *
 * @author Sliva Co
 */
public class ReverseLineReader implements AutoCloseable {

    private static final int BLOCK_SIZE = 1024;
    private final byte[] block = new byte[BLOCK_SIZE];
    private final Charset charset;
    private final RandomAccessFile raf;
    private int filePos;
    private int blockPos = -1;

    /**
     * Constructor.
     *
     * @param file File to read
     * @param charset File encoding
     * @throws IOException on error
     */
    public ReverseLineReader(File file, Charset charset) throws IOException {
        this.charset = charset;
        raf = new RandomAccessFile(file, "r");
        if (raf.length() > Integer.MAX_VALUE) {
            throw new IOException("Cannot read file size over 2GB");
        }
        filePos = (int) raf.length();
    }

    /**
     * Read next non-empty line of the file in reverse order.
     *
     * @return string or null if end of file reached
     * @throws IOException on error
     */
    public String readLine() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (;;) {
            if (blockPos < 0) {
                if (!readBlock()) {
                    break;
                }
            }
            byte b = block[blockPos];
            blockPos--;
            if (isCR(b)) {
                if (baos.size() == 0) {
                    //skip enpty lines
                    continue;
                }
                break;
            }
            baos.write(b);
        }
        return toString(baos, charset);
    }

    /**
     * Close the file.
     *
     * @throws IOException on error
     */
    @Override
    public void close() throws IOException {
        raf.close();
    }

    /**
     * Read next block from file in reversed order.
     *
     * @return true if read succeeded, false if no more blocks available to read
     * @throws IOException on error
     */
    private boolean readBlock() throws IOException {
        if (filePos > 0) {
            int len = Math.min(block.length, filePos);
            filePos -= len;
            raf.seek(filePos);
            int n = raf.read(block, 0, len);
            blockPos = n - 1;
        }
        return blockPos >= 0;
    }

    /**
     * Converts baos to reversed order string or return null if baos is empty.
     *
     * @param baos ByteArrayOutputStream
     * @param charset Charset
     * @return string or null
     * @throws UnsupportedEncodingException
     */
    private static String toString(ByteArrayOutputStream baos, Charset charset) throws UnsupportedEncodingException {
        if (baos.size() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder(baos.toString(charset.toString()));
        return sb.reverse().toString();
    }

    /**
     * Check if byte value is CR or LN control code.
     *
     * @param b byte value
     * @return true if byte value is CR or LN control code
     */
    private static boolean isCR(byte b) {
        return b == '\r' || b == '\n';
    }
}
