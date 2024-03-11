import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;


public class SceneChosing {
    private String foregroundsPath;
    private String backgroundsPath;
    private String croshairsPath;

    private List<Image> foregrounds;
    private List<Image> backgrounds;
    private List<Image> crosshairs;

    private ImageView chosenFgImageView;
    private ImageView currentBackGrImageView;
    private ImageView currentCrossImageView;
    private int currentBackGroundInd = 0;
    private int currentCrossInd = 0;
    private String introMp3Path;
    boolean sceneChosen = false;
    MediaPlayer introMP;


    public SceneChosing(String foregroundsPath, String backgroundsPath, String croshairsPath, String introMp3Path) {
        this.backgroundsPath = backgroundsPath;
        this.croshairsPath = croshairsPath;
        this.foregroundsPath = foregroundsPath;
        this.introMp3Path = introMp3Path;

        foregrounds = getImageList(foregroundsPath, 6);
        backgrounds = getImageList(backgroundsPath, 6);
        crosshairs = getImageList(croshairsPath, 7);

        chosenFgImageView = new ImageView(foregrounds.get(0));
        chosenFgImageView.fitWidthProperty().bind(DuckHunt.stage.widthProperty());
        chosenFgImageView.fitHeightProperty().bind(DuckHunt.stage.heightProperty());

        currentBackGrImageView = new ImageView(backgrounds.get(0));
        currentBackGrImageView.fitWidthProperty().bind(DuckHunt.stage.widthProperty());
        currentBackGrImageView.fitHeightProperty().bind(DuckHunt.stage.heightProperty());

        currentCrossImageView = new ImageView(crosshairs.get(0));
        currentCrossImageView.setFitWidth(12*DuckHunt.scale);
        currentCrossImageView.setFitHeight(12*DuckHunt.scale);

        currentCrossImageView.setLayoutX(DuckHunt.width/2 - (10*DuckHunt.scale));
        currentCrossImageView.setLayoutY(DuckHunt.height/2 - (18*DuckHunt.scale));
    }


    /**
     *
     * @param filePath is the path of the file consisting of .png files.
     * @param size is the number of .png's in the file whose path is given in the first parameter.
     * @return a List< Image > object consisting of the Image objects that were taken from the file whose path was given by the first parameter.
     */
    private List<Image> getImageList(String filePath, int size){
        List<Image> imageList = new ArrayList<>();
        for(int i = 1; i <= size; i++){
            Image image = new Image(filePath + "/" + i + ".png");
            imageList.add(image);
        }

        return imageList;
    }

    /**
     * Background and crosshair selection is handled here. The main function of this method is this.
     */
    public void manageSceneChoosing(){

        Text informativeText = new Text("USE ARROW KEYS TO NAVIGATE\n\tPRESS ENTER TO START\n\t   PRESS ESC TO EXIT");
        informativeText.setFont(Font.font("Arial", 8*DuckHunt.scale));
        informativeText.setFill(Color.ORANGE);
        informativeText.setLayoutX(62*DuckHunt.scale);
        informativeText.setLayoutY(11*DuckHunt.scale);


        Pane root = new Pane(currentBackGrImageView, currentCrossImageView, informativeText);


        Scene switchingScene = new Scene(root, DuckHunt.width, DuckHunt.height);

        DuckHunt.stage.setScene(switchingScene);


        switchingScene.setOnKeyPressed(event -> {
            if(!sceneChosen){
                if(event.getCode() == KeyCode.UP){
                    currentCrossInd = (currentCrossInd == 6) ? 0 : ++currentCrossInd;
                    currentCrossImageView.setImage(crosshairs.get(currentCrossInd));

                }else if(event.getCode() == KeyCode.DOWN){
                    currentCrossInd = (currentCrossInd == 0) ? 6 : --currentCrossInd;
                    currentCrossImageView.setImage(crosshairs.get(currentCrossInd));

                }else if(event.getCode() == KeyCode.RIGHT){
                    currentBackGroundInd = (currentBackGroundInd == 5) ? 0 : ++currentBackGroundInd;
                    currentBackGrImageView.setImage(backgrounds.get(currentBackGroundInd));


                }else if(event.getCode() == KeyCode.LEFT){
                    currentBackGroundInd  = (currentBackGroundInd == 0) ? 5 : --currentBackGroundInd;
                    currentBackGrImageView.setImage(backgrounds.get(currentBackGroundInd));
                }
            }
            if(event.getCode() == KeyCode.ENTER && !sceneChosen){  // the user chose the scene !
                sceneChosen = true;
                WelcomeScreen.titleAudioClip.stop();
                chosenFgImageView.setImage(foregrounds.get(currentBackGroundInd));

                Media introMedia = new Media(getClass().getResource(introMp3Path).toExternalForm());
                introMP = new MediaPlayer(introMedia);
                introMP.setVolume(DuckHunt.volume);
                introMP.play();

                introMP.setOnEndOfMedia(() -> {
                    Level level1 = new Level1(chosenFgImageView, currentBackGrImageView, currentCrossImageView);
                    ((Level1) level1).play();
                });


            }else if (event.getCode() == KeyCode.ESCAPE) {   // We go back the very first scene
                if(introMP != null){
                    if(introMP.getStatus() == MediaPlayer.Status.PLAYING){
                        introMP.stop();
                    }
                }

                
                WelcomeScreen welcomeScreen = new WelcomeScreen("assets/welcome/1.png", "assets/effects/Title.mp3");
                welcomeScreen.manageWelcomeScreen();
            }
        });
    }


}

