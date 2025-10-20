package identity;

import Validation.FormValidation;
import controllers.ChangePassowrdController;
import controllers.ShowIdentitiController;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;

public class App extends Application {

    private static Scene scene;
    private static double x, y;

    @Override
    public void start(Stage stage) throws IOException {
        final Font font = Font.loadFont(new FileInputStream(new File("C:\\Program Files\\TrainingData\\fonts\\URW-DIN-Arabic.ttf")), 12);
        scene = new Scene(loadFXML("/view/LoginPage"));

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.setScene(scene);
        stage.getIcons().add(new Image("/images/appicon.png"));
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent root = fxmlLoader.load();
        return root;
    }

    public static FXMLLoader loadFX(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader;
    }

    public static Parent lodChangePassowrdPage(String militariid) {
        Parent root = null;
        try {
            FXMLLoader fxmlLoader = loadFX("/view/ChangePassowrd");
            root = fxmlLoader.load();
            ChangePassowrdController controller = new ChangePassowrdController();
            controller = (ChangePassowrdController) fxmlLoader.getController();
            controller.setMilitaryId(militariid);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            scene.setFill(Color.TRANSPARENT);
            root.setOnMousePressed((MouseEvent event) -> {
                x = event.getSceneX();
                y = event.getSceneY();
            });
            root.setOnMouseDragged((MouseEvent event) -> {
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);
            });
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.showAndWait();
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return root;
    }

    public static Parent lodShowIdentitiPage(String militariid, String name, String idNumber, String rank, String phonaNumber, String specialty, String note) {
        Parent root = null;
        try {
            FXMLLoader fxmlLoader = loadFX("/view/showIdentiti");
            root = fxmlLoader.load();
            ShowIdentitiController controller = new ShowIdentitiController();
            controller = (ShowIdentitiController) fxmlLoader.getController();
            controller.setData(militariid, name, idNumber, rank, phonaNumber, specialty, note);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.showAndWait();

        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return root;
    }

    public static void showFxml(String fxml) {
        Pane root = null;
        Stage stage = new Stage();
        try {
            root = (Pane) loadFXML(fxml);
            stage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            root.setOnMousePressed((MouseEvent event) -> {
                x = event.getSceneX();
                y = event.getSceneY();
            });
            root.setOnMouseDragged((MouseEvent event) -> {
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);
            });
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.showAndWait();
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void showFxmlSpecificxy(String fxml, double xnum, double ynum) {
        Pane root = null;
        Stage stage = new Stage();
        try {
            root = (Pane) loadFXML(fxml);
            stage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setX(xnum);
            stage.setY(ynum);

            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.showAndWait();
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

}
