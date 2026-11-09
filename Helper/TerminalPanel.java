package Helper;

/** Prints centered panels, wrapping long content inside their borders. */
public final class TerminalPanel {
    private static final int WIDTH = 56;

    private TerminalPanel() {
    }

    public static void print(String color, String title, String... lines) {
        String border = "  +" + "-".repeat(WIDTH + 2) + "+";
        System.out.println();
        System.out.println(color + border);
        printLine(title);
        System.out.println(border);
        for (String line : lines) {
            printLine(line);
        }
        System.out.println(border + TerminalColors.RESET);
    }

    private static void printLine(String text) {
        do {
            int end = Math.min(WIDTH, text.length());
            String line = text.substring(0, end);
            int leftPadding = (WIDTH - line.length()) / 2;
            int rightPadding = WIDTH - line.length() - leftPadding;
            System.out.println("  | " + " ".repeat(leftPadding) + line + " ".repeat(rightPadding) + " |");
            text = text.substring(end);
        } while (!text.isEmpty());
    }
}
