import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class DuckHunt extends Application {
    public static double scale = 3;
    public static double width = 256*scale;
    public static double height = 240*scale;
    public static double volume = 0.025;
    public static Stage stage;
    private String faviconPath = "assets/favicon/1.png";


    /**
     * The main class, where title, width, height, volume and favicon is set, of the code.
     * It is enough to compile this class to play the game
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set. The primary stage will be embedded in
     * the browser if the application was launched as an applet.
     * Applications may create other stages, if needed, but they will not be
     * primary stages and will not be embedded in the browser.
     */

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("HUBBM Duck Hunt");
        stage.setWidth(width);
        stage.setHeight(height);
        stage.getIcons().add(new Image(faviconPath));
        stage.setResizable(false);  // one can not resize with cursor the opened window of the game

        WelcomeScreen welcomeScreen = new WelcomeScreen("assets/welcome/1.png", "assets/effects/Title.mp3");
        welcomeScreen.manageWelcomeScreen();  // opens the welcoming screen

        stage.show();
    }
}