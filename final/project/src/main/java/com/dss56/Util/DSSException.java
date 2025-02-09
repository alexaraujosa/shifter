package com.dss56.Util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/**
 * This class represents an exception that can be thrown by the DSS56 system. It is used as a base for other exceptions
 * specific to the project, and contains an utility method to get the stacktrace of a {@link Throwable}.
 * It extends the {@link Exception} class.
 */
public class DSSException extends Exception {
    public DSSException(String message) {
        super(message);
    }

    /**
     * Get the stacktrace of a {@link Throwable}.
     * The stacktrace will be returned in the form of a string.
     */
    public static String getStackTrace(Throwable e) {
        assert e != null : "The exception must not be null";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter printWriter = new PrintWriter(baos, true);
        e.printStackTrace(printWriter);
        String stackTrace = baos.toString();

        try {
            printWriter.close();
            baos.close();
        } catch(IOException ignore) {}

        return stackTrace;
    }

    public static class TableUtil {
        public static String formatAsTable(List<List<String>> rows) {
            int[] maxLengths = new int[rows.getFirst().size()];
            for (List<String> row : rows) {
                for (int i = 0; i < row.size(); i++) {
                    maxLengths[i] = Math.max(maxLengths[i], row.get(i).length());
                }
            }

            StringBuilder formatBuilder = new StringBuilder();
            for (int maxLength : maxLengths) {
                formatBuilder.append("%-").append(maxLength + 2).append("s");
            }
            String format = formatBuilder.toString();

            StringBuilder result = new StringBuilder();
            for (int i = 0; i < rows.size(); i++) {
                result.append(String.format(format, rows.get(i).toArray())).append("\n");

                if (i == 0) {
                    int totalWidth = Arrays.stream(maxLengths).sum() + (maxLengths.length * 2);
                    result.append("-".repeat(totalWidth)).append("\n");
                }
            }
            return result.toString();
        }
    }
}
