package MAIN;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
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

public class ForwardDialogue extends Stage {
    String username = null;
    public ForwardDialogue() {
        Pane pane = new Pane();

        initStyle(StageStyle.UNDECORATED);
        initModality(Modality.APPLICATION_MODAL);

        Rectangle bg = new Rectangle(350, 200, Color.valueOf("#f0ffff"));
        bg.setStrokeWidth(2);
        bg.setStroke(Color.valueOf("#464646"));
        bg.setStrokeType(StrokeType.INSIDE);

        Label header = new Label("Forward Test Result");
        header.setFont(new Font(24));
        header.setPrefWidth(320);
        header.setTextFill(Color.valueOf("#464646"));

        JFXTextField usernamefield = new JFXTextField();
        usernamefield.setPromptText("Forward To");
        usernamefield.setStyle("-fx-text-fill: #464646;      -fx-prompt-text-fill: #2ed2ff;" +
                "-fx-font-size: 14;                     -fx-focus-traversable: true;" +
                "-fx-pref-width: 150");
        usernamefield.setFocusColor(Color.valueOf("#2ed2ff"));
        usernamefield.setUnFocusColor(Color.valueOf("30d6ff80"));

        VBox dialogue = new VBox(10, header, new Separator(Orientation.HORIZONTAL), usernamefield);
        dialogue.setPadding(new Insets(15));

        JFXButton send = new JFXButton("Send");
        send.setAlignment(Pos.CENTER);
        send.setFont(new Font(14));
        send.setFocusTraversable(false);
        send.setPrefWidth(100);
        send.setStyle("-fx-border-color: #2ed2ff; -fx-border-radius: 16; -fx-text-fill: #464646;");
        send.setButtonType(JFXButton.ButtonType.RAISED);
        send.setRipplerFill(Color.BLACK);
        send.setTranslateX(bg.getWidth()/2.0 - 110);
        send.setTranslateY(bg.getHeight() - 50);
        send.setDefaultButton(true);
        send.setOnMouseEntered(mouseEvent -> {
            send.setStyle("-fx-background-color: #2ed2ff;       -fx-background-radius: 16;" +
                    "-fx-border-color: #2ed2ff;                 -fx-border-radius: 16;" +
                    "-fx-text-fill: WHITE");
        });
        send.setOnMouseExited(mouseEvent -> {
            send.setStyle("-fx-border-color: #2ed2ff; -fx-border-radius: 16; -fx-text-fill: #464646");
        });
        send.setOnAction(actionEvent -> {
            if (usernamefield.getText() != null) {
                Test_Result_Controller.ForwardUsername = usernamefield.getText();
                CloseDialogue();
            }
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
            CloseDialogue();
        });

        pane.getChildren().addAll(bg, dialogue, send, CANCEL);

        setScene(new Scene(pane));
    }

    void OpenDialogue() {
        showAndWait();
    }

    void CloseDialogue() {
        close();
    }
}
