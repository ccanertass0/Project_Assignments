import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

public class Level2 extends Level{
    int ammo = 3;
    Duck blueDuck = new Duck("blue", 0, 40);
    public Level2(ImageView chosenForegImgView, ImageView chosenBgImgView, ImageView chosenCrosImgView) {
        super(chosenForegImgView, chosenBgImgView, chosenCrosImgView);

    }


    protected void play(){
        blueDuck.flyDiagonally(true, true);
        root = new Pane(chosenBgImgView, blueDuck.getCurrentDuckImgView(), chosenForegImgView, chosenCrosImgView, levelText, ammoText);
        root.setCursor(Cursor.NONE);
        levelText.setText("Level 2/6");
        ammoText.setText("Ammo left : " + (Math.max(ammo, 0)));
        levelScene = new Scene(root, DuckHunt.width, DuckHunt.height);
        crossMovement();

        levelScene.setOnMouseClicked(event -> {
            if(ammo > 0){
                if (blueDuck.isAlive()) {
                    fireShotIfPossible(ammo--);
                    ammoText.setText("Ammo left : " + (Math.max(ammo, 0)));

                    //Since there is only one duck, once the if statement below is read, it means level is successfully completed.
                    if (blueDuck.getCurrentDuckImgView().getBoundsInParent().intersects(chosenCrosImgView.getBoundsInParent())) {
                        blueDuck.killDuck();
                        levelDoneMP.play();
                        instructionText.setText("Press ENTER to play next level");
                        root.getChildren().addAll(levelPassedText, instructionText);
                        levelScene.setOnKeyPressed(event1 -> {
                            if (event1.getCode() == KeyCode.ENTER) {
                                levelDoneMP.stop();
                                playTheChosenLevel(3);
                            }
                        });
                    }
                }
            }


            //read when the level is unsuccessful
            if(ammo == 0 && blueDuck.isAlive() && !instructionText.getText().equals("Press ENTER to play again\n\tPress ESC to exit")){
                gameOverMP.play();
                instructionText.setText("Press ENTER to play again\n\tPress ESC to exit");
                instructionText.setLayoutX(40*DuckHunt.scale);
                root.getChildren().addAll(gameOverText, instructionText);
                levelScene.setOnKeyPressed(event1 -> {
                    if(event1.getCode() == KeyCode.ENTER){
                        gameOverMP.stop();
                        playTheChosenLevel(1);
                    } else if (event1.getCode() == KeyCode.ESCAPE) {
                        gameOverMP.stop();
                        WelcomeScreen welcomeScreen = new WelcomeScreen("assets/welcome/1.png", "assets/effects/Title.mp3");
                        welcomeScreen.manageWelcomeScreen();
                    }
                });
            }
        });

        DuckHunt.stage.setScene(levelScene);
    }
}
