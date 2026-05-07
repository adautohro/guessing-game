import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * HighScore
 */
public class HighScore implements Serializable {
    private static final String PATH = "highscore.ser";
    private Map<GameConfig.Difficulty, Integer> highscores = new HashMap<>();

    public static HighScore read() {
        if (Files.notExists(Path.of(PATH))) {
            return new HighScore();
        }

        HighScore highscore = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(PATH))) {
            highscore = (HighScore) inputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return Objects.requireNonNull(highscore, "Failed to read highscore");
    }

    public void register(GameConfig.Difficulty difficulty, int minAttempts) {
        highscores.put(difficulty, minAttempts);
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(PATH))) {
            outputStream.writeObject(this);
        } catch (IOException e) {
            System.err.println("Failed to write highscore");
            e.printStackTrace();
        }

    }

    public Integer getHighscore(GameConfig.Difficulty difficulty) {
        return highscores.get(difficulty);
    }

}
