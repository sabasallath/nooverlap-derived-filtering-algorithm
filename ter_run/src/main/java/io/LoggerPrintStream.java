package io;

import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;


/**
 * A simple PrintStream container that redirect
 * an output stream to a logger
 */
public final class LoggerPrintStream extends OutputStream {

    private final ByteArrayOutputStream stream = new ByteArrayOutputStream(1024);
    private final Logger logger;

    public LoggerPrintStream(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void write(int b) {
        if (b == '\n') {
            try {
                String line = stream.toString("utf-8");
                stream.reset();
                logger.trace((line));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            stream.write(b);
        }
    }
}