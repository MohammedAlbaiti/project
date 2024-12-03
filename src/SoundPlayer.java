import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundPlayer {

    public static void playSound(String soundFilePath) {
        // Create a Media object from the file path
        Media sound = new Media("file:///" + soundFilePath);

        // Create a MediaPlayer object from the Media object
        MediaPlayer mediaPlayer = new MediaPlayer(sound);

        // Play the sound
        mediaPlayer.play();
    }

    public static void main(String[] args) {
        // Example usage
        playSound(""); // Replace with the path to your sound file
    }
}