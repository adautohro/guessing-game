import java.util.HashMap;
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

        try (Scanner scanner = new Scanner(System.in)) {
            final Integer difficultyIndex = InputValidator.validate(scanner,
                    Integer::parseInt,
                    n -> n >= 1 && n <= 3,
                    "Enter your choice: ",
                    "Invalid difficulty level! Please type a number between 1 and 3.\n");

            final int chances = switch (difficultyIndex) {
                case 1 -> 10;
                case 2 -> 5;
                case 3 -> 3;
                default ->
                    throw new IllegalArgumentException(
                            "Invalid difficulty level! Please type a number between 1 and 3.");
            };

            final var difficultyLevel = new HashMap<Integer, String>();
            difficultyLevel.put(1, "Easy");
            difficultyLevel.put(2, "Medium");
            difficultyLevel.put(3, "Hard");

            System.out.printf("""
                    Great! You have selected the %s difficulty level.
                    You have %d chances to guess the correct number.
                    Let's start the game!
                        """, difficultyLevel.get(difficultyIndex), chances);

            // Game Loop
            boolean wantReplay = false;
            do {
                startGame(chances, scanner);
                System.out.print("Replay? (y or n): ");
                final String answer = scanner.nextLine();
                wantReplay = switch (answer.toLowerCase()) {
                    case "y", "yes" -> true;
                    default -> false;
                };

            } while (wantReplay);

        }
    }

    private static void startGame(final int chances, final Scanner input) {
        final int number = new Random().nextInt(1, 100 + 1);
        for (int attempts = 1; attempts <= chances; attempts++) {
            System.out.println();

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
                System.out.printf("Congratulations! You guessed the correct number in %d attempts\n", attempts);
                return;
            }

        }
        System.out.println("Out of attemps! The number was " + number);
    }

    /**
     * InputValidator
     */
    private final static class InputValidator {
        public static <T> T validate(final Scanner inputReader, final Function<String, T> parser, final Predicate<T> validation,
                final String inputMessage, final String errorMessage) {

            System.out.print(inputMessage);
            final String input = inputReader.nextLine();

            try {
                final T value = parser.apply(input);
                if (!validation.test(value)) {
                    System.out.println(errorMessage);
                    return validate(inputReader, parser, validation, inputMessage, errorMessage);
                }

                return value;
            } catch (final Exception e) {
                System.out.println(errorMessage);
                return validate(inputReader, parser, validation, inputMessage, errorMessage);
            }

        }
    }
}
