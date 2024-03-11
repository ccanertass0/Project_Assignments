import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * The Level class will be the mother class for 6 other classes (Level1, Level2...)
 * Its fields and methods were given names as clear as possible
 */
public class Level {
    protected ImageView chosenForegImgView;
    protected ImageView chosenBgImgView;
    protected ImageView chosenCrosImgView;

    Text levelText = new Text();  // shows which level the user is in
    Text ammoText = new Text();  // shows how many ammo is left
    Text levelPassedText = new Text("YOU WIN");
    Text instructionText = new Text();  // tells the user what he/she can do at the end of the level (if level passed or failed then changed accordingly)
    Text gameOverText = new Text("GAME OVER");
    private String gunshotPath = "assets/effects/Gunshot.mp3";
    private String gameOverPath = "assets/effects/GameOver.mp3";
    private String levelDonePath =  "assets/effects/LevelCompleted.mp3";
    private String gameCompletedPath = "assets/effects/GameCompleted.mp3";
    Media fireShotMedia; // the sound of the gunshot
    MediaPlayer gameOverMP;
    MediaPlayer levelDoneMP;
    MediaPlayer gameCompletedMP;

    Scene levelScene;
    Pane root = new Pane();

    int killedDucks = 0;


    public Level(ImageView chosenForegImgView, ImageView chosenBgImgView, ImageView chosenCrosImgView) {
        this.chosenForegImgView = chosenForegImgView;
        this.chosenBgImgView = chosenBgImgView;
        this.chosenCrosImgView = chosenCrosImgView;

        setTexts();
        setSoundEffects();

    }

    /**
     * This method aligns the cursor with the ImageView object of the crosshair the user chose with the cursor
     */
    protected void crossMovement(){

        levelScene.setOnMouseMoved(event -> {

            // it appears when event.getX() method is used as parameter for setLayoutX() it aligns the cursor with the very left edge of the crosshair
            // the same thing applies for Y coordinates (cursor aligns with the upper edge)
            // so this ends up cursor being aligned with the top-left corner
            // Thus, half the width and height are subtracted from the cursor position to match the center of the crosshair and the cursor.
            double newX = event.getX() - chosenCrosImgView.getFitWidth()/2;
            double newY = event.getY() - chosenCrosImgView.getFitHeight()/2;  //cross and cursor matches perfectly !!

            chosenCrosImgView.setLayoutX(newX);
            chosenCrosImgView.setLayoutY(newY);

        });

    }



    /**
     * This method creates the necessary sound effects which all be used in all of the LevelX classes in total
     */
    private void setSoundEffects(){
        fireShotMedia = new Media(getClass().getResource(gunshotPath).toExternalForm());


        Media gameOverMedia = new Media(getClass().getResource(gameOverPath).toExternalForm());
        gameOverMP = new MediaPlayer(gameOverMedia);
        gameOverMP.setVolume(DuckHunt.volume);

        Media levelDoneMedia = new Media(getClass().getResource(levelDonePath).toExternalForm());
        levelDoneMP = new MediaPlayer(levelDoneMedia);
        levelDoneMP.setVolume(DuckHunt.volume);

        Media gameCompletedMedia = new Media(getClass().getResource(gameCompletedPath).toExternalForm());
        gameCompletedMP = new MediaPlayer(gameCompletedMedia);
        gameCompletedMP.setVolume(DuckHunt.volume);

    }




    /**
     * if there is ammo left, gun will fire
     * @param ammo is the ammo left at the time user tries to shot his/her gun
     */
    protected void fireShotIfPossible(int ammo){
        if(ammo > 0) {
            MediaPlayer fireShotMP = new MediaPlayer(fireShotMedia);
            fireShotMP.setVolume(DuckHunt.volume);
            fireShotMP.play();
        }
    }




    /**
     * This method is useful when the user completed the game, won the level or lost the game
     * levelNum parameter is passed and then user will start playing the chosen level
     * @param levelNum is the number of the level user will be directed to
     */
    protected void playTheChosenLevel(int levelNum){
        switch (levelNum){
            case 1:
                Level level1 = new Level1(chosenForegImgView, chosenBgImgView, chosenCrosImgView);
                ((Level1) level1).play();
                break;
            case 2:
                Level level2 = new Level2(chosenForegImgView, chosenBgImgView, chosenCrosImgView);
                ((Level2) level2).play();
                break;
            case 3:
                Level level3 = new Level3(chosenForegImgView, chosenBgImgView, chosenCrosImgView);
                ((Level3) level3).play();
                break;
            case 4:
                Level level4 = new Level4(chosenForegImgView, chosenBgImgView, chosenCrosImgView);
                ((Level4) level4).play();
                break;

            case 5:
                Level level5 = new Level5(chosenForegImgView, chosenBgImgView, chosenCrosImgView);
                ((Level5) level5).play();
                break;
            case 6:
                Level level6 = new Level6(chosenForegImgView, chosenBgImgView, chosenCrosImgView);
                ((Level6) level6).play();
                break;

        }
    }

    private void setTexts(){
        levelText.setFont(Font.font("Arial", 8*DuckHunt.scale));
        levelText.setFill(Color.ORANGE);
        levelText.setLayoutX(100*DuckHunt.scale);
        levelText.setLayoutY(15*DuckHunt.scale);

        ammoText.setFont(Font.font("Arial", 8*DuckHunt.scale));
        ammoText.setFill(Color.ORANGE);
        ammoText.setLayoutX(190*DuckHunt.scale);
        ammoText.setLayoutY(15*DuckHunt.scale);

        levelPassedText.setFont(Font.font("Arial", 14*DuckHunt.scale));
        levelPassedText.setFill(Color.ORANGE);
        levelPassedText.setLayoutX(95*DuckHunt.scale);
        levelPassedText.setLayoutY(80*DuckHunt.scale);

        instructionText.setFont(Font.font("Arial", 14*DuckHunt.scale));
        instructionText.setFill(Color.ORANGE);
        instructionText.setLayoutX(29*DuckHunt.scale);
        instructionText.setLayoutY(98*DuckHunt.scale);

        gameOverText.setFont(Font.font("Arial", 14*DuckHunt.scale));
        gameOverText.setFill(Color.ORANGE);
        gameOverText.setLayoutX(81*DuckHunt.scale);
        gameOverText.setLayoutY(80*DuckHunt.scale);

        Timeline timeline = new Timeline(  // flashing of the instruction texts (press esc to exit etc.)
                new KeyFrame(Duration.ZERO, new KeyValue(instructionText.visibleProperty(), true)),
                new KeyFrame(Duration.seconds(1), new KeyValue(instructionText.visibleProperty(), false)),
                new KeyFrame(Duration.seconds(1.5), new KeyValue(instructionText.visibleProperty(), true))
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

    }



}
