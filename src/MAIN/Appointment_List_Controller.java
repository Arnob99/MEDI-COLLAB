package MAIN;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

public class Appointment_List_Controller implements Initializable {

    @FXML private Label NotifyLabel;
    @FXML private JFXButton SetAppointmentButton;
    @FXML private Label DoctorIntendedLabel;
    @FXML private Label PatientIntendedLabel;
    @FXML private JFXTextField PatientIntendedDate;
    @FXML private JFXTextField PatientIntendedTimeRange;
    @FXML private JFXDatePicker DoctorIntendedDate;
    @FXML private JFXTextField DoctorIntendedTime;
    @FXML private AnchorPane AppointmentListDetailsPane;
    @FXML private Label ProfileAddress;
    @FXML private Label ProfileAddressLabel;
    @FXML private Label ProfileNameLabel;
    @FXML private Label ProfileEmailLabel;
    @FXML private Label ProfileContactLabel;
    @FXML private Label AppointmentListLabel;
    @FXML private JFXListView<VBox> AppointmentListListView;
    @FXML private AnchorPane AppointmentListProfilePane;

    Statement statement, statement1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            statement = Objects.requireNonNull(Medi_collab.connection()).createStatement();
            statement1 = Objects.requireNonNull(Medi_collab.connection()).createStatement();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        Rectangle2D screen = Screen.getPrimary().getVisualBounds();

        AppointmentListLabel.setLayoutX(screen.getWidth()/2.0 - AppointmentListLabel.getPrefWidth()/2.0);
        AppointmentListDetailsPane.setVisible(false);
        AppointmentListDetailsPane.setPrefWidth(screen.getWidth() - AppointmentListListView.getPrefWidth() -
                AppointmentListProfilePane.getPrefWidth() - 32);
        AppointmentListProfilePane.setVisible(false);

        if (Medi_collab.role.equals("Patient")) {
            PatientIntendedLabel.setText("My" + PatientIntendedLabel.getText());
            DoctorIntendedLabel.setText("Doctor's" + DoctorIntendedLabel.getText());

            PatientIntendedDate.setDisable(true);
            PatientIntendedDate.setStyle("-fx-opacity: 1");
            PatientIntendedTimeRange.setDisable(true);
            PatientIntendedTimeRange.setStyle("-fx-opacity: 1");
        }
        else {
            PatientIntendedLabel.setText("Patient's" + PatientIntendedLabel.getText());
            DoctorIntendedLabel.setText("My" + DoctorIntendedLabel.getText());
        }

        DoctorIntendedDate.setConverter(Medi_collab.localDateStringConverter);

        SetAppointmentButton.setLayoutX(AppointmentListDetailsPane.getPrefWidth()/2.0 - 65);

        try {
            RefreshAppointmentList();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void handleCloseLabel(MouseEvent mouseEvent) {
        ExitDialogue exitDialogue = new ExitDialogue(mouseEvent);
        exitDialogue.OpenDialogue();
    }

    public void handleGoBackButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main_Menu.fxml"));

        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
        scene.getStylesheets().add(getClass().getResource("/Resources/CSS/Main_Menu.css").toExternalForm());
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.show();
    }

    public void handleMinimizeLabel(MouseEvent mouseEvent) {
        ((Stage)((Node)mouseEvent.getSource()).getScene().getWindow()).setIconified(true);
    }

