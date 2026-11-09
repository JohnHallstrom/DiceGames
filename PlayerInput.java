import Helper.TerminalColors;

import java.util.Scanner;

public class PlayerInput {

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Reads one whole line of text from the user.
     *
     * @return the text the user entered.
     */
    public String readLine() {
        return scanner.nextLine().trim();
    }

    /**
     * Reads a line of text and converts it to an integer.
     *
     * @return the integer the user entered.
     */
    public int readInt() {
        return Integer.parseInt(readLine());
    }

    /**
     * Keeps asking until the user enters an integer within the given range.
     *
     * @param prompt  the message to show before reading input.
     * @param minimum the smallest allowed value.
     * @param maximum the largest allowed value.
     * @return the integer entered by the user.
     */
    public int readIntInRange(String prompt, int minimum, int maximum) {
        while (true) {
            System.out.print(TerminalColors.YELLOW + prompt + TerminalColors.RESET);

            try {
                int value = readInt();
                if (value >= minimum && value <= maximum) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // Ask again when the input is not an integer.
            }

            System.out.println(TerminalColors.RED + "\nInvalid input. Enter an integer between " + minimum + " and "
                    + maximum + "." + TerminalColors.RESET);
        }
    }

    /**
     * Keeps asking until the user enters a non empty name.
     *
     * @param prompt the message to show before reading the name.
     * @return the name entered by the user.
     */
    public String readName(String prompt) {
        while (true) {
            System.out.print(TerminalColors.YELLOW + prompt + TerminalColors.RESET);
            String name = readLine();

            if (!name.isEmpty()) {
                return name;
            }

            System.out.println(TerminalColors.RED + "The name cannot be empty." + TerminalColors.RESET);
        }
    }

    /**
     * Keeps asking until the player answers yes or no.
     *
     * @param prompt the message to show before reading the answer.
     * @return true when the player wants to continue.
     */
    public boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(TerminalColors.YELLOW + prompt + TerminalColors.RESET);
            String answer = readLine().toLowerCase();
            if (answer.equals("yes") || answer.equals("y")) {
                return true;
            }
            if (answer.equals("no") || answer.equals("n")) {
                return false;
            }
            System.out.println(TerminalColors.RED + "Please answer yes or no." + TerminalColors.RESET);
        }
    }
}
