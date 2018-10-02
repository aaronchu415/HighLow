import java.util.Random;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class HiLow extends Application {
    private final static int WIDTH = 400, HEIGHT = 400;
    private final static Color BACK_COLOR = Color.TRANSPARENT;
    private Text DISPLAY_TEXT;
    private int GAME_GUESS;
    private int USER_GUESS;
    private TextField userInput;
    private String userInputText;
    private Stage toastStage;// for makeToast() param
    private String toastMsg;
    private int count;
    private final int toastMsgTime = 1000;
    private final int fadeInTime = 100;
    private final int fadeOutTime = 1500;

    public void start(Stage primaryStage) {
        count = 0;
        setGameGuess();
        toastStage = primaryStage;
        DISPLAY_TEXT = new Text("Waiting for input..");
        DISPLAY_TEXT.setFill(Color.WHITE);
        HBox modeBox = new HBox(DISPLAY_TEXT);
        modeBox.setPrefHeight(HEIGHT * 0.33);
        modeBox.setAlignment(Pos.CENTER);
        modeBox.setSpacing(20);
        // Submit button
        Button submitButton = new Button("Submit Guess");
        submitButton.setOnAction(this::submitAnswer);
        // Reset button
        Button resetGameButton = new Button("Play Again");
        resetGameButton.setOnAction(this::resetGame);
        HBox buttonBox = new HBox(submitButton, resetGameButton);
        buttonBox.setPrefHeight(HEIGHT * 0.33);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(20);
        // Input box
        userInput = new TextField();
        userInputText = "Enter a value between 1-100";
        userInput.setPromptText(userInputText);
        userInput.setPrefWidth(200);
        Label userInputFieldLabel = new Label("Enter a Guess: ");
        userInputFieldLabel.setTextFill(Color.WHITE);
        HBox inputBox = new HBox(userInputFieldLabel, userInput);
        inputBox.setPrefHeight(HEIGHT * 0.33);
        inputBox.setPrefWidth(WIDTH);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setSpacing(20);
        VBox vbox = new VBox(modeBox, inputBox, buttonBox);
        vbox.setSpacing(5);
        vbox.setAlignment(Pos.CENTER);
        //Set Background
        BackgroundImage myBI = new BackgroundImage(new Image("https://media.istockphoto.com/photos/holdem-poker-table-with-chairs-picture-id164142832?k=6&m=164142832&s=612x612&w=0&h=NU-bwZ2Fx94DBpAYlPrudUF0zPR1NVEFovBM6j72bjs=",
                WIDTH, HEIGHT, false, true),
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        vbox.setBackground(new Background(myBI));
        Scene scene = new Scene(vbox, WIDTH, HEIGHT, BACK_COLOR);
        primaryStage.setScene(scene);
        primaryStage.setTitle("HiLow Game");
        primaryStage.setResizable(false);
        primaryStage.show();
    } // end of start()

    private void setGameGuess() {
        Random rand = new Random();
        GAME_GUESS = rand.nextInt(100) + 1;
    }

    private void resetGame(ActionEvent event) {
        setGameGuess();
        USER_GUESS = 0;
        DISPLAY_TEXT.setText("Waiting for input..");
        userInput.clear();
        userInput.setPromptText("Enter a value between 1-100");
    }

    private void submitAnswer(ActionEvent event) {
        try {
            USER_GUESS = Integer.parseInt(userInput.getText());
            if (USER_GUESS < 1 || USER_GUESS > 100) {
                throw new IllegalArgumentException();
            } else {
                count++;
                if (USER_GUESS == GAME_GUESS) {
                    toastMsg = "HOORAY!! You win! \nYou guessed " + count + " times!";
                    // this will let final toast stays longer.
                    Toast.makeText(toastStage, toastMsg, 3500, fadeInTime, fadeOutTime);
                } else if (USER_GUESS < GAME_GUESS) {
                    toastMsg = "Almost! Try a bigger number";
                    makeToast();
                } else {
                    toastMsg = "Almost! Try a smaller number";
                    makeToast();
                }
            }
        } catch (Exception e) {
            userInput.clear();
            toastMsg = "Not really, enter 1-100";
            makeToast();
            DISPLAY_TEXT.setText("Waiting for input..");
        }
    }

    private void makeToast() {
        Toast.makeText(toastStage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime);
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Following source code is from:
     * https://stackoverflow.com/questions/26792812/android-toast-equivalent-in-javafx
     **/
    public final static class Toast {
        public static void makeText(Stage ownerStage, String toastMsg, int toastDelay, int fadeInDelay, int fadeOutDelay) {
            Stage toastStage = new Stage();
            toastStage.initOwner(ownerStage);
            toastStage.setResizable(false);
            toastStage.initStyle(StageStyle.TRANSPARENT);
            Text text = new Text(toastMsg);
            text.setFont(Font.font("Verdana", 32));
            text.setFill(Color.GOLD);
            StackPane root = new StackPane(text);
            root.setStyle("-fx-background-radius: 20; -fx-background-color: rgba(0, 0, 0, 0.2); -fx-padding: 50px;");
            root.setOpacity(0);
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            toastStage.setScene(scene);
            toastStage.show();
            Timeline fadeInTimeline = new Timeline();
            KeyFrame fadeInKey1 = new KeyFrame(Duration.millis(fadeInDelay), new KeyValue(toastStage.getScene().getRoot().opacityProperty(), 1));
            fadeInTimeline.getKeyFrames().add(fadeInKey1);
            fadeInTimeline.setOnFinished((ae) ->
            {
                new Thread(() -> {
                    try {
                        Thread.sleep(toastDelay);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Timeline fadeOutTimeline = new Timeline();
                    KeyFrame fadeOutKey1 = new KeyFrame(Duration.millis(fadeOutDelay), new KeyValue(toastStage.getScene().getRoot().opacityProperty(), 0));
                    fadeOutTimeline.getKeyFrames().add(fadeOutKey1);
                    fadeOutTimeline.setOnFinished((aeb) -> toastStage.close());
                    fadeOutTimeline.play();
                }).start();
            });
            fadeInTimeline.play();
        }
    }
}