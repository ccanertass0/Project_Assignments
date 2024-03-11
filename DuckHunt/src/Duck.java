import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

public class Duck {
    private boolean isAlive = true;
    private String color;
    private double speedX = 22*DuckHunt.scale;
    private double speedY = -22*DuckHunt.scale;

    double gravityImitator = 1.1;

    private String blackDuckPath = "assets/duck_black";
    private String blueDuckPath = "assets/duck_blue";
    private String redDuckPath = "assets/duck_red";



    private List<Image> duckImages;
    private int currentImgVIndex = 0;
    private boolean isMovingRight = true;

    private double initXPoS;
    private double initYPos;

    Timeline flyingTimeline;
    Timeline fallingTimeline;


    private String gunshotPath = "assets/effects/Gunshot.mp3";
    private String duckFallinMP3Path = "assets/effects/DuckFalls.mp3";



    private ImageView currentDuckImgView;




    /**
     * a duck gets created here
     * @param color is the color of the duck we want to put in the scene
     * @param xPos  the x coordinate of the duck's initial position
     * @param yPos  the y coordinate of the duck's initial position
     */
    public Duck(String color, double xPos, double yPos) {
        this.color = color;
        this.initXPoS = xPos * DuckHunt.scale;
        this.initYPos = yPos * DuckHunt.scale;

        if(this.color.equals("black")){
            duckImages = createDuckImgVList(blackDuckPath);

        } else if (this.color.equals("blue")) {
            duckImages = createDuckImgVList(blueDuckPath);

        } else if (this.color.equals("red")) {
            duckImages = createDuckImgVList(redDuckPath);
        }

        currentDuckImgView = new ImageView(duckImages.get(3));

        currentDuckImgView.setTranslateX(initXPoS);
        currentDuckImgView.setTranslateY(initYPos);
        currentDuckImgView.setFitWidth(26*DuckHunt.scale);
        currentDuckImgView.setFitHeight(26*DuckHunt.scale);
    }





    /**
     * in our {@link Duck} constructor this method is used according to the chosen color of the duck
     * just th
     * @param path is the path of the images of the duck
     * @return a List< Image > object of the duck having the chosen duck's .png files
     */
    private List<Image> createDuckImgVList(String path){
        List<Image> duckList = new ArrayList<>();
        for(int i = 0; i < 8; i++){
            Image image = new Image(path +  "/" + (i+1) + ".png");

            duckList.add(image);

        }
        return duckList;
    }





    /**
     * ducks flies horizontally with this method the parameter determines the direction
     * @param isFlyingRight should be true if we want the duck to start flying rightwards at the time it was created, if we want this for leftwards then false should be given
     */

    public void flyHorizontally(boolean isFlyingRight){
        if(!isFlyingRight){
            isMovingRight = false;
            currentDuckImgView.setScaleX(-1);
        }
        flyingTimeline = new Timeline();
        flyingTimeline.setCycleCount(Timeline.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.millis(250), event -> {
            currentImgVIndex = (currentImgVIndex + 1) % 3;
            currentDuckImgView.setImage(duckImages.get(currentImgVIndex + 3));

            if(isMovingRight){
                if(currentDuckImgView.getTranslateX() + 36*DuckHunt.scale >= DuckHunt.width){
                    currentDuckImgView.setScaleX(-1);  //duck image reflected
                    currentDuckImgView.setTranslateX(currentDuckImgView.getTranslateX() - speedX); //negativ value of speed
                    isMovingRight = false;
                }else{
                    currentDuckImgView.setTranslateX(currentDuckImgView.getTranslateX() + speedX);
                }
            }else{
                if(currentDuckImgView.getTranslateX() <= 0){
                    currentDuckImgView.setScaleX(+1);
                    currentDuckImgView.setTranslateX(currentDuckImgView.getTranslateX() + speedX);
                    isMovingRight = true;
                }else{
                    currentDuckImgView.setTranslateX(currentDuckImgView.getTranslateX() - speedX);
                }
            }

        });
        flyingTimeline.getKeyFrames().add(keyFrame);
        flyingTimeline.play();
    }





