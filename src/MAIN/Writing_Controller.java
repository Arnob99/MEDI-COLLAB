package MAIN;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Writing_Controller {

    @FXML
    private Label WritingNotifyLabel;
    @FXML
    private JFXTextField WritingToTextField;
    @FXML
    private Label WritingChooseFileLabel;

    public File file = null;

    public void handleCloseLabel(MouseEvent mouseEvent) {
        ExitDialogue exitDialogue = new ExitDialogue(mouseEvent);
        exitDialogue.OpenDialogue();
    }

    public void handleCancelButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Main_Menu_" + Medi_collab.role + ".fxml"));

        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
        scene.getStylesheets().add(Medi_collab.stylesheetaddress);
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.show();
    }

    public void handleMinimizeLabel(MouseEvent mouseEvent) {
        ((Stage) ((Node)mouseEvent.getSource()).getScene().getWindow()).setIconified(true);
    }

    public void handleWritingSendButton(ActionEvent actionEvent) {
        String sender = null;
        try {
            sender = Medi_collab.User_Info_Resultset.getString("USERNAME");
            String receiver = WritingToTextField.getText();
            PreparedStatement preparedStatement = Medi_collab.connection().prepareStatement("INSERT INTO SHARED_FILES " +
                    "VALUES (?, ?, ?, SYSDATE)");

            FileInputStream fileInputStream = new FileInputStream(file);

            preparedStatement.setString(1, sender);
            preparedStatement.setString(2, receiver);
            preparedStatement.setBlob(3, fileInputStream);

            Statement statement = Medi_collab.connection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT USERNAME FROM USERS_TABLE " +
                    "WHERE USERNAME = '" + receiver + "'");

            if(resultSet.next()) {
                preparedStatement.execute();
                WritingNotifyLabel.setText("SENT!");
                WritingNotifyLabel.setTextFill(Color.valueOf("#464646"));
            }
            else{
                WritingNotifyLabel.setText("Invalid Receiver Username!");
                WritingNotifyLabel.setTextFill(Color.RED);
            }

        }
        catch (SQLException | FileNotFoundException throwables) {
            throwables.printStackTrace();

            WritingNotifyLabel.setText("NOT SENT!");
            WritingNotifyLabel.setTextFill(Color.RED);
        }
    }

    public void handleWritingChooseFileButton(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(null);

        if(file != null){
            WritingChooseFileLabel.setText(file.getName());
        }
    }
}
