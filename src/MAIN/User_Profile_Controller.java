package MAIN;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;

public class User_Profile_Controller implements Initializable {
    @FXML private JFXDatePicker UserProfileDateofBirthInfoDatePicker;
    @FXML private JFXTextArea UserProfileDescriptionTextArea;
    @FXML private Label UserProfileNotifyLabel;
    @FXML private JFXButton UserProfileUpdateProfileButton;
    @FXML private JFXTextField UserProfileAgeInfoTextField;
    @FXML private JFXTextField UserProfileEmailInfoTextField;
    @FXML private JFXTextField UserProfileAddressInfoTextField;
    @FXML private JFXTextField UserProfileContactNoInfoTextField;
    @FXML private Label UserProfileUsernameInfoLabel;
    @FXML private ImageView UserProfilePictureImageView;
    @FXML private Label UserProfileNameInfoLabel;

    public String username, name, age, email, address, contact, dob, description;
    public static boolean update_profile = false, save = false, cancel = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();

        UserProfilePictureImageView.setLayoutX(screen.getWidth()/2 - 75);
        UserProfileUpdateProfileButton.setLayoutX(screen.getWidth()/2 - 75);
        UserProfileUpdateProfileButton.setVisible(false);
        UserProfileDateofBirthInfoDatePicker.setPrefWidth(screen.getWidth()/2 - UserProfileDateofBirthInfoDatePicker.getLayoutX() - 10);
        UserProfileDateofBirthInfoDatePicker.setPromptText("Date of Birth (DD-MM-YYYY)");
        UserProfileDateofBirthInfoDatePicker.setConverter(Medi_collab.localDateStringConverter);
        UserProfileAgeInfoTextField.setPrefWidth(UserProfileDateofBirthInfoDatePicker.getPrefWidth());
        UserProfileEmailInfoTextField.setPrefWidth(UserProfileDateofBirthInfoDatePicker.getPrefWidth());
        UserProfileContactNoInfoTextField.setPrefWidth(UserProfileDateofBirthInfoDatePicker.getPrefWidth());

        try {
            ShowUserInfo();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void ShowUserInfo() throws SQLException {
        name = age = email = address = contact = dob = "null";
        description = "";

        Statement statement;
        ResultSet resultSet = Medi_collab.User_Info_Resultset;

        statement = Objects.requireNonNull(Medi_collab.connection()).createStatement();
        username = resultSet.getString("USERNAME");
        name = resultSet.getString("FIRSTNAME") + " " + resultSet.getString("LASTNAME");
        email = resultSet.getString("EMAIL");
        address = resultSet.getString("ADDRESS");
        contact = resultSet.getString("CONTACT");
        ResultSet resultSet1 = statement.executeQuery("SELECT TO_CHAR(DATEOFBIRTH, 'DD-MM-YYYY') AS DATEOFBIRTH " +
                "FROM USERS_TABLE WHERE USERNAME = '" + username + "'");
        if(resultSet1.next())
            dob = resultSet1.getString("DATEOFBIRTH");

        resultSet1 = statement.executeQuery("SELECT TRUNC(" +
                "(SYSDATE - (SELECT DATEOFBIRTH FROM USERS_TABLE " +
                "WHERE USERNAME = '" + username + "'))/365.25) FROM DUAL");
        if(resultSet1.next())
            age = resultSet1.getString(1);

        if(resultSet.getString("DESCRIPTION") != null)
            description = resultSet.getString("DESCRIPTION");

        if(dob == null || email == null || contact == null || address == null)
            UserProfileNotifyLabel.setText("Please Complete Your Profile.");

        UserProfileUsernameInfoLabel.setText(username);
        UserProfileNameInfoLabel.setText(name);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(dob, dateTimeFormatter);

        UserProfileDateofBirthInfoDatePicker.setValue(localDate);
        UserProfileAgeInfoTextField.setText(age);
        UserProfileEmailInfoTextField.setText(email);
        UserProfileAddressInfoTextField.setText(address);
        UserProfileContactNoInfoTextField.setText(contact);
        UserProfileDescriptionTextArea.setText(description);
    }

    public void handleCloseLabel(MouseEvent mouseEvent) {
        ExitDialogue exitDialogue = new ExitDialogue(mouseEvent);
        exitDialogue.OpenDialogue();
    }

    public void handleMinimizeLabel(MouseEvent mouseEvent) {
        ((Stage) ((Node)mouseEvent.getSource()).getScene().getWindow()).setIconified(true);
    }

    public void handleGoBackButton(ActionEvent actionEvent) throws IOException, SQLException {
        if (UserProfileUpdateProfileButton.isVisible()){
            SaveChanges saveChanges = new SaveChanges();
            saveChanges.OpenDialogue();

            if(save)
                handleUserProfileUpdateProfileButton();
        }

        if(!cancel) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main_Menu.fxml"));

            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
            scene.getStylesheets().add("/Resources/CSS/Main_Menu.css");
            scene.setFill(Color.TRANSPARENT);

            stage.setScene(scene);
            stage.show();
        }
    }

    public void handleUserProfileAddressInfoTextField() {
        UserProfileUpdateProfileButton.setVisible(true);
    }

    public void handleUserProfileContactNoInfoTextField() {
        UserProfileUpdateProfileButton.setVisible(true);
    }

    public void handleUserProfileDateofBirthInfoDatePicker() {
        UserProfileUpdateProfileButton.setVisible(true);
    }

    public void handleUserProfileDescriptionTextArea() {
        UserProfileUpdateProfileButton.setVisible(true);
    }

    public void handleUserProfileEmailInfoTextField() {
        UserProfileUpdateProfileButton.setVisible(true);
    }

    public void handleUserProfileUpdateProfileButton() throws SQLException {
        ConfirmationDialogue confirmationDialogue = new ConfirmationDialogue();
        confirmationDialogue.OpenDialogue();

        if(update_profile) {
            Statement statement = Objects.requireNonNull(Medi_collab.connection()).createStatement();

            String user = Medi_collab.User_Info_Resultset.getString("USERNAME"),
                    newdob = UserProfileDateofBirthInfoDatePicker.getValue().toString(),
                    newemail = UserProfileEmailInfoTextField.getText().strip(),
                    newaddress = UserProfileAddressInfoTextField.getText().strip(),
                    newcontact = UserProfileContactNoInfoTextField.getText().strip(),
                    newdescription = UserProfileDescriptionTextArea.getText().strip();

            System.out.println(user);

            statement.executeQuery("UPDATE USERS_TABLE " +
                    "SET DATEOFBIRTH = TO_DATE('" + newdob + "', 'DD-MM-YYYY'), " +
                    "EMAIL = '" + newemail + "', " +
                    "ADDRESS = '" + newaddress + "', " +
                    "CONTACT = '" + newcontact + "', " +
                    "DESCRIPTION = '" + newdescription + "'" +
                    "WHERE USERNAME = '" + user + "'");

            Medi_collab.User_Info_Resultset = statement.executeQuery("SELECT * FROM USERS_TABLE " +
                    "WHERE USERNAME = '" + user + "'");

            Medi_collab.User_Info_Resultset.next();

            update_profile = false;

            UserProfileUpdateProfileButton.setVisible(false);
            UserProfileNotifyLabel.setVisible(false);

            ShowUserInfo();
        }
    }
}
