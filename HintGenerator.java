import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * HintGenerator
 */
public final class HintGenerator {
    private final List<String> hints;
    private static final Random random = new Random();

    public HintGenerator(final int number) {
        if (number < 1 || number > 100) {
            throw new IllegalArgumentException("Invalid number");
        }

        hints = new ArrayList<String>();

        if ((number % 2) == 0)
            hints.add("Number is even");
        else
            hints.add("Number is odd");

        final var sequence = String.valueOf(number);
        if (sequence.length() > 1)
            hints.add("Number starts with " + sequence.charAt(0));

    }

    public String getHint() {
        final int index = random.nextInt(0, hints.size());
        return hints.remove(index);
    }
}
