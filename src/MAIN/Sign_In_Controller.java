package MAIN;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

    public void init(){

    }

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

    public void handleSignInLoginButton(ActionEvent actionEvent) throws IOException, SQLException {
        signinusername = SignInUsernameTextField.getText();
        signinpassword = SignInPasswordTextField.getText();

        boolean alright = false;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = Medi_collab.connection().createStatement();
            resultSet = statement.executeQuery("SELECT * FROM USERS_TABLE " +
                    "WHERE USERNAME = '" + signinusername + "' " +
                    "AND PASSWORD = '" + signinpassword + "'");

            if(resultSet.next()){
                alright = true;
                Medi_collab.role = resultSet.getString("ROLE");
            }
            else
                SignInNotifyLabel.setText("username or password is incorrect");
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
            SignInNotifyLabel.setText(throwable.getMessage());
        }

        if(alright){
            Medi_collab.User_Info_Resultset = resultSet;

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main_Menu.fxml"));

            Parent root = fxmlLoader.load();

            ((Stage) ((Node)actionEvent.getSource()).getScene().getWindow()).close();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource(Medi_collab.stylesheetaddress).toExternalForm());
            scene.setFill(Color.TRANSPARENT);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();

            Main_Menu_Controller main_menu_controller = fxmlLoader.getController();
            main_menu_controller.init();
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