    public void RefreshAppointmentList() throws SQLException {
        AppointmentListListView.getItems().clear();
        AppointmentListDetailsPane.setVisible(false);
        AppointmentListProfilePane.setVisible(false);
        NotifyLabel.setText("Please be sure to set the date and the time. \n" +
                "You CANNOT change it after it is set.");
        NotifyLabel.setTextFill(Color.RED);

        Map<VBox, Pair<String, String> > map = new HashMap<>();

        ResultSet resultSet = statement.executeQuery("SELECT * FROM APPOINTMENT_TABLE " +
                "WHERE PATIENT = '" + Medi_collab.User_Info_Resultset.getString("USERNAME") + "' " +
                "OR DOCTOR = '" + Medi_collab.User_Info_Resultset.getString("USERNAME") + "'");

        while (resultSet.next()){
            String doctor = resultSet.getString("DOCTOR"), patient = resultSet.getString("PATIENT");

            Label label = null, label1, label2;
            ResultSet temp;

            if(Medi_collab.User_Info_Resultset.getString("USERNAME").equals(patient)) {
                temp = statement1.executeQuery("SELECT FIRSTNAME, LASTNAME FROM USERS_TABLE " +
                        "WHERE USERNAME = '" + doctor + "'");

                label1 = new Label(doctor);
            }
            else {
                temp = statement1.executeQuery("SELECT FIRSTNAME, LASTNAME FROM USERS_TABLE " +
                        "WHERE USERNAME = '" + patient + "'");

                label1 = new Label(patient);
            }

            if(temp.next()) {
                label = new Label(temp.getString("FIRSTNAME") + " " + temp.getString("LASTNAME"));
                label.setFont(new Font(20));
                label.setTextFill(Color.valueOf("#464646"));
            }

            label1.setFont(new Font(14));
            label1.setTextFill(Color.valueOf("#464646"));

            label2 = new Label(new SimpleDateFormat("dd-MM-yyyy").format(resultSet.getDate("APPOINTMENT_DATE")));
            label2.setFont(new Font(12));
            if(resultSet.getString("APPOINTMENT_TIME") == null)
                label2.setTextFill(Color.RED);
            else
                label2.setTextFill(Color.valueOf("#2ed2ff"));

            VBox vBox = new VBox(5, label, label1, label2);
            vBox.setAlignment(Pos.CENTER_LEFT);
            vBox.setStyle(" -fx-background-color: TRANSPARENT;      -fx-background-radius: 10;" +
                    "       -fx-border-color: #2ed2ff;" +
                    "       -fx-border-radius: 10;                  -fx-padding: 8;");

            map.put(vBox, new Pair<>(resultSet.getString("ID"), new SimpleDateFormat("dd-MM-yyyy").format(resultSet.getDate("APPOINTMENT_DATE"))));

            AppointmentListListView.getItems().add(AppointmentListListView.getItems().size(), vBox);
        }
        AppointmentListListView.setOnMouseClicked(mouseEvent -> {
            Pair<String, String> pair = map.get(AppointmentListListView.getSelectionModel().getSelectedItem());

            if (pair != null) {
                try {
                    showAppointmentDetails(pair);
                }
                catch (SQLException | ParseException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }

    private void showAppointmentDetails(Pair<String, String> pair) throws SQLException, ParseException {
        NotifyLabel.setVisible(true);
        SetAppointmentButton.setVisible(true);

        String doctor, patient;

        ResultSet resultSet = statement.executeQuery("SELECT * FROM APPOINTMENT_TABLE " +
                "WHERE ID = '" + pair.getKey() + "' AND APPOINTMENT_DATE = TO_DATE('" + pair.getValue() + "', 'dd-mm-yyyy')"),
                temp;
        if (resultSet.next()) {
            AppointmentListDetailsPane.setVisible(true);
            AppointmentListProfilePane.setVisible(true);

            PatientIntendedDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(resultSet.getDate("APPOINTMENT_DATE")));
            PatientIntendedTimeRange.setText(
                    "In Between " + new SimpleDateFormat("hh:mm a").format(new SimpleDateFormat("HH:mm").parse(resultSet.getString("VISITING_FROM"))) +
                            " and " + new SimpleDateFormat("hh:mm a").format(new SimpleDateFormat("HH:mm").parse(resultSet.getString("VISITING_TO")))
            );

            DoctorIntendedDate.getEditor().setText(new SimpleDateFormat("dd-MM-yyyy").format(resultSet.getDate("APPOINTMENT_DATE")));
            if (resultSet.getString("APPOINTMENT_TIME") != null)
                DoctorIntendedTime.setText(resultSet.getString("APPOINTMENT_TIME"));
            else
                DoctorIntendedTime.setText("");

            PseudoClass Disabled = PseudoClass.getPseudoClass("Disabled");
            DoctorIntendedDate.setDisable(Medi_collab.role.equals("Patient") || resultSet.getString("APPOINTMENT_TIME") != null);
            DoctorIntendedDate.pseudoClassStateChanged(Disabled, Medi_collab.role.equals("Patient") || resultSet.getString("APPOINTMENT_TIME") != null);
            DoctorIntendedTime.setDisable(Medi_collab.role.equals("Patient") || resultSet.getString("APPOINTMENT_TIME") != null);
            DoctorIntendedTime.pseudoClassStateChanged(Disabled, Medi_collab.role.equals("Patient") || resultSet.getString("APPOINTMENT_TIME") != null);

            if (Medi_collab.role.equals("Patient") || resultSet.getString("APPOINTMENT_TIME") != null) {
                NotifyLabel.setVisible(false);
                SetAppointmentButton.setVisible(false);
            }
            SetAppointmentButton.setOnAction(actionEvent -> {
                try {
                    int intId = 0;
                    ResultSet tempResultset = statement1.executeQuery("SELECT MAX(ID) AS MAX FROM APPOINTMENT_TABLE " +
                            "WHERE APPOINTMENT_DATE = TO_DATE('" + DoctorIntendedDate.getEditor().getText() + "', 'dd-mm-yyyy')");
                    if (tempResultset.next())
                        intId = tempResultset.getInt("MAX");

                    PreparedStatement preparedStatement = Objects.requireNonNull(Medi_collab.connection()).prepareStatement(
                            "UPDATE APPOINTMENT_TABLE SET ID = ?, APPOINTMENT_DATE = TO_DATE(?, 'dd-mm-yyyy'), " +
                                    "APPOINTMENT_TIME = ? WHERE ID = ? AND APPOINTMENT_DATE = TO_DATE(?, 'dd-mm-yyyy')"
                    );
                    preparedStatement.setString(1, String.valueOf(intId + 1));
                    preparedStatement.setString(2, DoctorIntendedDate.getEditor().getText());
                    preparedStatement.setString(3, DoctorIntendedTime.getText());
                    preparedStatement.setString(4, pair.getKey());
                    preparedStatement.setString(5, pair.getValue());

                    preparedStatement.execute();

                    RefreshAppointmentList();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });

            doctor = resultSet.getString("DOCTOR");
            patient = resultSet.getString("PATIENT");

            if (Medi_collab.User_Info_Resultset.getString("USERNAME").equals(patient))
                temp = statement.executeQuery("SELECT FIRSTNAME, LASTNAME, EMAIL, CONTACT, ADDRESS " +
                        "FROM USERS_TABLE WHERE USERNAME = '" + doctor + "'");
            else {
                if (Medi_collab.role.equals("Doctor"))
                    temp = statement.executeQuery("SELECT FIRSTNAME, LASTNAME, EMAIL, CONTACT, DESCRIPTION " +
                            "FROM USERS_TABLE WHERE USERNAME = '" + patient + "'");
                else
                    temp = statement.executeQuery("SELECT FIRSTNAME, LASTNAME, EMAIL, CONTACT, ADDRESS " +
                            "FROM USERS_TABLE WHERE USERNAME = '" + patient + "'");
            }
            temp.next();

            ProfileNameLabel.setText(temp.getString("FIRSTNAME") + " " + temp.getString("LASTNAME"));
            ProfileEmailLabel.setText(temp.getString("EMAIL"));
            ProfileContactLabel.setText(temp.getString("CONTACT"));

            if (Medi_collab.User_Info_Resultset.getString("USERNAME").equals(patient))
                ProfileAddressLabel.setText(temp.getString("ADDRESS"));
            else {
                if (Medi_collab.role.equals("Doctor")) {
                    ProfileAddress.setText("About Patient");
                    ProfileAddressLabel.setText(temp.getString("DESCRIPTION"));
                } else
                    ProfileAddressLabel.setText(temp.getString("ADDRESS"));
            }

            ProfileAddressLabel.setWrapText(true);
        }
    }
}
