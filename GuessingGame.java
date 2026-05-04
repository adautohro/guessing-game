import java.util.Random;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Main
 */
public class GuessingGame {

    public static void main(final String[] args) {
        System.out.println("""

                Welcome to the Number Guessing Game!
                I'm thinking of a number between 1 and 100
                Can you guess it?
                                """);

        System.out.println("""
                Please select the difficulty level:
                1. Easy (10 chances)
                2. Medium (5 chances)
                3. Hard (3 chances)
                                """);

        try (Scanner inputReader = new Scanner(System.in)) {
            final Integer difficultyIndex = InputValidator.validate(inputReader,
                    Integer::parseInt,
                    n -> n >= 1 && n <= 3,
                    "Enter your choice: ",
                    "Invalid difficulty level! Please type a number between 1 and 3.\n");

            final Difficulty difficultyLevel = switch (difficultyIndex) {
                case 1 -> Difficulty.EASY;
                case 2 -> Difficulty.MEDIUM;
                case 3 -> Difficulty.HARD;
                default ->
                    throw new IllegalArgumentException(
                            "Invalid difficulty level! Please type a number between 1 and 3.");
            };

            System.out.printf("""
                    Great! You have selected the %s difficulty level.
                    Let's start the game!
                        """, difficultyLevel.getText(), difficultyLevel.getChances());

            // Game Loop
            boolean wantReplay = false;
            do {
                startGame(difficultyLevel.getChances(), inputReader);
                System.out.print("Replay? (y or n): ");
                final String answer = inputReader.nextLine();
                wantReplay = switch (answer.toLowerCase()) {
                    case "y", "yes" -> true;
                    default -> false;
                };

            } while (wantReplay);

        }
    }

    private static void startGame(final int chances, final Scanner input) {
        final int number = new Random().nextInt(1, 100 + 1);
        for (int attempts = 0; attempts < chances; attempts++) {
            System.out.printf("You have %d " + ((chances - attempts == 1) ? "attempt" : "attempts") + "\n\n",
                    chances - attempts);

            final Integer guess = InputValidator.validate(
                    input,
                    Integer::parseInt,
                    n -> n >= 1 && n <= 100,
                    "Enter your guess: ",
                    "Invalid guess. Please type a number between 1 and 100\n");

            if (number < guess) {
                System.out.println("Incorrect! The number is less than " + guess);
            } else if (number > guess) {
                System.out.println("Incorrect! The number is greater than " + guess);
            } else {
                System.out.printf("Congratulations! You guessed the correct number in %d attempts\n",
                        chances - attempts);
                return;
            }

        }
        System.out.println("Out of attemps! The number was " + number);
    }

    private static enum Difficulty {
        EASY("Easy", 10), MEDIUM("Medium", 5), HARD("Hard", 3),
        ;

        public String getText() {
            return text;
        }

        public int getChances() {
            return chances;
        }

        private final String text;
        private final int chances;

        private Difficulty(final String text, final int chances) {

            this.text = text;
            this.chances = chances;
        }

    }

    /**
     * InputValidator
     */
    private final static class InputValidator {
        public static <T> T validate(final Scanner inputReader, final Function<String, T> parser,
                final Predicate<T> validation,
                final String inputMessage, final String errorMessage) {

            while (true) {
                System.out.print(inputMessage);
                final String input = inputReader.nextLine();

                try {
                    final T value = parser.apply(input);
                    if (!validation.test(value)) {
                        System.out.println(errorMessage);
                        continue;
                    }

                    return value;
                } catch (final RuntimeException e) {
                    System.out.println(errorMessage);
                    continue;
                }
            }

        }
    }
}
