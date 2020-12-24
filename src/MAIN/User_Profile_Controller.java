package MAIN;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.xml.transform.Result;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User_Profile_Controller {
    @FXML
    private JFXButton UserProfileChangePicButton;
    @FXML
    private Label CloseLabel;
    @FXML
    private Label MinimizeLabel;
    @FXML
    private Button UserProfileGoBackButton;
    @FXML
    private Label UserProfileUsernameInfoLabel;
    @FXML
    private Label UserProfileNameInfoLabel;
    @FXML
    private Label UserProfileAgeInfoLabel;
    @FXML
    private Label UserProfileEmailInfoLabel;
    @FXML
    private Label UserProfileAddressInfoLabel;
    @FXML
    private Label UserProfileContactNoInfoLabel;

    public String username, name, age, email, address, contact, role;

    public void ShowUserInfo(ResultSet resultSet) {
        name = age = email = address = contact = "UNKNOWN";

        Statement statement = null;

        try {
            statement = Medi_collab.connection().createStatement();
            username = resultSet.getString("USERNAME");
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if(!Medi_collab.admin_logged_in){
            try {
                name = resultSet.getString("FIRSTNAME") + " " + resultSet.getString("LASTNAME");
                email = resultSet.getString("EMAIL");
                role = resultSet.getString("ROLE");
                address = resultSet.getString("ADDRESS");
                contact = resultSet.getString("CONTACT");

                ResultSet temp = null;
                temp = statement.executeQuery("SELECT TRUNC(" +
                        "(SYSDATE - (SELECT DATEOFBIRTH FROM USERS_TABLE " +
                        "WHERE USERNAME = '" + username + "'))/365.25) FROM DUAL");
                if(temp.next())
                    age = temp.getString(1);
            }
            catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        UserProfileUsernameInfoLabel.setText("   " + username);
        UserProfileNameInfoLabel.setText("   " + name);
        UserProfileAgeInfoLabel.setText("   " + age);
        UserProfileEmailInfoLabel.setText("   " + email);
        UserProfileAddressInfoLabel.setText("   " + address);
        UserProfileContactNoInfoLabel.setText("   " + contact);
    }

    public void handleUserProfileChangePicButton(ActionEvent actionEvent) {
    }

    public void handleCloseLabel(MouseEvent mouseEvent) {
        ExitDialogue exitDialogue = new ExitDialogue(mouseEvent);
        exitDialogue.OpenDialogue();
    }

    public void handleMinimizeLabel(MouseEvent mouseEvent) {
        ((Stage) ((Node)mouseEvent.getSource()).getScene().getWindow()).setIconified(true);
    }

    public void handleUserProfileGoBackButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Main_Menu_" + role + ".fxml"));

        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
        scene.getStylesheets().add(Medi_collab.stylesheetaddress);
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.show();
    }
}
