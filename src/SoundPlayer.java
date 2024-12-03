import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundPlayer {

    public static void playGeneralSounds() {
        String bip = "C:\\Users\\WinDows\\OneDrive\\Documents\\GitHub\\project\\src/GeneralRoadSounds.mp3";
        Media hit = new Media(new File(bip).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
    }

    public static void playCrash() {
        // Create a Media object from the file path
        Media sound = new Media("file:///");

        // Create a MediaPlayer object from the Media object
        MediaPlayer mediaPlayer = new MediaPlayer(sound);

        // Play the sound
        mediaPlayer.play();
    }
}