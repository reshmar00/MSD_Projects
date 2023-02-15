package com.example.synthesizer;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;


public class AudioComponentWidgetBase extends Pane {
    AudioComponent audioComponent_;
    static AnchorPane parent_;
    String name_;
    int hzValue;
    public Line line_;

    public static boolean speakerConnected_ = false;

    Circle inputCircle_;
    Circle outputCircle_;

    double mouseStartDragX_, mouseStartDragY_, widgetStartDragX_, widgetStartDragY_;
    static HBox baseLayout;
    AudioComponentWidgetBase(AudioComponent ac, AnchorPane parent, String name)
    {
        audioComponent_ = ac;
        parent_ = parent;
        name_ = name;
        baseLayout = new HBox();
        baseLayout.setStyle("-fx-border-color: darkgray; -fx-border-image-width: 8; -fx-background-color: white");

        /* Left side */
        VBox leftSide = new VBox();
        Label title = new Label();
        title.setMouseTransparent(true);
        VBox center = new VBox();
        inputCircle_ = new Circle(10);
        outputCircle_ = new Circle(10);
        center.getChildren().add(title);
        center.setAlignment(Pos.CENTER);
        center.setOnMousePressed(this::handleDrag);
        center.setOnMouseDragged(this::handleDrag);
        leftSide.getChildren().add(center);
        switch (name_) {
            case "Sine Wave" -> {
                title.setText("SineWave (440 Hz)");
                String font_name = Font.getFamilies().get(109); //Font Name:Monaco (109)
                int size = 15;
                Font font = Font.font(font_name, FontWeight.EXTRA_BOLD, FontPosture.REGULAR, size);
                title.setFont(font);
                /* Create and set action for slider to vary the pitch */
                Slider slider = new Slider();
                slider.setMin(220);
                slider.setMax(880);
                slider.setOnMouseDragged(e -> changeDisplayFreq(e, slider, title));
                leftSide.getChildren().add(slider);
            }
            case "Volume" -> {
                title.setText("Volume");
                String font_name = Font.getFamilies().get(109); //Font Name:Monaco (109)
                int size = 15;
                Font font = Font.font(font_name, FontWeight.EXTRA_BOLD, FontPosture.REGULAR, size);
                title.setFont(font);
                inputCircle_.setFill(Color.GREEN);
                leftSide.getChildren().add(inputCircle_);
            }
            case "Mixer" -> {
                title.setText("Mixer");
                String font_name = Font.getFamilies().get(109); //Font Name:Monaco (109)
                int size = 15;
                Font font = Font.font(font_name, FontWeight.EXTRA_BOLD, FontPosture.REGULAR, size);
                title.setFont(font);
                inputCircle_.setFill(Color.RED);
                leftSide.getChildren().add(inputCircle_);
            }
        }

        leftSide.setAlignment(Pos.CENTER);
        leftSide.setPadding(new Insets(10, 30,10,30));
        leftSide.setSpacing(5);

        /* Right side */
        VBox rightSide = new VBox();

        Button closeBtn = new Button("x");
        closeBtn.setOnAction(e-> closeWidget());
        rightSide.getChildren().add(closeBtn);

        switch (name_) {
            case "Sine Wave" -> {
                outputCircle_.setFill(Color.BLUE);
                /* Handling the line connection between widgets */
                outputCircle_.setOnMousePressed(e -> startConnection(e, outputCircle_));
                outputCircle_.setOnMouseDragged(this::moveConnection);
                outputCircle_.setOnMouseReleased(e -> endConnection(e, inputCircle_));
                rightSide.getChildren().add(outputCircle_);
            }
            case "Volume", "Mixer" -> {
                /* Handling the line connection between widgets */
                outputCircle_.setFill(Color.BLACK);
                outputCircle_.setOnMousePressed(e -> startConnection(e, outputCircle_));
                outputCircle_.setOnMouseDragged(this::moveConnection);
                outputCircle_.setOnMouseReleased(e -> endConnection(e, inputCircle_));
                rightSide.getChildren().add(outputCircle_);
            }
        }

        rightSide.setAlignment(Pos.CENTER);
        rightSide.setPadding(new Insets(10, 20,10,20));
        rightSide.setSpacing(5);

        baseLayout.getChildren().add(leftSide);
        baseLayout.getChildren().add(rightSide);
        baseLayout.setAlignment(Pos.CENTER);

        this.getChildren().add(baseLayout);

        this.setLayoutX(50);
        this.setLayoutY(100);

        parent_.getChildren().add(this);
    }

    public AudioComponentWidgetBase() {
    }

