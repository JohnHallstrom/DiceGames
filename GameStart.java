import Helper.TerminalColors;
import Helper.TerminalPanel;

/** Runs game rounds and displays final results. */
public class GameStart {
    private static final int SCORE_BAR_WIDTH = 20;
    private static final boolean ANIMATIONS_ENABLED = System.console() != null
            && !"dumb".equals(System.getenv("TERM"))
            && !"false".equalsIgnoreCase(System.getProperty("dice.animation"));
    private static final String[][] DIE_FACES = {
            { "       ", "   o   ", "       " },
            { "     o ", "       ", " o     " },
            { "     o ", "   o   ", " o     " },
            { " o   o ", "       ", " o   o " },
            { " o   o ", "   o   ", " o   o " },
            { " o   o ", " o   o ", " o   o " }
    };

    public static int playRound(PlayerInput input, Player player, Die die, int numberOfRounds) {
        int streak = 0;
        int bestStreak = 0;

        for (int round = 1; round <= numberOfRounds; round++) {
            TerminalPanel.print(TerminalColors.BRIGHT_CYAN + TerminalColors.BOLD,
                    "ROUND " + round + " / " + numberOfRounds);
            printScoreboard(player.getScore(), numberOfRounds, streak);

            int guess = input.readIntInRange(
                    TerminalColors.YELLOW + "  Your guess (1-" + die.getNumberOfSides() + "): "
                            + TerminalColors.RESET,
                    1,
                    die.getNumberOfSides());
            die.roll();

            int result = die.getCurrentValue();
            System.out.println();
            if (ANIMATIONS_ENABLED) {
                animateRoll(result);
            } else {
                showDie(result);
            }

            if (guess == result) {
                player.increaseScore();
                bestStreak = Math.max(bestStreak, ++streak);
                System.out.println(TerminalColors.BRIGHT_GREEN + TerminalColors.BOLD
                        + "  JACKPOT! Dead-on guess. +1 point!" + TerminalColors.RESET);
            } else {
                streak = 0;
                System.out.println(TerminalColors.BRIGHT_RED + "  Not this time! Your guess was " + guess + "."
                        + TerminalColors.RESET);
            }
        }
        return bestStreak;
    }

    public static boolean endRound(int score, int rounds, int bestStreak, PlayerInput input) {
        double accuracy = score * 100.0 / rounds;
        TerminalPanel.print(TerminalColors.BRIGHT_MAGENTA + TerminalColors.BOLD, "FINAL RESULTS",
                "SCORE: " + score + " / " + rounds,
                String.format("ACCURACY: %.1f%%     BEST STREAK: %d", accuracy, bestStreak),
                "TITLE: " + getVerdict(score, rounds));

        boolean playAgain = input.readYesNo("Play another round? (yes/no): ");
        if (!playAgain) {
            System.out.println(TerminalColors.BRIGHT_CYAN + TerminalColors.BOLD
                    + "\n  Thanks for playing Dice Duel. May your next roll be legendary!" + TerminalColors.RESET);
        }
        return playAgain;
    }

    private static void printScoreboard(int score, int rounds, int streak) {
        int filled = (int) Math.round((double) score / rounds * SCORE_BAR_WIDTH);
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < SCORE_BAR_WIDTH; i++) {
            bar.append(i < filled ? TerminalColors.BRIGHT_GREEN + "#" : TerminalColors.DIM + "-");
        }
        System.out.printf("%s  SCORE [%s%s] %d/%d     %sSTREAK x%d%s%n",
                TerminalColors.GREEN,
                bar,
                TerminalColors.RESET,
                score,
                rounds,
                TerminalColors.BRIGHT_MAGENTA,
                streak,
                TerminalColors.RESET);
    }

    private static void showDie(int value) {
        int width = Math.max(7, Integer.toString(value).length() + 2);
        String border = "                 +" + "-".repeat(width) + "+";
        System.out.println(TerminalColors.BRIGHT_YELLOW + TerminalColors.BOLD + border);
        String[] face = value >= 1 && value <= DIE_FACES.length
                ? DIE_FACES[value - 1]
                : new String[] { " ".repeat(width), center(Integer.toString(value), width), " ".repeat(width) };
        for (String row : face) {
            System.out.println("                 |" + row + "|");
        }
        System.out.println(border + TerminalColors.RESET);
        System.out.println(TerminalColors.CYAN + "                 THE DIE LANDED ON " + value + "!"
                + TerminalColors.RESET);
    }

    private static void animateRoll(int value) {
        String[] frames = { "[ o    ]", "[  o   ]", "[   o  ]", "[    o ]", "[   o  ]", "[  o   ]" };
        for (int i = 0; i < 8; i++) {
            System.out.print("\r\u001B[2K" + TerminalColors.BRIGHT_YELLOW + "  ROLLING " + frames[i % frames.length]
                    + TerminalColors.RESET);
            System.out.flush();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Roll animation interrupted; displaying the result.");
                break;
            }
        }
        System.out.print("\r\u001B[2K");
        showDie(value);
    }

    private static String center(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text + " ".repeat(width - padding - text.length());
    }

    private static String getVerdict(int score, int rounds) {
        if (score == rounds) {
            return "DICE DUEL LEGEND";
        }
        if (score / (double) rounds >= 0.75) {
            return "TABLE TAKEOVER";
        }
        if (score > 0) {
            return "LUCK IS ON THE RISE";
        }
        return "THE DICE KEEP THEIR SECRETS";
    }
}
