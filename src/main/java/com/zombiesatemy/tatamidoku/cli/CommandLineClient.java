package com.zombiesatemy.tatamidoku.cli;

import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class CommandLineClient {
    private static final int NO_COMMAND = 0;
    private static final int EXIT = 1;
    private static final int SUCCESS = 2;
    private final Terminal mTerminal;
    private final LineReader mLineReader;
    private final PrintWriter mWriter;

    public static void main(String[] args) {
        try {
            new CommandLineClient(args).startREPL();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private CommandLineClient(String[] args) throws IOException {
        mTerminal = TerminalBuilder.builder()
                .system(true)
                .build();
        mLineReader = LineReaderBuilder.builder()
                .terminal(mTerminal)
                .build();
        mWriter = mTerminal.writer();
    }

    private void startREPL() throws Exception {
        try {
            runREPL();
        } finally {
        }
        mWriter.println("Farewell!");
        mWriter.flush();
    }

    private void runREPL() {
        while (true) {
            switch (readAndInterpretLine()) {
                case NO_COMMAND:
                    break;
                case EXIT:
                    return;
                case SUCCESS:
                    break;
            }
        }
    }

    private int readAndInterpretLine() {
        final String prompt = "tatamidoku> ";
        String line;
        try {
            line = mLineReader.readLine(prompt);
        } catch (UserInterruptException e) {
            // Ignore
            return NO_COMMAND;
        } catch (EndOfFileException e) {
            return EXIT;
        }
        if (!interpretCommand(line.trim())) {
            return EXIT;
        }
        return SUCCESS;
    }

    private boolean interpretCommand(String input) {
        try {
            if (input.equals("exit")) {
              return false;
            } else {
              mWriter.format("ERROR: Unknown meta command \"%s\"\n", input);
            }
            mWriter.flush();
        } catch (Exception e) {
            mWriter.println("UNCAUGHT ERROR");
            e.printStackTrace(mWriter);
            mWriter.flush();
        }
        return true;
    }

}
