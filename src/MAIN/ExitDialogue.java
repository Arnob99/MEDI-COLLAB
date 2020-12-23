package MAIN;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.awt.*;
import java.awt.event.MouseEvent;

import static javafx.scene.text.Font.font;

public class ExitDialogue extends Stage {
    private Stage oldstage;

    public ExitDialogue(javafx.scene.input.MouseEvent mouseEvent) {
        oldstage = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();

        Pane root = new Pane();

        initStyle(StageStyle.UNDECORATED);
        initModality(Modality.APPLICATION_MODAL);

        Rectangle bg = new Rectangle(350, 200, Color.valueOf("#f0ffff"));
        bg.setStrokeWidth(4);
        bg.setStroke(Color.valueOf("#464646"));

        Text header = new Text("QUIT?");
        header.setFont(Font.font(24));
        header.setFill(Color.valueOf("#464646"));

        Text content = new Text("Are you sure you want to exit the application?");
        content.setFont(Font.font(14));
        content.setFill(Color.valueOf("#464646"));

        VBox dialogue = new VBox(10, header, new Separator(Orientation.HORIZONTAL), content);
        dialogue.setPadding(new Insets(15));

        JFXButton OK = new JFXButton("OK");
        OK.setStyle("-fx-alignment: CENTER;             -fx-background-color: #2ed2ff; " +
                    "-fx-background-radius: 16 0 0 16;  -fx-border-radius: 16 0 0 16; " +
                    "-fx-font-size: 14;                 -fx-min-height: 32; " +
                    "-fx-min-width: 100;                -fx-text-fill: WHITE;");
        OK.setButtonType(JFXButton.ButtonType.RAISED);
        OK.setFocusTraversable(false);
        OK.setRipplerFill(Color.BLACK);
        OK.setTranslateX(bg.getWidth()/2 - 101);
        OK.setTranslateY(bg.getHeight() - 50);
        OK.setDefaultButton(true);
        OK.setOnAction(e -> {
            CloseDialogue();
            oldstage.close();
        });

        JFXButton CANCEL = new JFXButton("Cancel");
        CANCEL.setStyle("-fx-alignment: CENTER;             -fx-background-color: #2ed2ff; " +
                "-fx-background-radius: 0 16 16 0;  -fx-border-radius: 0 16 16 0; " +
                "-fx-font-size: 14;                 -fx-min-height: 32; " +
                "-fx-min-width: 100;                -fx-text-fill: WHITE;");
        CANCEL.setButtonType(JFXButton.ButtonType.RAISED);
        CANCEL.setFocusTraversable(false);
        CANCEL.setRipplerFill(Color.BLACK);
        CANCEL.setTranslateX(bg.getWidth()/2 + 1);
        CANCEL.setTranslateY(bg.getHeight() - 50);
        CANCEL.setDefaultButton(true);
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
