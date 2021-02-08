package MAIN;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class Sign_Up_Controller {
    @FXML private JFXTextField SignUpUsername;
    @FXML private JFXPasswordField SignUpConfirmPassword;
    @FXML private JFXRadioButton SignUpDoctorRadioButton;
    @FXML private JFXTextField SignUpFirstName;
    @FXML private JFXTextField SignUpLastName;
    @FXML private JFXRadioButton SignUpMedicalStaffRadioButton;
    @FXML private Label SignUpNotifyLabel;
    @FXML private JFXPasswordField SignUpPassword;
    @FXML private JFXRadioButton SignUpPatientRadioButton;

    public String username, contact, address, confirmPass, dob, email, firstName, lastName, pass, role, description;
    public boolean signed_up;

    public void handleCloseLabelMouseClicked(MouseEvent mouseEvent) {
        ExitDialogue exitDialogue = new ExitDialogue(mouseEvent);
        exitDialogue.OpenDialogue();
    }

    public void handleMinimizeLabelClicked(MouseEvent mouseEvent) {
        ((Stage)((Node)mouseEvent.getSource()).getScene().getWindow()).setIconified(true);
    }

    public void handleCancelButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Sign_In.fxml"));

        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
        scene.getStylesheets().add(getClass().getResource("/Resources/CSS/Sign_In.css").toExternalForm());
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.show();
    }

    public void handleSignUpSignUpButton(ActionEvent actionEvent) throws IOException, SQLException {
        boolean alright = true;
        signed_up = false;
        username = firstName = lastName = pass = confirmPass = dob = address = contact = email = description = "";

        Statement statement = Objects.requireNonNull(Medi_collab.connection()).createStatement();

        SignUpNotifyLabel.setText("");

        username = SignUpUsername.getText().strip();

        ResultSet resultSet = statement.executeQuery("select * from USERS_TABLE where USERNAME = '" + username + "'");
        if(resultSet.next()){
            SignUpNotifyLabel.setText("Sorry, this username is already taken!");
            alright = false;
        }

        pass = SignUpPassword.getText();
        if(pass.length() == 0 && alright) {
            SignUpNotifyLabel.setText("You must fill up all the fields.");
            alright = false;
        }

        confirmPass = SignUpConfirmPassword.getText();
        if(confirmPass.length() == 0 && alright) {
            SignUpNotifyLabel.setText("You must fill up all the fields.");
            alright = false;
        }

        if(!pass.equals(confirmPass)){
            SignUpNotifyLabel.setText("Password and Confirm Password must be same!");
            alright = false;
        }

        firstName = SignUpFirstName.getText().strip();
        if(firstName.length() == 0 && alright) {
            SignUpNotifyLabel.setText("You must fill up all the fields.");
            alright = false;
        }

        lastName = SignUpLastName.getText().strip();
        if(lastName.length() == 0 && alright) {
            SignUpNotifyLabel.setText("You must fill up all the fields.");
            alright = false;
        }

        if(SignUpPatientRadioButton.isSelected())
            role = SignUpPatientRadioButton.getText();
        else if(SignUpDoctorRadioButton.isSelected())
            role = SignUpDoctorRadioButton.getText();
        else if(SignUpMedicalStaffRadioButton.isSelected())
            role = "Staff";
        else if(alright) {
            SignUpNotifyLabel.setText("You must fill up all the fields.");
            alright = false;
        }

        if(alright){
            PreparedStatement preparedStatement = Objects.requireNonNull(Medi_collab.connection()).prepareStatement("INSERT INTO USERS_TABLE " +
                    "VALUES (?, ?, ?, ?, null, ?, ?, ?, ?, ?)");

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, pass);
            preparedStatement.setString(3, firstName);
            preparedStatement.setString(4, lastName);
            preparedStatement.setString(5, email);
            preparedStatement.setString(6, role);
            preparedStatement.setString(7, address);
            preparedStatement.setString(8, contact);
            preparedStatement.setString(9, description);

            preparedStatement.executeQuery();

            signed_up = true;
            SignUpNotifyLabel.setText("Signed up successfully. Login with your username and password.");
        }

        if(signed_up) {
            Parent root = FXMLLoader.load(getClass().getResource("Sign_In.fxml"));

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
            scene.getStylesheets().add(getClass().getResource("/Resources/CSS/Sign_In.css").toExternalForm());
            scene.setFill(Color.TRANSPARENT);

            stage.setScene(scene);
            stage.show();
        }
    }
}