    /**
     * This is simlar to the method above {@link Duck#flyHorizontally(boolean)}
     * This makes the duck fly with a degree of 45 from the coordinate axes.
     * @param isFlyingRight true if it flies rightwards at the time of the duck creation, else false
     * @param isFlyingUpward true if it flies upwards at the time of the duck creation, else false
     */
    public void flyDiagonally(boolean isFlyingRight, boolean isFlyingUpward){
        currentDuckImgView = new ImageView(duckImages.get(0));
        currentDuckImgView.setTranslateX(initXPoS);
        currentDuckImgView.setTranslateY(initYPos);
        currentDuckImgView.setFitWidth(26*DuckHunt.scale);
        currentDuckImgView.setFitHeight(26*DuckHunt.scale);


        if(!isFlyingRight) {// moves left
            speedX *= -1;
            currentDuckImgView.setScaleX(-1);
        }
        if(!isFlyingUpward){
            speedY *= -1;
            currentDuckImgView.setScaleY(-1);
        }

        flyingTimeline = new Timeline();
        flyingTimeline.setCycleCount(Timeline.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.millis(200), event -> {
            currentDuckImgView.setTranslateX(currentDuckImgView.getTranslateX() + speedX);
            currentDuckImgView.setTranslateY(currentDuckImgView.getTranslateY() + speedY);

            currentImgVIndex = (currentImgVIndex + 1) % 3;
            currentDuckImgView.setImage(duckImages.get(currentImgVIndex));



            if(currentDuckImgView.getTranslateX() <= -10*DuckHunt.scale || currentDuckImgView.getTranslateX() >= DuckHunt.width - 39*DuckHunt.scale){ // reaches left or right edges
                currentDuckImgView.setScaleX((currentDuckImgView.getScaleX() == 1) ? -1 : 1);
                speedX *= -1;
            }

            if(currentDuckImgView.getTranslateY() <= 0 || currentDuckImgView.getTranslateY() >= DuckHunt.height - 41*DuckHunt.scale ){  // reaches top or bottom edges
                currentDuckImgView.setScaleY((currentDuckImgView.getScaleY() == 1) ? -1 : 1);
                speedY *= -1;
            }


        });
        flyingTimeline.getKeyFrames().add(keyFrame);
        flyingTimeline.play();
    }




    /**
     * Duck gets killed with this method. Moreover, we make the duck start falling with this method
     */
    public void killDuck(){
        isAlive = false;

        flyingTimeline.stop();
        speedY = (speedY > 0) ? speedY : -speedY;

        currentDuckImgView.setImage(duckImages.get(6));

        Timeline deadTimeline = new Timeline(new KeyFrame(Duration.millis(380), event -> {
            currentDuckImgView.setImage(duckImages.get(7));
            currentDuckImgView.setScaleY(1);
            Media fallingMedia = new Media(getClass().getResource(duckFallinMP3Path).toExternalForm());
            MediaPlayer fallingMP = new MediaPlayer(fallingMedia);
            fallingMP.setVolume(DuckHunt.volume);
            fallingMP.play();
        }));
        deadTimeline.play();

        deadTimeline.setOnFinished(event -> {
            fallingTimeline.play();
            currentDuckImgView.setTranslateY(currentDuckImgView.getTranslateY() + speedY/2);
        });



        fallingTimeline = new Timeline();
        fallingTimeline.setCycleCount(Timeline.INDEFINITE);


        KeyFrame keyFrame = new KeyFrame(Duration.millis(300), event -> {

            gravityImitator *= 1.1;  // a little imitation of gravity effect , the duck has acceleration here
            if(!isMovingRight){
                currentDuckImgView.setScaleX(-1);  // if it was flying rightwards it will die as looking rightwards, the same thing applies for the left direction surely
            }
            currentDuckImgView.setTranslateY(currentDuckImgView.getTranslateY() + speedY*gravityImitator);

        });



        fallingTimeline.getKeyFrames().add(keyFrame);

    }



    public ImageView getCurrentDuckImgView() {
        return currentDuckImgView;
    }


    public boolean isAlive() {
        return isAlive;
    }


}
