package MAIN;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;

public class User_Profile_Controller implements Initializable {
    @FXML private JFXButton UserProfileChangePicButton;
    @FXML private Label UserProfileUsernameInfoLabel;
    @FXML private ImageView UserProfilePictureImageView;
    @FXML private Label UserProfileAddressInfoLabel;
    @FXML private Label UserProfileAgeInfoLabel;
    @FXML private Label UserProfileContactNoInfoLabel;
    @FXML private Label UserProfileEmailInfoLabel;
    @FXML private Label UserProfileNameInfoLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();

        UserProfilePictureImageView.setLayoutX(screen.getWidth()/2 - 75);
        UserProfileChangePicButton.setLayoutX(screen.getWidth()/2 - 75);
        UserProfileChangePicButton.setVisible(false);

        ShowUserInfo();
    }

    public String username, name, age, email, address, contact;

    public void ShowUserInfo() {
        name = age = email = address = contact = "UNKNOWN";

        Statement statement;
        ResultSet resultSet = Medi_collab.User_Info_Resultset;

        try {
            statement = Objects.requireNonNull(Medi_collab.connection()).createStatement();
            username = resultSet.getString("USERNAME");
            name = resultSet.getString("FIRSTNAME") + " " + resultSet.getString("LASTNAME");
            email = resultSet.getString("EMAIL");
            address = resultSet.getString("ADDRESS");
            contact = resultSet.getString("CONTACT");

            ResultSet temp = statement.executeQuery("SELECT TRUNC(" +
                    "(SYSDATE - (SELECT DATEOFBIRTH FROM USERS_TABLE " +
                    "WHERE USERNAME = '" + username + "'))/365.25) FROM DUAL");
            if(temp.next())
                age = temp.getString(1);
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        UserProfileUsernameInfoLabel.setText(username);
        UserProfileNameInfoLabel.setText(name);
        UserProfileAgeInfoLabel.setText("   " + age);
        UserProfileEmailInfoLabel.setText("   " + email);
        UserProfileAddressInfoLabel.setText("   " + address);
        UserProfileContactNoInfoLabel.setText("   " + contact);
    }

    public void handleUserProfileChangePicButton() {
    }

    public void handleCloseLabel(MouseEvent mouseEvent) {
        ExitDialogue exitDialogue = new ExitDialogue(mouseEvent);
        exitDialogue.OpenDialogue();
    }

    public void handleMinimizeLabel(MouseEvent mouseEvent) {
        ((Stage) ((Node)mouseEvent.getSource()).getScene().getWindow()).setIconified(true);
    }

    public void handleGoBackButton(ActionEvent actionEvent) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main_Menu.fxml"));

        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
        scene.getStylesheets().add(Medi_collab.stylesheetaddress);
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.show();

        Main_Menu_Controller main_menu_controller = fxmlLoader.getController();
        main_menu_controller.init();
    }

//    public void handleRefreshButton() throws SQLException {
//        Statement statement = Objects.requireNonNull(Medi_collab.connection()).createStatement();
//
//        Medi_collab.User_Info_Resultset = statement.executeQuery("SELECT * FROM USERS_TABLE WHERE USERNAME = '" + username + "'");
//
//        ShowUserInfo();
//    }
}
