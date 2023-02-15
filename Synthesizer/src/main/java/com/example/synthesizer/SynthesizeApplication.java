/*
    Start by drawing some GUI elements and reacting to important events
    (like clicking a button). Then move/modify the code from main that actually
    plays a sound to be run when the play button is pressed. Note, rather than
    the loop we used to keep the program from exiting before the sound stopped,
    you can either open a clip before the button is clicked, and simply play it
    in the handler, or add a LineListener to close the clip when a "STOP" event
    occurs. If you keep the current loop, you'll freeze the UI while sound is playing.
*/

// Created by Reshma Raghavan
// Completed GUI Part 1 on October 7, 2022, at 11:21 am.

package com.example.synthesizer;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayList;


public class SynthesizeApplication extends Application {

    private AnchorPane mainCanvas_;
    public static Circle speaker_ = new Circle(400, 200, 15);
    public static Scene scene_;
    public static AudioComponent speakerInput_;
    public static ArrayList<AudioComponentWidgetBase> widgets_ = new ArrayList<>();
    @Override
    public void start(Stage stage){

        /* Main Pane/parent */
        BorderPane root = new BorderPane();
        scene_ = new Scene(root, 600, 400);

        /* Right Panel Info */
        VBox rightPanel = new VBox();
        rightPanel.setPadding(new Insets(10));
        rightPanel.setStyle("-fx-background-color: darkgray");

        Button sineWaveBtn = new Button("Sine Wave");
        String buttonFontName = Font.getFamilies().get(109); //Font Name:Monaco (109)
        int buttonFontSize = 13;
        Font buttonFont = Font.font(buttonFontName, FontWeight.SEMI_BOLD, FontPosture.REGULAR, buttonFontSize);
        sineWaveBtn.setFont(buttonFont);
        rightPanel.getChildren().add(sineWaveBtn); // Displays button
        sineWaveBtn.setOnAction(e -> createSWComponent()); // Action on click

        /* Adding space */
        VBox spacingBetSWAndVolW = new VBox();
        spacingBetSWAndVolW.setPadding(new Insets(10));
        spacingBetSWAndVolW.setSpacing(5);
        spacingBetSWAndVolW.setStyle("-fx-background-color: darkgray");
        rightPanel.getChildren().add(spacingBetSWAndVolW);

        Button volumeBtn = new Button("Volume");
        volumeBtn.setFont(buttonFont);
        rightPanel.getChildren().add(volumeBtn); // Displays button
        volumeBtn.setOnAction(e -> createVolumeComponent()); // Action on click

        //Adding space
        VBox spacingBetVolAndMixW = new VBox();
        spacingBetVolAndMixW.setPadding(new Insets(10));
        spacingBetVolAndMixW.setSpacing(5);
        spacingBetVolAndMixW.setStyle("-fx-background-color: darkgray");
        rightPanel.getChildren().add(spacingBetVolAndMixW);

        Button mixerBtn = new Button("Mixer");
        mixerBtn.setFont(buttonFont);
        rightPanel.getChildren().add(mixerBtn); // Displays button
        mixerBtn.setOnAction(e -> createMixerComponent()); // Action on click

        /*Center Panel Info*/
        mainCanvas_ = new AnchorPane();
        mainCanvas_.setStyle("-fx-background-color: lightblue");
        Circle mySpeaker = createSpeakerWidget();
        mainCanvas_.getChildren().add(mySpeaker);

        /*Bottom Panel Info*/
        HBox bottomPanel = new HBox();
        bottomPanel.setStyle("-fx-background-color: lightgray");

        Button playBtn = new Button("Play");
        playBtn.setFont(buttonFont);
        if (mySpeaker!=null) { // change
            playBtn.setOnAction(e -> playNetwork());
        }

        bottomPanel.getChildren().add(playBtn);
        bottomPanel.setAlignment(Pos.CENTER);

        /*Top Panel Info*/
        HBox topPanel = new HBox();
        topPanel.setStyle("-fx-background-color: lightgray");
        topPanel.setAlignment(Pos.BOTTOM_CENTER);

        Text topPanelTitle = new Text("Reshma's Synthesizer");
        String font_name = Font.getFamilies().get(41); //Font Name:Cochin (41)

        int size = 20;
        Font font = Font.font(font_name, FontWeight.EXTRA_BOLD, FontPosture.REGULAR, size);


        // Setting font to the text
        topPanelTitle.setFont(font);
        // Adding the text node to the top panel
        topPanel.getChildren().add(topPanelTitle);


        // Adding borderPane elements to borderPane
        root.setRight(rightPanel);
        root.setCenter(mainCanvas_);
        root.setBottom(bottomPanel);
        root.setTop(topPanel);

        // Write once
        stage.setTitle("Synthesizer");
        stage.setScene(scene_);
        stage.show();
    }

    private Circle createSpeakerWidget() {
        speaker_.setFill(Color.BLACK);
        speakerInput_ = null;
        /* Condition to check if widget is connected to speaker before playing anything */
        if (AudioComponentWidgetBase.speakerConnected_){
            playNetwork();
        }
        return speaker_;
    }

    private void playNetwork() {
        if (widgets_.size() == 0){
            return;
        }
        try{
            AudioFormat format16 = new AudioFormat(44100, 16, 1, true, false);

            Clip c = AudioSystem.getClip();
            AudioListener listener = new AudioListener(c);
            AudioComponent ac = speakerInput_;

            byte[] data = ac.getClip().getData();
            c.open(format16, data, 0, data.length);
            c.start();
            c.addLineListener(listener);
        }
        catch(LineUnavailableException e){
            e.printStackTrace();
        }
    }

    private void createSWComponent() {
        SineWave sineWaveComponent = new SineWave(440);
        AudioComponentWidgetBase acw = new AudioComponentWidgetBase(sineWaveComponent, mainCanvas_, "Sine Wave");
        widgets_.add(acw);
    }

    private void createMixerComponent() {
        AudioComponent mixerComponent = new Mixer();
        AudioComponentWidgetBase acw = new AudioComponentWidgetBase(mixerComponent, mainCanvas_, "Mixer");
        widgets_.add(acw);
    }

    private void createVolumeComponent() {
        AudioComponent volumeComponent = new VolumeAdjuster(0.5);
        AudioComponentWidgetBase acw = new AudioComponentWidgetBase(volumeComponent, mainCanvas_, "Volume");
        widgets_.add(acw);
    }

    public static void main(String[] args) {
        launch();
    }
}