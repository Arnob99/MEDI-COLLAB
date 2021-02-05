package MAIN;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Separator;
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

public class ExitDialogue extends Stage {
    private final Stage oldstage;

    public ExitDialogue(javafx.scene.input.MouseEvent mouseEvent) {
        oldstage = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();

        Pane root = new Pane();

        initStyle(StageStyle.UNDECORATED);
        initModality(Modality.APPLICATION_MODAL);

        Rectangle bg = new Rectangle(350, 200, Color.valueOf("#f0ffff"));
        bg.setStrokeWidth(2);
        bg.setStroke(Color.valueOf("#464646"));
        bg.setStrokeType(StrokeType.INSIDE);

        Text header = new Text("QUIT?");
        header.setFont(Font.font(24));
        header.setFill(Color.valueOf("#464646"));

        Text content = new Text("Are you sure you want to exit the application?");
        content.setFont(Font.font(14));
        content.setFill(Color.valueOf("#464646"));

        VBox dialogue = new VBox(10, header, new Separator(Orientation.HORIZONTAL), content);
        dialogue.setPadding(new Insets(15));

        JFXButton OK = new JFXButton("OK");
        OK.setFocusTraversable(false);
        OK.setFont(new Font(14));
        OK.setAlignment(Pos.CENTER);
        OK.setPrefHeight(30);
        OK.setPrefWidth(100);
        OK.setStyle("-fx-border-color: #2ed2ff; " +
                "-fx-border-radius: 16;                 -fx-text-fill: #464646; ");
        OK.setButtonType(JFXButton.ButtonType.RAISED);
        OK.setRipplerFill(Color.BLACK);
        OK.setTranslateX(bg.getWidth()/2 - 110);
        OK.setTranslateY(bg.getHeight() - 50);
        OK.setDefaultButton(true);
        OK.setOnMouseEntered(e -> {
            OK.setStyle("-fx-background-color: #2ed2ff;         -fx-background-radius: 16;" +
                    "-fx-border-color: #2ed2ff;                 -fx-border-radius: 16; " +
                    "-fx-text-fill: WHITE");
        });
        OK.setOnMouseExited(e -> {
            OK.setStyle("-fx-border-color: #2ed2ff;" +
                    "-fx-border-radius: 16;                     -fx-text-fill: #464646");
        });
        OK.setOnAction(e -> {
            CloseDialogue();
            oldstage.close();
        });

        JFXButton CANCEL = new JFXButton("Cancel");
        CANCEL.setFocusTraversable(false);
        CANCEL.setFont(new Font(14));
        CANCEL.setPrefHeight(30);
        CANCEL.setPrefWidth(100);
        CANCEL.setAlignment(Pos.CENTER);
        CANCEL.setStyle("-fx-border-color: #2ed2ff; " +
                "-fx-border-radius: 16;                     -fx-text-fill: #464646;");
        CANCEL.setButtonType(JFXButton.ButtonType.RAISED);
        CANCEL.setRipplerFill(Color.BLACK);
        CANCEL.setTranslateX(bg.getWidth()/2 + 10);
        CANCEL.setTranslateY(bg.getHeight() - 50);
        CANCEL.setCancelButton(true);
        CANCEL.setOnMouseEntered(e -> {
            CANCEL.setStyle("-fx-background-color: #2ed2ff;         -fx-background-radius: 16;" +
                    "-fx-border-color: #2ed2ff;                     -fx-border-radius: 16;" +
                    "-fx-text-fill: WHITE");
        });
        CANCEL.setOnMouseExited(e -> {
            CANCEL.setStyle("-fx-border-color: #2ed2ff; " +
                    "-fx-border-radius: 16;                         -fx-text-fill: #464646");
        });
        CANCEL.setOnAction(e -> CloseDialogue());

        root.getChildren().addAll(bg, dialogue, OK, CANCEL);

        setScene(new Scene(root));
    }

    void OpenDialogue() {
        show();
    }

    void CloseDialogue() {
        close();
    }
}
