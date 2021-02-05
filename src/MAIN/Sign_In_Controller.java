package MAIN;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Random;
import java.util.ResourceBundle;

public class Sign_In_Controller implements Initializable {
    @FXML private Label SignInQuoteTextField;
    @FXML private Label SignInNotifyLabel;
    @FXML private JFXPasswordField SignInPasswordTextField;
    @FXML private JFXTextField SignInUsernameTextField;

    public String signinusername, signinpassword;
    public String[] quote = {
            "There is Nothing More Important than Our\n" +
                    "Good Health. That's Our Principal Asset.\n" +
                    "\n- Arlen Specter",

            "It is Health that is Real Wealth and \n" +
                    "not Pieces of Gold and Silver.\n" +
                    "\n- Mahatma Gandhi",

            "When Diet is Wrong, Medicine is of No Use.\n" +
                    "When Diet is Correct, Medicine is of No Need.",

            "Health is Like Money. We Never have a True\n" +
                    "Idea of Its Value Until We Lose It.\n" +
                    "\n- Josh Billings"
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int random = new Random().nextInt(quote.length);
        SignInQuoteTextField.setText(quote[random]);
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
            SignInNotifyLabel.setText("Logging In");
            SignInNotifyLabel.setTextFill(Color.WHITE);
            Medi_collab.User_Info_Resultset = resultSet;

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main_Menu.fxml"));

            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/Resources/CSS/Main_Menu.css").toExternalForm());
            scene.setFill(Color.TRANSPARENT);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();

            ((Stage) ((Node)actionEvent.getSource()).getScene().getWindow()).close();
        }
    }

    public void handleSignInSignUpButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Sign_Up.fxml"));

        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
        scene.getStylesheets().add(getClass().getResource("/Resources/CSS/Sign_Up.css").toExternalForm());
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.show();
    }
}
