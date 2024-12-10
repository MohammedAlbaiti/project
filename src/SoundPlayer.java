import java.net.URL;
import javafx.util.Duration;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundPlayer {

private static MediaPlayer mediaPlayer;

    public static void playGeneralSounds(int durationInSeconds) {
        if(!TrafficSimulation.isMuted){
            Platform.runLater(() -> {
                try {
                    // Ensure the file is properly located
                    URL resource = SoundPlayer.class.getResource("/GeneralRoadSounds.mp3");
                    if (resource == null) {
                        System.out.println("Sound file not found.");
                        return;
                    }
    
                    Media media = new Media(resource.toString());
                    mediaPlayer = new MediaPlayer(media);
    
                    // Handle media readiness
                    mediaPlayer.setOnReady(() -> {
                        mediaPlayer.play();
    
                        // Schedule stopping the media player after the specified duration
                        PauseTransition delay = new PauseTransition(Duration.seconds(durationInSeconds));
                        delay.setOnFinished(event -> stopGeneralSounds());
                        delay.play();
                    });
    
                    // Handle errors
                    mediaPlayer.setOnError(() -> System.out.println("Error: " + mediaPlayer.getError()));
    
                } catch (Exception e) {
                    System.out.println("An error occurred: " + e.getMessage());
                }
            });
        }
    }

    public static void CarCrash(int durationInSeconds) {
        if(!TrafficSimulation.isMuted){
            Platform.runLater(() -> {
                try {
                    // Ensure the file is properly located
                    URL resource = SoundPlayer.class.getResource("/CarCrash.mp3");
                    if (resource == null) {
                        System.out.println("Sound file not found.");
                        return;
                    }
    
                    Media media = new Media(resource.toString());
                    mediaPlayer = new MediaPlayer(media);
    
                    // Handle media readiness
                    mediaPlayer.setOnReady(() -> {
                        mediaPlayer.play();
    
                        // Schedule stopping the media player after the specified duration
                        PauseTransition delay = new PauseTransition(Duration.seconds(durationInSeconds));
                        delay.setOnFinished(event -> stopGeneralSounds());
                        delay.play();
                    });
    
                    // Handle errors
                    mediaPlayer.setOnError(() -> System.out.println("Error: " + mediaPlayer.getError()));
    
                } catch (Exception e) {
                    System.out.println("An error occurred: " + e.getMessage());
                }
            });
        }
    }

    public static void stopGeneralSounds() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }
}

