package MAIN;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class Sign_In_Controller {
    public String signinusername, signinpassword;

    @FXML
    private Label CloseLabel;
    @FXML
    private Label MinimizeLabel;
    @FXML
    private Hyperlink SignInForgotPasswordHyperlink;
    @FXML
    private Label SignInNotifyLabel;
    @FXML
    private JFXPasswordField SignInPasswordTextField;
    @FXML
    private JFXTextField SignInUsernameTextField;
    @FXML
    private JFXButton SignInLoginButton;
    @FXML
    private JFXButton SignInSignUpButton;
    @FXML
    private AnchorPane SignInSignInPane;

    public void handleCloseLabelMouseClicked(MouseEvent mouseEvent) {
        ExitDialogue exitDialogue = new ExitDialogue(mouseEvent);
        exitDialogue.OpenDialogue();
    }

    public void handleMinimizeLabelClicked(MouseEvent mouseEvent) {
        ((Stage)((Node)mouseEvent.getSource()).getScene().getWindow()).setIconified(true);
    }

    public void handleSignInForgotPasswordHyperlinkOnAction(ActionEvent actionEvent) {
        signinusername = SignInUsernameTextField.getText();
        if(signinusername.length() == 0)
            SignInNotifyLabel.setText("Please enter username first.");
    }

    public void handleSignInLoginButton(ActionEvent actionEvent) throws IOException {
        signinusername = SignInUsernameTextField.getText();
        signinpassword = SignInPasswordTextField.getText();

        Medi_collab.admin_logged_in = false;

        boolean alright = false;
        Statement statement = null;
        ResultSet resultSet = null;
        String role = "";

        try {
            statement = Medi_collab.connection().createStatement();

            resultSet = statement.executeQuery("SELECT * " +
                    "FROM ADMIN_TABLE " +
                    "WHERE USERNAME = '" + signinusername + "'");

            if(resultSet.next()) {
                if (resultSet.getString("PASSWORD").equals(signinpassword)) {
                    Medi_collab.admin_logged_in = true;
                    alright = true;
                }
                else
                    SignInNotifyLabel.setText("username or password is incorrect");
            }
            else{
                resultSet = statement.executeQuery("SELECT * " +
                        "FROM USERS_TABLE " +
                        "WHERE USERNAME = '" + signinusername + "'");

                if(resultSet.next()){
                    if(resultSet.getString("PASSWORD").equals(signinpassword)) {
                        alright = true;
                        role = resultSet.getString("ROLE");
                    }
                    else
                        SignInNotifyLabel.setText("username or password is incorrect");
                }
                else
                    SignInNotifyLabel.setText("username or password is incorrect");
            }
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }

        if(alright){
            Medi_collab.User_Info_Resultset = resultSet;

            Parent root = FXMLLoader.load(getClass().getResource("Main_Menu_" + role + ".fxml"));

            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

            Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
            scene.getStylesheets().add(getClass().getResource(Medi_collab.stylesheetaddress).toExternalForm());
            scene.setFill(Color.TRANSPARENT);

            stage.setScene(scene);
            stage.show();
        }
    }

    public void handleSignInSignUpButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Sign_Up.fxml"));

        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
        scene.getStylesheets().add(getClass().getResource(Medi_collab.stylesheetaddress).toExternalForm());
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.show();
    }
}
