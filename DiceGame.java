import Helper.TerminalColors;
import Helper.TerminalPanel;

/**
 * Sets up and runs the dice guessing game.
 */
public class DiceGame {
    private final PlayerInput input = new PlayerInput();

    public static void main(String[] args) {
        new DiceGame().play();
    }

    private void play() {
        showTitle();

        boolean playAgain;
        do {
            showSetup();
            String playerName = input.readName("What is your name? ");
            int numberOfRounds = input.readIntInRange("How many rounds do you want to play? ", 1,
                    Integer.MAX_VALUE);
            int numberOfSides = input.readIntInRange("How many sides should the die have? ", 1,
                    Integer.MAX_VALUE);

            Player player = new Player(playerName);
            Die die = new Die(numberOfSides);
            showGameCard(player, numberOfRounds, numberOfSides);

            int bestStreak = GameStart.playRound(input, player, die, numberOfRounds);
            playAgain = GameStart.endRound(player.getScore(), numberOfRounds, bestStreak, input);
        } while (playAgain);
    }

    private static void showTitle() {
        TerminalPanel.print(TerminalColors.BRIGHT_MAGENTA + TerminalColors.BOLD,
                "D I C E   D U E L   *   R O L L   T H E   D I C E",
                "", "YOUR LUCK. YOUR GUESS. YOUR GLORY.", "");
        System.out.println(TerminalColors.CYAN + "                 STEP UP TO THE TABLE" + TerminalColors.RESET);
    }

    private static void showSetup() {
        System.out.println();
        System.out.println(TerminalColors.BRIGHT_CYAN + TerminalColors.BOLD
                + "  >>> NEW GAME: SET YOUR STAKES <<<" + TerminalColors.RESET);
    }

    private static void showGameCard(Player player, int rounds, int sides) {
        TerminalPanel.print(TerminalColors.BLUE, "GAME CARD",
                "PLAYER: " + player.getName(),
                "ROUNDS: " + rounds + "     DIE: " + sides + " sides",
                "RULE: Guess the exact number to score a point!");
    }
}
