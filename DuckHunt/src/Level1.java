
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

public class Level1 extends Level{
    int ammo = 3;
    Duck blackDuck = new Duck("black", 0 ,40);

    public Level1(ImageView chosenForegImgView, ImageView chosenBgImgView, ImageView chosenCrosImgView) {
        super(chosenForegImgView, chosenBgImgView, chosenCrosImgView);

    }

    /**
     * every action of the level is controlled with this play method.
     * This method is used in all the child classes of the Level class.
     * They perform similar tasks
     */

    protected void play(){
        blackDuck.flyHorizontally(true);
        root = new Pane(chosenBgImgView, blackDuck.getCurrentDuckImgView(), chosenForegImgView, chosenCrosImgView, levelText, ammoText);
        root.setCursor(Cursor.NONE);
        levelText.setText("Level 1/6");
        ammoText.setText("Ammo Left : " + ammo);
        levelScene = new Scene(root, DuckHunt.width, DuckHunt.height);
        crossMovement();

        levelScene.setOnMouseClicked(event -> {
            if(ammo > 0){
                if(blackDuck.isAlive()) {
                    fireShotIfPossible(ammo--);
                    ammoText.setText("Ammo left : " + (Math.max(ammo, 0)));

                    // there is only one duck so once the if statement below is read, it means level is successfully completed
                    if (blackDuck.getCurrentDuckImgView().getBoundsInParent().intersects(chosenCrosImgView.getBoundsInParent())) {

                        blackDuck.killDuck();
                        levelDoneMP.play();
                        instructionText.setText("Press ENTER to play next level");
                        root.getChildren().addAll(levelPassedText , instructionText);

                        levelScene.setOnKeyPressed(event1 -> {
                            if(event1.getCode() == KeyCode.ENTER){
                                levelDoneMP.stop();
                                playTheChosenLevel(2);
                            }
                        });

                    }
                }
            }



            // read when the game is over.
            if(ammo == 0 && blackDuck.isAlive() && !instructionText.getText().equals("Press ENTER to play again\n\tPress ESC to exit")){
                gameOverMP.play();
                instructionText.setText("Press ENTER to play again\n\tPress ESC to exit");
                root.getChildren().addAll(gameOverText, instructionText);
                instructionText.setLayoutX(40*DuckHunt.scale);
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
