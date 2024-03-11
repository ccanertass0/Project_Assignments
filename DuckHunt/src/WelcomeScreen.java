import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;



public class WelcomeScreen {
    private String welcomeImagePath;
    private String mp3Path;  // the welcoming screen music path

    public static AudioClip titleAudioClip;

    public WelcomeScreen(String welcomeImagePath, String mp3Path) {

        this.welcomeImagePath = welcomeImagePath;
        this.mp3Path = mp3Path;
    }

    /**
     * Creates the welcoming screen, uses {@link Pane} class.
     * Handles key pressing events in the created scene.
     * Texts, sounds of the welcoming are also set here.
     */
    public void manageWelcomeScreen(){
        Image welcomeImage = new Image(welcomeImagePath);

        ImageView welcImagView = new ImageView(welcomeImage);  // the welcoming screen background
        welcImagView.setPreserveRatio(true);
        welcImagView.fitWidthProperty().bind(DuckHunt.stage.widthProperty()); //strecthing
        welcImagView.fitHeightProperty().bind(DuckHunt.stage.heightProperty());
        if(titleAudioClip == null) {
            WelcomeScreen.titleAudioClip = new AudioClip(getClass().getResource(mp3Path).toExternalForm());
            titleAudioClip.setVolume(DuckHunt.volume);
            WelcomeScreen.titleAudioClip.setCycleCount(AudioClip.INDEFINITE);
        }

        if(!WelcomeScreen.titleAudioClip.isPlaying()){
            WelcomeScreen.titleAudioClip.play();
        }

        Text startQuitText = new Text("PRESS ENTER TO START\n    PRESS ESC TO EXIT");
        startQuitText.setFont(Font.font("Arial", 14*DuckHunt.scale));
        startQuitText.setFill(Color.ORANGE);
        startQuitText.setLayoutX(40*DuckHunt.scale);
        startQuitText.setLayoutY(167*DuckHunt.scale);

        Timeline timeline = new Timeline(  // for flashing effect
                new KeyFrame(Duration.ZERO, new KeyValue(startQuitText.visibleProperty(), true)),
                new KeyFrame(Duration.seconds(0.7), new KeyValue(startQuitText.visibleProperty(), false)),  // text is displayed for 0.7 seconds
                new KeyFrame(Duration.seconds(1.2), new KeyValue(startQuitText.visibleProperty(), true))    // not displayed for 0.5 seconds
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        Pane root = new Pane(welcImagView, startQuitText);

        Scene welcomeScene = new Scene(root, DuckHunt.width, DuckHunt.height);

        welcomeScene.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                SceneChosing sceneChosing = new SceneChosing("assets/foreground", "assets/background",  //background selection scene starts
                        "assets/crosshair", "assets/effects/Intro.mp3");
                sceneChosing.manageSceneChoosing();  // opens the background selection scene
            } else if (event.getCode() == KeyCode.ESCAPE) {
                Platform.exit();
            }
        });

        DuckHunt.stage.setScene(welcomeScene);

    }
}
