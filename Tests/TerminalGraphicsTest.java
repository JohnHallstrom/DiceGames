import Helper.TerminalPanel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

public class TerminalGraphicsTest {
    public static void main(String[] args) throws Exception {
        String panel = capture(() -> TerminalPanel.print("", "TEST", "Odd", "A".repeat(150), ""));
        for (String line : panel.split("\\R")) {
            if (!line.isEmpty()) {
                check(line.length() == 62, "Panel borders and wrapped rows must align");
                if (line.startsWith("  | ") && !line.substring(4, 60).trim().isEmpty()) {
                    String content = line.substring(4, 60);
                    int leftPadding = content.length() - content.stripLeading().length();
                    int rightPadding = content.length() - content.stripTrailing().length();
                    check(rightPadding - leftPadding >= 0 && rightPadding - leftPadding <= 1,
                            "Panel titles, text and wrapped rows must be centered");
                }
            }
        }

        Method showDie = GameStart.class.getDeclaredMethod("showDie", int.class);
        showDie.setAccessible(true);
        for (int value : new int[] { 1, 2, 3, 4, 5, 6, 7, 100, Integer.MAX_VALUE }) {
            String[] lines = capture(() -> showDie.invoke(null, value)).split("\\R");
            for (int row = 1; row < 5; row++) {
                check(lines[row].length() == lines[0].length(), "Die rows must align for " + value);
            }
            if (value <= 6) {
                long pips = String.join("", lines[1], lines[2], lines[3]).chars().filter(c -> c == 'o').count();
                check(pips == value, "Incorrect pip count for " + value);
            }
            check(lines[5].contains("LANDED ON " + value + "!"), "Result must match the die");
        }

        Method animateRoll = GameStart.class.getDeclaredMethod("animateRoll", int.class);
        animateRoll.setAccessible(true);
        String animation = capture(() -> animateRoll.invoke(null, 6));
        check(animation.split("ROLLING", -1).length - 1 == 8, "Animation must render eight frames");
        check(animation.contains("\r") && animation.contains("LANDED ON 6!"), "Animation must redraw and reveal");

        Thread.currentThread().interrupt();
        try {
            String interrupted = capture(() -> animateRoll.invoke(null, 1));
            check(Thread.currentThread().isInterrupted(), "Animation must preserve interruption");
            check(interrupted.contains("LANDED ON 1!"), "Interrupted animation must still show the result");
        } finally {
            Thread.interrupted();
        }

        InputStream originalInput = System.in;
        try {
            String input = "Alex\n2\n1\n1\n1\nmaybe\ny\nSam\n1\n1\n1\nn\n";
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            String game = capture(() -> DiceGame.main(new String[0]));
            check(game.contains("SCORE: 2 / 2"), "Scoring must be preserved");
            check(game.contains("BEST STREAK: 2"), "Streak tracking must be preserved");
            check(game.contains("Please answer yes or no."), "Replay must validate input");
            check(game.contains("PLAYER: Sam"), "Replay must start a new game");
            check(game.contains("Thanks for playing"), "Declining replay must exit");
            check(!game.contains("ROLLING") && !game.replace("\r\n", "\n").contains("\r"),
                    "Redirected output must not animate");
        } finally {
            System.setIn(originalInput);
        }
        System.out.println("Terminal graphics tests passed.");
    }

    private static String capture(CheckedAction action) throws Exception {
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (PrintStream output = new PrintStream(buffer, true, StandardCharsets.UTF_8)) {
            System.setOut(output);
            action.run();
        } finally {
            System.setOut(originalOutput);
        }
        return buffer.toString(StandardCharsets.UTF_8).replaceAll("\u001B\\[[0-9;]*[A-Za-z]", "");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private interface CheckedAction {
        void run() throws Exception;
    }
}
