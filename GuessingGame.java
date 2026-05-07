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

            final GameConfig.Difficulty difficultyLevel = switch (difficultyIndex) {
                case 1 -> GameConfig.Difficulty.EASY;
                case 2 -> GameConfig.Difficulty.MEDIUM;
                case 3 -> GameConfig.Difficulty.HARD;
                default ->
                    throw new IllegalArgumentException(
                            "Invalid difficulty level! Please type a number between 1 and 3.");
            };

            System.out.printf("""
                    Great! You have selected the %s difficulty level.
                    Let's start the game!
                        """, difficultyLevel.getText());

            // Game Loop
            boolean wantReplay = false;
            do {
                startGame(difficultyLevel, inputReader);
                System.out.print("Replay? (y or n): ");
                final String answer = inputReader.nextLine();
                wantReplay = switch (answer.toLowerCase()) {
                    case "y", "yes" -> true;
                    default -> false;
                };

            } while (wantReplay);

        }
    }

    private static void startGame(final GameConfig.Difficulty difficulty, final Scanner input) {
        final int number = new Random().nextInt(1, 100 + 1);
        HintGenerator hintGenerator = new HintGenerator(number);
        HighScore highscore = HighScore.read();
        final int chances = difficulty.getChances();

        if (highscore.getHighscore(difficulty) != null) {
            System.out.printf("Your current highscore for this difficulty is %d attempt(s)\n",
                    highscore.getHighscore(difficulty));
            System.out.println();
        }

        for (int attempts = 1; attempts <= chances; attempts++) {
            System.out.printf("You have %d attempt(s)\n\n",
                    chances - attempts + 1);

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
                        attempts);

                if (highscore.getHighscore(difficulty) == null || attempts < highscore.getHighscore(difficulty)) {
                    highscore.register(difficulty, attempts);
                }
                return;
            }

            if ((attempts % 3) == 0) {
                hintGenerator.getHint().ifPresent(h -> System.out.println("Hint: " + h));
            }

        }
        System.out.println("Out of attemps! The number was " + number);
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
