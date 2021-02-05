package MAIN;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SaveChanges extends Stage {
    public SaveChanges() {
        Pane root = new Pane();

        initStyle(StageStyle.UNDECORATED);
        initModality(Modality.APPLICATION_MODAL);

        Rectangle bg = new Rectangle(350, 200, Color.valueOf("#f0ffff"));
        bg.setStrokeWidth(2);
        bg.setStroke(Color.valueOf("#464646"));
        bg.setStrokeType(StrokeType.INSIDE);

        Text header = new Text("Do You Want to Update Profile?");
        header.setFont(Font.font(20));
        header.setFill(Color.valueOf("#464646"));

        VBox dialogue = new VBox(10, header);
        dialogue.setPadding(new Insets(30, 0, 30, 15));

        JFXButton SAVE = new JFXButton("Yes");
        SAVE.setAlignment(Pos.CENTER);
        SAVE.setFocusTraversable(false);
        SAVE.setFont(new Font(14));
        SAVE.setPrefHeight(30);
        SAVE.setPrefWidth(100);
        SAVE.setStyle("-fx-border-color: #2ed2ff;           -fx-border-radius: 16;" +
                "-fx-text-fill: #464646; ");
        SAVE.setButtonType(JFXButton.ButtonType.RAISED);
        SAVE.setRipplerFill(Color.BLACK);
        SAVE.setTranslateX(bg.getWidth()/2 - 160);
        SAVE.setTranslateY(bg.getHeight() - 50);
        SAVE.setDefaultButton(true);
        SAVE.setOnMouseEntered(e -> {
            SAVE.setStyle("-fx-background-color: #2ed2ff;       -fx-background-radius: 16;" +
                    "-fx-border-color: #2ed2ff;                 -fx-border-radius: 16; " +
                    "-fx-text-fill: WHITE;");
        });
        SAVE.setOnMouseExited(e -> {
            SAVE.setStyle("-fx-border-color: #2ed2ff;           -fx-border-radius: 16;" +
                    "-fx-text-fill: #464646; ");
        });
        SAVE.setOnAction(e -> {
            User_Profile_Controller.save = true;
            User_Profile_Controller.cancel = false;
            CloseDialogue();
        });

        JFXButton DONT_SAVE = new JFXButton("No");
        DONT_SAVE.setAlignment(Pos.CENTER);
        DONT_SAVE.setFocusTraversable(false);
        DONT_SAVE.setPrefHeight(30);
        DONT_SAVE.setPrefWidth(100);
        DONT_SAVE.setFont(new Font(14));
        DONT_SAVE.setStyle("-fx-border-color: #2ed2ff;          -fx-border-radius: 16;" +
                "-fx-text-fill: #464646; ");
        DONT_SAVE.setButtonType(JFXButton.ButtonType.RAISED);
        DONT_SAVE.setRipplerFill(Color.BLACK);
        DONT_SAVE.setTranslateX(bg.getWidth()/2 - 50);
        DONT_SAVE.setTranslateY(bg.getHeight() - 50);
        DONT_SAVE.setOnMouseEntered(e -> {
            DONT_SAVE.setStyle("-fx-background-color: #2ed2ff;          -fx-background-radius: 16;" +
                    "-fx-border-color: #2ed2ff;                         -fx-border-radius: 16;" +
                    "-fx-text-fill: WHITE;");
        });
        DONT_SAVE.setOnMouseExited(e -> {
            DONT_SAVE.setStyle("-fx-border-color: #2ed2ff;          -fx-border-radius: 16;" +
                    "-fx-text-fill: #464646; ");
        });
        DONT_SAVE.setOnAction(e -> {
            User_Profile_Controller.save = false;
            User_Profile_Controller.cancel = false;
            CloseDialogue();
        });

        JFXButton CANCEL = new JFXButton("Cancel");
        CANCEL.setAlignment(Pos.CENTER);
        CANCEL.setFont(new Font(14));
        CANCEL.setFocusTraversable(false);
        CANCEL.setPrefHeight(30);
        CANCEL.setPrefWidth(100);
        CANCEL.setStyle("-fx-border-color: #2ed2ff;             -fx-border-radius: 16;" +
                "-fx-text-fill: #464646; ");
        CANCEL.setButtonType(JFXButton.ButtonType.RAISED);
        CANCEL.setRipplerFill(Color.BLACK);
        CANCEL.setTranslateX(bg.getWidth()/2 + 60);
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

        root.getChildren().addAll(bg, dialogue, SAVE, DONT_SAVE, CANCEL);

        setScene(new Scene(root));
    }

    void OpenDialogue() {
        showAndWait();
    }

    void CloseDialogue() {
        close();
    }
}
