package MAIN;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.SQLException;

public class ConfirmationDialogue extends Stage {
    public ConfirmationDialogue() {
        Pane pane = new Pane();

        initStyle(StageStyle.UNDECORATED);
        initModality(Modality.APPLICATION_MODAL);

        Rectangle bg = new Rectangle(350, 220, Color.valueOf("#f0ffff"));
        bg.setStrokeWidth(2);
        bg.setStroke(Color.valueOf("#464646"));
        bg.setStrokeType(StrokeType.INSIDE);

        Label header = new Label("Please Confirm.");
        header.setFont(Font.font(24));
        header.setPrefWidth(320);
        header.setTextFill(Color.valueOf("#464646"));

        Label content = new Label("Please Enter Your Password to Update Profile.");
        content.setFont(Font.font(14));
        content.setTextFill(Color.valueOf("#464646"));

        JFXPasswordField passwordField = new JFXPasswordField();
        passwordField.setPromptText("Password");
        passwordField.setFont(new Font(13));
        passwordField.setFocusTraversable(true);
        passwordField.setStyle("-fx-text-fill: #464646;     -fx-prompt-text-fill: #2ed2ff;");
        passwordField.setFocusColor(Color.valueOf("#2ed2ff"));
        passwordField.setUnFocusColor(Color.valueOf("#30d6ff80"));

        VBox dialogue = new VBox(10, header, new Separator(Orientation.HORIZONTAL),
                content, passwordField);
        dialogue.setPadding(new Insets(15));

        JFXButton update = new JFXButton("Update");
        update.setAlignment(Pos.CENTER);
        update.setFont(new Font(14));
        update.setFocusTraversable(false);
        update.setPrefHeight(30);
        update.setPrefWidth(100);
        update.setStyle("-fx-border-color: #2ed2ff; " +
                "-fx-border-radius: 16;                     -fx-text-fill: #464646; ");
        update.setButtonType(JFXButton.ButtonType.RAISED);
        update.setRipplerFill(Color.BLACK);
        update.setTranslateX(bg.getWidth()/2 - 110);
        update.setTranslateY(bg.getHeight() - 50);
        update.setDefaultButton(true);
        update.setOnMouseEntered(e -> {
            update.setStyle("-fx-background-color: #2ed2ff;     -fx-background-radius: 16; " +
                    "-fx-border-color: #2ed2ff;                 -fx-border-radius: 16; " +
                    "-fx-text-fill: WHITE;");
        });
        update.setOnMouseExited(e -> {
            update.setStyle("-fx-border-color: #2ed2ff; " +
                    "-fx-border-radius: 16;                     -fx-text-fill: #464646; ");
        });
        update.setOnAction(e -> {
            try {
                if (Medi_collab.User_Info_Resultset.getString("PASSWORD").equals(passwordField.getText()))
                    User_Profile_Controller.update_profile = true;
            }
            catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            CloseDialogue();
        });

        JFXButton CANCEL = new JFXButton("Cancel");
        CANCEL.setAlignment(Pos.CENTER);
        CANCEL.setFocusTraversable(false);
        CANCEL.setFont(new Font(14));
        CANCEL.setPrefHeight(30);
        CANCEL.setPrefWidth(100);
        CANCEL.setStyle("-fx-border-color: #2ed2ff;             -fx-border-radius: 16;" +
                "-fx-text-fill: #464646; ");
        CANCEL.setButtonType(JFXButton.ButtonType.RAISED);
        CANCEL.setRipplerFill(Color.BLACK);
        CANCEL.setTranslateX(bg.getWidth()/2 + 10);
        CANCEL.setTranslateY(bg.getHeight() - 50);
        CANCEL.setCancelButton(true);
        CANCEL.setOnMouseEntered(e -> {
            CANCEL.setStyle("-fx-background-color: #2ed2ff;         -fx-background-radius: 16;" +
                    "-fx-border-color: #2ed2ff;                     -fx-border-radius: 16;" +
                    "-fx-text-fill: WHITE;");
        });
        CANCEL.setOnMouseExited(e -> {
            CANCEL.setStyle("-fx-border-color: #2ed2ff;             -fx-border-radius: 16;" +
                    "-fx-text-fill: #464646; ");
        });
        CANCEL.setOnAction(e -> {
            User_Profile_Controller.cancel = true;
            CloseDialogue();
        });

        pane.getChildren().addAll(bg, dialogue, update, CANCEL);

        setScene(new Scene(pane));
    }

    void OpenDialogue() {
        showAndWait();
    }

    void CloseDialogue() {
        close();
    }
}