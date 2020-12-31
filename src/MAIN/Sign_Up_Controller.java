package MAIN;

import com.jfoenix.controls.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

public class Sign_Up_Controller {
    @FXML
    private JFXTextField SignUpDateofBirth;
    @FXML
    private JFXTextField SignUpAddress;
    @FXML
    private JFXTextField SignUpContactNo;
    @FXML
    private JFXTextField SignUpUsername;
    @FXML
    private JFXPasswordField SignUpConfirmPassword;
    @FXML
    private JFXRadioButton SignUpDoctorRadioButton;
    @FXML
    private JFXTextField SignUpEmailAddress;
    @FXML
    private JFXTextField SignUpFirstName;
    @FXML
    private JFXTextField SignUpLastName;
    @FXML
    private JFXRadioButton SignUpMedicalStaffRadioButton;
    @FXML
    private Label SignUpNotifyLabel;
    @FXML
    private JFXPasswordField SignUpPassword;
    @FXML
    private JFXRadioButton SignUpPatientRadioButton;

    public String username, contact, address, conpass, dob, email, fname, lname, pass, role;
    public boolean signed_up;

    public void handleCloseLabelMouseClicked(MouseEvent mouseEvent) {
        ExitDialogue exitDialogue = new ExitDialogue(mouseEvent);
        exitDialogue.OpenDialogue();
    }

    public void handleMinimizeLabelClicked(MouseEvent mouseEvent) {
        ((Stage)((Node)mouseEvent.getSource()).getScene().getWindow()).setIconified(true);
    }

    public void handleSignUpCancelButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Sign_In.fxml"));

        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
        scene.getStylesheets().add(getClass().getResource(Medi_collab.stylesheetaddress).toExternalForm());
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.show();
    }

    public void handleSignUpSignUpButton(ActionEvent actionEvent) throws IOException {
        boolean alright = true;
        signed_up = false;

        Statement statement = null;
        try {
            statement = Medi_collab.connection().createStatement();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        SignUpNotifyLabel.setText("");

        username = SignUpUsername.getText();
        try {
            ResultSet resultSet = statement.executeQuery("select * from USERS_TABLE where USERNAME = '" + username + "'");
            if(resultSet.next()){
                SignUpNotifyLabel.setText("Sorry, this username is already taken!");
                alright = false;
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        pass = SignUpPassword.getText();
        if(pass.length() == 0 && alright) {
            SignUpNotifyLabel.setText("You must fill up all the fields.");
            alright = false;
        }

        conpass = SignUpConfirmPassword.getText();
        if(conpass.length() == 0 && alright) {
            SignUpNotifyLabel.setText("You must fill up all the fields.");
            alright = false;
        }

        if(!pass.equals(conpass)){
            SignUpNotifyLabel.setText("Password and Confirm Password must be same!");
            alright = false;
        }

        fname = SignUpFirstName.getText();
        if(fname.length() == 0 && alright) {
            SignUpNotifyLabel.setText("You must fill up all the fields.");
            alright = false;
        }

        lname = SignUpLastName.getText();
        if(lname.length() == 0 && alright) {
            SignUpNotifyLabel.setText("You must fill up all the fields.");
            alright = false;
        }

        email = SignUpEmailAddress.getText();
        if(email.length() == 0 && alright) {
            SignUpNotifyLabel.setText("You must fill up all the fields.");
            alright = false;
        }

        dob = SignUpDateofBirth.getText();
        if(dob.length() == 0 && alright){
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

        address = SignUpAddress.getText();
        if(address.length() == 0 && alright){
            SignUpNotifyLabel.setText("You must fill up all the fields.");
            alright = false;
        }

        contact = SignUpContactNo.getText();
        if(contact.length() == 0 && alright){
            SignUpNotifyLabel.setText("You must fill up all the fields.");
            alright = false;
        }

        if(alright){
            try {
                statement.executeQuery("INSERT INTO USERS_TABLE VALUES (" +
                        "'" + username + "', " +
                        "'" + pass +"', " +
                        "'" + fname + "', " +
                        "'" + lname + "', " +
                        "TO_DATE('" + dob +"', 'dd-mm-yyyy'), " +
                        "'" + email + "', " +
                        "'" + role + "', " +
                        "'" + address + "', " +
                        "'" + contact + "')");
                signed_up = true;
                SignUpNotifyLabel.setText("Signed up successfully. Login with your username and password.");
            }
            catch (SQLException throwables) {
                throwables.printStackTrace();
                SignUpNotifyLabel.setText(throwables.getMessage());
            }
        }

        if(signed_up) {
            Parent root = FXMLLoader.load(getClass().getResource("Sign_In.fxml"));

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
            scene.getStylesheets().add(getClass().getResource(Medi_collab.stylesheetaddress).toExternalForm());
            scene.setFill(Color.TRANSPARENT);

            stage.setScene(scene);
            stage.show();
        }
    }
}
