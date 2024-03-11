import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

public class Level3 extends Level{
    int ammo = 6;

    Duck blackDuck = new Duck("black", 0, 40);
    Duck blueDuck = new Duck("blue", 250,65);
    Duck[] ducks = {blueDuck, blackDuck};

    public Level3(ImageView chosenForegImgView, ImageView chosenBgImgView, ImageView chosenCrosImgView) {
        super(chosenForegImgView, chosenBgImgView, chosenCrosImgView);
    }

    protected void play(){
        blackDuck.flyHorizontally(true);
        blueDuck.flyHorizontally(false);

        root = new Pane(chosenBgImgView, blueDuck.getCurrentDuckImgView(), blackDuck.getCurrentDuckImgView(),chosenForegImgView, chosenCrosImgView, levelText, ammoText);
        root.setCursor(Cursor.NONE);
        levelText.setText("Level 3/6");
        ammoText.setText("Ammo Left : " + ammo);
        levelScene = new Scene(root, DuckHunt.width, DuckHunt.height);
        crossMovement();

        levelScene.setOnMouseClicked(event -> {
            if(ammo > 0){
                if(killedDucks != 2) {
                    fireShotIfPossible(ammo--);
                    ammoText.setText("Ammo left : " + (Math.max(ammo, 0)));
                }
                for(Duck duck : ducks){
                    if(duck.isAlive()){
                        if(duck.getCurrentDuckImgView().getBoundsInParent().intersects(chosenCrosImgView.getBoundsInParent())){
                            duck.killDuck();
                            killedDucks++;
                        }
                    }
                }
            }


            // read when level is successfully completed
            if(killedDucks == 2 && !instructionText.getText().equals("Press ENTER to play next level")){
                levelDoneMP.play();
                instructionText.setText("Press ENTER to play next level");
                root.getChildren().addAll(levelPassedText, instructionText);
                levelScene.setOnKeyPressed(event1 -> {
                    if(event1.getCode() == KeyCode.ENTER){
                        levelDoneMP.stop();
                        playTheChosenLevel(4);
                    }
                });

            }




            // read when the user fails to pass the level
            if(ammo == 0 && killedDucks != 2 && !instructionText.getText().equals("Press ENTER to play again\n\tPress ESC to exit")){
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
