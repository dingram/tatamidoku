package com.zombiesatemy.tatamidoku.cli;

import com.zombiesatemy.tatamidoku.game.GameState;
import com.zombiesatemy.tatamidoku.game.Layout;
import com.zombiesatemy.tatamidoku.game.LayoutGenerator;
import com.zombiesatemy.tatamidoku.game.Placement;
import com.zombiesatemy.tatamidoku.game.PlacementImpl;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;

public final class CommandLineClient {
    private static final int NO_COMMAND = 0;
    private static final int EXIT = 1;
    private static final int SUCCESS = 2;
    private final Terminal mTerminal;
    private final LineReader mLineReader;
    private final PrintWriter mWriter;
    private TextRenderer mRenderer;
    private GameState mGameState;

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
        mRenderer = new TextRenderer(mTerminal);
        mGameState = new GameState();
        resetLayout(3, 2);
        try {
            runREPL();
        } finally {
        }
        mWriter.println("Farewell!");
        mWriter.flush();
    }

    private void resetLayout(int groupSize, int groupCount) {
        final Layout layout = LayoutGenerator.generateGroupless(groupSize, groupCount);
        mGameState.setLayout(layout);
        mGameState.setPlacement(PlacementImpl.fromSideLength(layout.getSideLength()));
    }

    private void runREPL() {
        while (true) {
            switch (readAndInterpretLine()) {
                case NO_COMMAND:
                    break;
                case SUCCESS:
                    break;
                case EXIT:
                    return;
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
            final Scanner scanner = new Scanner(input);
            final String command = scanner.next();
            final Placement prevPlacement = mGameState.getPlacement();
            switch (command.toLowerCase()) {
                case "exit":
                    return false;
                case "print":
                    mRenderer.render(mGameState, false);
                    break;
                case "printgroups":
                case "print_groups":
                    mRenderer.render(mGameState, true);
                    break;
                case "new":
                    int groupSize = mGameState.getLayout().getGroupSize();
                    int groupCount = mGameState.getLayout().getGroupCount();
                    if (scanner.hasNext()) {
                        groupSize = scanner.nextInt();
                        groupCount = scanner.nextInt();
                        if (groupSize < 3) {
                            printError("Minimum group size is 3");
                            break;
                        } else if (groupSize > 5) {
                            printError("Maximum group size is 5");
                            break;
                        }
                        if (groupCount < 2) {
                            printError("Minimum group size is 2");
                            break;
                        } else if (groupCount > 3) {
                            printError("Maximum group count is 3");
                            break;
                        }
                    }
                    resetLayout(groupSize, groupCount);
                    break;
                case "set": {
                    int column = scanner.nextInt();
                    int row = scanner.nextInt();
                    int value = scanner.nextInt();
                    final Layout layout = mGameState.getLayout();
                    if (value < 1 || value > layout.getGroupSize()) {
                        printError("%d is not a valid value.", value);
                        break;
                    }
                    final int sideLength = layout.getSideLength();
                    if (column < 0 || column >= sideLength) {
                        printError("%d is not a valid column (must be 0-%d).", column, sideLength - 1);
                        break;
                    }
                    if (row < 0 || row >= sideLength) {
                        printError("%d is not a valid row (must be 0-%d).", row, sideLength - 1);
                        break;
                    }
                    mGameState.setPlacement(prevPlacement.withValueAt(column, row, value));
                    if (!mGameState.isValid()) {
                        printError("Invalid move.");
                        mGameState.setPlacement(prevPlacement);
                    }
                    break;
                }
                case "clear": {
                    if (scanner.hasNext()) {
                        int column = scanner.nextInt();
                        int row = scanner.nextInt();
                        final int sideLength = mGameState.getLayout().getSideLength();
                        if (column < 0 || column >= sideLength) {
                            printError("%d is not a valid column (must be 0-%d).", column, sideLength - 1);
                            break;
                        }
                        if (row < 0 || row >= sideLength) {
                            printError("%d is not a valid row (must be 0-%d).", row, sideLength - 1);
                            break;
                        }
                        mGameState.setPlacement(prevPlacement.withNoValueAt(column, row));
                    } else {
                        // Reset the whole thing.
                        mGameState.setPlacement(PlacementImpl.fromSideLength(mGameState.getLayout().getSideLength()));
                    }
                    break;
                }
                default:
                    printError("Unknown command \"%s\"", command);
                    break;
            }
            mWriter.flush();
        } catch (NoSuchElementException e) {
            printError("Malformed command \"%s\"", input);
        } catch (Exception e) {
            printError("UNCAUGHT EXCEPTION");
            e.printStackTrace(mWriter);
            mWriter.flush();
        }
        return true;
    }

    private void printError(String s) {
        mWriter.println(
                new AttributedStringBuilder()
                        .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.RED).bold())
                        .append("ERROR: ")
                        .append(s)
                        .toAnsi(mTerminal)
        );
        mWriter.flush();
    }

    private void printError(String s, Object... values) {
        printError(String.format(s, values));
    }
}
