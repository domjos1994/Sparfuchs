package de.domjos.sparfuchs.utils.general;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

public final class Dialogs {

    public static Stage printFXML(String path, ResourceBundle language, String header, Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Dialogs.class.getResource(path), language);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Dialogs.class.getResource("/styles/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle(header);
        stage.getIcons().add(new Image(Dialogs.class.getResourceAsStream("/images/header.png")));
        return stage;
    }

    public static void printAlert(Alert.AlertType type, String title, String header, String content, boolean debug, Stage stage) {
        if(!debug) {
            Dialogs.printNotification(type, title, header, stage);
        } else {
            Alert alert = new Alert(type);
            Dialogs.setIcon(alert);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.showAndWait();
        }
    }

    public static boolean printConfirmDialog(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type, title, ButtonType.YES, ButtonType.NO);
        Dialogs.setIcon(alert);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
        return alert.getResult()==ButtonType.YES;
    }

    public static void printException(Throwable ex, boolean debug, Stage stage) {
        if(!debug) {
            Dialogs.printNotification(Alert.AlertType.ERROR, "Unhandled " + ex.getClass().getName(), ex.toString(), stage);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            Dialogs.setIcon(alert);
            alert.setTitle("Unhandled " + ex.getClass().getName());
            alert.setHeaderText(ex.getLocalizedMessage());
            alert.setContentText(ex.toString());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String exceptionText = sw.toString();
            Label label = new Label("The exception stacktrace was:");
            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);
            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            alert.getDialogPane().setExpandableContent(expContent);
            alert.showAndWait();
        }
    }

    public static void printNotification(Alert.AlertType alertType, String title, String message, Stage stage) {
        Notifications notifications = Notifications.create();
        notifications.text(title);
        notifications.text(message);
        if(stage!=null) {
            notifications.owner(stage);
        }
        notifications.position(Pos.BASELINE_CENTER);
        switch (alertType) {
            case ERROR:
                notifications.darkStyle().showError();
                break;
            case WARNING:
                notifications.darkStyle().showWarning();
                break;
            case INFORMATION:
                notifications.darkStyle().showInformation();
                break;
            case CONFIRMATION:
                notifications.darkStyle().showConfirm();
                break;
            default:
                notifications.darkStyle().show();
        }
    }

    public static File printDirectoryChooser(String title) {
        return Dialogs.printFileChooser(title, true, true, false, new LinkedList<>()).get(0);
    }

    public static File printSingleOpenFileChooser(String title, List<String> extensions) {
        return Dialogs.printFileChooser(title, true, false, false, extensions).get(0);
    }

    public static List<File> printMultiOpenFileChooser(String title, List<String> extensions) {
        return Dialogs.printFileChooser(title, true, false, true, extensions);
    }

    public static File printSaveFileChooser(String title, List<String> extensions) {
        return Dialogs.printFileChooser(title, false, false, false, extensions).get(0);
    }

    private static List<File> printFileChooser(String title, boolean open, boolean dir, boolean multiSelect, List<String> extensions) {
        List<File> files = new LinkedList<>();
        if(dir) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle(title);
            files.add(directoryChooser.showDialog(null));
            return files;
        } else {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(title);
            if(extensions!=null) {
                for(String extension : extensions) {
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(extension.split(":")[0], extension.split(":")[1].split("\\|")));
                }
            }
            if(open) {
                if(multiSelect) {
                    files = fileChooser.showOpenMultipleDialog(null);
                } else {
                    files.add(fileChooser.showOpenDialog(null));
                }
            } else {
                files.add(fileChooser.showSaveDialog(null));
            }
        }
        return files;
    }

    private static void setIcon(Dialog dialog) {
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Dialogs.class.getResourceAsStream("/images/header.png")));
    }

    private static GridPane addControls(List<List<Control>> controls) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        for(int i = 0; i<=controls.size()-1; i++) {
            for(int j = 0; j<=controls.get(i).size()-1; j++) {
                gridPane.add(controls.get(i).get(j), j, i);
            }
        }
        return gridPane;
    }
}
