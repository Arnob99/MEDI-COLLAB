package MAIN;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ApprovalDialogue extends Stage {
    public ApprovalDialogue() throws FileNotFoundException {
        Pane pane = new Pane();

        initStyle(StageStyle.UNDECORATED);
        initModality(Modality.APPLICATION_MODAL);

        Rectangle bg = new Rectangle(450, 250, Color.valueOf("f0ffff"));
        bg.setStroke(Color.valueOf("#464646"));
        bg.setStrokeWidth(2);
        bg.setStrokeType(StrokeType.INSIDE);

        File file = new File("src/Resources/Images/confirm_icon.png");
        ImageView imageView = new ImageView(new Image(new FileInputStream(file)));
        imageView.setFitWidth(120);
        imageView.setPreserveRatio(true);

        String s = "The appointment request is pending for doctor's approval. " +
                "The doctor will give you a certain time for your appointment. " +
                "If the doctor cannot give you a time, it is mostly because the doctor is very busy. " +
                "In that case someone will be in contact with you. " +
                "Thank you for your patience. ";

        Label label = new Label(s);
        label.setPrefWidth(bg.getWidth() - imageView.getFitWidth() - 42);
        label.setStyle("-fx-font-size: 14;  -fx-text-fill: #464646;" +
                "-fx-wrap-text: TRUE;");

        HBox dialogue = new HBox(10, imageView, new Separator(Orientation.VERTICAL), label);
        dialogue.setPadding(new Insets(10));

        JFXButton OK = new JFXButton("OK");
        OK.setAlignment(Pos.CENTER);
        OK.setFont(new Font(14));
        OK.setFocusTraversable(false);
        OK.setPrefWidth(100);
        OK.setPrefHeight(30);
        OK.setStyle("-fx-border-color: #2ed2ff; -fx-border-radius: 16; -fx-text-fill: #464646;");
        OK.setButtonType(JFXButton.ButtonType.RAISED);
        OK.setRipplerFill(Color.BLACK);
        OK.setTranslateX(bg.getWidth()/2.0 - 50);
        OK.setTranslateY(bg.getHeight() - 60);
        OK.setDefaultButton(true);
        OK.setOnMouseEntered(mouseEvent -> {
            OK.setStyle("-fx-background-color: #2ed2ff; -fx-background-radius: 16;" +
                    "-fx-border-color: #2ed2ff;         -fx-border-radius: 16;" +
                    "-fx-text-fill: WHITE;");
        });
        OK.setOnMouseExited(mouseEvent -> {
            OK.setStyle("-fx-border-color: #2ed2ff; -fx-border-radius: 16; -fx-text-fill: #464646;");
        });
        OK.setOnAction(actionEvent -> {
            CloseDialogue();
        });

        pane.getChildren().addAll(bg, dialogue, OK);

        Scene scene = new Scene(pane);
        scene.getStylesheets().add(getClass().getResource("/Resources/CSS/ApprovalDialogue.css").toExternalForm());
        setScene(scene);
    }

    void OpenDialogue() throws InterruptedException {
        showAndWait();
    }

    void CloseDialogue() {
        close();
    }
}
