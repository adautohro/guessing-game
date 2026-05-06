/**
 * GameConfig
 */
public class GameConfig {

    /**
     * Difficulty
     */
    public static enum Difficulty {
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
}
