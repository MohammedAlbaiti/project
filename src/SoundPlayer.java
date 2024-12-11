import java.net.URL;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class SoundPlayer {
    private static final URL resource = SoundPlayer.class.getResource("resources/GeneralRoadSounds.mp3");
    private static final URL resource1 = SoundPlayer.class.getResource("resources/CarCrash.mp3");
    private static MediaPlayer generalSoundMediaPlayer;
    private static MediaPlayer crashSoundMediaPlayer;
    public static void playGeneralSounds(int durationInSeconds) {
        if (resource != null) {
            generalSoundMediaPlayer = new MediaPlayer(new Media(resource.toString()));
            if(!TrafficSimulation.isMuted ){
                generalSoundMediaPlayer.setVolume(0.02);
            }
            else{
                generalSoundMediaPlayer.setVolume(0);
            }
            Platform.runLater(() -> {
                 // Set volume to 2%
                generalSoundMediaPlayer.setOnReady(() -> generalSoundMediaPlayer.play());

                PauseTransition delay = new PauseTransition(Duration.seconds(durationInSeconds));
                delay.setOnFinished(event -> generalSoundMediaPlayer.stop());
                delay.play();
            });
        } else {
            System.out.println("GeneralRoadSounds.mp3 not found");
        }
    }

    public static void CarCrash(int durationInSeconds) {
        if (resource1 != null) {
            crashSoundMediaPlayer = new MediaPlayer(new Media(resource1.toString()));
            if(!TrafficSimulation.isMuted ){
                crashSoundMediaPlayer.setVolume(1);
            }
            else{
                crashSoundMediaPlayer.setVolume(0);
            }
            Platform.runLater(() -> {
                crashSoundMediaPlayer.setOnReady(() -> crashSoundMediaPlayer.play());

                PauseTransition delay = new PauseTransition(Duration.seconds(durationInSeconds));
                delay.setOnFinished(event -> crashSoundMediaPlayer.stop());
                delay.play();
            });
        } 
    }

    public static void stopGeneralSounds() {
        generalSoundMediaPlayer.setVolume(0);
    }

    public static void resumeSounds() {
        generalSoundMediaPlayer.setVolume(0.02);
    }
}