    private void handleDrag(MouseEvent e) {
        Bounds parentBounds = parent_.getBoundsInParent();
        AnchorPane.setLeftAnchor(this, e.getSceneX() - parentBounds.getMinX());
        AnchorPane.setTopAnchor(this, e.getSceneY() - parentBounds.getMinY());

        if (line_ != null) {
            Bounds outputBounds = outputCircle_.localToScene(outputCircle_.getBoundsInLocal());
            line_.setStartX(outputBounds.getCenterX() - parentBounds.getMinX());
            line_.setStartY(outputBounds.getCenterY() - parentBounds.getMinY());
        }
    }
    private void startConnection(MouseEvent e, Circle output) {
        // If there is a line (i.e., if we are connected to someone else)...
        if (line_ != null){
            // ... remove the existing line so that we can create a new connection
            parent_.getChildren().remove(line_);
        }
        Bounds parentBounds = parent_.getBoundsInParent();
        Bounds outputBounds = output.localToScene(output.getBoundsInLocal());

        line_ = new Line();
        line_.setStrokeWidth(5);
        line_.setStartX(outputBounds.getCenterX() - parentBounds.getMinX());
        line_.setStartY(outputBounds.getCenterY() - parentBounds.getMinY());
        line_.setEndX(e.getSceneX());
        line_.setEndY(e.getSceneY());

        // For any widget, we have to add it to the parent
        // so that it will be drawn by javafx
        parent_.getChildren().add(line_);
        //System.out.println();
    }
    private void moveConnection(MouseEvent e) {
        Bounds parentBounds = parent_.getBoundsInParent();
        line_.setEndX(e.getSceneX() - parentBounds.getMinX());
        line_.setEndY(e.getSceneY() - parentBounds.getMinY());
    }

    private void endConnection(MouseEvent e, Circle input) {
        Bounds parentBounds = parent_.getBoundsInParent();
        Bounds inputBounds;
        AudioComponentWidgetBase widget = null;
        double distanceFromInput = 16.0;
        for(int i = 0; i < SynthesizeApplication.widgets_.size(); i++){
            widget = SynthesizeApplication.widgets_.get(i);
            if(widget.name_.equals("Volume") || widget.name_.equals("Mixer")){
                inputBounds = widget.inputCircle_.localToScene(widget.inputCircle_.getBoundsInLocal());
                distanceFromInput = Math.sqrt(Math.pow(inputBounds.getCenterX() - e.getSceneX(), 2.0) + Math.pow(inputBounds.getCenterY() - e.getSceneY(), 2.0));
                if (distanceFromInput <= 15){
                    break;
                }
            }
        }
        Circle speaker = SynthesizeApplication.speaker_;
        Bounds speakerBounds = speaker.localToScene(speaker.getBoundsInLocal());
        double distanceFromSpeaker = Math.sqrt(Math.pow(speakerBounds.getCenterX() - e.getSceneX(), 2.0) + Math.pow(speakerBounds.getCenterY() - e.getSceneY(), 2.0));

        if (distanceFromInput <= 15){
            /* Handle actually connecting to speaker */
            widget.getAudioComponent().connectInput(audioComponent_);
            line_.setEndX(e.getSceneX() - parentBounds.getMinX());
            line_.setEndY(e.getSceneY() - parentBounds.getMinY());
            SynthesizeApplication.widgets_.add(this);
        }
        else if (distanceFromSpeaker <= 15){
            /* Handle actually connecting to speaker */
            SynthesizeApplication.speakerInput_ = audioComponent_;
            line_.setEndX(e.getSceneX() - parentBounds.getMinX());
            line_.setEndY(e.getSceneY() - parentBounds.getMinY());
            SynthesizeApplication.widgets_.add(this);
            speakerConnected_ = true;
        }
        else{
            parent_.getChildren().remove(line_);
            line_ = null;
        }
    }

    public void changeDisplayFreq(MouseEvent e, Slider slider, Label title) {
        hzValue = (int)slider.getValue();
        title.setText("SineWave (" + hzValue + "Hz)");
        audioComponent_ = new SineWave(hzValue);
    }

    private void closeWidget() {
        parent_.getChildren().remove(this);
        SynthesizeApplication.widgets_.remove(this);
        parent_.getChildren().remove(line_);
        line_ = null;
    }

    public AudioComponent getAudioComponent()
    {
        return audioComponent_;
    }

    public Slider createSliderComponent(Label title){
        Slider slider = new Slider(220, 880, 440);
        slider.setOnMouseDragged(e->changeDisplayFreq(e, slider, title));

        VBox verticalComponentWidget = new VBox();
        verticalComponentWidget.getChildren().add(title);
        verticalComponentWidget.getChildren().add(slider);
        verticalComponentWidget.setOnMousePressed(this::handleMousePress);
        return slider;
    }

    private void handleMousePress(MouseEvent e) {
        //  System.out.println("mouse was pressed");
    }

}
