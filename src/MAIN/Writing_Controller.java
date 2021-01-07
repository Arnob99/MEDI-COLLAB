package MAIN;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
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
    private AnchorPane WritingCredentialsPane;
    @FXML
    private AnchorPane WritingWriteHerePane;
    @FXML
    private AnchorPane WritingAnchorPane;
    @FXML
    private JFXButton CancelButton;
    @FXML
    private JFXButton WritingSendButton;
    @FXML
    private Label WritingWritingLabel;
    @FXML
    private JFXTextArea WritingWriteHereTextArea;
    @FXML
    private JFXTextArea WritingDescriptionTextArea;
    @FXML
    private JFXTextField WritingSubjectTextField;
    @FXML
    private Label WritingNotifyLabel;
    @FXML
    private JFXTextField WritingToTextField;
    @FXML
    private Label WritingChooseFileLabel;

    public File file = null;

    public void init(){
        if(Medi_collab.role.equals("Doctor"))
            WritingWritingLabel.setText("Prescription");
        else if(Medi_collab.role.equals("Staff"))
            WritingWritingLabel.setText("Test Result");
        else
            System.out.println("Serious problem");

        Rectangle2D screen = Screen.getPrimary().getVisualBounds();

        WritingSendButton.setLayoutX(screen.getWidth()/2 - 121);
        CancelButton.setLayoutX(screen.getWidth()/2 + 1);
    }

    public void handleCloseLabel(MouseEvent mouseEvent) {
        ExitDialogue exitDialogue = new ExitDialogue(mouseEvent);
        exitDialogue.OpenDialogue();
    }

    public void handleCancelButton(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void handleMinimizeLabel(MouseEvent mouseEvent) {
        ((Stage) ((Node)mouseEvent.getSource()).getScene().getWindow()).setIconified(true);
    }

    public void handleWritingSendButton(ActionEvent actionEvent) {
        String sender = null;
        boolean alright = true;
        try {
            sender = Medi_collab.User_Info_Resultset.getString("USERNAME");
            String receiver = WritingToTextField.getText();

            PreparedStatement preparedStatement = Medi_collab.connection().prepareStatement("INSERT INTO SHARED_FILES " +
                    "VALUES (?, ?, ?, ?, ?, ?, SYSDATE)");

            FileInputStream fileInputStream = null;

            preparedStatement.setString(1, sender);
            preparedStatement.setString(2, receiver);
            preparedStatement.setString(3, WritingSubjectTextField.getText());

            File temp = null;
            FileWriter fileWriter = null;

            if(WritingDescriptionTextArea.getText().strip().length() != 0){
                temp = new File("temp_description.txt");

                fileWriter = new FileWriter(temp);
                fileWriter.write(WritingDescriptionTextArea.getText().strip());
                fileWriter.close();

                fileInputStream = new FileInputStream(temp);
                preparedStatement.setBlob(4, fileInputStream);
                fileInputStream.close();

                temp.delete();
            }
            else
                preparedStatement.setBlob(4, InputStream.nullInputStream());

            temp = new File("temp_writing.txt");

            fileWriter = new FileWriter(temp);
            fileWriter.write(WritingWriteHereTextArea.getText().strip());
            fileWriter.close();

            fileInputStream = new FileInputStream(temp);
            preparedStatement.setBlob(5, fileInputStream);
            fileInputStream.close();

            temp.delete();

            if(file != null) {
                fileInputStream = new FileInputStream(file);
                preparedStatement.setBlob(6, fileInputStream);
                fileInputStream.close();

                file.delete();
            }
            else
                preparedStatement.setBlob(6, InputStream.nullInputStream());

            Statement statement = Medi_collab.connection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT USERNAME FROM USERS_TABLE " +
                    "WHERE USERNAME = '" + receiver + "' AND ROLE = 'Patient'");

            if(!resultSet.next()) {
                alright = false;
                WritingNotifyLabel.setText("Invalid Receiver Username!");
                WritingNotifyLabel.setTextFill(Color.RED);
            }

            if(WritingWriteHereTextArea.getText().strip().length() == 0){
                alright = false;
                WritingNotifyLabel.setText("Main Writing Field Cannot Be Empty!");
                WritingNotifyLabel.setTextFill(Color.RED);
            }

            if(alright) {
                preparedStatement.execute();
                ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
            }
        }
        catch (SQLException | IOException throwables) {
            throwables.printStackTrace();

            WritingNotifyLabel.setText("NOT SENT!");
            WritingNotifyLabel.setTextFill(Color.RED);
        }
    }

    public void handleWritingChooseFileButton() {
        FileChooser fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(null);

        if(file != null){
            WritingChooseFileLabel.setText(file.getName());
        }
    }
}
